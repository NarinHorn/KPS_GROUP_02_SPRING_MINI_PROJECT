package springkpsgroup02.kps.Repository;

import org.apache.ibatis.annotations.*;
import springkpsgroup02.kps.Model.Entity.EmailVerification;

import java.util.UUID;

@Mapper
public interface EmailVerificationRepo {
    @Select("SELECT * FROM user_verifications WHERE user_id = #{userId}")
    @Results(id="emailMapper",value = {
            @Result(property = "expireTime",column = "expiry_time"),
            @Result(property = "verification",column = "verified_code"),
            @Result(property = "userId",column = "user_id")
    })
    public EmailVerification getUserVerifyById(UUID userId);

    @Insert("INSERT INTO user_verifications(expiry_time,verified_code,user_id) VALUES(#{e.expireTime},#{e.verification},#{e.userId})")
    public void insertUserVerify(@Param("e") EmailVerification emailVerification);

    @Delete("DELETE FROM user_verifications WHERE user_id = #{userId}")
    public void deleteOTPCodeByUserUUID(UUID userId);
}
