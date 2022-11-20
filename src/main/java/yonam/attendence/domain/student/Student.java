package yonam.attendence.domain.student;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
public class Student implements Serializable {
    private Long id;

    @NotEmpty
    private String name;

    private String phone;

    @NotEmpty
    private Long parentId;

    @NotEmpty
    Long teacherLesson;
}
