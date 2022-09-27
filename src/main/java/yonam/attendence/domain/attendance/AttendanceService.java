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
import yonam.attendence.web.attendance.StudentParentAttendance;

import java.sql.SQLException;
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

    public List<StudentParentAttendance> studentParentAttendancesWithTeacher(Long lesson) {
        return studentRepository.studentParentByTeacherLesson(lesson);
    }

    public List<Attendance> studentAttendances(Long studentId) {
        return attendanceRepository.findByStudentId(studentId);
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
            String message = getMessage(inTime, student);

            log.info("message = {}", message);
            attendance.setStudent_id(studentId);
            attendanceRepository.save(attendance);
            messageService.send(new MessageForm(message, parent.getPhone()));
        }
    }

    private String getMessage(LocalDateTime inTime, Student student) {
        StringBuilder message = new StringBuilder("[학원 등원 알림 메시지] ");
        message.append(student.getName());
        message.append("학생이 ");
        message.append(inTime.getYear());
        message.append("년 ");
        message.append(inTime.getMonthValue());
        message.append("월 ");
        message.append(inTime.getDayOfMonth());
        message.append("일 ");
        message.append(inTime.getHour());
        message.append("시 ");
        message.append(inTime.getMinute());
        message.append("분에 등원하였습니다.");
        return message.toString();
    }
}
