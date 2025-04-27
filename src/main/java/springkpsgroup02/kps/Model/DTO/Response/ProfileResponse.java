package springkpsgroup02.kps.Model.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileResponse {
    private UUID profileId;
    private String username;
    private String email;
    private Integer level;
    private Integer xp;
    private String profileImage;
    private Boolean isVerified;
    private Date createdAt;
}
