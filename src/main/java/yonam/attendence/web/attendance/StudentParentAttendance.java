package yonam.attendence.web.attendance;

import lombok.Data;
import yonam.attendence.domain.parent.Parent;
import yonam.attendence.domain.student.Student;

import javax.validation.constraints.NotEmpty;

@Data
public class StudentParentAttendance {
    @NotEmpty
    private Student student;
    @NotEmpty
    private Parent parent;

    public StudentParentAttendance(Student student, Parent parent) {
        this.student = student;
        this.parent = parent;
    }
}
