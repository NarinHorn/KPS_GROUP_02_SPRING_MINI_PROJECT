package springkpsgroup02.kps.Model.Entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HabitLog {
    private UUID habitLogId;
    private Date logDate;
    @Schema(defaultValue = "COMPLETED")
    private String status;
    private Integer xpEarned;
    private Habit habit;
}
