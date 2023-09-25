package tg.kindhands_bot.kindhands.components;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import tg.kindhands_bot.kindhands.entities.User;
import tg.kindhands_bot.kindhands.services.KindHandsBot;

import java.util.List;

/**
 * Отправляет зарегистрированным пользователям сообщения в боте, отправленные из контроллеров
 * -----//-----
 * Sends registered users messages in the bot sent from controllers
 */
public class MessagesBotFromControllers {
    private final KindHandsBot bot;

    public MessagesBotFromControllers(KindHandsBot bot) {
        this.bot = bot;
    }

    /**
     * Отправляет пользователю сообщение в боте
     * -----//-----
     * Sends a message to the user in the bot
     */
    public void sendMessageUser(User user, String message) {
        bot.sendMessage(ProcessingBotMessages.returnMessageUser(user, message));
    }

    /**
     * Отправляет группе пользователей сообщение в боте
     * -----//-----
     * Sends a message to the user in the bot
     */
    public void sendMessageUsers(List<User> users) {

    }
}
