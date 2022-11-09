package yonam.attendence.domain.login;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yonam.attendence.domain.teacher.Teacher;
import yonam.attendence.domain.teacher.TeacherRepository;

@Transactional
@Service
@RequiredArgsConstructor
public class LoginService {

    private final LoginRepository loginRepository;
    private final TeacherRepository teacherRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * @return null 로그인 실패
     */
    public Teacher login(LoginForm form) {
        LoginForm loginForm = loginRepository.findById(form.getId());

        if (loginForm == null) {
            return null; //해당 강사 없음
        }

        if (passwordEncoder.matches(form.getPassword(), loginForm.getPassword())) {
            return teacherRepository.findById(loginForm.getId()); //찾음
        }

        return null; //비밀번호 안맞음
    }
}
