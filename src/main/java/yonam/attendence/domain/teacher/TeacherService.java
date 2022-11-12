package yonam.attendence.domain.teacher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yonam.attendence.domain.login.LoginForm;
import yonam.attendence.domain.login.LoginRepository;
import yonam.attendence.web.teacher.TeacherAddResult;
import yonam.attendence.web.teacher.TeacherAddResultConst;

import java.sql.SQLException;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class TeacherService {

    private final TeacherRepository teacherRepository;
    private final LoginRepository loginRepository;
    private final PasswordEncoder passwordEncoder;

    public TeacherAddResult save(TeacherAddForm form) {
        LoginForm login = loginRepository.findById(form.getId());

        if (login != null) { //이미 해당 아이디로 등록된 회원이 있다면
            return new TeacherAddResult(false, TeacherAddResultConst.DUPLICATE_ID);
        }
        login = new LoginForm();
        String encodedPassword = passwordEncoder.encode(form.getPassword());
        login.setId(form.getId());
        login.setPassword(encodedPassword);

        LoginForm save = null;
        Teacher saveTeacher = null;
        String message = null;

        try {
            save = loginRepository.save(login);
            saveTeacher = new Teacher();
            saveTeacher.setLyceumId(1L);
            saveTeacher.setLoginId(form.getId());
            saveTeacher.setName(form.getName());
            saveTeacher.setPhone(form.getPhone());
            saveTeacher = teacherRepository.save(saveTeacher);
        } catch (SQLException e) {
            message = e.getMessage();
        }

        if (save == null || saveTeacher == null) {
            message = message == null ? "데이터베이스 오류 발생" : message;
            return new TeacherAddResult(false, message);
        }

        return new TeacherAddResult(true, null);
    }
}
