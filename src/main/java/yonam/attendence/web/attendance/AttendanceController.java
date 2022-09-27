package yonam.attendence.web.attendance;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import yonam.attendence.domain.attendance.AttendanceService;
import yonam.attendence.domain.student.Student;
import yonam.attendence.domain.student.StudentService;
import yonam.attendence.domain.teacher.Teacher;
import yonam.attendence.web.SessionConst;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;
    private final StudentService studentService;

    @ResponseBody
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<StudentParentAttendance> attendance(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Teacher loginTeacher = (Teacher) session.getAttribute(SessionConst.LOGIN_TEACHER);
        return attendanceService.studentParentAttendancesWithTeacher(loginTeacher.getLesson());
    }

    @ResponseBody
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public String attendanceToday(@RequestParam("studentIdList") List<Long> studentIdList) {
        attendanceService.attendanceToday(studentIdList);
        return "200 ok";
    }

    @ResponseBody
    @GetMapping(path = "/student", produces = MediaType.APPLICATION_JSON_VALUE)
    public AttendanceStudentResult studentForm(@RequestParam("studentId") Long studentId) {
        Student student = studentService.findById(studentId);
        return new AttendanceStudentResult(student, attendanceService.studentAttendances(studentId));
    }
}