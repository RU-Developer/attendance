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
        attendanceRepository.update(attendance);
    }

    public List<Attendance> studentAttendances(Long studentId) {
        return attendanceRepository.findByStudentId(studentId);
    }

    public void leaveAcademyToday(List<Long> studentIdList) {
        LocalDate dateAttendance = LocalDate.now();
        LocalDateTime outTime = LocalDateTime.now();
        Attendance attendance = new Attendance();
        attendance.setDateAttendance(dateAttendance);
        attendance.setOutTime(outTime);

        for (Long studentId : studentIdList) {
            Student student = studentRepository.findById(studentId);

            if (student == null) {
                continue;
            }

            Parent parent = parentRepository.findById(student.getParentId());

            if (parent == null) {
                continue;
            }

            String message = getMessage(outTime, "하원", student);

            attendance.setStudentId(studentId);

            if (attendanceRepository.findByDateAttendanceWithStudentId(dateAttendance, studentId) == null) {
                attendanceRepository.save(attendance);
                messageService.send(new MessageForm(message, parent.getPhone()));
                return;
            }

            attendanceRepository.updateOutTime(attendance);
            messageService.send(new MessageForm(message, parent.getPhone()));
        }
    }

    public void attendanceToday(List<Long> studentIdList) {
        LocalDate dateAttendance = LocalDate.now();
        LocalDateTime inTime = LocalDateTime.now();
        Attendance attendance = new Attendance();
        attendance.setDateAttendance(dateAttendance);
        attendance.setInTime(inTime);

        for (Long studentId : studentIdList) {
            log.info("studentId={}", studentId);
            Student student = studentRepository.findById(studentId);
            if (student == null) {
                continue;
            }
            Parent parent = parentRepository.findById(student.getParentId());
            if (parent == null) {
                continue;
            }
            String message = getMessage(inTime, "등원", student);

            log.info("message = {}", message);
            attendance.setStudentId(studentId);

            if (attendanceRepository.findByDateAttendanceWithStudentId(dateAttendance, studentId) == null) {
                log.info("attendance save");
                attendanceRepository.save(attendance);
                messageService.send(new MessageForm(message, parent.getPhone()));
                continue;
            }

            log.info("attendance update");
            attendanceRepository.updateInTime(attendance);
            messageService.send(new MessageForm(message, parent.getPhone()));
        }
    }

    private String getMessage(LocalDateTime time, String method, Student student) {
        StringBuilder message = new StringBuilder("[학원 " + method + " 알림 메시지] ");
        message.append(student.getName());
        message.append("학생이 ");
        message.append(time.getYear());
        message.append("년 ");
        message.append(time.getMonthValue());
        message.append("월 ");
        message.append(time.getDayOfMonth());
        message.append("일 ");
        message.append(time.getHour());
        message.append("시 ");
        message.append(time.getMinute());
        message.append("분에 ");
        message.append(method);
        message.append("하였습니다.");
        return message.toString();
    }
}
