package yonam.attendence.web.attendance;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import yonam.attendence.domain.attendance.Attendance;
import yonam.attendence.domain.attendance.AttendanceService;
import yonam.attendence.domain.student.Student;
import yonam.attendence.domain.student.StudentParent;
import yonam.attendence.domain.student.StudentService;
import yonam.attendence.domain.teacher.Teacher;
import yonam.attendence.web.SessionConst;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Objects;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;
    private final StudentService studentService;

    @ResponseBody
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<StudentParent> attendance(HttpServletRequest request) {
        log.info("AttendanceController.attendance");
        HttpSession session = request.getSession();
        Teacher loginTeacher = (Teacher) session.getAttribute(SessionConst.LOGIN_TEACHER);
        return studentService.studentParentWithTeacherLesson(loginTeacher.getLesson());
    }

    @ResponseBody
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public void attendanceToday(@RequestBody List<Long> studentIdList, HttpServletRequest request) {
        log.info("AttendanceController.attendanceToday");
        log.info("studentIdList: {}", studentIdList.toString());
        Teacher loginTeacher = (Teacher) request.getSession().getAttribute(SessionConst.LOGIN_TEACHER);
        attendanceService.attendanceToday(studentIdList, loginTeacher.getLesson());
    }

    @ResponseBody
    @PostMapping(path = "/leave", produces = MediaType.APPLICATION_JSON_VALUE)
    public void leaveAcademyToday(@RequestBody List<Long> studentIdList, HttpServletRequest request) {
        log.info("AttendanceController.leaveAcademyToday");
        log.info("studentIdList: {}", studentIdList.toString());
        Teacher loginTeacher = (Teacher) request.getSession().getAttribute(SessionConst.LOGIN_TEACHER);
        attendanceService.leaveAcademyToday(studentIdList, loginTeacher.getLesson());
    }

    @ResponseBody
    @GetMapping(path = "/student", produces = MediaType.APPLICATION_JSON_VALUE)
    public AttendanceStudentResult studentForm(@RequestParam Long studentId, HttpServletRequest request) {
        log.info("AttendanceController.studentForm");
        log.info("studentId={}", studentId);
        Student student = studentService.findById(studentId);
        Teacher loginTeacher = (Teacher) request.getSession().getAttribute(SessionConst.LOGIN_TEACHER);
        if (!Objects.equals(student.getTeacherLesson(), loginTeacher.getLesson())) {
            return null;
        }
        return new AttendanceStudentResult(student, attendanceService.studentAttendances(studentId));
    }

    @ResponseBody
    @PostMapping(path = "/student", produces = MediaType.APPLICATION_JSON_VALUE)
    public void attendanceUpdate(@RequestBody Attendance attendance, HttpServletRequest request) {
        log.info("AttendanceController.attendanceUpdate");
        log.info("studentId: {}", attendance.getStudentId());
        Student student = studentService.findById(attendance.getStudentId());
        Teacher loginTeacher = (Teacher) request.getSession().getAttribute(SessionConst.LOGIN_TEACHER);
        if (!Objects.equals(student.getTeacherLesson(), loginTeacher.getLesson())) {
            return;
        }
        attendanceService.updateAttendance(attendance);
    }

    @ResponseBody
    @PostMapping(path = "/student/delete", produces = MediaType.APPLICATION_JSON_VALUE)
    public void attendanceDelete(@RequestParam Long attendanceId, HttpServletRequest request) {
        log.info("AttendanceController.attendanceDelete");
        log.info("attendanceId: {}", attendanceId);
        Teacher loginTeacher = (Teacher) request.getSession().getAttribute(SessionConst.LOGIN_TEACHER);
        attendanceService.deleteAttendance(attendanceId, loginTeacher.getLesson());
    }
}
