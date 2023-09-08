package tg.kindhands_bot.kindhands.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/volunteer/user")
public class UserController {
    /**
     * Добавляет пользователя в черный список, с указанной причиной.
     * -----||-----
     * Adds a user to the blacklist, with the specified reason.
     */
    @PutMapping("/blacklist/add/{id}") // POST http://localhost:8080/volunteer/user/blacklist/add/1?messageBlock=message
    public ResponseEntity<?> addUserBlacklist(@PathVariable Long id, @RequestParam String messageBlock) {
        return ResponseEntity.ok().build();
    }

    /**
     * Добавляет пользователю, взятое им, животное.
     * -----||-----
     * Adds an animal taken by the user.
     */
    @PutMapping("/addAnimal/{id}") // POST http://localhost:8080/volunteer/user/addAnimal/1?idAnimal=2
    public ResponseEntity<?> addUserAnimal(@PathVariable Long id, @RequestParam Long idAnimal) {
        return ResponseEntity.ok().build();
    }

    /**
     * Продлевает испытательный срок пользователю.
     * -----||-----
     * Extends the probation period to the user.
     */
    @PutMapping("/term/{id}") // POST http://localhost:8080/volunteer/user/term/1?term=2
    public ResponseEntity<?> extendProbationPeriod(@PathVariable Long id, @RequestParam Integer term) {
        return ResponseEntity.ok().build();
    }
}
