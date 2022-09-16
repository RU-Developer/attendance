package yonam.attendence.domain.login;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import yonam.attendence.domain.teacher.Teacher;
import yonam.attendence.domain.teacher.TeacherRepository;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final TeacherRepository teacherRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * @return null 로그인 실패
     */
    public Teacher login(String id, String password) {
        Teacher teacher = teacherRepository.findById(id);

        if (teacher == null) {
            return null; //해당 강사 없음
        }

        if (passwordEncoder.matches(password, teacher.getPassword())) {
            return teacher; //찾음
        }

        return null; //비밀번호 안맞음
    }
}
