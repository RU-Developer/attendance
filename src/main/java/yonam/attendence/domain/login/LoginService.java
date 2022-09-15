package yonam.attendence.domain.login;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yonam.attendence.domain.teacher.Teacher;
import yonam.attendence.domain.teacher.TeacherRepository;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final TeacherRepository teacherRepository;

    /**
     * @return null 로그인 실패
     */
    public Teacher login(String id, String password) {
        Teacher teacher = teacherRepository.findById(id);

        if (password.equals(teacher.getPassword())) {
            return teacher;
        }

        return null;
    }
}
