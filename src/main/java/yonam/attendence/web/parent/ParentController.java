package yonam.attendence.web.parent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import yonam.attendence.domain.attendance.Attendance;
import yonam.attendence.domain.attendance.AttendanceService;
import yonam.attendence.domain.message.MessageResult;
import yonam.attendence.domain.message.MessageService;
import yonam.attendence.domain.message.ValidationForm;
import yonam.attendence.domain.parent.Parent;
import yonam.attendence.domain.parent.ParentService;
import yonam.attendence.domain.student.Student;
import yonam.attendence.domain.student.StudentService;
import yonam.attendence.domain.student.StudentTeacher;
import yonam.attendence.domain.util.Util;
import yonam.attendence.web.SessionConst;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Objects;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/parents")
public class ParentController {

    private final AttendanceService attendanceService;
    private final ParentService parentService;
    private final StudentService studentService;
    private final MessageService messageService;

    @GetMapping("/login")
    public String parentLoginForm(@ModelAttribute("loginForm") ParentLoginForm form) {
        return "parents/loginForm";
    }

    @PostMapping("/login")
    public String login(@Validated @ModelAttribute("loginForm") ParentLoginForm form, BindingResult bindingResult,
                        @RequestParam(defaultValue = "/") String redirectURL,
                        HttpServletRequest request) {

        if (bindingResult.hasErrors()) {
            return "parents/loginForm";
        }

        form.setPhone(Util.deleteSpecialSymbolsAtPhoneNumber(form.getPhone()));

        HttpSession session = request.getSession();
        log.info("form phone : session validated = {} : {}", form.getPhone(), (String) session.getAttribute(SessionConst.PHONE_VALIDATED));

        if (!form.getPhone().equals((String) session.getAttribute(SessionConst.PHONE_VALIDATED))) {
            bindingResult.rejectValue("phone", "휴대폰 번호를 인증해야 합니다.");
            return "parents/loginForm";
        }

        Parent loginParent = parentService.login(form);

        if (loginParent == null) {
            bindingResult.rejectValue("phone", "학생번호와 부모님 번호로 찾을 수 있는 계정이 없습니다.");
            return "parents/loginForm";
        }
        session.setAttribute(SessionConst.LOGIN_PARENT, loginParent);

        return "redirect:" + redirectURL;
    }

    @ResponseBody
    @PostMapping(path = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ParentLoginResult loginAndroid(@Validated @RequestBody ParentLoginForm form, BindingResult bindingResult,
                                          HttpServletRequest request) {

        if (bindingResult.hasErrors()) {
            return new ParentLoginResult(false);
        }

        form.setPhone(Util.deleteSpecialSymbolsAtPhoneNumber(form.getPhone()));

        HttpSession session = request.getSession();

        if (!form.getPhone().equals(session.getAttribute(SessionConst.PHONE_VALIDATED))) {
            bindingResult.rejectValue("phone", "휴대폰 번호를 인증해야 합니다.");
            return new ParentLoginResult(false);
        }

        Parent loginParent = parentService.login(form);

        if (loginParent == null) {
            return new ParentLoginResult(false);
        }

        session.setAttribute(SessionConst.LOGIN_PARENT, loginParent);

        return new ParentLoginResult(true);
    }

    @PostMapping("/validation")
    public String validation(@Validated ValidationForm form, BindingResult bindingResult,
                             HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return "redirect:/parents/login";
        }

        String validatedPhone = messageService.phoneValidation(form.getValidationCode());
        log.info("validatedPhone={}", validatedPhone);

        if (validatedPhone == null) {
            return "redirect:/parents/login";
        }

        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.PHONE_VALIDATED, validatedPhone);
        log.info("validatedPhone={}", (String) session.getAttribute(SessionConst.PHONE_VALIDATED));
        return "redirect:/parents/login";
    }

    @ResponseBody
    @PostMapping(path = "/validation", produces = MediaType.APPLICATION_JSON_VALUE)
    public ValidationResult validationAndroid(@Validated @RequestBody ValidationForm form, BindingResult bindingResult,
                                              HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return new ValidationResult(false);
        }

        String validatedPhone = messageService.phoneValidation(form.getValidationCode());

        if (validatedPhone == null) {
            return new ValidationResult(false);
        }

        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.PHONE_VALIDATED, validatedPhone);
        return new ValidationResult(true);
    }

    @PostMapping("/sendvalidation")
    public String sendValidation(@Validated @ModelAttribute("loginForm") ParentLoginForm form, BindingResult bindingResult) {
        log.info("/sendvalidation html");
        if (bindingResult.hasErrors()) {
            return "redirect:/parents/login";
        }

        log.info("phone={}", form.getPhone());

        form.setPhone(Util.deleteSpecialSymbolsAtPhoneNumber(form.getPhone()));
        MessageResult messageResult = messageService.sendPhoneValidation(form.getPhone());
        log.info("sendvalidation result = {}", messageResult.isSuccess());

        if (messageResult.isSuccess()) {
            bindingResult.reject(null, null, "인증번호가 발송되었습니다.");
            return "redirect:/parents/login";
        }

        bindingResult.rejectValue("phone", null,
                "인증번호 발송에 실패하였습니다. 잠시후 재시도 해주시기 바랍니다.");
        return "redirect:/parents/login";
    }

    @ResponseBody
    @PostMapping(path = "/sendvalidation", produces = MediaType.APPLICATION_JSON_VALUE)
    public MessageResult sendValidationAndroid(@Validated @RequestBody ParentLoginForm form, BindingResult bindingResult) {
        log.info("/sendvalidation android");
        if (bindingResult.hasErrors()) {
            StringBuilder sb = new StringBuilder();
            for (ObjectError error : bindingResult.getAllErrors()) {
                sb.append(error.getDefaultMessage());
                sb.append("\n");
            }

            return new MessageResult(false, sb.toString());
        }

        form.setPhone(Util.deleteSpecialSymbolsAtPhoneNumber(form.getPhone()));
        MessageResult messageResult = messageService.sendPhoneValidation(form.getPhone());
        log.info("sendvalidation result = {}", messageResult.isSuccess());

        return messageResult;
    }

    @ResponseBody
    @GetMapping(path = "/students", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Student> children(HttpServletRequest request) {
        Parent loginedParent = (Parent) request.getSession().getAttribute(SessionConst.LOGIN_PARENT);
        return parentService.children(loginedParent.getId());
    }

    @ResponseBody
    @GetMapping(path = "/student", produces = MediaType.APPLICATION_JSON_VALUE)
    public StudentTeacher student(@RequestParam Long studentId, HttpServletRequest request) {
        Parent loginedParent = (Parent) request.getSession().getAttribute(SessionConst.LOGIN_PARENT);
        return studentService.findStudentTeacher(studentId, loginedParent.getId());
    }

    @ResponseBody
    @GetMapping(path = "/student/attendances", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Attendance> studentAttendances(@RequestParam Long studentId, HttpServletRequest request) {
        Parent loginedParent = (Parent) request.getSession().getAttribute(SessionConst.LOGIN_PARENT);
        Student student = studentService.findById(studentId);
        if (!Objects.equals(student.getParentId(), loginedParent.getId())) {
            return null;
        }
        return attendanceService.studentAttendances(studentId);
    }
}
