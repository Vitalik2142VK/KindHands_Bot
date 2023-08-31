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
            textCommands(update);
        } else if (update.hasCallbackQuery()) {
            buttonCommands(update);
        }
    }

    /**
     * Метод для обработки, введенного пользователем, текста или текстовых команд.
     * -----||-----
     * A method for processing user-entered text or text commands.
     */
    public void textCommands(Update update) {
        String messageText = update.getMessage().getText();
        long chatId = update.getMessage().getChatId();

        ProcessingBotMessages botMessages = new ProcessingBotMessages(update, userRepository);

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
                SendMessage message = new SendMessage(String.valueOf(chatId),"Вы выбрали собачий приют.");
                NavigationMenu.menuShelter(message, true);
                sendMessage(message);
                break;
            }
            case "CAT_SH": {
                SendMessage message = new SendMessage(String.valueOf(chatId),"Вы выбрали кошачий приют.");
                NavigationMenu.menuShelter(message, false);
                sendMessage(message);
                break;
            }
        }

        switch (callbackData){
            case "INFO_GET_D":
                SendMessage message = new SendMessage(String.valueOf(chatId),"Информация о собачем приюте: ");
                sendMessage(message);
                break;

            case "HOW_GET_D":
                 message = new SendMessage(String.valueOf(chatId),"Как взять собаку из приюта: ");
                sendMessage(message);
                break;

            case "SEND_RP_D":
                message = new SendMessage(String.valueOf(chatId),"Отчёт о питомце(собаке): ");
                sendMessage(message);
                break;

            case "CALL_VL_D":
            case "CALL_VL_C":
                message = new SendMessage(String.valueOf(chatId),"Вызов волонтёра...");
                sendMessage(message);
                break;

            case "INFO_GET_C":
                message = new SendMessage(String.valueOf(chatId),"Информация о кошачем приюте: ");
                sendMessage(message);
                break;

            case "HOW_GET_C":
                message = new SendMessage(String.valueOf(chatId),"Как взять кошку из приюта: ");
                sendMessage(message);
                break;

            case "SEND_RP_C":
                message = new SendMessage(String.valueOf(chatId),"Отчёт о питомце(кошке): ");
                sendMessage(message);
                break;

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
