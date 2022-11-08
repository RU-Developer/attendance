package yonam.attendence.domain.teacher;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
public class Teacher implements Serializable {

    @NotEmpty
    private Long lesson;

    @NotEmpty
    private String id;

    @NotEmpty
    private String password;

    @NotEmpty
    private String name;
}
