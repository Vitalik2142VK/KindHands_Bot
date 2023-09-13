package tg.kindhands_bot.kindhands.components;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import tg.kindhands_bot.kindhands.entities.ReportAnimal;
import tg.kindhands_bot.kindhands.entities.User;
import tg.kindhands_bot.kindhands.repositories.ReportAnimalRepository;
import tg.kindhands_bot.kindhands.repositories.UserRepository;

import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;

/**
 * Класс для обработки и отправки сообщений.
 * -----||-----
 * A class for processing and sending messages.
 */
public class ProcessingBotMessages {

    private final Update update;
    private final UserRepository userRepository;

    private final ReportAnimalRepository reportAnimalRepository;

    public ProcessingBotMessages(Update update, UserRepository userRepository, ReportAnimalRepository reportAnimalRepository) {
        this.update = update;
        this.userRepository = userRepository;
        this.reportAnimalRepository = reportAnimalRepository;
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
     *
     * -----||-----
     *
     */
    public SendMessage reportAnimalCommand() {
        //User user = userRepository

        return returnMessage("Пришлите фотографию питомца;" +
                "\nРацион животного;" +
                "\nОбщее самочувствие и привыкание к новому месту;" +
                "\nИзменение в поведении: отказ от старых привычек, приобретение новых.");
    }

    /**
     *
     * -----||-----
     *
     */
    public SendMessage setReportAnimal() {
        ReportAnimal report = new ReportAnimal();
        report.setDescription(update.getMessage().getText());
        report.setDate(LocalDate.now());


        reportAnimalRepository.save(report);

        return returnMessage("Отчет отправлен.");
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
     * Метод для редактирования существующего сообщения пользователя.
     * -----||-----
     * A method for editing an existing user message.
     */
    public EditMessageText editExistMessage(String text) {
        EditMessageText message = new EditMessageText();
        message.setChatId(update.getCallbackQuery().getMessage().getChatId());
        message.setText(text);
        message.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
        return message;
    }

    /**
     * Преобразования Строки в sendMessage.
     * -----||-----
     * Converting a String to SendMessage.
     */
    public SendMessage returnMessage(String text) {
        SendMessage message = new SendMessage();
        message.setChatId(update.getMessage().getChatId());
        message.setText(text);
        return message;
    }

    /**
     * Отправка сообщения для заблокированных пользователей
     * -----||-----
     * Sending a message when user is blocked
     */
    public SendMessage blockedMessage() {
        String firstNameUser = update.getMessage().getChat().getFirstName();
        String answer = firstNameUser + ", ваш аккаунт заблокирован";
        return returnMessage(answer);
    }
}
