package tg.kindhands_bot.kindhands.services;

import com.pengrad.telegrambot.BotUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.OngoingStubbing;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import tg.kindhands_bot.kindhands.components.ProcessingBotMessages;
import tg.kindhands_bot.kindhands.entities.User;
import tg.kindhands_bot.kindhands.enums.BotState;
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

@ExtendWith(MockitoExtension.class)
public class ChoosingActionTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private ReportAnimalRepository reportAnimalRepository;
    @Mock
    private VolunteerService volunteerService;
    @Mock
    private KindHandsBot bot;

    private ProcessingBotMessages botMessages;

    private ChoosingAction choosingAction;

    private String json;

    @BeforeEach
    public void beforeEach() throws URISyntaxException, IOException {
        choosingAction = new ChoosingAction(bot = Mockito.mock(KindHandsBot.class), userRepository, reportAnimalRepository, volunteerService);
        json = Files.readString(
                Paths.get(KindHandsBot.class.getResource("text_update.json").toURI())
        );
    }

    @Test
    public void checkUserTrueUserNull() throws URISyntaxException, IOException {
        Update update = getUpdate(json, "/start");
        long chatId = update.getMessage().getChatId();

        Mockito.when(userRepository.findByChatId(chatId)).thenReturn(null);

        assertTrue(choosingAction.checkUser(update));
    }

    @Test
    public void checkUserTrue() throws URISyntaxException, IOException {
        Update update = getUpdate(json, "/start");

        long chatId = update.getMessage().getChatId();

        List<User> users = new ArrayList<>(List.of(
                createUser(1L, 102030L, "Name", false, "", BotState.NULL)
        ));

        Mockito.when(userRepository.findByChatId(chatId)).thenReturn(findUserByChatId(users, chatId));

        assertTrue(choosingAction.checkUser(update));
    }

    @Test
    public void checkUserFalse() throws URISyntaxException, IOException {
        Update update = getUpdate(json, "/start");
        long chatId = update.getMessage().getChatId();

        List<User> users = new ArrayList<>(List.of(
                createUser(1L, 102030L, "Name", true, "Text_block", BotState.NULL)
        ));

        Mockito.when(userRepository.findByChatId(chatId)).thenReturn(findUserByChatId(users, chatId));

        assertFalse(choosingAction.checkUser(update));

        botMessages = new ProcessingBotMessages(update, userRepository, reportAnimalRepository);
        SendMessage actual = botMessages.blockedMessage();

        assertEquals("102030" ,actual.getChatId());
        assertEquals(update.getMessage().getChat().getFirstName() + ", ваш аккаунт заблокирован",
                actual.getText());
    }

    @Test
    public void textCommand() throws NoSuchFieldException, IllegalAccessException {
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

        botMessages = new ProcessingBotMessages(update, userRepository, reportAnimalRepository);

        Field field = choosingAction.getClass().getDeclaredField("botMessages");
        field.setAccessible(true);
        botMessages = (ProcessingBotMessages) field.get(choosingAction);
        SendMessage actual = botMessages.startCommand();

        assertEquals("102030" ,actual.getChatId());
        assertEquals("Здравствуйте," + update.getMessage().getChat().getFirstName() + "! Я бот приюта для животных \"В добрые руки\".",
                actual.getText());
    }


}
