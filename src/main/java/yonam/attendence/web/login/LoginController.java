package yonam.attendence.web.login;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import yonam.attendence.domain.login.LoginForm;
import yonam.attendence.domain.login.LoginService;
import yonam.attendence.domain.teacher.Teacher;
import yonam.attendence.web.SessionConst;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm") LoginForm form) {
        return "login/loginForm";
    }

    @PostMapping("/login")
    public String login(@Validated @ModelAttribute("loginForm") LoginForm form, BindingResult bindingResult,
                        @RequestParam(defaultValue = "/") String redirectURL,
                        HttpServletRequest request) {

        if (bindingResult.hasErrors()) {
            return "login/loginForm";
        }

        Teacher loginTeacher = loginService.login(form);

        if (loginTeacher == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 일치하지 않습니다.");
            return "login/loginForm";
        }

        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_TEACHER, loginTeacher);

        return "redirect:" + redirectURL;
    }

    @ResponseBody
    @PostMapping(path = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public LoginResult loginAndroid(@Validated @RequestBody LoginForm form,
                                    BindingResult bindingResult, HttpServletRequest request) {

        log.info("id: {}, password: {}", form.getId(), form.getPassword());
        if (bindingResult.hasErrors()) {
            return new LoginResult(false);
        }

        Teacher loginTeacher = loginService.login(form);

        if (loginTeacher == null) {
            return new LoginResult(false);
        }

        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_TEACHER, loginTeacher);

        return new LoginResult(true);
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session != null) {
            session.invalidate();
        }

        return "redirect:/";
    }
}
