package yonam.attendence.domain.student;

import lombok.Data;
import yonam.attendence.domain.parent.Parent;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class StudentParent implements Serializable {
    @NotNull
    private Student student;
    @NotNull
    private Parent parent;

    public StudentParent() {
    }

    public StudentParent(Student student, Parent parent) {
        this.student = student;
        this.parent = parent;
    }
}
