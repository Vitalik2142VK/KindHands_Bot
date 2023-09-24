package tg.kindhands_bot.kindhands.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import tg.kindhands_bot.kindhands.config.BotConfig;
import tg.kindhands_bot.kindhands.exceptions.RuntimeExceptionAndSendMessage;
import tg.kindhands_bot.kindhands.repositories.photo.ReportAnimalPhotoRepository;
import tg.kindhands_bot.kindhands.repositories.ReportAnimalRepository;
import tg.kindhands_bot.kindhands.repositories.UserRepository;
import tg.kindhands_bot.kindhands.repositories.tamed.TamedAnimalRepository;

@Component
public class KindHandsBot extends TelegramLongPollingBot {

    private final Logger log = LoggerFactory.getLogger(KindHandsBot.class);

    private final ChoosingAction choosingAction;

    private final BotConfig config;


    public KindHandsBot(UserRepository userRepository,
                        ReportAnimalRepository reportAnimalRepository,
                        ReportAnimalPhotoRepository reportAnimalPhotoRepository,
                        TamedAnimalRepository tamedAnimalRepository,
                        VolunteerService volunteers,
                        BotConfig config) {
        super(config.getToken());
        this.config = config;
        choosingAction = new ChoosingAction(this, userRepository, reportAnimalRepository, reportAnimalPhotoRepository,
                tamedAnimalRepository ,volunteers);
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
        try {
            if (choosingAction.checkUser(update)) {
                if (update.hasMessage() && update.getMessage().hasText()) {
                    choosingAction.textCommands();
                } else if (update.hasCallbackQuery()) {
                    choosingAction.buttonCommands();
                } else if (update.hasMessage() && update.getMessage().hasPhoto()) {
                    choosingAction.checkBotState();
                }
            }
        } catch (RuntimeExceptionAndSendMessage e) {
            if (update.hasMessage()) {
                sendMessage(choosingAction.errorMessage(e.getSendMessage()));
            } else if (update.hasCallbackQuery()) {
                sendMessage(choosingAction.errorEditMessage(e.getSendMessage()));
            }
            log.error("Exception: '" + e.getMessage() + "'", e);
        } catch (Exception e) {
            sendMessage(choosingAction.errorMessage());
            log.error("Exception: '" + e.getMessage() + "'", e);
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
            log.error("Exception: '" + e.getMessage() + "'", e);
        }
    }

    public void sendMessage(EditMessageText message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Exception: '" + e.getMessage() + "'", e);
        }
    }
}
