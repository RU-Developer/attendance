package yonam.attendence.domain.student;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

@Data
public class Student {
    @NotEmpty
    private Long id;

    @NotEmpty
    private String name;

    private String phone;

    @NotEmpty
    private Long tuition;

    @NotEmpty
    private LocalDate regdate;
}
