package tg.kindhands_bot.kindhands.services;

import com.pengrad.telegrambot.BotUtils;
import org.telegram.telegrambots.meta.api.objects.Update;
import tg.kindhands_bot.kindhands.entities.User;
import tg.kindhands_bot.kindhands.enums.BotState;

import java.util.List;

public class AdditionalMethods {
    // Дополнительые методы

    public static Update getUpdate(String json, String replaced) {
        return BotUtils.fromJson(json.replace("%text%", replaced), Update.class);
    }

    public static User createUser(Long id, Long chatId, String name, Boolean blocked,
                            String denialReason, BotState botState) {
        User user = new User();
        user.setId(id);
        user.setChatId(chatId);
        user.setFirstName(name);
        user.setBlocked(blocked);
        user.setDenialReason(denialReason);
        user.setBotState(botState);
        return user;
    }

    public static User findUserByChatId(List<User> users, Long chatId) {
        for (var user : users) {
            if (user.getChatId().equals(chatId)) {
                return user;
            }
        }
        throw new NullPointerException();
    }

    public static User addUser(List<User> users, User user) {
        users.add(user);
        return user;
    }
}
