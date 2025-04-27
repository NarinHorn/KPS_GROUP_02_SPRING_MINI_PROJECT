package springkpsgroup02.kps.Repository;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import springkpsgroup02.kps.Model.Entity.Achievement;

import java.util.List;
import java.util.UUID;

@Mapper
@Repository
public interface AchievementRepository {

    @Results(id = "achievementMapper", value = {
            @Result(property = "achievementId", column = "achievement_id"),
            @Result(property = "title", column = "title"),
            @Result(property = "description", column = "description"),
            @Result(property = "badge", column = "badge"),
            @Result(property = "xpRequired", column = "xp_required")
    })
    @Select("SELECT * FROM achievements LIMIT #{size} OFFSET #{size} * (#{page} - 1)")
    List<Achievement> retrievedAllAchievement( @Param("page") Integer page , @Param("size") Integer size);

    @ResultMap("achievementMapper")
    @Select("""
        SELECT * FROM achievements
        WHERE xp_required <= #{xp}
        AND achievement_id NOT IN (
            SELECT achievement_id FROM app_user_achievements
            WHERE app_user_id = #{appUserId}
        )
        LIMIT #{size} OFFSET #{size} * (#{page} - 1)
    """)
    // get achievement by current user id and xp completed requirement
    List<Achievement> retrievedAchievementById(@Param("appUserId") UUID appUserId,
                                               @Param("xp") Integer xp,
                                               @Param("page") Integer page,
                                               @Param("size") Integer size);


}
