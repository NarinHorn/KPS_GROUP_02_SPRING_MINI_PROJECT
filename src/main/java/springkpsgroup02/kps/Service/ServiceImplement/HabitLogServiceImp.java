package springkpsgroup02.kps.Service.ServiceImplement;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import springkpsgroup02.kps.Exception.InvalidException;
import springkpsgroup02.kps.Exception.NotFoundException;
import springkpsgroup02.kps.Jwt.UserContext;
import springkpsgroup02.kps.Model.DTO.Request.HabitLogRequest;
import springkpsgroup02.kps.Model.Entity.Habit;
import springkpsgroup02.kps.Model.Entity.HabitLog;
import springkpsgroup02.kps.Model.Enums.Status;
import springkpsgroup02.kps.Repository.HabitLogRepository;
import springkpsgroup02.kps.Repository.HabitRepository;
import springkpsgroup02.kps.Service.HabitLogService;

import java.util.List;
import java.util.UUID;

import static springkpsgroup02.kps.Jwt.UserContext.getUserIdAsUUID;

@Service
@RequiredArgsConstructor
public class HabitLogServiceImp implements HabitLogService {
    private final HabitLogRepository habitLogRepository;
    private final HabitRepository habitRepository;
    @Override
    public HabitLog createHabitLog(HabitLogRequest habitLogRequest) {
        boolean isStatusFound = false;
        Habit habit = habitRepository.findHabitById(habitLogRequest.getHabitId());
        if (habit == null) {
            throw new NotFoundException("Habit ID " + habitLogRequest.getHabitId() + " not found");
        }
        HabitLog habitLog = habitLogRepository.createHabitLog(habitLogRequest);
        for (Status status : Status.values()) {
            if (status.name().equalsIgnoreCase(habitLogRequest.getStatus())) {
                isStatusFound = true;
                break;
            }
        }
        if(!isStatusFound) {
            throw new InvalidException("Allow Only [COMPLETED, MISSED]");
        }
        if(habitLog == null) {
           throw new NotFoundException("Habit ID " + habitLogRequest.getHabitId() + " not found");
        }

       habitLogRepository.updateUserXp(getUserIdAsUUID());
         Integer xp = habitLogRepository.getXp(getUserIdAsUUID());
        if (xp >= 100) {
            int newLevel = (xp/100);
            habitLogRepository.updateLevel(newLevel, getUserIdAsUUID());
            }
        return habitLog;
    }
    @Override
    public List<HabitLog> getAllHabitLogsByHabitId(UUID habitId, Integer offset, Integer limit) {
        Habit habit = habitRepository.findHabitById(habitId);
        if (habit == null) {
            throw new NotFoundException("Habit ID " + habitId + " not found");
        }
        return habitLogRepository.findAllHabitLogsByHabitId(habitId, offset, limit);
    }
}
