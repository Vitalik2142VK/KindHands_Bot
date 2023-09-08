package tg.kindhands_bot.kindhands.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/volunteer/animal")
public class AnimalController {
    /**
     * Выводит список всех животных.
     * -----||-----
     * Displays a list of all animals.
     */
    @GetMapping("/getAll") // GET http://localhost:8080/volunteer/animal/getAll
    public ResponseEntity<?> getAllAnimals() {
        return ResponseEntity.ok().build();
    }

    /**
     * Добавляет новое животное.
     * -----||-----
     * Adds a new animal.
     */
    @PostMapping("/add") // POST http://localhost:8080/volunteer/animal
    public ResponseEntity<?> addAnimal() {
        return ResponseEntity.ok().build();
    }

    /**
     * Изменяет статус животного.
     * -----||-----
     * Changes the status of the animal.
     */
    @PutMapping("/{id}") // POST http://localhost:8080/volunteer/animal/1
    public ResponseEntity<?> changeStatusAnimal(@PathVariable Long id) {
        return ResponseEntity.ok().build();
    }

    /**
     * Удаляет животное из БД.
     * -----||-----
     * Removes an animal from the database.
     */
    @DeleteMapping("/{id}") // POST http://localhost:8080/volunteer/animal/1
    public ResponseEntity<?> removeAnimal(@PathVariable Long id) {
        return ResponseEntity.ok().build();
    }

}
