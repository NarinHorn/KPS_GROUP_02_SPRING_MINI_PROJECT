package springkpsgroup02.kps.Repository;

import org.apache.ibatis.annotations.*;
import springkpsgroup02.kps.Model.DTO.Request.HabitLogRequest;
import springkpsgroup02.kps.Model.Entity.HabitLog;

import java.util.List;
import java.util.UUID;

@Mapper
public interface HabitLogRepository {
    // insert habit log
    @Select("""
            INSERT INTO habit_logs(status, habit_id,xp_earned)
            values (#{habitLog.status},#{habitLog.habitId},10)
            
            returning *
            """)
    @Results(id = "habitLogMapper", value = {
            @Result(property = "habitLogId", column = "habit_log_id"),
            @Result(property = "logDate", column = "log_date"),
            @Result(property = "status", column = "status"),
            @Result(property = "xpEarned", column = "xp_earned"),
            @Result(property = "habit", column = "habit_id",
                    one = @One(select = "springkpsgroup02.kps.Repository.HabitRepository.findHabitById")
            )
    })
    HabitLog createHabitLog(@Param("habitLog") HabitLogRequest habitLogRequest);
    // get all habit logs
    @Select("""
            select * from habit_logs where habit_id = #{habitId}
            offset #{limit} * (#{offset} - 1)
            limit #{limit}
            """)
    @ResultMap("habitLogMapper")
    List<HabitLog> findAllHabitLogsByHabitId(UUID habitId, Integer offset, Integer limit);

    @Update("""
            update app_users set xp = xp + 10
            where app_user_id = #{appUserId}
            """)
    void updateUserXp(UUID appUserId);

    @Select("""
    SELECT xp from app_users
    WHERE app_user_id = #{appUserId}
    """)
    @Result(property = "xp" , column = "xp")
    Integer getXp(UUID appUserId);
    @Update("""
    UPDATE app_users SET level = #{level}
    where app_user_id = #{appUserId}
    """)
    void updateLevel(Integer level , UUID appUserId);
}
