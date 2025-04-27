package springkpsgroup02.kps.Service;

import org.springframework.stereotype.Service;
import springkpsgroup02.kps.Model.DTO.Request.ProfileUpdateRequest;
import springkpsgroup02.kps.Model.DTO.Response.ProfileResponse;
import springkpsgroup02.kps.Model.Entity.Profile;


public interface ProfileService {
    ProfileResponse getCurrentUsr();
    ProfileResponse updateProfile(ProfileUpdateRequest profileUpdateRequest);

    void deleteProfile();
}
