package tg.kindhands_bot.kindhands.service;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import tg.kindhands_bot.kindhands.config.BotConfig;
import tg.kindhands_bot.kindhands.repositories.UserRepository;
import tg.kindhands_bot.kindhands.components.volunteer.ForVolunteers;

@Component
public class KindHandsBot extends TelegramLongPollingBot {

    private final ChoosingAction choosingAction;

    private final BotConfig config;


    public KindHandsBot(UserRepository userRepository,
                        ForVolunteers volunteers, BotConfig config) {
        super(config.getToken());
        this.config = config;
        choosingAction = new ChoosingAction(this, userRepository, volunteers);
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
        if (choosingAction.checkUser(update)) {
            if (update.hasMessage() && update.getMessage().hasText()) {
                choosingAction.textCommands(update);
            } else if (update.hasCallbackQuery()) {
                choosingAction.buttonCommands(update);
            }
        }
    }

    /**
     * Метод для отправки ответа пользователю.
     * -----||-----
     * A method for sending a response to the user.
     */
    public void sendMessage(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMessage(EditMessageText message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
