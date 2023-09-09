package tg.kindhands_bot.kindhands.services;

import org.telegram.telegrambots.meta.api.objects.Update;
import tg.kindhands_bot.kindhands.components.NavigationMenu;
import tg.kindhands_bot.kindhands.components.ProcessingBotMessages;
import tg.kindhands_bot.kindhands.repositories.UserRepository;

import static tg.kindhands_bot.kindhands.utils.CommandConstants.START_COMMAND;
import static tg.kindhands_bot.kindhands.utils.MessageConstants.*;

/**
 * Переопределяет полученные данные в требуемый класс или метод.
 * -----||-----
 * Overrides the received data into the required class or method.
 */
public class ChoosingAction {

    private final KindHandsBot bot;

    private final UserRepository userRepository;

    private final VolunteerService volunteers;

    private ProcessingBotMessages botMessages = null;

    public ChoosingAction(KindHandsBot bot, UserRepository userRepository, VolunteerService volunteers) {
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
            case START_COMMAND: {
                if (userRepository.findByChatId(chatId) == null) {
                    bot.sendMessage(botMessages.startCommand(chatId, update.getMessage().getFrom().getFirstName()));
                }
                bot.sendMessage(NavigationMenu.choosingShelter(chatId));
                break;
            }
            default:
                bot.sendMessage(botMessages.defaultMessage());
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

        ProcessingBotMessages botMessages = new ProcessingBotMessages(update, userRepository);

        switch (callbackData) {
            case DOG_BUTTON:
            case CAT_BUTTON: {
                bot.sendMessage(NavigationMenu.menuShelter(update, callbackData));
                break;
            }
        }

        menuShelterHandler(botMessages, update, callbackData);
        menuAssistShelterHandler(botMessages, update, callbackData);
        menuShelterInfoHandler(botMessages, update, callbackData);

    }

    /**
     * Метод обработки кнопок "меню приюта"
     * -----//-----
     * The method of processing the "shelter menu" buttons
     */
    private void menuShelterHandler(ProcessingBotMessages botMessages, Update update, String callbackData) {

        switch (callbackData) {
            case CAT_INFO:
            case DOG_INFO:
                bot.sendMessage(NavigationMenu.menuShelterInfo(update, callbackData));
                break;

            case DOG_TAKE_INFO:
                bot.sendMessage(botMessages.editExistMessage("Как взять собаку из приюта: "));
                break;

            case DOG_SEND_REPORT:
                bot.sendMessage(botMessages.editExistMessage("Отчёт о питомце(собаке): "));
                break;


            case CAT_TAKE_INFO:
                bot.sendMessage(botMessages.editExistMessage("Как взять кошку из приюта: "));
                break;

            case CAT_SEND_REPORT:
                bot.sendMessage(botMessages.editExistMessage("Отчёт о питомце(кошке): "));
                break;

            case CALL_VOLUNTEER:
                bot.sendMessage(botMessages.editExistMessage(volunteers.inviteVolunteer()));
                break;

            case ASSISTANCE_SHELTER:
                bot.sendMessage(NavigationMenu.menuAssistShelter(update));
        }
    }


    /**
     * Метод обратки кнопок меню "Помощь приюту".
     * -----//-----
     * The method of processing the buttons of the "Help to shelter" menu.
     */
    private void menuAssistShelterHandler(ProcessingBotMessages botMessages, Update update, String callbackData) {

        switch (callbackData) {
            case REQUISITES:
                bot.sendMessage(botMessages.editExistMessage("Реквизиты:"));
                break;

            case NECESSARY:
                bot.sendMessage(botMessages.editExistMessage("Необходимое:"));
                break;

            case BECOME_VOLUNTEER:
                bot.sendMessage(botMessages.editExistMessage("Стать волонтёром:"));
                break;
        }

    }

    /**
     * Метод обработки кнопок меню "Узнать информацию о приюте".
     * -----//-----
     * The method of processing the menu buttons "Find out information about the shelter".
     */
    private void menuShelterInfoHandler(ProcessingBotMessages botMessages, Update update, String callbackData) {

        switch (callbackData) {
            case CAT_ABOUT_SHELTER:
                bot.sendMessage(botMessages.editExistMessage("."));
                break;

            case DOG_ABOUT_SHELTER:
                bot.sendMessage(botMessages.editExistMessage(".."));
                break;

            case CAT_SCHEDULE:
                bot.sendMessage(botMessages.editExistMessage("..."));
                break;

            case DOG_SCHEDULE:
                bot.sendMessage(botMessages.editExistMessage("...."));
                break;

            case DOG_SECURITY_CONTACT:
                bot.sendMessage(botMessages.editExistMessage("....."));
                break;

            case CAT_SECURITY_CONTACT:
                bot.sendMessage(botMessages.editExistMessage("......"));
                break;

            case SAFETY_RECOMMENDATION:
                bot.sendMessage(botMessages.editExistMessage("......."));
                break;

            case USER_CALL_CONTACT:
                bot.sendMessage(botMessages.editExistMessage("........"));
                break;
        }
    }
}
