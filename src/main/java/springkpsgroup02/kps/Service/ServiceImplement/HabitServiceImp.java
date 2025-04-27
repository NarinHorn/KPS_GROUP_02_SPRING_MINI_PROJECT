package springkpsgroup02.kps.Service.ServiceImplement;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import springkpsgroup02.kps.Exception.InvalidException;
import springkpsgroup02.kps.Exception.NotFoundException;
import springkpsgroup02.kps.Model.DTO.Request.HabitRequest;
import springkpsgroup02.kps.Model.Entity.Habit;
import springkpsgroup02.kps.Model.Enums.Frequency;
import springkpsgroup02.kps.Repository.HabitRepository;
import springkpsgroup02.kps.Service.HabitService;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static springkpsgroup02.kps.Jwt.UserContext.getUserIdAsUUID;

@Service
@RequiredArgsConstructor
public class HabitServiceImp  implements HabitService {

    // inject Habit repository
    private final HabitRepository habitRepo;
    @Override
    public List<Habit> getAllHabits(Integer offset, Integer limit, UUID userId) {
        return habitRepo.findAllHabits(offset,limit, userId);
    }

    @Override
    public Habit getHabitById(UUID habitId) {
        Habit habit = habitRepo.findHabitById(habitId);
        if(habit == null) {
            throw new NotFoundException("Habit ID " + habitId + " not found");
        }
        return habit;
    }

    @Override
    public Habit createHabit(HabitRequest habitRequest , UUID userId) {

        boolean isFrequencyFound = false;
        for (Frequency frequency : Frequency.values()) {
            if (frequency.name().equalsIgnoreCase(habitRequest.getFrequency())) {
                isFrequencyFound = true;
                break;
            }
        }

        if(!isFrequencyFound) {
            throw new InvalidException("Allow Only [WEEKLY, MONTHLY, DAILY]");
        }

        return habitRepo.insertHabit(habitRequest , userId);
    }

    @Override
    public Habit updateHabitById(UUID habitId, HabitRequest habitRequest, UUID userId) {
        Habit habit = habitRepo.updateHabitById(habitId, habitRequest,userId);
        if(habit == null) {
            throw new NotFoundException("Habit ID " + habitId + " not found");
        }
        boolean isFrequencyFound = false;
        for (Frequency frequency : Frequency.values()) {
            if (frequency.name().equalsIgnoreCase(habitRequest.getFrequency())) {
                isFrequencyFound = true;
                break;
            }
        }
        if(!isFrequencyFound) {
            throw new InvalidException("Allow Only [WEEKLY, MONTHLY, DAILY]");
        }

        return habit;
    }

    @Override
    public void deleteHabitById(UUID habitId ,UUID userId) {
        Habit habit = habitRepo.findHabitById(habitId);
        if(habit == null) {
            throw new NotFoundException("Habit ID " + habitId + " not found");
        }
        habitRepo.deleteHabitById(habitId, userId)
        ;
    }


}
