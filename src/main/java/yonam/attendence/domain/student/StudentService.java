package yonam.attendence.domain.student;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;

    public List<Student> findByParentId(Long parentId) {
        return studentRepository.findByParendId(parentId);
    }
}
