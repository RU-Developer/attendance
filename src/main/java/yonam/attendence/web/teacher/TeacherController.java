package yonam.attendence.web.teacher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import yonam.attendence.domain.message.MessageResult;
import yonam.attendence.domain.message.MessageService;
import yonam.attendence.domain.message.ValidationForm;
import yonam.attendence.domain.teacher.Teacher;
import yonam.attendence.domain.teacher.TeacherAddForm;
import yonam.attendence.domain.teacher.TeacherService;
import yonam.attendence.domain.util.Util;
import yonam.attendence.web.SessionConst;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Objects;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/teachers")
public class TeacherController {

    private final TeacherService teacherService;
    private final MessageService messageService;

    @ResponseBody
    @PostMapping(path = "/add", produces = MediaType.APPLICATION_JSON_VALUE)
    public TeacherAddResult save(@Validated @RequestBody TeacherAddForm form, BindingResult bindingResult,
                                 HttpServletRequest request) {
        log.info("TeacherController.save");
        log.info("id: {}, password: {}, phone: {}", form.getId(), form.getPassword(), form.getPhone());
        if (bindingResult.hasErrors()) {
            StringBuilder sb = new StringBuilder();

            for (ObjectError error : bindingResult.getAllErrors()) {
                sb.append(error.getDefaultMessage());
                sb.append("\n");
            }

            return new TeacherAddResult(false, sb.toString());
        }

        form.setPhone(Util.deleteSpecialSymbolsAtPhoneNumber(form.getPhone()));
        HttpSession session = request.getSession();
        if (!Objects.equals(form.getPhone(), session.getAttribute(SessionConst.PHONE_VALIDATED))) {
            return new TeacherAddResult(false, "휴대폰 번호를 인증해야 합니다.");
        }

        return teacherService.save(form);
    }

    @ResponseBody
    @PostMapping(path = "/profile", produces = MediaType.APPLICATION_JSON_VALUE)
    public Teacher profile(HttpServletRequest request) {
        log.info("TeacherController.profile");
        return (Teacher) request.getSession().getAttribute(SessionConst.LOGIN_TEACHER);
    }

    @ResponseBody
    @PostMapping(path = "/sendvalidation", produces = MediaType.APPLICATION_JSON_VALUE)
    public void sendValidation(@Validated @RequestBody PhoneValidationForm form) {
        log.info("TeacherController.sendValidation");
        form.setPhone(Util.deleteSpecialSymbolsAtPhoneNumber(form.getPhone()));
        MessageResult messageResult = messageService.sendPhoneValidation(form.getPhone());
        log.info("sendvalidation result = {}", messageResult.isSuccess());
    }

    @ResponseBody
    @PostMapping(path = "/validation", produces = MediaType.APPLICATION_JSON_VALUE)
    public void validation(@Validated @RequestBody ValidationForm form, HttpServletRequest request) {
        log.info("TeacherController.validation");
        String validatedPhone = messageService.phoneValidation(form.getValidationCode());
        if (validatedPhone == null) {
            validatedPhone = "";
        }
        request.getSession().setAttribute(SessionConst.PHONE_VALIDATED, validatedPhone);
        log.info("validatedPhone={}", (String) request.getSession().getAttribute(SessionConst.PHONE_VALIDATED));
    }
}
