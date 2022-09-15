package yonam.attendence.web.teacher;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import yonam.attendence.domain.teacher.Teacher;
import yonam.attendence.domain.teacher.TeacherRepository;

@Controller
@RequiredArgsConstructor
@RequestMapping("/teachers")
public class TeacherController {

    private final TeacherRepository teacherRepository;

    @GetMapping("/add")
    public String addForm(@ModelAttribute("teacher") Teacher teacher) {
        return "teachers/addTeacherForm";
    }

    @PostMapping("/add")
    public String save(@Validated @ModelAttribute Teacher teacher, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "teachers/addTeacherForm";
        }

        teacherRepository.save(teacher);
        return "redirect:/";
    }

    @ResponseBody
    @PostMapping(path = "/add", produces = MediaType.APPLICATION_JSON_VALUE)
    public TeacherAddResult saveAndroid(@Validated @ModelAttribute Teacher teacher, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new TeacherAddResult(false);
        }

        teacherRepository.save(teacher);
        return new TeacherAddResult(true);
    }
}
