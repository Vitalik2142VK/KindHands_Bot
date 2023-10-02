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

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
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
        AnimalPhoto animalPhoto = animalPhotoRepository.findById(id).orElseThrow(() -> new NullPointerException("The photo is not found"));

        return Pair.of(animalPhoto.getData(), animalPhoto.getMediaType());
    }

    /**
     * Сохранение принятой фотографии
     * -----||-----
     * Uploading photo in DB
     */

    public void uploadPhoto(Long id, MultipartFile photo) {
        try {
            Animal animal = animalsRepository.findById(id).orElseThrow(() -> new NullPointerException("Животное по id '" + id + "' не найдено."));

            var animalPhoto = new AnimalPhoto();

            var contentType = photo.getContentType();
            var extension = StringUtils.getFilenameExtension(photo.getOriginalFilename());
            var fileName = UUID.randomUUID() + "." + extension;
            var pathToPhoto = photoPath.resolve(fileName);
            byte[] data = photo.getBytes();
            Files.write(pathToPhoto, data);

            animalPhoto.setFilePath(pathToPhoto.toString());
            animalPhoto.setFileSize(photo.getSize());
            animalPhoto.setMediaType(contentType);
            animalPhoto.setData(data);
            animalPhotoRepository.save(animalPhoto);

            animal.setAnimalPhoto(animalPhoto);
            animalsRepository.save(animal);
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
//        long idPhoto = animal.getAnimalPhoto().getId();
//
//        AnimalPhoto photo = animalPhotoRepository.findById(idPhoto).orElseThrow(() -> new NullPointerException("Фото животного с id '" + idPhoto + "' не найдено."));
//        animal.setAnimalPhoto(photo);

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
