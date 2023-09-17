package tg.kindhands_bot.kindhands.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/volunteer/user")
@Tag(name = "Пользователи.", description = "Эндпоинты для работы волонтера с пользователями.")
public class UserController {
    /**
     * Добавляет пользователя в черный список, с указанной причиной.
     * -----||-----
     * Adds a user to the blacklist, with the specified reason.
     */
    @PutMapping("/blacklist/add/{id}") // POST http://localhost:8080/volunteer/user/blacklist/add/1?messageBlock=message
    @Operation(summary = "Занесение пользователя в черный список(блокировка)")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Пользователь заблокирован и причина указана."
            )})
    public ResponseEntity<?> addUserBlacklist(@PathVariable Long id, @RequestParam String messageBlock) {
        return ResponseEntity.ok().build();
    }

    /**
     * Добавляет пользователю, взятое им, животное.
     * -----||-----
     * Adds an animal taken by the user.
     */
    @PutMapping("/addAnimal/{id}") // POST http://localhost:8080/volunteer/user/addAnimal/1?idAnimal=2
    @Operation(summary = "Добавление животного пользователю.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Пользователю добавлено животное."
            )})
    public ResponseEntity<?> addUserAnimal(@PathVariable Long id, @RequestParam Long idAnimal) {
        return ResponseEntity.ok().build();
    }

    /**
     * Продлевает испытательный срок пользователю.
     * -----||-----
     * Extends the probation period to the user.
     */
    @PutMapping("/term/{id}") // POST http://localhost:8080/volunteer/user/term/1?term=2
    @Operation(summary = "Продление испытательного срока.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Пользователю продлен испытательный срок."
            )})
    public ResponseEntity<?> extendProbationPeriod(@PathVariable Long id, @RequestParam Integer term) {
        return ResponseEntity.ok().build();
    }
}
