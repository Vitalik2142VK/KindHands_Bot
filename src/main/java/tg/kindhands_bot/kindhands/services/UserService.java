package tg.kindhands_bot.kindhands.services;

import org.springframework.stereotype.Service;
import tg.kindhands_bot.kindhands.components.ProcessingBotMessages;
import tg.kindhands_bot.kindhands.entities.User;
import tg.kindhands_bot.kindhands.repositories.UserRepository;

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

        bot.sendMessage(ProcessingBotMessages.returnMessageUser("Уважаемый пользователь, "+user.getFirstName() +
                " Вы заблокированы по причине: "+ messageBlock, user));

        return "Пользователь " + user.getFirstName() + " добавлен в черный список";
    }

    /**
     * Метод продления испытательного срока
     * -----||-----
     *  method
     */
    public String extendProbationPeriod(Long id, Integer term) {
//        User user = userRepository.getById(id);
//        SendMessage message = new SendMessage(user.getChatId(), "Вам продлен испытательный срок на 14 или 30 дней, отправляйте отчеты вовремя");
        return "Пользователю продлен испытательный срок на "+term;
    }
}
