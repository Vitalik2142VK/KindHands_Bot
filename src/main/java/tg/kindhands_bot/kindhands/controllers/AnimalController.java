package tg.kindhands_bot.kindhands.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tg.kindhands_bot.kindhands.entities.Animal;
import tg.kindhands_bot.kindhands.repositories.photo.AnimalPhotoRepository;
import tg.kindhands_bot.kindhands.services.AnimalService;

import java.util.Collection;

@RestController
@RequestMapping("/volunteer/animal")
@Tag(name = "Животные.", description = "Эндпоинты для работы с животными.")
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
    @Operation(summary = "Получить всех животных")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Все животные получены."
            )})
      public ResponseEntity<Collection<Animal>> getAllAnimals() {
        return ResponseEntity.ok(animalService.getAllAnimals());
    }

    /**
     * Выводит оригинал фотографии животного
     * -----||-----
     * Show the original photo
     */
    @PatchMapping(value = "/photo/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    // PATCH http://localhost:8080/volunteer/animal/photo/1
    @Operation(summary = "Добавить фотографию животному")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Фотографию обработана"
            )})
    public ResponseEntity<?> uploadPhoto(@PathVariable Long id, @RequestPart MultipartFile photo) {
        var pair = animalService.uploadPhoto(id, photo);
        byte[] data = pair.getLeft();

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(pair.getRight()))
                .contentLength(data.length)
                .body(data);
    }

    /**
     * Сохранение принятой фотографии
     * -----||-----
     * Uploading photo in DB
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
        var pair = animalService.getPhoto(id);
        byte[] data = pair.getLeft();

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(pair.getRight()))
                .contentLength(data.length)
                .body(data);
    }

    /**
     * Добавляет новое животное.
     * -----||-----
     * Adds a new animal.
     */
  @Operation(summary = "Добавить животное")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Животное добавлено."
            )})
    @PostMapping("/add") // POST http://localhost:8080/volunteer/animal
    public Animal addAnimal(@RequestBody Animal animal) {
        return animalService.addAnimal(animal);
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
        animalService.removeAnimal(id);
        return ResponseEntity.ok().build();
    }
}
