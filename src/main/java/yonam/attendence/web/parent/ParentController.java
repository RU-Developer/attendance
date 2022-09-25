package yonam.attendence.web.parent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import yonam.attendence.domain.parent.Parent;
import yonam.attendence.domain.parent.ParentService;
import yonam.attendence.web.SessionConst;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/parents")
public class ParentController {

    private final ParentService parentService;

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

        Parent loginParent = parentService.login(form);

        if (loginParent == null) {
            bindingResult.reject("loginFail", "학생번호와 부모님 번호로 찾을 수 있는 계정이 없습니다.");
            return "parents/loginForm";
        }

        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_PARENT, loginParent);

        return "redirect:" + redirectURL;
    }

    @ResponseBody
    @PostMapping(path = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ParentLoginResult loginAndroid(@Validated @ModelAttribute("loginForm") ParentLoginForm form, BindingResult bindingResult,
                               HttpServletRequest request) {

        if (bindingResult.hasErrors()) {
            return new ParentLoginResult(false);
        }

        Parent loginParent = parentService.login(form);

        if (loginParent == null) {
            return new ParentLoginResult(false);
        }

        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_PARENT, loginParent);

        return new ParentLoginResult(true);
    }
}
