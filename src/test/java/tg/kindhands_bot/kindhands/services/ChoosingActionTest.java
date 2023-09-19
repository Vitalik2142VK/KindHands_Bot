package tg.kindhands_bot.kindhands.services;

import com.pengrad.telegrambot.BotUtils;
//import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
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
                createUser(1L, 102030L, "Name", true, "", BotState.NULL)
        ));

        Mockito.when(userRepository.findByChatId(chatId)).thenReturn(findUserByChatId(users, chatId));

        assertFalse(choosingAction.checkUser(update));

        botMessages = new ProcessingBotMessages(update, userRepository, reportAnimalRepository);
        SendMessage actual = botMessages.blockedMessage();

        assertEquals("102030" ,actual.getChatId());
        assertEquals(update.getMessage().getChat().getFirstName() + ", ваш аккаунт заблокирован",
                actual.getText());



    }

    // Дополнительые методы

    private Update getUpdate(String json, String replaced) {
        return BotUtils.fromJson(json.replace("%text%", replaced), Update.class);
    }

    private User createUser(Long id, Long chatId, String name, Boolean blocked,
                            String denialReason, BotState botState) {
        User user = new User();
        user.setId(id);
        user.setChatId(chatId);
        user.setName(name);
        user.setBlocked(blocked);
        user.setDenialReason(denialReason);
        user.setBotState(botState);
        return user;
    }

    private User findUserByChatId(List<User> users, Long chatId) {
        for (var user : users) {
            if (user.getChatId().equals(chatId)) {
                return user;
            }
        }
        throw new NullPointerException();
    }
}
