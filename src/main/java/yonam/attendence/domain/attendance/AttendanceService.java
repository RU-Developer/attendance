package yonam.attendence.domain.attendance;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yonam.attendence.domain.message.MessageForm;
import yonam.attendence.domain.message.MessageService;
import yonam.attendence.domain.parent.Parent;
import yonam.attendence.domain.parent.ParentRepository;
import yonam.attendence.domain.student.Student;
import yonam.attendence.domain.student.StudentRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final StudentRepository studentRepository;
    private final ParentRepository parentRepository;
    private final AttendanceRepository attendanceRepository;
    private final MessageService messageService;


    public void updateAttendance(Attendance attendance) {
        log.info("AttendanceService.updateAttendance");
        Attendance validated = attendanceRepository.findById(attendance.getId());
        validated.setDateAttendance(attendance.getDateAttendance());
        attendanceRepository.updateDateAttendance(validated);
    }

    public void deleteAttendance(Long attendanceId, Long lesson) {
        log.info("AttendanceService.deleteAttendance");
        Long studentId = attendanceRepository.findById(attendanceId).getStudentId();
        Student student = studentRepository.findById(studentId);
        if (!Objects.equals(student.getTeacherLesson(), lesson)) {
            return;
        }
        attendanceRepository.delete(attendanceId);
    }

    public List<Attendance> studentAttendances(Long studentId) {
        return attendanceRepository.findByStudentId(studentId);
    }

    public void leaveAcademyToday(List<Long> studentIdList, Long lesson) {
        LocalDate dateAttendance = LocalDate.now();
        LocalDateTime outTime = LocalDateTime.now();
        Attendance attendance = new Attendance();
        attendance.setDateAttendance(dateAttendance);

        for (Long studentId : studentIdList) {
            Student student = studentRepository.findById(studentId);
            log.info("leaveAcademyToday studentId = {}", studentId);

            if (student == null || !Objects.equals(student.getTeacherLesson(), lesson)) {
                log.error("해당 강사의 담당 학생 찾을 수 없음");
                continue;
            }

            Parent parent = parentRepository.findById(student.getParentId());

            if (parent == null) {
                log.error("해당 학생의 부모 데이터를 찾을 수 없음");
                continue;
            }

            String message = getMessage(outTime, "하원", student);
            attendance.setStudentId(studentId);

            if (attendanceRepository.findByDateAttendanceWithStudentId(dateAttendance, studentId) == null) {
                log.info("attendance save");
                attendanceRepository.save(attendance);
            }

            log.info("message = {}", message);
            messageService.send(new MessageForm(message, parent.getPhone()));
        }
    }

    public void attendanceToday(List<Long> studentIdList, Long lesson) {
        LocalDate dateAttendance = LocalDate.now();
        LocalDateTime inTime = LocalDateTime.now();
        Attendance attendance = new Attendance();
        attendance.setDateAttendance(dateAttendance);

        for (Long studentId : studentIdList) {
            log.info("attendanceToday studentId={}", studentId);
            Student student = studentRepository.findById(studentId);
            if (student == null || !Objects.equals(student.getTeacherLesson(), lesson)) {
                log.error("해당 강사의 담당 학생 찾을 수 없음");
                continue;
            }

            Parent parent = parentRepository.findById(student.getParentId());
            if (parent == null) {
                log.error("해당 학생의 부모 데이터를 찾을 수 없음");
                continue;
            }
            String message = getMessage(inTime, "등원", student);
            attendance.setStudentId(studentId);

            if (attendanceRepository.findByDateAttendanceWithStudentId(dateAttendance, studentId) == null) {
                log.info("attendance save");
                attendanceRepository.save(attendance);
            }
            log.info("message = {}", message);
            messageService.send(new MessageForm(message, parent.getPhone()));
        }
    }

    private String getMessage(LocalDateTime time, String method, Student student) {
        StringBuilder message = new StringBuilder("[학원 " + method + " 알림 메시지] ");
        message.append(student.getName()).append("학생이 ")
                .append(time.getYear()).append("년 ")
                .append(time.getMonthValue()).append("월 ")
                .append(time.getDayOfMonth()).append("일 ")
                .append(time.getHour()).append("시 ")
                .append(time.getMinute()).append("분에 ")
                .append(method).append("하였습니다.");
        return message.toString();
    }
}
