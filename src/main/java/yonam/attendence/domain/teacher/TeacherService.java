package yonam.attendence.domain.teacher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yonam.attendence.web.teacher.TeacherAddResult;
import yonam.attendence.web.teacher.TeacherAddResultConst;

import java.sql.SQLException;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class TeacherService {

    private final TeacherRepository teacherRepository;
    private final PasswordEncoder passwordEncoder;

    public TeacherAddResult save(Teacher teacher) {
        Teacher byId = teacherRepository.findById(teacher.getId());

        if (byId != null) { //이미 해당 아이디로 등록된 회원이 있다면
            return new TeacherAddResult(false, TeacherAddResultConst.DUPLICATE_ID);
        }

        String encodedPassword = passwordEncoder.encode(teacher.getPassword());
        teacher.setPassword(encodedPassword);

        Teacher save = null;
        String message = null;

        try {
            save = teacherRepository.save(teacher);
        } catch (SQLException e) {
            message = e.getMessage();
        }

        if (save == null) {
            message = message == null ? "데이터베이스 오류 발생" : message;
            return new TeacherAddResult(false, message);
        }

        return new TeacherAddResult(true, null);
    }
}
