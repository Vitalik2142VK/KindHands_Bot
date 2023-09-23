package tg.kindhands_bot.kindhands.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import tg.kindhands_bot.kindhands.components.ProcessingBotMessages;
import tg.kindhands_bot.kindhands.entities.User;
import tg.kindhands_bot.kindhands.enums.BotState;
import tg.kindhands_bot.kindhands.repositories.ReportAnimalPhotoRepository;
import tg.kindhands_bot.kindhands.repositories.ReportAnimalRepository;
import tg.kindhands_bot.kindhands.repositories.UserRepository;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static tg.kindhands_bot.kindhands.services.AdditionalMethods.*;
import static tg.kindhands_bot.kindhands.utils.MessageConstants.*;

@ExtendWith(MockitoExtension.class)
public class ChoosingActionTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private ReportAnimalRepository reportAnimalRepository;
    @Mock
    private ReportAnimalPhotoRepository reportPhotoRepository;

    @Mock
    private VolunteerService volunteerService;

    @Mock
    private KindHandsBot bot;

    private ProcessingBotMessages botMessages;

    private ChoosingAction choosingAction;

    private String json;

    @BeforeEach
    public void beforeEach() throws URISyntaxException, IOException {
        choosingAction = new ChoosingAction(bot = Mockito.mock(KindHandsBot.class), userRepository, reportAnimalRepository,
                reportPhotoRepository, volunteerService);
        json = Files.readString(
                Paths.get(KindHandsBot.class.getResource("text_update.json").toURI())
        );
    }

    @Test
    public void checkUserTrueUserNull() {
        Update update = getUpdateButton(json, "/start");
        long chatId = update.getMessage().getChatId();

        Mockito.when(userRepository.findByChatId(chatId)).thenReturn(null);

        assertTrue(choosingAction.checkUser(update));
    }

    @Test
    public void checkUserTrue() {
        Update update = getUpdate(json, "/start");

        long chatId = update.getMessage().getChatId();

        List<User> users = new ArrayList<>(List.of(
                createUser(1L, 102030L, "Name", false, "", BotState.NULL)
        ));

        Mockito.when(userRepository.findByChatId(chatId)).thenReturn(findUserByChatId(users, chatId));

        assertTrue(choosingAction.checkUser(update));
    }

    @Test
    public void checkUserFalse() {
        Update update = getUpdate(json, "/start");
        long chatId = update.getMessage().getChatId();

        List<User> users = new ArrayList<>(List.of(
                createUser(1L, 102030L, "Name", true, "Text_block", BotState.NULL)
        ));

        Mockito.when(userRepository.findByChatId(chatId)).thenReturn(findUserByChatId(users, chatId));

        assertFalse(choosingAction.checkUser(update));

        reflectionBotMessages();
        SendMessage actual = botMessages.blockedMessage();

        assertEquals("102030" ,actual.getChatId());
        assertEquals(update.getMessage().getChat().getFirstName() + ", ваш аккаунт заблокирован",
                actual.getText());
    }

    @Test
    public void textCommand() {
        Update update = getUpdate(json, "/start");
        long chatId = update.getMessage().getChatId();

        User user = createUser(1L, 102030L, "Name", false, null, BotState.NULL);
        List<User> users = new ArrayList<>();

        Mockito.when(userRepository.findByChatId(chatId)).thenReturn(null);
        Mockito.when(userRepository.save(any(User.class))).thenReturn(addUser(users, user));

        choosingAction.checkUser(update);
        choosingAction.textCommands();

        ArgumentCaptor<User> argumentCaptor = ArgumentCaptor.forClass(User.class);
        Mockito.verify(userRepository).save(argumentCaptor.capture());
        User actualUser = argumentCaptor.getValue();

        assertEquals(user, actualUser);

        reflectionBotMessages();
        SendMessage actual = botMessages.startCommand();

        assertEquals("102030" ,actual.getChatId());
        assertEquals("Здравствуйте," + update.getMessage().getChat().getFirstName() + "! Я бот приюта для животных \"В добрые руки\".",
                actual.getText());
    }

    @Test
    public void textButtonCommands() {
        Update update = getUpdateButton(json, "CAT_SH");

        long chatId = update.getCallbackQuery().getMessage().getChatId();
        User user = createUser(1L, 102030L, "Name", false, null, BotState.NULL);

        Mockito.when(userRepository.findByChatId(chatId)).thenReturn(user);

        choosingAction.checkUser(update);
        choosingAction.buttonCommands();

        assertEquals("CAT_SH" ,update.getCallbackQuery().getData());

        EditMessageText actual = getEditMessageTextActual();
        assertEquals("102030", actual.getChatId());
        assertEquals(7416, actual.getMessageId());
        assertEquals("Вы выбрали кошачий приют.", actual.getText());
        assertEquals(menuShelterButton("CAT_SH"), actual.getReplyMarkup());
    }

    @Test
    public void menuShelterHandler() {
        Update update = getUpdateButton(json, "INFO_GET_C");

        long chatId = update.getCallbackQuery().getMessage().getChatId();
        User user = createUser(1L, 102030L, "Name", false, null, BotState.NULL);

        Mockito.when(userRepository.findByChatId(chatId)).thenReturn(user);

        choosingAction.checkUser(update);
        choosingAction.buttonCommands();

        assertEquals("INFO_GET_C" ,update.getCallbackQuery().getData());

        EditMessageText actual = getEditMessageTextActual();
        assertEquals("102030", actual.getChatId());
        assertEquals(7416, actual.getMessageId());
        assertEquals("Информация о кошачем приюте:", actual.getText());
        assertEquals(menuGetShelterInfoButton("INFO_GET_C"), actual.getReplyMarkup());
    }

    // Дополнительные методы

    private void reflectionBotMessages() {
        try {
            Field field = choosingAction.getClass().getDeclaredField("botMessages");
            field.setAccessible(true);
            botMessages = (ProcessingBotMessages) field.get(choosingAction);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private EditMessageText getEditMessageTextActual() {
        ArgumentCaptor<EditMessageText> argumentCaptor = ArgumentCaptor.forClass(EditMessageText.class);
        Mockito.verify(bot).sendMessage(argumentCaptor.capture());
        return argumentCaptor.getValue();
    }

    private InlineKeyboardMarkup menuShelterButton(String shelter) {

        String animalText;

        switch (shelter) {
            case DOG_BUTTON:
                animalText = "_D";
                break;
            case CAT_BUTTON:
                animalText = "_C";
                break;
            default:
                return null; // заменить на Exception
        }

        var infoButton = createButton("Узнать информацию о приюте", SCHEMA_INFO + animalText);
        var howGetButton = createButton("Как взять животное из приюта", SCHEMA_TAKE_INFO + animalText);
        var sendReportButton = createButton("Прислать отчёт о питомце", SCHEMA_SEND_REPORT + animalText);
        var callVolunteerButton = createButton("Позвать волонтёра", CALL_VOLUNTEER);
        var assistanceToShelterButton = createButton("Помощь приюту", ASSISTANCE_SHELTER);

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInTwoLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInThreeLine = new ArrayList<>();

        rowInLine.add(infoButton);
        rowInLine.add(howGetButton);
        rowInTwoLine.add(sendReportButton);
        rowInTwoLine.add(callVolunteerButton);
        rowInThreeLine.add(assistanceToShelterButton);

        rowsInLine.add(rowInLine);
        rowsInLine.add(rowInTwoLine);
        rowsInLine.add(rowInThreeLine);

        markup.setKeyboard(rowsInLine);
        return markup;
    }

    private InlineKeyboardMarkup menuGetShelterInfoButton(String shelter) {

        String animalText;

        switch (shelter) {
            case DOG_INFO:
                animalText = "_D";
                break;
            case CAT_INFO:
                animalText = "_C";
                break;
            default:
                return null;
        }

        var aboutShelterButton = createButton("Подробнее о приюте", ABOUT_SHELTER + animalText);
        var scheduleButton = createButton("Расписание приюта", SCHEDULE + animalText);
        var securityContactButton = createButton("Контактные данные охраны", SECURITY_CONTACT + animalText);
        var safetyRecommendationButton = createButton("Рекомендации по технике безопасности",
                SAFETY_RECOMMENDATION + animalText);
        var callContactButton = createButton("Записать контактные данные для связи", USER_CALL_CONTACT);
        var addressShelterButton = createButton("Адрес приюта", ADDRESS_SHELTER + animalText);
        var travelToShelterButton = createButton("Схема проезда", TRAVEL_SHELTER + animalText);

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInTwoLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInThreeLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInFourLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInFiveLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInSixLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInSevenLine = new ArrayList<>();

        rowInLine.add(aboutShelterButton);
        rowInTwoLine.add(scheduleButton);
        rowInThreeLine.add(securityContactButton);
        rowInFourLine.add(safetyRecommendationButton);
        rowInFiveLine.add(callContactButton);
        rowInSixLine.add(addressShelterButton);
        rowInSevenLine.add(travelToShelterButton);

        rowsInLine.add(rowInLine);
        rowsInLine.add(rowInTwoLine);
        rowsInLine.add(rowInSixLine);
        rowsInLine.add(rowInSevenLine);
        rowsInLine.add(rowInThreeLine);
        rowsInLine.add(rowInFourLine);
        rowsInLine.add(rowInFiveLine);

        markup.setKeyboard(rowsInLine);
        return markup;
    }

    private InlineKeyboardButton createButton(String text, String callbackData) {

        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(text);
        button.setCallbackData(callbackData);

        return button;
    }
}
