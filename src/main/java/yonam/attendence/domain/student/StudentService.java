package yonam.attendence.domain.student;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yonam.attendence.domain.attendance.AttendanceRepository;
import yonam.attendence.domain.parent.Parent;
import yonam.attendence.domain.parent.ParentRepository;
import yonam.attendence.domain.teacher.Teacher;
import yonam.attendence.domain.teacher.TeacherRepository;
import yonam.attendence.domain.util.Util;

import java.util.List;
import java.util.Objects;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final ParentRepository parentRepository;
    private final TeacherRepository teacherRepository;
    private final AttendanceRepository attendanceRepository;

    public Student findById(Long id) {
        log.info("StudentService.findById id = {}", id);
        return studentRepository.findById(id);
    }

    public List<StudentParent> studentParentWithTeacherLesson(Long lesson) {
        log.info("StudentService.studentParentWithTeacherLesson lesson = {}", lesson);
        return studentRepository.studentParentByTeacherLesson(lesson);
    }

    public void withdraw(Long studentId, Long lesson) {
        log.info("StudentService.withdraw studentId = {}, lesson = {}", studentId, lesson);
        Student student = studentRepository.findById(studentId);
        if (student == null) {
            return;
        }

        if (!Objects.equals(student.getTeacherLesson(), lesson)) {
            return;
        }

        Parent parent = parentRepository.findById(student.getParentId());
        if (parent == null) {
            return;
        }

        attendanceRepository.deleteByStudentId(studentId);
        studentRepository.delete(studentId);

        List<Student> byParentId = studentRepository.findByParentId(parent.getId());

        if (byParentId == null || byParentId.size() == 0) {
            parentRepository.delete(parent.getId());
        }
    }

    public void updateStudentParent(StudentParent studentParent, Long lesson) {
        log.info("StudentService.updateStudentParent lesson = {}", lesson);
        log.info("studentid = {}", studentParent.getStudent().getId());
        Student student = studentRepository.findById(studentParent.getStudent().getId());
        if (student == null) {
            log.info("student == null");
            return;
        }

        if (!Objects.equals(student.getTeacherLesson(), lesson)) {
            return;
        }

        log.info("parentid = {}", student.getParentId());
        Parent parent = parentRepository.findById(student.getParentId());
        if (parent == null) {
            log.info("parent == null");
            return;
        }

        student.setPhone(Util.deleteSpecialSymbolsAtPhoneNumber(studentParent.getStudent().getPhone()));
        student.setName(studentParent.getStudent().getName());

        parent.setPhone(Util.deleteSpecialSymbolsAtPhoneNumber(studentParent.getParent().getPhone()));
        parent.setName(studentParent.getParent().getName());

        studentRepository.update(student);
        parentRepository.update(parent);
    }

    public StudentParent saveStudentParent(StudentParent studentParent) {
        log.info("StudentService.saveStudentParent");
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
            Student student = studentRepository.save(studentParent.getStudent());
            log.info("student save = {}", student);
        }

        return studentParent;
    }

    public StudentTeacher findStudentTeacher(Long studentId, Long parentId) {
        log.info("StudentService.findStudentTeacher studentId = {}, parentId = {}", studentId, parentId);
        Student student = studentRepository.findById(studentId);
        if (!Objects.equals(student.getParentId(), parentId)) {
            return null;
        }

        Teacher teacher = teacherRepository.findByLesson(student.getTeacherLesson());
        StudentTeacher result = new StudentTeacher();
        result.setStudent(student);
        result.setTeacher(teacher);
        return result;
    }
}
