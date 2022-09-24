package yonam.attendence.domain.parent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import yonam.attendence.domain.student.Student;
import yonam.attendence.domain.student.StudentRepository;
import yonam.attendence.web.parent.ParentLoginForm;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParentService {

    private final ParentRepository parentRepository;
    private final StudentRepository studentRepository;

    public Parent login(ParentLoginForm form) {
        Student student = studentRepository.findByPhone(form.getStudentPhone());

        if (student == null) {
            return null; //학생 폰번호 매칭 없음
        }

        Parent parent = parentRepository.findById(student.getParentId());

        if (parent == null) {
            return null; //부모 폰번호 매칭 없음
        }

        if (parent.getPhone().equals(form.getParentPhone())) {
            return parent; //찾음
        }

        return null; //부모 폰번호 일치 안함
    }
}
