package springkpsgroup02.kps.Model.DTO.Request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor@Builder
public class HabitLogRequest {
    @Schema(defaultValue = "COMPLETED")
    @NotBlank(message = "Status is required")
    private String status;
    private UUID habitId;
}
