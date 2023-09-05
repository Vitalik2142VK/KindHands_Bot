package tg.kindhands_bot.kindhands.controllers;

import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tg.kindhands_bot.kindhands.services.VolunteerService;

@RestController
@PropertySource("application.properties")
@RequestMapping("volunteer")
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
    public ResponseEntity getReports() {
        return ResponseEntity.ok().build();
    }

    /**
     * Выводит список всех животных.
     * -----||-----
     * Displays a list of all animals.
     */
    @GetMapping("/animal/getAll") // GET http://localhost:8080/volunteer/animal/getAll
    public ResponseEntity getAllAnimals() {
        return ResponseEntity.ok().build();
    }

    /**
     * Добавляет новое животное.
     * -----||-----
     * Adds a new animal.
     */
    @PostMapping("/animal") // POST http://localhost:8080/volunteer/animal
    public ResponseEntity addAnimal() {
        return ResponseEntity.ok().build();
    }

    /**
     * Изменяет статус животного.
     * -----||-----
     * Changes the status of the animal.
     */
    @PutMapping("/animal/{id}") // POST http://localhost:8080/volunteer/animal/1
    public ResponseEntity changeStatusAnimal(@PathVariable Long id) {
        return ResponseEntity.ok().build();
    }

    /**
     * Удаляет животное из БД.
     * -----||-----
     * Removes an animal from the database.
     */
    @DeleteMapping("/animal/{id}") // POST http://localhost:8080/volunteer/animal/1
    public ResponseEntity removeAnimal(@PathVariable Long id) {
        return ResponseEntity.ok().build();
    }

    /**
     * Добавляет пользователя в черный список, с указанной причиной.
     * -----||-----
     * Adds a user to the blacklist, with the specified reason.
     */
    @PutMapping("/user/blacklist/add/{id}") // POST http://localhost:8080/volunteer/user/blacklist/add/1?messageBlock=message
    public ResponseEntity addUserBlacklist(@PathVariable Long id, @RequestParam String messageBlock) {
        return ResponseEntity.ok().build();
    }

    /**
     * Добавляет пользователю, взятое им, животное.
     * -----||-----
     * Adds an animal taken by the user.
     */
    @PutMapping("/user/addAnimal/{id}") // POST http://localhost:8080/volunteer/user/addAnimal/1?idAnimal=2
    public ResponseEntity addUserAnimal(@PathVariable Long id, @RequestParam Long idAnimal) {
        return ResponseEntity.ok().build();
    }

    /**
     * Продлевает испытательный срок пользователю.
     * -----||-----
     * Extends the probation period to the user.
     */
    @PutMapping("/user/term/{id}") // POST http://localhost:8080/volunteer/user/term/1?term=2
    public ResponseEntity extendProbationPeriod(@PathVariable Long id, @RequestParam Integer term) {
        return ResponseEntity.ok().build();
    }
}
