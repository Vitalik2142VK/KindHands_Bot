package tg.kindhands_bot.kindhands.service;

import org.telegram.telegrambots.meta.api.objects.Update;
import tg.kindhands_bot.kindhands.components.NavigationMenu;
import tg.kindhands_bot.kindhands.components.ProcessingBotMessages;
import tg.kindhands_bot.kindhands.components.volunteer.ForVolunteers;
import tg.kindhands_bot.kindhands.repositories.UserRepository;

public class ChoosingAction {

    private final KindHandsBot bot;

    private final UserRepository userRepository;

    private final ForVolunteers volunteers;

    private ProcessingBotMessages botMessages = null;

    public ChoosingAction(KindHandsBot bot, UserRepository userRepository, ForVolunteers volunteers) {
        this.bot = bot;
        this.userRepository = userRepository;
        this.volunteers = volunteers;
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
                    bot.sendMessage(botMessages.startCommand(chatId, update.getMessage().getFrom().getFirstName()));
                }
                bot.sendMessage(NavigationMenu.choosingShelter(chatId));
                break;
            }
            default: bot.sendMessage(botMessages.defaultMessage());
        }
    }

    /**
     * Метод для проверки пользователя на блокировку
     * -----||-----
     * A method for handling of blocked users
     */
    public boolean checkUser(Update update) {
        if (botMessages == null) {
            botMessages = new ProcessingBotMessages(update, userRepository);
        }

        long chatId;

        if (update.hasMessage()) {
            chatId = update.getMessage().getChatId();
        } else if (update.hasCallbackQuery()) {
            chatId = update.getCallbackQuery().getMessage().getChatId();
        } else {
            bot.sendMessage(botMessages.defaultMessage());
            return false;
        }

        var user = userRepository.findByChatId(chatId);

        if (user != null && user.getBlocked()) {
            bot.sendMessage(botMessages.blockedMessage());
            return false;
        }
        return true;
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
            case "DOG_SH":
            case "CAT_SH": {
                bot.sendMessage(NavigationMenu.menuShelter(update, callbackData));
                break;
            }
        }

        menuShelterHandler(botMessages, callbackData);

    }

    /**
     * Метод обработки кнопок "меню приюта"
     * -----//-----
     *The method of processing the "shelter menu" buttons
     */

    private void menuShelterHandler(ProcessingBotMessages botMessages, String callbackData) {

//        long chatId = botMessages.getUpdate().getCallbackQuery().getMessage().getChatId();

        switch (callbackData){
            case "INFO_GET_D":
                bot.sendMessage(botMessages.editExistMessage("Информация о собачем приюте: "));
                break;

            case "HOW_GET_D":
                bot.sendMessage(botMessages.editExistMessage("Как взять собаку из приюта: "));
                break;

            case "SEND_RP_D":
                bot.sendMessage(botMessages.editExistMessage("Отчёт о питомце(собаке): "));
                break;

            case "INFO_GET_C":
                bot.sendMessage(botMessages.editExistMessage("Информация о кошачем приюте: "));
                break;

            case "HOW_GET_C":
                bot.sendMessage(botMessages.editExistMessage("Как взять кошку из приюта: "));
                break;

            case "SEND_RP_C":
                bot.sendMessage(botMessages.editExistMessage("Отчёт о питомце(кошке): "));
                break;

            case "CALL_VL":
                bot.sendMessage(botMessages.editExistMessage(volunteers.inviteVolunteer()));
                break;
        }
    }
}
