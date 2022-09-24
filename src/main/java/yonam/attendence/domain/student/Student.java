package yonam.attendence.domain.student;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class Student {
    @NotEmpty
    private Long id;
    @NotEmpty
    private Long parentId;
    @NotEmpty
    private String phone;
    @NotEmpty
    private String name;
}
