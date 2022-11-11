package yonam.attendence.domain.parent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yonam.attendence.domain.student.Student;
import yonam.attendence.domain.student.StudentRepository;
import yonam.attendence.web.parent.ParentLoginForm;

import java.util.List;

@Transactional
@Slf4j
@Service
@RequiredArgsConstructor
public class ParentService {

    private final ParentRepository parentRepository;
    private final StudentRepository studentRepository;

    public Parent login(ParentLoginForm form) {

        Parent parent = parentRepository.findByPhone(form.getPhone());

        if (parent == null) {
            return null; //부모님 폰과 번호에 매칭 없음
        }

        return parent;
    }

    public Parent findById(Long parentId) {
        return parentRepository.findById(parentId);
    }

    public List<Student> children(Long parentId) {
        return studentRepository.findByParentId(parentId);
    }
}
