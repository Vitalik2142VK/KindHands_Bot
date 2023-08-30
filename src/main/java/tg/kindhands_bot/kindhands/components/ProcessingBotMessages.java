package tg.kindhands_bot.kindhands.components;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import tg.kindhands_bot.kindhands.entities.User;
import tg.kindhands_bot.kindhands.repositories.UserRepository;

/**
 * Класс для обработки и отправки сообщений.
 * -----||-----
 * A class for processing and sending messages.
 */
public class ProcessingBotMessages {

    private final Update update;
    private UserRepository userRepository;

    public ProcessingBotMessages(Update update, UserRepository userRepository) {
        this.update = update;
        this.userRepository = userRepository;
    }

    public ProcessingBotMessages(Update update) {
        this.update = update;
    }

    /**
     * Отправка сообщения при старте бота.
     * -----||-----
     * The method for sending the edited message.
     */
    public SendMessage startCommand(Long chatId, String name) {
        User user = new User();
        user.setChatId(chatId);
        user.setName(name);
        user.setBlocked(false);
        userRepository.save(user);

        String firstNameUser = update.getMessage().getChat().getFirstName();
        String answer = "Здравствуйте," + firstNameUser + "! Я бот приюта для животных \"В добрые руки\".";
        return returnMessage(answer);
    }

    /**
     * Отправка сообщения при вводе некорректных данных со стороны пользователя.
     * -----||-----
     * Sending a message when incorrect data is entered by the user.
     */
    public SendMessage defaultMessage() {
        String answer = "Не корректно введено сообщение.";
        return returnMessage(answer);
    }

    /**
     * Метод для редактирования существующего сообщения.
     * -----||-----
     * A method for editing an existing message.
     */
    public EditMessageText editExistMessage(String text) {
        EditMessageText message = new EditMessageText();
        message.setChatId(update.getCallbackQuery().getMessage().getChatId());
        message.setText(text);
        message.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
        return message;
    }


    // Методы для более удобной работы класса.
    // Methods for more convenient operation of the class.


    /**
     * Метод для отправки отредактированного сообщения.
     * -----||-----
     * The method for sending the edited message.
     */
    private SendMessage returnMessage(String text) {
        SendMessage message = new SendMessage();
        message.setChatId(update.getMessage().getChatId());
        message.setText(text);
        return message;
    }
}
