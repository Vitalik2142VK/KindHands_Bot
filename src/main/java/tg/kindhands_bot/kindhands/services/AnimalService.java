package tg.kindhands_bot.kindhands.services;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import tg.kindhands_bot.kindhands.entities.Animal;
import tg.kindhands_bot.kindhands.entities.photo.AnimalPhoto;
import tg.kindhands_bot.kindhands.repositories.AnimalsRepository;
import tg.kindhands_bot.kindhands.repositories.photo.AnimalPhotoRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.UUID;

import static tg.kindhands_bot.kindhands.components.CheckMethods.makeLoweredPhoto;

@Service
public class AnimalService {
    private final AnimalsRepository animalsRepository;
    private final AnimalPhotoRepository animalPhotoRepository;

    @Value("${animals.photo.storage.path}")
    private Path photoPath;

    public AnimalService(AnimalsRepository animalsRepository, AnimalPhotoRepository animalPhotoRepository) {
        this.animalsRepository = animalsRepository;
        this.animalPhotoRepository = animalPhotoRepository;
    }


    /**
     * Возвращает оригинальный файл.
     * -----||-----
     * It returns original file.
     */

    public Pair<byte[], String> getPhoto(Long id) {
        AnimalPhoto animalPhoto = animalPhotoRepository.findById(id).orElse(null);
        if (animalPhoto == null) {
            throw new RuntimeException("The photo is not found");
        }
        return Pair.of(animalPhoto.getData(), animalPhoto.getMediaType());
    }

    /**
     * Сохранение принятой фотографии
     * -----||-----
     * Uploading photo in DB
     */

    public Pair<byte[], String> uploadPhoto(Long id, MultipartFile photo) {
        try {
            var animalPhoto = animalPhotoRepository.findById(id).orElse(new AnimalPhoto());
            var filePath = animalPhoto.getFilePath();

            if (filePath != null) Files.delete(Path.of(filePath));

            var contentType = photo.getContentType();
            var extension = StringUtils.getFilenameExtension(photo.getOriginalFilename());
            var fileName = UUID.randomUUID() + "." + extension;
            var pathToPhoto = photoPath.resolve(fileName);
            byte[] data = photo.getBytes();

            animalPhoto.setFilePath(pathToPhoto.toString());
            animalPhoto.setFileSize(photo.getSize());
            animalPhoto.setMediaType(contentType);
            animalPhoto.setData(makeLoweredPhoto(pathToPhoto));
            animalPhotoRepository.save(animalPhoto);

            return Pair.of(data, contentType);
        } catch (IOException e) {
            throw new RuntimeException();
        }
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

    /**
     * Удаляет животное из БД.
     * -----||-----
     * Removes an animal from the database.
     */
    public void removeAnimal(Long id) {
       animalsRepository.deleteById(id);
    }
}
