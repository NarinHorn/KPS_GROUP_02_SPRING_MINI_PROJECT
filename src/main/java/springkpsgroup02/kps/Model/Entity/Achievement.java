package springkpsgroup02.kps.Model.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Achievement {
    private UUID achievementId;
    private String title;
    private String description;
    private String badge;
    private Integer xpRequired;
}
