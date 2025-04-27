package springkpsgroup02.kps.Repository;

import org.apache.ibatis.annotations.*;
import springkpsgroup02.kps.Model.DTO.Request.ProfileUpdateRequest;
import springkpsgroup02.kps.Model.DTO.Response.ProfileResponse;
import springkpsgroup02.kps.Model.Entity.Profile;

import java.util.UUID;

@Mapper
public interface ProfileRepository {

    @Select("""
                SELECT * FROM app_users
                WHERE app_user_id = #{userId}
            """)
    @Results(id = "profileMapper", value = {
            @Result(property = "profileId", column = "app_user_id"),
            @Result(property = "username", column = "username"),
            @Result(property = "email", column = "email"),
            @Result(property = "level", column = "level"),
            @Result(property = "xp", column = "xp"),
            @Result(property = "profileImage", column = "profile_image"),
            @Result(property = "isVerified", column = "is_verified"),
            @Result(property = "createdAt", column = "created_at"),
    })
    ProfileResponse getCurrentUser(UUID userId);

    @Select("INSERT INTO app_users(username,email,password,profile_image) VALUES (#{p.username},#{p.email},#{p.password},#{p.profileImage}) RETURNING *")
    @ResultMap("profileMapper")
    public ProfileResponse registerUser(@Param("p") Profile profile);

    @Select("""
                UPDATE app_users
                SET username = #{profileUpdateRequest.username}, profile_image = #{profileUpdateRequest.profileImage}
                WHERE app_user_id = #{userId}
                RETURNING *;
            """)
    @ResultMap("profileMapper")
    ProfileResponse updateProfile(@Param("profileUpdateRequest") ProfileUpdateRequest profileUpdateRequest,UUID userId);

    @Delete("""
            DELETE FROM app_users
            WHERE app_user_id = #{userId}
            """)
    void deleteProfile(UUID userId);

    @Select("SELECT * FROM app_users WHERE email = #{user} OR username = #{user}")
    @Result(property = "id", column = "app_user_id")
    @Result(property = "profileImage", column = "profile_image")
    @Result(property = "isVerified", column = "is_verified")
    @Result(property = "createdAt", column = "created_at")
    public Profile getUserByEmailOrUserName(String user);

    @Update("""
                UPDATE app_users
                SET is_verified = #{isVerify}
                WHERE app_user_id = #{uuid}
            """)
    void updateProfileToVerify(UUID uuid, boolean isVerify);
}