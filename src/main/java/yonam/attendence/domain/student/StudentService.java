package yonam.attendence.domain.student;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yonam.attendence.domain.parent.Parent;
import yonam.attendence.domain.parent.ParentRepository;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final ParentRepository parentRepository;

    public Student findById(Long id) {
        return studentRepository.findById(id);
    }

    public List<StudentParent> studentParentWithTeacherLesson(Long lesson) {
        return studentRepository.studentParentByTeacherLesson(lesson);
    }

    public StudentParent saveStudentParent(StudentParent studentParent) {
        log.info("student name = {}, parent name = {}", studentParent.getStudent().getName(), studentParent.getParent().getName());
        Parent parent = parentRepository.findByPhone(studentParent.getParent().getPhone());

        if (parent == null) {
            log.info("save parent");
            parentRepository.save(studentParent.getParent());
            parent = parentRepository.findByPhone(studentParent.getParent().getPhone());
        }

        log.info("parent_id = {}", parent.getId());

        List<Student> students = studentRepository.findByParentId(parent.getId());
        studentParent.getStudent().setParentId(parent.getId());

        log.info("students = {}", students);
        boolean flag = true;
        if (students != null) {
            for (Student student : students) {
                if (student.getName().equals(studentParent.getStudent().getName())) {
                    flag = false;
                    break;
                }
            }
        }

        if (flag) {
            studentParent.getStudent().setRegDate(LocalDate.now()); //요청으로 온게 언제던간에 오늘로 변경(서버시간으로)
            Student student = studentRepository.save(studentParent.getStudent());
            log.info("student save = {}", student);
        }

        return studentParent;
    }
}
