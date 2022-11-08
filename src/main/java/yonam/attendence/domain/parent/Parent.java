package yonam.attendence.domain.parent;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
public class Parent implements Serializable {

    private Long id;

    @NotEmpty
    private String name;

    @NotEmpty
    private String phone;
}
