package yonam.attendence.domain.attendance;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class Attendance {
    @NotEmpty
    private Long id;
    @NotEmpty
    private LocalDate dateAttendance;

    private LocalDateTime inTime;
    private LocalDateTime outTime;

    @NotEmpty
    private Long student_id;
}
