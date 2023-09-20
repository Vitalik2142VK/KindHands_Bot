package tg.kindhands_bot.kindhands.services;

import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import tg.kindhands_bot.kindhands.components.NavigationMenu;
import tg.kindhands_bot.kindhands.components.ProcessingBotMessages;
import tg.kindhands_bot.kindhands.repositories.ReportAnimalPhotoRepository;
import tg.kindhands_bot.kindhands.repositories.ReportAnimalRepository;
import tg.kindhands_bot.kindhands.components.shelters.CatShelter;
import tg.kindhands_bot.kindhands.components.shelters.DogShelter;
import tg.kindhands_bot.kindhands.repositories.UserRepository;
import tg.kindhands_bot.kindhands.components.send_data.SendCatData;
import tg.kindhands_bot.kindhands.components.send_data.SendDogData;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

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
    private final ReportAnimalRepository reportAnimalRepository;

    private final VolunteerService volunteers;

    private ProcessingBotMessages botMessages = null;

    private Update update;

    private final DogShelter dogShelter = new DogShelter();
    private final CatShelter catShelter = new CatShelter();

    private final SendDogData sendDogData = new SendDogData();
    private final SendCatData sendCatData = new SendCatData();

    private final ReportAnimalPhotoRepository reportAnimalPhotoRepository;

    public ChoosingAction(KindHandsBot bot,
                          UserRepository userRepository,
                          ReportAnimalRepository reportAnimalRepository,
                          VolunteerService volunteers,
                          ReportAnimalPhotoRepository reportAnimalPhotoRepository) {
        this.bot = bot;
        this.userRepository = userRepository;
        this.reportAnimalRepository = reportAnimalRepository;
        this.volunteers = volunteers;
        this.reportAnimalPhotoRepository = reportAnimalPhotoRepository;
    }

    /**
     * Метод для обработки, введенного пользователем, текста или текстовых команд.
     * -----||-----
     * A method for processing user-entered text or text commands.
     */
    public void textCommands() {
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
            default: checkBotState();
        }
    }

    /**
     * Метод для проверки пользователя на блокировку
     * -----||-----
     * A method for handling of blocked users
     */
    public boolean checkUser(Update update) {
        this.update = update;

        if (botMessages == null) {
            botMessages = new ProcessingBotMessages(update, userRepository, reportAnimalRepository, reportAnimalPhotoRepository);
        } else {
            botMessages.setUpdate(update);
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
    public void buttonCommands() {
        String callbackData = update.getCallbackQuery().getData();
        long chatId = update.getCallbackQuery().getMessage().getChatId();

        botMessages = new ProcessingBotMessages(update, userRepository, reportAnimalRepository, reportAnimalPhotoRepository);

        switch (callbackData) {
            case DOG_BUTTON:
            case CAT_BUTTON: {
                bot.sendMessage(NavigationMenu.menuShelter(update, callbackData));
                break;
            }
        }

        menuShelterHandler(callbackData);
        menuAssistShelterHandler(callbackData);
        menuShelterInfoHandler(callbackData);
        menuHowGetAnimalFromShelterHandler(callbackData);

    }

    /**
     * Метод обработки кнопок "меню приюта"
     * -----//-----
     * The method of processing the "shelter menu" buttons
     */
    private void menuShelterHandler(String callbackData) {

        switch (callbackData) {
            case CAT_INFO:
            case DOG_INFO:
                bot.sendMessage(NavigationMenu.menuShelterInfo(update, callbackData));
                break;

            case DOG_TAKE_INFO:
            case CAT_TAKE_INFO:
                bot.sendMessage(NavigationMenu.menuHowGetAnimalFromShelter(update, callbackData));
                break;

            case DOG_SEND_REPORT:
            case CAT_SEND_REPORT:
                bot.sendMessage(botMessages.reportAnimalCommand());
                break;

            case CALL_VOLUNTEER:
                bot.sendMessage(botMessages.editExistMessage(volunteers.inviteVolunteer()));
                break;

            case ASSISTANCE_SHELTER:
                bot.sendMessage(NavigationMenu.menuAssistShelter(update));
        }
    }

    public void checkBotState() {
        var user = userRepository.findByChatId(update.getMessage().getChatId());
        var photoSizes = update.getMessage().getPhoto();

        if (user == null) {
            throw new NullPointerException("Exception при попытке поиска user в методе checkBotState() класса ChoosingAction, пользователь с id: '"
                                + update.getMessage().getChatId() + "' не найден");
        }

        switch (Objects.requireNonNull(user).getBotState()) {
            case NULL: {
                bot.sendMessage(botMessages.defaultMessage());
                break;
            }
            case SET_REPORT_ANIMAL: {
                try {
                    var photo = bot.downloadFile(bot.execute(new GetFile(photoSizes.get(photoSizes.size() - 1).getFileId())), new File("photos/reports" + UUID.randomUUID() + ".png"));
                    bot.sendMessage(botMessages.setReportAnimal(photo));
                    break;
                } catch (TelegramApiException | IOException e) {
                    throw new RuntimeException(e);
                }
            }
            default: bot.sendMessage(botMessages.defaultMessage());
        }
    }

    /**
     * Метод обратки кнопок меню "Помощь приюту".
     * -----//-----
     * The method of processing the buttons of the "Help to shelter" menu.
     */
    private void menuAssistShelterHandler(String callbackData) {

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
    private void menuShelterInfoHandler(String callbackData) {

        switch (callbackData) {
            case CAT_ABOUT_SHELTER:
                bot.sendMessage(botMessages.editExistMessage(catShelter.getDetailedInfo()));
                break;

            case DOG_ABOUT_SHELTER:
                bot.sendMessage(botMessages.editExistMessage(dogShelter.getDetailedInfo()));
                break;

            case CAT_SCHEDULE:
                bot.sendMessage(botMessages.editExistMessage(catShelter.getWorkSchedule()));
                break;

            case DOG_SCHEDULE:
                bot.sendMessage(botMessages.editExistMessage(dogShelter.getWorkSchedule()));
                break;

            case DOG_SECURITY_CONTACT:
                bot.sendMessage(botMessages.editExistMessage(dogShelter.getSecurityContactDetails()));
                break;

            case CAT_SECURITY_CONTACT:
                bot.sendMessage(botMessages.editExistMessage(catShelter.getSecurityContactDetails()));
                break;

            case DOG_SAFETY_RECOMMENDATION:
                bot.sendMessage(botMessages.editExistMessage(dogShelter.getInfoSafetyPrecautions()));
                break;

            case CAT_SAFETY_RECOMMENDATION:
                bot.sendMessage(botMessages.editExistMessage(catShelter.getInfoSafetyPrecautions()));
                break;

            case USER_CALL_CONTACT:
                bot.sendMessage(botMessages.editExistMessage("ЗАГЛУШКА!"));
                break;

            case DOG_ADDRESS_SHELTER:
                bot.sendMessage(botMessages.editExistMessage(dogShelter.getAddress()));
                break;

            case CAT_ADDRESS_SHELTER:
                bot.sendMessage(botMessages.editExistMessage(catShelter.getAddress()));
                break;

            case DOG_TRAVEL_SHELTER:
                bot.sendMessage(botMessages.editExistMessage(dogShelter.getDrivingDirections()));
                break;

            case CAT_TRAVEL_SHELTER:
                bot.sendMessage(botMessages.editExistMessage(catShelter.getDrivingDirections()));
                break;
        }
    }

    /**
     * Метод обработки кнопок меню "Как взять животное из приюта".
     * -----//-----
     * The method of processing the menu buttons "How to take an animal from a shelter".
     */
    private void menuHowGetAnimalFromShelterHandler(String callbackData) {

        switch (callbackData) {

            case DOG_INTRODUCTION_RULES:
                bot.sendMessage(botMessages.editExistMessage(sendDogData.PrintTheAnimalIntroductionRules()));
                break;

            case CAT_INTRODUCTION_RULES:
                bot.sendMessage(botMessages.editExistMessage(sendCatData.PrintTheAnimalIntroductionRules()));
                break;

            case DOG_LIST_DOCUMENTS:
                bot.sendMessage(botMessages.editExistMessage(sendDogData.printListOfDocuments()));
                break;

            case CAT_LIST_DOCUMENTS:
                bot.sendMessage(botMessages.editExistMessage(sendCatData.printListOfDocuments()));
                break;

            case DOG_TRANSPORTING:
                bot.sendMessage(botMessages.editExistMessage(sendDogData.printRecommendationsForTransporting()));
                break;

            case CAT_TRANSPORTING:
                bot.sendMessage(botMessages.editExistMessage(sendCatData.printRecommendationsForTransporting()));
                break;

            case DOG_HOUSE_SMALL_ANIMAL:
                bot.sendMessage(botMessages.editExistMessage(sendDogData.printRecommendationsForHouseSmallAnimal()));
                break;

            case CAT_HOUSE_SMALL_ANIMAL:
                bot.sendMessage(botMessages.editExistMessage(sendCatData.printRecommendationsForHouseSmallAnimal()));
                break;

            case DOG_HOUSE_ADULT_ANIMAL:
                bot.sendMessage(botMessages.editExistMessage(sendDogData.printRecommendationsForHouseAdultAnimal()));
                break;

            case CAT_HOUSE_ADULT_ANIMAL:
                bot.sendMessage(botMessages.editExistMessage(sendCatData.printRecommendationsForHouseAdultAnimal()));
                break;

            case DOG_HOUSE_DISABLED_ANIMAL:
                bot.sendMessage(botMessages.editExistMessage(sendDogData.printRecommendationsForHouseDisabledAnimal()));
                break;

            case CAT_HOUSE_DISABLED_ANIMAL:
                bot.sendMessage(botMessages.editExistMessage(sendCatData.printRecommendationsForHouseDisabledAnimal()));
                break;

            case DOG_REJECTION_REASON:
                bot.sendMessage(botMessages.editExistMessage(sendDogData.printRejectionReason()));
                break;

            case CAT_REJECTION_REASON:
                bot.sendMessage(botMessages.editExistMessage(sendCatData.printRejectionReason()));
                break;

            case DOG_COMMUNICATION_ADVICES:
                bot.sendMessage(botMessages.editExistMessage(sendDogData.printInitialCommunicationCynologistAdvices()));
                break;

            case VERIFIED_DOG_HANDLERS:
                bot.sendMessage(botMessages.editExistMessage(sendDogData.printInformationToVerifiedDogHandlers()));
                break;

            case USER_CALL_CONTACT:
                bot.sendMessage(botMessages.editExistMessage("ЗАГЛУШКА!"));
                break;
        }
    }
}
