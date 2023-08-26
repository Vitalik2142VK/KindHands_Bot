package tg.kindhands_bot.kindhands.components;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Класс для обработки и отправки сообщений
 * -----||-----
 * A class for processing and sending messages
 */
public class ProcessingBotMessages {

    private Update update;

    long chatId;
    String firstNameUser;

    public ProcessingBotMessages(Update update) {
        this.update = update;
        chatId = update.getMessage().getChatId();
        firstNameUser = update.getMessage().getChat().getFirstName();
    }

    /**
     * Отправка сообщения при старте бота.
     * -----||-----
     * The method for sending the edited message.
     */
    public SendMessage startCommand() {
        String answer = "Здравствуйте," + firstNameUser + "! Я бот приюта для животных \"В добрые руки\". Выберите необходимую вам категорию.";
        return returnMessage(answer);
    }

    public SendMessage defaultMessage() {
        String answer = "Не корректно введено сообщение. Выберите необходимую вам категорию.";
        return returnMessage(answer);
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
        message.setChatId(chatId);
        message.setText(text);
        return message;
    }
}
