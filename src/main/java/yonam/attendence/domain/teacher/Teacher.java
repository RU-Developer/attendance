package yonam.attendence.domain.teacher;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
public class Teacher implements Serializable {

    private Long lesson;

    @NotEmpty
    private String name;

    private String belong;
    private String phone;
    private String loginId;

    private Long lyceumId;
}
