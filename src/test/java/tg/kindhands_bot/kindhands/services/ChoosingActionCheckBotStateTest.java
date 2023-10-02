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
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import tg.kindhands_bot.kindhands.components.ProcessingBotMessages;
import tg.kindhands_bot.kindhands.entities.ReportAnimal;
import tg.kindhands_bot.kindhands.entities.User;
import tg.kindhands_bot.kindhands.entities.Volunteer;
import tg.kindhands_bot.kindhands.entities.tamed.TamedAnimal;
import tg.kindhands_bot.kindhands.entities.tamed.TamedCat;
import tg.kindhands_bot.kindhands.enums.BotState;
import tg.kindhands_bot.kindhands.repositories.ReportAnimalRepository;
import tg.kindhands_bot.kindhands.repositories.UserRepository;
import tg.kindhands_bot.kindhands.repositories.photo.ReportAnimalPhotoRepository;
import tg.kindhands_bot.kindhands.repositories.tamed.TamedAnimalRepository;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.nio.channels.FileLock;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static tg.kindhands_bot.kindhands.AdditionalMethods.*;

@ExtendWith(MockitoExtension.class)
public class ChoosingActionCheckBotStateTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private ReportAnimalRepository reportAnimalRepository;
    @Mock
    private ReportAnimalPhotoRepository reportPhotoRepository;
    @Mock
    private TamedAnimalRepository tamedAnimalRepository;
    @Mock
    private tg.kindhands_bot.kindhands.repositories.VolunteersRepository volunteersRepository;

    @Mock
    private KindHandsBot bot;

    private ProcessingBotMessages botMessages;


    private ChoosingAction choosingAction;

    private String json;

    @BeforeEach
    public void beforeEach() throws URISyntaxException, IOException {
        choosingAction = new ChoosingAction(bot = Mockito.mock(KindHandsBot.class), userRepository, reportAnimalRepository,
                reportPhotoRepository, tamedAnimalRepository, volunteersRepository);
        json = Files.readString(
                Paths.get(KindHandsBot.class.getResource("text_update.json").toURI())
        );
    }

    @Test
    public void checkBotStateException() {
        Update update = getUpdate(json, "Random text.");
        long chatId = update.getMessage().getChatId();

        Mockito.when(userRepository.findByChatId(chatId)).thenReturn(null);

        assertTrue(choosingAction.checkUser(update));
        assertThrows(NullPointerException.class, () -> choosingAction.checkBotState(), "Exception при попытке поиска user в методе checkBotState() класса ChoosingAction, пользователь с id: '"
                + update.getMessage().getChatId() + "' не найден");
    }

    @Test
    public void checkBotStateNull() {
        Update update = getUpdate(json, "Random text.");
        long chatId = update.getMessage().getChatId();

        List<User> users = new ArrayList<>(List.of(
                createUser(1L, 102030L, "Name", false, "", BotState.NULL)
        ));

        Mockito.when(userRepository.findByChatId(chatId)).thenReturn(findUserByChatId(users, chatId));

        assertTrue(choosingAction.checkUser(update));

        choosingAction.textCommands();

        SendMessage actual = getSendMessageActual();

        assertEquals("102030", actual.getChatId());
        assertEquals("Не корректно введено сообщение.", actual.getText());
    }

