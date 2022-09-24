package yonam.attendence.domain.parent;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class Parent {

    @NotEmpty
    private Long id;

    @NotEmpty
    private String phone;
}
