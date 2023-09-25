package tg.kindhands_bot.kindhands.services;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import tg.kindhands_bot.kindhands.entities.Animal;
import tg.kindhands_bot.kindhands.entities.User;
import tg.kindhands_bot.kindhands.entities.tamed.TamedCat;
import tg.kindhands_bot.kindhands.entities.tamed.TamedDog;
import tg.kindhands_bot.kindhands.repositories.AnimalsRepository;
import tg.kindhands_bot.kindhands.repositories.UserRepository;
import tg.kindhands_bot.kindhands.repositories.tamed.TamedAnimalRepository;

import java.util.Collection;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final AnimalsRepository animalsRepository;
    private final TamedAnimalRepository tamedAnimalRepository;

    private final KindHandsBot bot;

    public UserService(UserRepository userRepository,
                       AnimalsRepository animalsRepository,
                       TamedAnimalRepository tamedAnimalRepository,
                       KindHandsBot bot) {
        this.userRepository = userRepository;
        this.animalsRepository = animalsRepository;
        this.tamedAnimalRepository = tamedAnimalRepository;
        this.bot = bot;
    }

    /**
     * Метод добавления пользователя в Blacklist
     * -----||-----
     * add user in Blacklist method
     */

    public String addUserBlacklist(Long id, String messageBlock) {
        User user = userRepository.getById(id);
        user.setBlocked(true);
        user.setDenialReason(messageBlock);
        userRepository.save(user);

//        bot.sendMessage(ProcessingBotMessages.returnMessageUser("Уважаемый пользователь, "+user.getFirstName() +
//                " Вы заблокированы по причине: "+ messageBlock, user));

        return "Пользователь " + user.getLastName() + " " + user.getFirstName() + " " + user.getPatronymic() + " добавлен в черный список";
    }

    /**
     * Добавляет пользователю, взятое им, животное.
     * -----||-----
     * Adds an animal taken by the user.
     */
    public String addUserAnimal(@PathVariable Long idUser, @RequestParam Long idAnimal) {
        User user = userRepository.findById(idUser).orElse(null);
        if (user == null) {
            throw new NullPointerException("Пользователь с id '" + idUser + "' не найден");
        }

        Animal animal = animalsRepository.findById(idAnimal).orElse(null);
        if (animal == null) {
            throw new NullPointerException("Животное с id '" + idUser + "' не найдено");
        }

        switch (animal.getTypeAnimal()) {
            case CAT: {
                TamedCat tamedCat = new TamedCat();
                tamedCat.setUser(user);
                tamedCat.setAnimal(animal);
                //Дата приручения
                tamedAnimalRepository.save(tamedCat);
                break;
            }
            case DOG: {
                TamedDog tamedDog = new TamedDog();
                tamedDog.setUser(user);
                tamedDog.setAnimal(animal);
                //Дата приручения
                tamedAnimalRepository.save(tamedDog);
                break;
            }
            default: return "Не реализован функционал, для данного вида животного.";
        }
        return "Пользователю " + user.getLastName() + " " + user.getFirstName() + " " + user.getPatronymic() + " добавлено животное " + animal.getName();
    }

    /**
     * Метод для изменения значения поля needHelp у пользователя после оказания помощи
     * -----||-----
     * Method for changing the value of the user's needHelp field
     */
    public String isNeedHelp(Long id) {
        User user = userRepository.getById(id);
        if (user == null) {
            throw new NullPointerException("Пользователь с id: " + id + " не найден");
        }
        user.setNeedHelp(false);
        userRepository.save(user);

        return "Проблема пользователя " + user.getLastName() + " " + user.getFirstName() + " " + user.getPatronymic() + " решена";//добавить фио
    }

    /**
     * Метод полученя всех пользователей, запросивших  помощь волонтера
     * -----||-----
     * Method of getting all users who requested the help of a volunteer
     */
    public Collection<User> needVolunteerHelper() {
        return userRepository.findByNeedHelpTrue();
    }
}
