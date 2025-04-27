package springkpsgroup02.kps.Service;

import springkpsgroup02.kps.Model.DTO.Request.HabitLogRequest;
import springkpsgroup02.kps.Model.Entity.HabitLog;

import java.util.List;
import java.util.UUID;

public interface HabitLogService {
    HabitLog createHabitLog(HabitLogRequest habitLogRequest);

    List<HabitLog> getAllHabitLogsByHabitId(UUID habitId, Integer offset, Integer limit);
}
