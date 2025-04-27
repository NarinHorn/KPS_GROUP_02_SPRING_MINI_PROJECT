package springkpsgroup02.kps.Model.DTO.Request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AuthRequest {

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    @Email(message = "must be a well-formed email address")
    private String identifier;

    @NotBlank(message = "Password is required")
    @Schema(defaultValue = "PASSWORD")
    private String password;
}
