package yonam.attendence.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import yonam.attendence.domain.teacher.Teacher;
import yonam.attendence.domain.teacher.TeacherRepository;
import yonam.attendence.web.argumentresolver.Login;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final TeacherRepository teacherRepository;

    @GetMapping("/")
    public String homeLogin(@Login Teacher loginTeacher, Model model) {
        if (loginTeacher == null) {
            return "home";
        }

        model.addAttribute("teacher", loginTeacher);
        return "loginHome";
    }
}
