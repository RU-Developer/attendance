package yonam.attendence.web.student;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import yonam.attendence.domain.parent.Parent;
import yonam.attendence.domain.parent.ParentService;
import yonam.attendence.domain.student.Student;
import yonam.attendence.domain.student.StudentService;
import yonam.attendence.domain.teacher.Teacher;
import yonam.attendence.domain.util.Util;
import yonam.attendence.web.SessionConst;
import yonam.attendence.domain.student.StudentParent;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;
    private final ParentService parentService;

    @ResponseBody
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<StudentParent> student(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Teacher loginTeacher = (Teacher) session.getAttribute(SessionConst.LOGIN_TEACHER);
        return studentService.studentParentWithTeacherLesson(loginTeacher.getLesson());
    }

    @ResponseBody
    @PostMapping(path = "/add", produces = MediaType.APPLICATION_JSON_VALUE)
    public void save(@Validated @RequestBody StudentParent studentParent, HttpServletRequest request) {
        studentParent.getStudent().setPhone(Util.deleteSpecialSymbolsAtPhoneNumber(studentParent.getStudent().getPhone()));
        studentParent.getParent().setPhone(Util.deleteSpecialSymbolsAtPhoneNumber(studentParent.getParent().getPhone()));
        Teacher loginTeacher = (Teacher) request.getSession().getAttribute(SessionConst.LOGIN_TEACHER);
        studentParent.getStudent().setTeacherLesson(loginTeacher.getLesson());
        studentService.saveStudentParent(studentParent);
    }

    @ResponseBody
    @PostMapping(path = "/profile", produces = MediaType.APPLICATION_JSON_VALUE)
    public StudentParent profile(@RequestBody Long studentId) {
        Student student = studentService.findById(studentId);
        if (student == null) {
            return null;
        }

        Parent parent = parentService.findById(student.getParentId());
        if (parent == null) {
            return null;
        }

        return new StudentParent(student, parent);
    }

    @ResponseBody
    @PostMapping(path = "/withdraw", produces = MediaType.APPLICATION_JSON_VALUE)
    public void withdraw(@RequestBody Long studentId) {
        studentService.withdraw(studentId);
    }

    @ResponseBody
    @PutMapping(path = "/profile", produces = MediaType.APPLICATION_JSON_VALUE)
    public void update(@RequestBody StudentParent studentParent) {
        studentService.updateStudentParent(studentParent);
    }
}
