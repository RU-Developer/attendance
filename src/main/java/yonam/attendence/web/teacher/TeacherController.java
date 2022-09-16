package yonam.attendence.web.teacher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import yonam.attendence.domain.teacher.Teacher;
import yonam.attendence.domain.teacher.TeacherService;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/teachers")
public class TeacherController {

    private final TeacherService teacherService;

    @GetMapping("/add")
    public String addForm(@ModelAttribute("teacher") Teacher teacher) {
        return "teachers/addTeacherForm";
    }

    @PostMapping("/add")
    public String save(@Validated @ModelAttribute Teacher teacher, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "teachers/addTeacherForm";
        }

        teacherService.save(teacher);
        return "redirect:/";
    }

    @ResponseBody
    @PostMapping(path = "/add", produces = MediaType.APPLICATION_JSON_VALUE)
    public TeacherAddResult saveAndroid(@Validated @ModelAttribute Teacher teacher, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new TeacherAddResult(false);
        }

        teacherService.save(teacher);
        return new TeacherAddResult(true);
    }
}