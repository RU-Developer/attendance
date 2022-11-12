package yonam.attendence.domain.student;

import lombok.Data;
import yonam.attendence.domain.teacher.Teacher;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class StudentTeacher implements Serializable {
    @NotNull
    private Student student;
    @NotNull
    private Teacher teacher;
}
