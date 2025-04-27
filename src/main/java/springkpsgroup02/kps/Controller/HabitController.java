package springkpsgroup02.kps.Controller;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;
import springkpsgroup02.kps.Jwt.UserContext;
import springkpsgroup02.kps.Model.DTO.Request.HabitRequest;
import springkpsgroup02.kps.Model.DTO.Response.ApiResponse;
import springkpsgroup02.kps.Model.DTO.Response.BaseResponse;
import springkpsgroup02.kps.Model.Entity.Habit;
import springkpsgroup02.kps.Service.HabitService;

import java.util.List;
import java.util.UUID;

import static springkpsgroup02.kps.Jwt.UserContext.getUserIdAsUUID;

@RestController
@RequestMapping("api/v1/habit")
//@RequiredArgsConstructor
public class HabitController extends BaseResponse {

    // inject habit service
    private final HabitService habitService;
    public HabitController(HabitService habitService) {
        this.habitService = habitService;
    }

    // get all habits
    @GetMapping
    @Operation( summary = "Get All Habits")
    public ResponseEntity<ApiResponse<List<Habit>>> getAllHabits(@RequestParam (defaultValue = "1") @Positive Integer page, @RequestParam (defaultValue = "10") @Positive Integer size) {
        return responseEntity(true,"Fetched all habits successfully!",HttpStatus.OK,habitService.getAllHabits(page,size,getUserIdAsUUID()));
    }

    // get habit by id
    @GetMapping("{habit-id}")
    @Operation( summary = "Get Habit By ID")
    public ResponseEntity<ApiResponse<Habit>> getHabit(@PathVariable("habit-id") UUID habitId) {
        return responseEntity(true,"Habit fetched successfully!",HttpStatus.OK,habitService.getHabitById(habitId));

    }

    // create habit
    @PostMapping
    @Operation( summary = "Create Habit")
    public ResponseEntity<ApiResponse<Habit>> createHabit(@RequestBody @Valid HabitRequest habitRequest) {
        return responseEntity(true,"Create Habit successfully!",HttpStatus.CREATED,habitService.createHabit(habitRequest, getUserIdAsUUID()));
    }

    // update habit
    @PutMapping("{habit-id}")
    @Operation( summary = "Update Habit By ID")
    public ResponseEntity<ApiResponse<Habit>> updateHabitById(@PathVariable("habit-id") UUID habitId, @RequestBody @Valid HabitRequest habitRequest) {
        return responseEntity(true,"Update Habit successfully",HttpStatus.OK,habitService.updateHabitById(habitId,habitRequest ,getUserIdAsUUID()));
    }

    // delete habit
    @DeleteMapping("{habit-id}")
    @Operation( summary = "Delete Habit By ID")
    public ResponseEntity<ApiResponse<Habit>> deleteHabitById(@PathVariable("habit-id") UUID habitId) {
        habitService.deleteHabitById(habitId,getUserIdAsUUID());
        return responseEntity(true,"Delete Habit successfully",HttpStatus.OK,null);

    }

}

