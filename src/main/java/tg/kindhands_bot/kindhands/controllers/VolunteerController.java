package tg.kindhands_bot.kindhands.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.api.objects.Update;
import tg.kindhands_bot.kindhands.entities.User;
import tg.kindhands_bot.kindhands.entities.Volunteer;
import tg.kindhands_bot.kindhands.services.UserService;
import tg.kindhands_bot.kindhands.services.VolunteerService;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/volunteer")
@Tag(name = "Волонтеры.", description = "Эндпоинты для работы волонтеров.")
public class VolunteerController {

    private final VolunteerService volunteerService;


    public VolunteerController(VolunteerService volunteerService) {
        this.volunteerService = volunteerService;
    }

    /**
     * Выводит не проверенные доклады пользователей о животных.
     * -----||-----
     * Displays unverified user reports about animals.
     */
    @GetMapping("/reports") // GET http://localhost:8080/volunteer/reports
    @Operation(summary = "Получение отчетов")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Отчет получен."
            )})

    public ResponseEntity<?> getReports() {
        return ResponseEntity.ok().build();
    }


    /**
     * Удалить волонтера.
     * -----||-----
     * delete volunteer
     */
    @DeleteMapping("{id}") //GET http://localhost:8080/volunteer/1
    @Operation(summary = "Удаление волонтера по id.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Волонтер удален."
            )})
    public ResponseEntity<?> deleteVolunteer(@PathVariable Long id) {
        volunteerService.deleteVolunteer(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Выводит всех волонтеров.
     * -----||-----
     * get all volunteers
     */
    @GetMapping //GET http://localhost:8080/volunteer
    @Operation(summary = "Получение всех волонтеров.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Все волонтеры получены."
            )
    })
    public ResponseEntity<List<Volunteer>> getAllVolunteers() {
        List<Volunteer> result = volunteerService.getAllVolunteers();
        if (result.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    /**
     * Волонтер меняет поле пользователя needHelp на false после оказания помощи
     * -----||-----
     * The volunteer changes the user's needHelp field to false after providing assistance
     */
    @PutMapping("/adopted/help/change/{id}")//GET http://localhost:8080/volunteer/adopted/help/change/3
    @Operation(summary = "Изменение волонтером статуса пользователя после оказания помощи")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Помощь оказана."
            )
    })
    public ResponseEntity<Collection<User>> changeIsNeedHelp(@RequestParam(required = false) Long id) {
        return ResponseEntity.ok().build();
    }
}
