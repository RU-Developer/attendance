package yonam.attendence.domain.teacher;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
public class TeacherAddForm implements Serializable {

    @NotEmpty
    private String id;

    @NotEmpty
    private String password;

    @NotEmpty
    private String name;

    @NotEmpty
    private String phone;
}
