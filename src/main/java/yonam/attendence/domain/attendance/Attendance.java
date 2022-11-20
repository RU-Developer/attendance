package yonam.attendence.domain.attendance;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.time.LocalDate;

@Data
public class Attendance implements Serializable {
    @NotEmpty
    private Long id;
    @NotEmpty
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateAttendance;
    @NotEmpty
    private Long studentId;
}
