package tg.kindhands_bot.kindhands.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tg.kindhands_bot.kindhands.entities.User;
import tg.kindhands_bot.kindhands.services.UserService;

import java.util.Collection;

@RestController
@RequestMapping("/volunteer/user")
@Tag(name = "Пользователи.", description = "Эндпоинты для работы волонтера с пользователями.")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

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
        return ResponseEntity.ok(userService.addUserBlacklist(id, messageBlock));
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
        return ResponseEntity.ok(userService.addUserAnimal(id, idAnimal));
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
        return ResponseEntity.ok(userService.extendProbationPeriod(id, term));
    }

    /**
     * Волонтеры получают список пользователей, запросивших помощь
     * -----||-----
     * Volunteers receive a list of users who have requested help
     */
    @GetMapping("/help")//GET http://localhost:8080/volunteer/user/help
    @Operation(summary = "Получение волонтерами списка пользователей, которым нужна помощь")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Список получен."
            )
    })
    public ResponseEntity<Collection<User>> needVolunteerHelper() {
        Collection<User> users = userService.needVolunteerHelper();
        if (users.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(users);
    }

    /**
     * Волонтер меняет поле пользователя needHelp на false после оказания помощи
     * -----||-----
     * The volunteer changes the user's needHelp field to false after providing assistance
     */
    @PutMapping("/adopted/help/change/{id}")//PUT http://localhost:8080/volunteer/user/help/change/3
    @Operation(summary = "Изменение волонтером статуса пользователя после оказания помощи")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Помощь оказана."
            )
    })
    public ResponseEntity<?> changeIsNeedHelp(@PathVariable Long id) {
        return ResponseEntity.ok(userService.isNeedHelp(id));
    }

    /**
     * Выводит оригинал фотографии животного
     * -----||-----
     * Show the original photo
     */
    @GetMapping("/photo/{id}")
    // GET http://localhost:8080/volunteer/animal/photo/1
    @Operation(summary = "Получить фотографию животного")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Фотографию успешно найдена"
            )})
    public ResponseEntity<?> getPhotoAnimal(Long id) {
        var pair = userService.getPhoto(id);
        byte[] data = pair.getLeft();

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(pair.getRight()))
                .contentLength(data.length)
                .body(data);
    }
}
