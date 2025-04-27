package springkpsgroup02.kps.Controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springkpsgroup02.kps.Model.DTO.Request.ProfileUpdateRequest;
import springkpsgroup02.kps.Model.DTO.Response.ApiResponse;
import springkpsgroup02.kps.Model.DTO.Response.BaseResponse;
import springkpsgroup02.kps.Model.DTO.Response.ProfileResponse;
import springkpsgroup02.kps.Model.Entity.Habit;
import springkpsgroup02.kps.Model.Entity.Profile;
import springkpsgroup02.kps.Service.HabitService;
import springkpsgroup02.kps.Service.ProfileService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/profile")
public class ProfileController extends BaseResponse {
    // inject profile service
    private final ProfileService profileService;

    // get current user profile
    @GetMapping
    @Operation( summary = "Get Current Profile")
    public ResponseEntity<ApiResponse<ProfileResponse>> getCurrentProfile() {
        return responseEntity(true, "Fetched current user successfully!", HttpStatus.OK,profileService.getCurrentUsr());
    }
    // update current user profile
    @PutMapping
    @Operation( summary = "Update Current Profile")
    public ResponseEntity<ApiResponse<ProfileResponse>> updateProfile(@RequestBody @Valid ProfileUpdateRequest profileUpdateRequest) {
        return responseEntity(true, "Updated current user successfully!", HttpStatus.OK,profileService.updateProfile(profileUpdateRequest));
    }
    // delete current user profile
    @DeleteMapping
    @Operation( summary = "Delete Current Profile")
    public ResponseEntity<ApiResponse<ProfileResponse>> deleteProfile() {
        profileService.deleteProfile();
        return responseEntity(true, "Deleted current user successfully!", HttpStatus.OK,null);
    }





}
