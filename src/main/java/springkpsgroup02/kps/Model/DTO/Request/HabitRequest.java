package springkpsgroup02.kps.Model.DTO.Request;

import jakarta.validation.constraints.NotNull;
import springkpsgroup02.kps.Model.Enums.Frequency;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.UUID;
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class HabitRequest {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Frequency is required")
    private String frequency;
}
