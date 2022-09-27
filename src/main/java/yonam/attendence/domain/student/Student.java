package yonam.attendence.domain.student;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

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
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate regDate;

    @NotEmpty
    private Long parentId;

    @NotEmpty Long teacherLesson;
}