//    @Test
//    public void checkBotStateSetReportAnimalPhoto() throws TelegramApiException {
//        Update update = getUpdate(json, "Random text.");
//        long chatId = update.getMessage().getChatId();
//
//        LocalDate date = LocalDate.now();
//
//        List<User> users = new ArrayList<>(List.of(
//                createUser(1L, 102030L, "LastName", "FirstName", "8800", false, "", BotState.SET_REPORT_ANIMAL_PHOTO)
//        ));
//
//        TamedAnimal tamedAnimal = createTamedCat(1L, users.get(0), null, date.minusDays(1), date.minusDays(1));
//
//        List<ReportAnimal> reports = new ArrayList<>();
//        ReportAnimal reportExpected = new ReportAnimal();
//        reportExpected.setId(1L);
//
//        File file = new File();
//
//        Mockito.when(userRepository.findByChatId(chatId)).thenReturn(findUserByChatId(users, chatId));
//        //Mockito.when(bot.downloadFile(any(File.class))).thenReturn(file);
//        Mockito.when(tamedAnimalRepository.findByUser_ChatId(chatId)).thenReturn(tamedAnimal);
//        Mockito.when(tamedAnimalRepository.save(any(TamedAnimal.class))).thenReturn(tamedAnimal);
//        Mockito.when(reportAnimalRepository.findByDateAndTamedAnimal_Id(date, tamedAnimal.getId())).thenReturn(null);
//        Mockito.when(reportAnimalRepository.save(any(ReportAnimal.class))).thenReturn(addReportAnimal(reports, reportExpected));
//
//        choosingAction.checkUser(update);
//        choosingAction.checkBotState();
//
//        SendMessage actual = getSendMessageActual();
//
//        assertEquals("102030", actual.getChatId());
//        assertEquals("Не корректно введено сообщение.", actual.getText());
//    }

    @Test
    public void checkBotStateSetReportAnimal() {
        Update update = getUpdate(json, "Description report.");
        long chatId = update.getMessage().getChatId();

        LocalDate date = LocalDate.now();

        List<User> users = new ArrayList<>(List.of(
                createUser(1L, 102030L, "LastName", "FirstName", "8800", false, "", BotState.SET_REPORT_ANIMAL)
        ));

        TamedAnimal tamedAnimal = createTamedCat(1L, users.get(0), null, date.minusDays(1), date.minusDays(1));

        List<ReportAnimal> reports = new ArrayList<>();
        ReportAnimal report = createReportAnimal(1L, tamedAnimal, null, date, 1);


        File file = new File();

        Mockito.when(userRepository.findByChatId(chatId)).thenReturn(findUserByChatId(users, chatId));
        Mockito.when(reportAnimalRepository.findByDateAndTamedAnimal_User_ChatId(date, 102030L)).thenReturn(report);

        choosingAction.checkUser(update);
        choosingAction.checkBotState();

        ArgumentCaptor<ReportAnimal> argumentCaptor = ArgumentCaptor.forClass(ReportAnimal.class);
        Mockito.verify(reportAnimalRepository).save(argumentCaptor.capture());
        ReportAnimal reportActual = argumentCaptor.getValue();

        assertEquals("Description report." , reportActual.getDescription());

        SendMessage actual = getSendMessageActual();

        assertEquals("102030", actual.getChatId());
        assertEquals("Отчет отправлен.", actual.getText());
    }

    @Test
    public void checkBotStateSetNumPhone() {
        Update update = getUpdate(json, "88005553535");
        long chatId = update.getMessage().getChatId();

        List<User> users = new ArrayList<>(List.of(
                createUser(1L, 102030L, "Name", false, "", BotState.SET_NUM_PHONE)
        ));

        Mockito.when(userRepository.findByChatId(chatId)).thenReturn(findUserByChatId(users, chatId));

        choosingAction.checkUser(update);
        choosingAction.checkBotState();

        ArgumentCaptor<User> argumentCaptor = ArgumentCaptor.forClass(User.class);
        Mockito.verify(userRepository).save(argumentCaptor.capture());
        User actualUser = argumentCaptor.getValue();

        assertEquals("+7(800)555-35-35", actualUser.getPhone());
        assertEquals(BotState.SET_FULL_NAME, actualUser.getBotState());

        SendMessage actual = getSendMessageActual();

        assertEquals("102030", actual.getChatId());
        assertEquals("Номер телефона добавлен.\n\nВведите одним сообщением Вашу: " +
                "Фамилию Имя Отчество(при наличии)", actual.getText());
    }

    @Test
    public void checkBotStateSetFullName() {
        Update update = getUpdate(json, "Иванов-петров>, иван, иванович.");
        long chatId = update.getMessage().getChatId();

        List<User> users = new ArrayList<>(List.of(
                createUser(1L, 102030L, "" ,"Name", "88005553535", false, "", BotState.SET_FULL_NAME)
        ));

        Mockito.when(userRepository.findByChatId(chatId)).thenReturn(findUserByChatId(users, chatId));

        choosingAction.checkUser(update);
        choosingAction.checkBotState();

        ArgumentCaptor<User> argumentCaptor = ArgumentCaptor.forClass(User.class);
        Mockito.verify(userRepository).save(argumentCaptor.capture());
        User actualUser = argumentCaptor.getValue();

        assertEquals("Иванов-Петров", actualUser.getLastName());
        assertEquals("Иван",actualUser.getFirstName());
        assertEquals("Иванович",actualUser.getPatronymic());
        assertEquals(BotState.NULL, actualUser.getBotState());

        SendMessage actual = getSendMessageActual();

        assertEquals("102030", actual.getChatId());
        assertEquals("Фамилия Имя Отчество добавлены.", actual.getText());
    }

    @Test
    public void checkBotStateBecomeVolunteer() {
        Update update = getUpdate(json, "88005550000");
        long chatId = update.getMessage().getChatId();

        List<User> users = new ArrayList<>(List.of(
                createUser(1L, 102030L ,"Name", false, "", BotState.BECOME_VOLUNTEER)
        ));

        Mockito.when(userRepository.findByChatId(chatId)).thenReturn(findUserByChatId(users, chatId));

        choosingAction.checkUser(update);
        choosingAction.checkBotState();

        ArgumentCaptor<User> argumentCaptor = ArgumentCaptor.forClass(User.class);
        Mockito.verify(userRepository).save(argumentCaptor.capture());
        User actualUser = argumentCaptor.getValue();

        assertEquals(BotState.SET_FULL_NAME, actualUser.getBotState());

        ArgumentCaptor<Volunteer> argumentCaptor_2 = ArgumentCaptor.forClass(Volunteer.class);
        Mockito.verify(volunteersRepository).save(argumentCaptor_2.capture());
        Volunteer actualVolunteer = argumentCaptor_2.getValue();

        assertEquals(users.get(0), actualVolunteer.getUser());
        assertEquals("+7(800)555-00-00", actualVolunteer.getPhone());

        SendMessage actual = getSendMessageActual();

        assertEquals("102030", actual.getChatId());
        assertEquals("Номер телефона добавлен.\n\nВведите одним сообщением Вашу: " +
                "Фамилию Имя Отчество(при наличии)", actual.getText());
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

    private SendMessage getSendMessageActual() {
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(bot).sendMessage(argumentCaptor.capture());
        return argumentCaptor.getValue();
    }
}
