package tg.kindhands_bot.kindhands.services;

import org.springframework.stereotype.Service;
import tg.kindhands_bot.kindhands.components.ProcessingBotMessages;
import tg.kindhands_bot.kindhands.entities.User;
import tg.kindhands_bot.kindhands.repositories.UserRepository;

import java.util.Collection;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final KindHandsBot bot;


    public UserService(UserRepository userRepository, KindHandsBot bot) {
        this.userRepository = userRepository;
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

        bot.sendMessage(ProcessingBotMessages.returnMessageUser("Уважаемый пользователь, " + user.getName() +
                " Вы заблокированы по причине: " + messageBlock, user));

        return "Пользователь " + user.getName() + " добавлен в черный список";
    }


    /**
     * Метод для изменения значения поля needHelp у пользователя после оказания помощи
     * -----||-----
     * Method for changing the value of the user's needHelp field
     */
    public String isNeedHelp(Long id, boolean needHelp) {
        User user = userRepository.getById(id);
        if (user == null) {
            throw new NullPointerException("Пользователь с id: " + id + " не найден");
        }
        user.setNeedHelp(false);
        userRepository.save(user);

        return "Проблема пользователя " + user.getName() + " решена";//добавить фио
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
