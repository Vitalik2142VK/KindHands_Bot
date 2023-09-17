package tg.kindhands_bot.kindhands.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tg.kindhands_bot.kindhands.entities.Animal;
import tg.kindhands_bot.kindhands.services.AnimalService;

import java.util.Collection;

@RestController
@RequestMapping("/volunteer/animal")
public class AnimalController {
    private final AnimalService animalService;

    public AnimalController(AnimalService animalService) {
        this.animalService = animalService;
    }

    /**
     * Выводит список всех животных.
     * -----||-----
     * Displays a list of all animals.
     */
    @GetMapping("/getAll") // GET http://localhost:8080/volunteer/animal/getAll
    public ResponseEntity<Collection<Animal>> getAllAnimals() {
        return ResponseEntity.ok(animalService.getAllAnimals());
    }

    /**
     * Добавляет новое животное.
     * -----||-----
     * Adds a new animal.
     */
    @PostMapping("/add") // POST http://localhost:8080/volunteer/animal
    public Animal addAnimal(@RequestBody Animal animal) {
        return animalService.addAnimal(animal);
    }

//    /**
//     * Изменяет статус животного.
//     * -----||-----
//     * Changes the status of the animal.
//     */
//    @PutMapping("/{id}") // POST http://localhost:8080/volunteer/animal/1
//    public ResponseEntity<?> changeStatusAnimal(@PathVariable Long id) {
//        return ResponseEntity.ok().build();
//    }

    /**
     * Удаляет животное из БД.
     * -----||-----
     * Removes an animal from the database.
     */
    @DeleteMapping("/{id}") // POST http://localhost:8080/volunteer/animal/1
    public ResponseEntity<?> removeAnimal(@PathVariable Long id) {
        animalService.removeAnimal(id);
        return ResponseEntity.ok().build();
    }

}
