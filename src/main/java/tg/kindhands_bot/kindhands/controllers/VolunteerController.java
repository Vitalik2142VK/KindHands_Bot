package tg.kindhands_bot.kindhands.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.api.objects.Update;
import tg.kindhands_bot.kindhands.entities.User;
import tg.kindhands_bot.kindhands.entities.Volunteer;
import tg.kindhands_bot.kindhands.enums.ReportStatus;
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
     * Выводит не проверенные отчеты пользователей о животных.
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
        return ResponseEntity.ok(volunteerService.getReports());
    }

    /**
     * Меняет статус отчета, указанный волонтером.
     * -----||-----
     * Changes the status of the report specified by the volunteer.
     */
    @PutMapping("/reports/{id}") // GET http://localhost:8080/volunteer/reports
    @Operation(summary = "Изменить статус отчета")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Статус отчета изменен."
            )})
    public ResponseEntity<?> changeStatusReport(@PathVariable Long id, @RequestParam String messageUser, @RequestParam ReportStatus reportStatus) {
        return ResponseEntity.ok(volunteerService.changeStatusReport(id, messageUser, reportStatus));
    }


    /**
     * Принимает кандидата в волонтеры
     * -----||-----
     * Accepts a volunteer candidate
     */
    @PutMapping("{id}") // PUT http://localhost:8080/volunteer/2
    @Operation(summary = "Принять волонтера")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Волонтер принят."
            )})
    public ResponseEntity<?> addVolunteer(@PathVariable Long id) {
        return ResponseEntity.ok(volunteerService.addVolunteer(id));
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
        return ResponseEntity.ok(volunteerService.deleteVolunteer(id));
    }

    /**
     * Получение всех действующих волонтеров
     * -----||-----
     * Getting all active volunteers
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
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(result);
    }

    /**
     * Выводит список всех желающих стать волонтером.
     * -----||-----
     * Displays a list of everyone who wants to become a volunteer.
     */
    @GetMapping("/become") //GET http://localhost:8080/volunteer/become
    @Operation(summary = "получить всех желающих стать волонтером.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Все желающие получены."
            )
    })
    public ResponseEntity<List<Volunteer>> getAllBecomeVolunteers() {
        List<Volunteer> result = volunteerService.getAllBecomeVolunteers();
        if (result.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(result);
    }


//    @PostMapping("/add") // GET http://localhost:8080/volunteer/add
//    @Operation(summary = "Добавление волонтера.")
//    @ApiResponses(value = {
//            @ApiResponse(
//                    responseCode = "200",
//                    description = "Волонтер добавлен"
//            )
//    })
//    public ResponseEntity<Volunteer> addSVolunteer(@RequestBody Volunteer volunteer) {
//        Volunteer addedVolunteer = volunteerService.addVolunteer(update, volunteer.getPhone());
//        return ResponseEntity.ok(addedVolunteer);
//    }
}
