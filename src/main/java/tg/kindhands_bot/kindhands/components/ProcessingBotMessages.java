package tg.kindhands_bot.kindhands.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import tg.kindhands_bot.kindhands.entities.ReportAnimal;
import tg.kindhands_bot.kindhands.entities.photo.ReportAnimalPhoto;
import tg.kindhands_bot.kindhands.entities.User;
import tg.kindhands_bot.kindhands.enums.BotState;
import tg.kindhands_bot.kindhands.exceptions.IncorrectDataExceptionAndSendMessage;
import tg.kindhands_bot.kindhands.exceptions.NullPointerExceptionAndSendMessage;
import tg.kindhands_bot.kindhands.repositories.photo.ReportAnimalPhotoRepository;
import tg.kindhands_bot.kindhands.repositories.ReportAnimalRepository;
import tg.kindhands_bot.kindhands.repositories.UserRepository;
import tg.kindhands_bot.kindhands.repositories.tamed.TamedAnimalRepository;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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
    private final TamedAnimalRepository tamedAnimalRepository;

    public ProcessingBotMessages(Update update,
                                 UserRepository userRepository,
                                 ReportAnimalRepository reportAnimalRepository,
                                 ReportAnimalPhotoRepository reportAnimalPhotoRepository, TamedAnimalRepository tamedAnimalRepository) {
        this.update = update;
        this.userRepository = userRepository;
        this.reportAnimalRepository = reportAnimalRepository;
        this.reportAnimalPhotoRepository = reportAnimalPhotoRepository;
        this.tamedAnimalRepository = tamedAnimalRepository;
    }

    /**
     * Отправка сообщения при старте бота.
     * -----||-----
     * The method for sending the edited message.
     */
    public SendMessage startCommand() {
        long chatId = update.getMessage().getChatId();
        String name = update.getMessage().getChat().getFirstName();

        User user = new User();
        user.setChatId(chatId);
        user.setFirstName(name);
        user.setBlocked(false);
        userRepository.save(user);

        log.info("Новый пользователь '" + name + "' добавлен.");

        String answer = "Здравствуйте," + name + "! Я бот приюта для животных \"В добрые руки\".";
        return returnMessage(answer);
    }

    /**
     * Переводит статус бота на принятие отчета от пользователя
     * -----||-----
     * Translates the status of the bot to accepting a report from the user
     */
    public EditMessageText reportAnimalCommand() {
        long chatId = update.getCallbackQuery().getMessage().getChatId();

        var tamedAnimal = tamedAnimalRepository.findByUser_ChatId(chatId);
        if (tamedAnimal == null) {
            throw new NullPointerExceptionAndSendMessage("Пользователь с id: '" + chatId + "' не зарегистрирован, как приютивший животное.",
                    "Вы не брали животное в нашем приюте или произошла ошибка.\nОбратитесь к волонтерам.");
        }

        changeStateBot(BotState.SET_REPORT_ANIMAL_PHOTO, chatId);

        return editExistMessage("Пришлите Фотографию питомца: ");
    }

    /**
     * Переводит статус бота на принятие контактных данных от пользователя
     * -----||-----
     * Translates the status of the bot to accepting contact data from the user
     */
    public EditMessageText setUserContactCommand() {
        changeStateBot(BotState.SET_NUM_PHONE, update.getCallbackQuery().getMessage().getChatId());

        return editExistMessage("Укажите свой номер телефона для связи. \n\n(Подходящие форматы: +7(800)000-00-00, 88000000000).");
    }

    /**
     * Создает отчет и добавляет, переданную пользователем фотографию без текста
     * -----||-----
     * Creates a report and adds a user-submitted photo without text
     */
    public SendMessage setReportAnimalPhoto(java.io.File photo) throws IOException {
        long chatId = update.getMessage().getChatId();
        var date = LocalDate.now();

        var tamedAnimal = tamedAnimalRepository.findByUser_ChatId(chatId);
        if (tamedAnimal == null) {
            throw new NullPointerException("Пользователь с id: '" + chatId + "' не зарегистрирован, как приютивший животное.");
        }

        var report = reportAnimalRepository.findByDateAndTamedAnimal_Id(date, tamedAnimal.getId());
        if (report == null) {
            report = new ReportAnimal();
            report.setDate(date);

            tamedAnimal.setNumReportsSent(tamedAnimal.getNumReportsSent() + 1);
            tamedAnimal.setDateLastReport(date);
            tamedAnimalRepository.save(tamedAnimal);

            report.setTamedAnimal(tamedAnimal);
        }
        report.setPhoto(saveReportPhoto(photo));
        reportAnimalRepository.save(report);

        changeStateBot(BotState.SET_REPORT_ANIMAL, chatId);

        return returnMessage("Опишите: " +
                "\n- Рацион животного;" +
                "\n- Общее самочувствие и привыкание к новому месту;" +
                "\n- Изменение в поведении: отказ от старых привычек, приобретение новых.");
    }

    /**
     * Сохранение фотографий репортов
     * -----||-----
     * Saving reports photos.
     */
    public ReportAnimalPhoto saveReportPhoto(File photo) {
        byte[] data = makeLoweredPhoto(photo.toPath());

        ReportAnimalPhoto reportAnimalPhoto = new ReportAnimalPhoto();
        reportAnimalPhoto.setTimeLastReport(LocalDateTime.now());
        reportAnimalPhoto.setFilePath(photo.getAbsolutePath());
        reportAnimalPhoto.setFileSize(data.length);
        reportAnimalPhoto.setMediaType(StringUtils.getFilenameExtension(photo.getPath()));
        reportAnimalPhoto.setData(data);

        reportAnimalPhotoRepository.save(reportAnimalPhoto);
        return reportAnimalPhoto;
    }

    /**
     * В созданном ранее отчете, добавляет описание от пользователя
     * -----||-----
     * In a previously created report, adds a description from the user
     */
    public SendMessage setReportAnimal() {
        long chatId = update.getMessage().getChatId();
        var date = LocalDate.now();
        var report = reportAnimalRepository.findByDateAndTamedAnimal_User_ChatId(date, chatId);

        if (report == null) {
            throw new NullPointerException("Отчет пользователя по id: '" + chatId + "' не найден.");
        }
        report.setDescription(update.getMessage().getText());
        reportAnimalRepository.save(report);

        changeStateBot(BotState.NULL, chatId);

        return returnMessage("Отчет отправлен.");
    }

    /**
     * Проверка и сохранение номера телефона пользователя
     * -----||-----
     * Checking and saving the user's phone number
     */
    public SendMessage setNumberPhoneUser() {
        long chatId = update.getMessage().getChatId();
        String phone = update.getMessage().getText();

        User user = userRepository.findByChatId(chatId);
        if (user == null) {
            throw new NullPointerException("Пользователь с chatId '" + chatId + "' не найден.");
        }

        user.setPhone(CheckMethods.checkNumberPhone(phone));
        user.setBotState(BotState.SET_FULL_NAME);
        userRepository.save(user);

        return returnMessage("Номер телефона добавлен.\n\nВведите одним сообщением Вашу: " +
                "Фамилию Имя Отчество(при наличии)");
    }

    /**
     * Проверка и сохранение номера телефона пользователя
     * -----||-----
     * Checking and saving the user's phone number
     */
    public SendMessage setFullNameUser() {
        long chatId = update.getMessage().getChatId();
        String fullName = update.getMessage().getText();

        User user = userRepository.findByChatId(chatId);
        if (user == null) { throw new NullPointerException("Пользователь с chatId '" + chatId + "' не найден."); }

        List<String> arrFullName;
        try {
            arrFullName = CheckMethods.checkFullName(fullName);
        } catch (NullPointerException | IncorrectDataExceptionAndSendMessage e) {
            return returnMessage(e.getMessage());
        }

        user.setLastName(arrFullName.get(0));
        user.setFirstName(arrFullName.get(1));
        if (arrFullName.size() == 3) {
            user.setPatronymic(arrFullName.get(2));
        }
        user.setBotState(BotState.NULL);
        userRepository.save(user);

        return returnMessage("Фамилия Имя Отчество добавлены.");
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


    // Рассмотреть вариант вывести в отдельный класс
    // Вспомогательные методы


    /**
     * Метод для уменьшения размера фотографии
     * -----||-----
     * A method for lowering size of photo
     */
    public byte[] makeLoweredPhoto(Path photo) {
        try (InputStream is = Files.newInputStream(photo);
             BufferedInputStream bis = new BufferedInputStream(is, 1000);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            BufferedImage image = ImageIO.read(bis);

            int height = image.getHeight() / (image.getWidth() / 100);
            BufferedImage loweredPhoto = new BufferedImage(100, height, image.getType());
            Graphics2D graphics = loweredPhoto.createGraphics();
            graphics.drawImage(image, 0, 0, 100, height, null);
            graphics.dispose();

            String fileName = photo.getFileName().toString();
            ImageIO.write(
                    loweredPhoto,
                    fileName.substring(fileName.lastIndexOf(".") + 1),
                    baos);
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Метод для редактирования существующего сообщения бота.
     * -----||-----
     * A method for editing an existing bot message.
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
        if (update.hasMessage()) {
            message.setChatId(update.getMessage().getChatId());
        } else if (update.hasCallbackQuery()) {
            message.setChatId(update.getCallbackQuery().getMessage().getChatId());
        }
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
    public void changeStateBot(BotState botState, long chatId) {
        User user = userRepository.findByChatId(chatId);
        if (user == null) { throw new NullPointerException("Пользователь с chatId '" + chatId + "' не найден.");}

        user.setBotState(botState);
        userRepository.save(user);
    }

    public void setUpdate(Update update) {
        this.update = update;
    }


    /**
     * отправка сообщения пользователю.
     * -----||-----
     * Send Message for user.
     */
    public static SendMessage returnMessageUser(User user, String text) {
        if (user.getChatId() == null) {
            throw new NullPointerException("У пользователя отсутствует chatId");
        }
        SendMessage message = new SendMessage();
        message.setChatId(user.getChatId());
        message.setText(text);
        return message;
    }

    /**
     * Пользователь запроси помощь волонтера и получил сообщение об этом,
     * меняется значение поля needHelp.
     * -----||-----
     * The user requested the help of a volunteer and received a message about it,
     * the value of the needHelp field changes.
     */

    public EditMessageText userNeedHelp() {
        long chatId = update.getCallbackQuery().getMessage().getChatId();
        User user = userRepository.findByChatId(chatId);
        if (user == null) {
            throw new NullPointerException("Пользователь с id: " + chatId + " не найден");
        }
        user.setNeedHelp(true);
        userRepository.save(user);
        return editExistMessage("Мы отправили Ваш запрос волонтеру. С Вами свяжутся.");
    }
}






