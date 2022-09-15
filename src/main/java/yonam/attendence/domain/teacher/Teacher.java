package yonam.attendence.domain.teacher;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class Teacher {

    @NotEmpty
    private String id;

    @NotEmpty
    private String name;

    @NotEmpty
    private String password;
}
