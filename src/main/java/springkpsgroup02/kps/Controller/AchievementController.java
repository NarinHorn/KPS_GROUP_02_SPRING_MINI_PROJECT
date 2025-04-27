package springkpsgroup02.kps.Controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springkpsgroup02.kps.Jwt.UserContext;
import springkpsgroup02.kps.Model.DTO.Response.ApiResponse;
import springkpsgroup02.kps.Model.DTO.Response.BaseResponse;
import springkpsgroup02.kps.Model.Entity.Achievement;
import springkpsgroup02.kps.Repository.ProfileRepository;
import springkpsgroup02.kps.Service.AchievementService;


import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/achievements")
public class AchievementController extends BaseResponse {

    private final AchievementService achievementService;
    private final ProfileRepository profileRepository;



    // get all achievement

    @GetMapping
    @Operation( summary = "Get All Achievement")
    public ResponseEntity<ApiResponse<List<Achievement>>> getAllAchievement(
            @RequestParam(defaultValue = "1") @Positive Integer page,
            @RequestParam(defaultValue = "10") @Positive Integer size )

    {
        List<Achievement> achievementList = achievementService.getAllAchievement(page , size );

        return responseEntity(true , "Achievements retrieved successfully!", HttpStatus.OK , achievementList);
    }
    // get achievement by current user id and xp completed requirement
    @GetMapping("/app-users")
    @Operation( summary = "Get All Achievement By Current User")
    public ResponseEntity<ApiResponse<List<Achievement>>> getAllAchievementByAppUser(
            @RequestParam(defaultValue = "1") @Positive Integer page,
            @RequestParam(defaultValue = "10") @Positive Integer size)
    {
        UUID uuid = UUID.fromString(UserContext.getUserId());
        Integer xp = profileRepository.getCurrentUser(uuid).getXp();
        List<Achievement> achievementListByUser = achievementService.getAchievementById( uuid , xp ,page , size );

        return responseEntity(
                true ,
                "Achievements for the specified App User retrieved successfully!" ,
                HttpStatus.OK, achievementListByUser);

    }
}

