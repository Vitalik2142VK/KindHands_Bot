package tg.kindhands_bot.kindhands.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/volunteer/animal")
@Tag(name = "Животные.", description = "Эндпоинты для работы с животными.")
public class AnimalController {
    /**
     * Выводит список всех животных.
     * -----||-----
     * Displays a list of all animals.
     */
    @GetMapping("/getAll") // GET http://localhost:8080/volunteer/animal/getAll
    @Operation(summary = "Получить всех животных")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Все животные получены."
            )})
    public ResponseEntity<?> getAllAnimals() {
        return ResponseEntity.ok().build();
    }

    /**
     * Добавляет новое животное.
     * -----||-----
     * Adds a new animal.
     */
    @PostMapping("/add") // POST http://localhost:8080/volunteer/animal
    @Operation(summary = "Добавить животное")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Животное добавлено."
            )})
    public ResponseEntity<?> addAnimal() {
        return ResponseEntity.ok().build();
    }

    /**
     * Изменяет статус животного.
     * -----||-----
     * Changes the status of the animal.
     */
    @PutMapping("/{id}") // POST http://localhost:8080/volunteer/animal/1
    @Operation(summary = "Изменение статуса животного")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Статус животного изменен."
            )})
    public ResponseEntity<?> changeStatusAnimal(@PathVariable Long id) {
        return ResponseEntity.ok().build();
    }

    /**
     * Удаляет животное из БД.
     * -----||-----
     * Removes an animal from the database.
     */
    @DeleteMapping("/{id}") // POST http://localhost:8080/volunteer/animal/1
    @Operation(summary = "Удалить животное")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Животное удалено."
            )})
    public ResponseEntity<?> removeAnimal(@PathVariable Long id) {
        return ResponseEntity.ok().build();
    }

}
