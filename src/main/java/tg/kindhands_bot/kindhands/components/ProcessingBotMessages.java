package tg.kindhands_bot.kindhands.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import tg.kindhands_bot.kindhands.entities.ReportAnimal;
import tg.kindhands_bot.kindhands.entities.ReportAnimalPhoto;
import tg.kindhands_bot.kindhands.entities.User;
import tg.kindhands_bot.kindhands.enums.BotState;
import tg.kindhands_bot.kindhands.repositories.ReportAnimalPhotoRepository;
import tg.kindhands_bot.kindhands.repositories.ReportAnimalRepository;
import tg.kindhands_bot.kindhands.repositories.UserRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Класс для обработки и отправки сообщений.
 * -----||-----
 * A class for processing and sending messages.
 */
public class ProcessingBotMessages {

    private final Logger log = LoggerFactory.getLogger(ProcessingBotMessages.class);

    private Update update;
    private final UserRepository userRepository;

    private final ReportAnimalRepository reportAnimalRepository;

    private final ReportAnimalPhotoRepository reportAnimalPhotoRepository;

    public ProcessingBotMessages(Update update,
                                 UserRepository userRepository,
                                 ReportAnimalRepository reportAnimalRepository,
                                 ReportAnimalPhotoRepository reportAnimalPhotoRepository) {
        this.update = update;
        this.userRepository = userRepository;
        this.reportAnimalRepository = reportAnimalRepository;
        this.reportAnimalPhotoRepository = reportAnimalPhotoRepository;
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

        log.info("Новый пользователь '" + name + "' добавлен.");

        String firstNameUser = update.getMessage().getChat().getFirstName();
        String answer = "Здравствуйте," + firstNameUser + "! Я бот приюта для животных \"В добрые руки\".";
        return returnMessage(answer);
    }

    /**
     * Переводит статус бота на принятия отчета от пользователя
     * -----||-----
     * Translates the status of the bot to accepting a report from the user
     */
    public EditMessageText reportAnimalCommand() {
        User user = userRepository.findByChatId(update.getCallbackQuery().getMessage().getChatId());
        user.setBotState(BotState.SET_REPORT_ANIMAL);
        userRepository.save(user);

        return editExistMessage("Пришлите:" +
                "\nФотографию питомца;" +
                "\nРацион животного;" +
                "\nОбщее самочувствие и привыкание к новому месту;" +
                "\nИзменение в поведении: отказ от старых привычек, приобретение новых.");
    }

    /**
     * Принимает отчет о животном от пользователя и меняет статус бота на NULL
     * -----||-----
     * Accepts an animal report from the user and changes the bot status to NULL
     */
    public SendMessage setReportAnimal(java.io.File photo) throws IOException {
        var date = LocalDate.now();
        var report = reportAnimalRepository.findByDateAndChatId(date, update.getMessage().getChatId());

        if (report == null) {
            saveReportPhoto(photo);

            report = new ReportAnimal();
            report.setDate(date);
            //report.setReportNumber();
            //Заменить после создания TamedAnimal
            report.setChatId(update.getMessage().getChatId());
        }
        report.setDescription(update.getMessage().getText());
        reportAnimalRepository.save(report);

        changeStateBotNull();

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
     * Сохранение фотографий репортов
     * -----||-----
     * Saving reports photos.
     */
    public void saveReportPhoto(File photo) throws IOException {
        ReportAnimalPhoto reportAnimalPhoto = new ReportAnimalPhoto();
        reportAnimalPhoto.setTimeLastReport(LocalDateTime.now());
        reportAnimalPhoto.setFilePath(photo.getAbsolutePath());
        reportAnimalPhoto.setFileSize(photo.length());
        reportAnimalPhoto.setMediaType(StringUtils.getFilenameExtension(photo.getPath()));
        reportAnimalPhoto.setData(Files.readAllBytes(photo.toPath()));

        reportAnimalPhotoRepository.save(reportAnimalPhoto);
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

    /**
     * Меняет статус бота для пользователя на NULL
     * -----||-----
     * Sending a message when user is blocked
     */
    public void changeStateBotNull() {
        User user = userRepository.findByChatId(update.getMessage().getChatId());
        if (user == null) { throw new NullPointerException();}


        user.setBotState(BotState.NULL);
        userRepository.save(user);
    }

    public void setUpdate(Update update) {
        this.update = update;
    }


    /**
     * отправка сообщения пользователю.
     * -----||-----
     *  Send Message for user.
     */
    public static SendMessage returnMessageUser(String text, User user) {
        SendMessage message = new SendMessage();
        message.setChatId(user.getChatId());
        message.setText(text);
        return message;
    }
}
