package tg.kindhands_bot.kindhands.service;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import tg.kindhands_bot.kindhands.components.NavigationMenu;
import tg.kindhands_bot.kindhands.components.ProcessingBotMessages;
import tg.kindhands_bot.kindhands.config.BotConfig;
import tg.kindhands_bot.kindhands.repositories.UserRepository;

@Component
public class KindHandsBot extends TelegramLongPollingBot {
    private final UserRepository userRepository;

    private final BotConfig config;

    private ProcessingBotMessages botMessages = null;

    public KindHandsBot(UserRepository userRepository,
                        BotConfig config) {
        super(config.getToken());
        this.userRepository = userRepository;
        this.config = config;
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    /**
     * Основной метод для работы бота.
     * При первом обращении с командой '/start' к боту, высылается привествие
     * При обращении с командой '/start' более одного раза приветствие уже не высылается
     * -----||-----
     * The main method for the bot to work.
     * When you first use the '/start' command to the bot, a greeting is sent
     * When using the '/start' command more than once, the greeting is no longer sent
     */
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            if (checkUser(update, true)) {
                textCommands(update);
            }
        } else if (update.hasCallbackQuery()) {
            if (checkUser(update, false)) {
                buttonCommands(update);}
        }
    }

    /**
     * Метод для проверки пользователя на блокировку
     * -----||-----
     * A method for handling of blocked users
     */
    public boolean checkUser(Update update, Boolean messageOrQuery) {
        var chatId = messageOrQuery ? update.getMessage().getChatId() : update.getCallbackQuery().getMessage().getChatId();
        var user = userRepository.findByChatId(chatId);

        if (botMessages == null) {
            botMessages = new ProcessingBotMessages(update, userRepository);
        }
        if (user != null && user.getBlocked()) {
            sendMessage(botMessages.blockedMessage());
            return false;
        }
        return true;
    }

    /**
     * Метод для обработки, введенного пользователем, текста или текстовых команд.
     * -----||-----
     * A method for processing user-entered text or text commands.
     */
    public void textCommands(Update update) {
        String messageText = update.getMessage().getText();
        long chatId = update.getMessage().getChatId();

        switch (messageText) {
            case "/start": {
                if (userRepository.findByChatId(chatId) == null) {
                    sendMessage(botMessages.startCommand(chatId, update.getMessage().getFrom().getFirstName()));
                }
                sendMessage(NavigationMenu.choosingShelter(chatId));
                break;
            }
            default: sendMessage(botMessages.defaultMessage());
        }
    }

    /**
     * Метод для обработки, выбранной пользователем, кнопки.
     * -----||-----
     * The method for processing the button selected by the user.
     */
    public void buttonCommands(Update update) {
        String callbackData = update.getCallbackQuery().getData();
        long chatId = update.getCallbackQuery().getMessage().getChatId();

        ProcessingBotMessages botMessages = new ProcessingBotMessages(update);

        switch (callbackData) {
            case "DOG_SH": {
                sendMessage(botMessages.editExistMessage("Вы выбрали собачий приют."));
                break;
            }
            case "CAT_SH": {
                sendMessage(botMessages.editExistMessage("Вы выбрали кошачий приют."));
                break;
            }
        }
    }

    /**
     * Метод для отправки ответа пользователю.
     * -----||-----
     * A method for sending a response to the user.
     */
    private void sendMessage(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendMessage(EditMessageText message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
