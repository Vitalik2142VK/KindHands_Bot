package tg.kindhands_bot.kindhands.services;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import tg.kindhands_bot.kindhands.entities.Animal;
import tg.kindhands_bot.kindhands.repositories.AnimalsRepository;

import java.util.Collection;

@Service
public class AnimalService {
    private final AnimalsRepository animalsRepository;

    public AnimalService(AnimalsRepository animalsRepository) {
        this.animalsRepository = animalsRepository;
    }


    /**
     * Выводит список всех животных.
     * -----||-----
     * Displays a list of all animals.
     */

    public Collection<Animal> getAllAnimals() {
        return animalsRepository.findAll();
    }

    /**
     * Добавляет новое животное.
     * -----||-----
     * Adds a new animal.
     */
    public Animal addAnimal(Animal animal) {
        return animalsRepository.save(animal);
    }

//    /**
//     * Изменяет статус животного.
//     * -----||-----
//     * Changes the status of the animal.
//     */
//    public ResponseEntity<?> changeStatusAnimal(@PathVariable Long id) {
//        return ResponseEntity.ok().build();
//    }

    /**
     * Удаляет животное из БД.
     * -----||-----
     * Removes an animal from the database.
     */

    public void removeAnimal(Long id) {
       animalsRepository.deleteById(id);
    }
}
