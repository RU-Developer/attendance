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


    @ResponseBody
    @PostMapping(path = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ParentLoginResult login(@Validated @RequestBody ParentLoginForm form, BindingResult bindingResult,
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

    @ResponseBody
    @PostMapping(path = "/validation", produces = MediaType.APPLICATION_JSON_VALUE)
    public ValidationResult validation(@Validated @RequestBody ValidationForm form, BindingResult bindingResult,
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

    @ResponseBody
    @PostMapping(path = "/sendvalidation", produces = MediaType.APPLICATION_JSON_VALUE)
    public MessageResult sendValidation(@Validated @RequestBody ParentLoginForm form, BindingResult bindingResult) {
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
