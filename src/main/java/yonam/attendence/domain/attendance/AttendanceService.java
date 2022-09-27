package yonam.attendence.domain.attendance;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yonam.attendence.domain.student.StudentRepository;
import yonam.attendence.web.attendance.StudentParentAttendance;

import java.util.List;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final StudentRepository studentRepository;

    @Transactional
    public List<StudentParentAttendance> studentParentAttendancesWithTeacher(Long lesson) {
        return studentRepository.studentParentByTeacherLesson(lesson);
    }
}
