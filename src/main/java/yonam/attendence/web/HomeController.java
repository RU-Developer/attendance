package yonam.attendence.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import yonam.attendence.domain.parent.Parent;
import yonam.attendence.domain.student.Student;
import yonam.attendence.domain.student.StudentService;
import yonam.attendence.domain.teacher.Teacher;
import yonam.attendence.web.argumentresolver.Login;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final StudentService studentService;

    @GetMapping("/")
    public String homeLogin(@Login Teacher loginTeacher, @Login Parent loginParent, Model model) {
        if (loginTeacher != null) {
            model.addAttribute("teacher", loginTeacher);
            return "loginHome";
        }

        if (loginParent != null) {
            model.addAttribute("parent", loginParent);
            List<Student> students = studentService.findByParentId(loginParent.getId());
            model.addAttribute("student", students.get(0));
            return "parentLoginHome";
        }

        return "home";
    }
}
