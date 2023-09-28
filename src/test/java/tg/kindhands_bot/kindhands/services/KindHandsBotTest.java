package tg.kindhands_bot.kindhands.services;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import tg.kindhands_bot.kindhands.components.NavigationMenu;
import tg.kindhands_bot.kindhands.config.BotConfig;
import tg.kindhands_bot.kindhands.entities.User;
import tg.kindhands_bot.kindhands.enums.BotState;
import tg.kindhands_bot.kindhands.repositories.VolunteersRepository;
import tg.kindhands_bot.kindhands.repositories.photo.ReportAnimalPhotoRepository;
import tg.kindhands_bot.kindhands.repositories.ReportAnimalRepository;
import tg.kindhands_bot.kindhands.repositories.UserRepository;
import tg.kindhands_bot.kindhands.repositories.tamed.TamedAnimalRepository;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static tg.kindhands_bot.kindhands.services.AdditionalMethods.*;
import static tg.kindhands_bot.kindhands.utils.MessageConstants.CAT_BUTTON;
import static tg.kindhands_bot.kindhands.utils.MessageConstants.DOG_BUTTON;

@ExtendWith(MockitoExtension.class)
public class KindHandsBotTest {

    @Mock
    private BotConfig botConfig;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ReportAnimalRepository reportAnimalRepository;
    @Mock
    private ReportAnimalPhotoRepository reportPhotoRepository;
    @Mock
    private TamedAnimalRepository tamedAnimalRepository;
    @Mock
    private VolunteersRepository volunteersRepository;

    @InjectMocks
    private KindHandsBot bot;

    private ChoosingAction choosingAction;
    private String json;

    @BeforeEach
    public void beforeEach() throws URISyntaxException, IOException {
        bot = new KindHandsBot(userRepository, reportAnimalRepository, reportPhotoRepository,tamedAnimalRepository ,volunteersRepository, botConfig);
        json = Files.readString(
                Paths.get(KindHandsBot.class.getResource("text_update.json").toURI())
        );
    }

    @Test
    public void onUpdateReceived() throws NoSuchFieldException, IllegalAccessException {
        Update update = getUpdate(json, "/start");
        long chatId = update.getMessage().getChatId();

        User user = createUser(1L, 102030L, "Name", false, null, BotState.NULL);

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> buttonsShelter = choosingShelter();

        markup.setKeyboard(buttonsShelter);

        Mockito.when(userRepository.findByChatId(chatId)).thenReturn(user);

        bot.onUpdateReceived(update);

        Field fieldChoosingAction = bot.getClass().getDeclaredField("choosingAction");
        fieldChoosingAction.setAccessible(true);
        choosingAction = (ChoosingAction) fieldChoosingAction.get(bot);

        assertTrue(choosingAction.checkUser(update));

        SendMessage actual = NavigationMenu.choosingShelter(chatId);

        assertEquals("102030" ,actual.getChatId());
        assertEquals("Выберите нужный вам приют.", actual.getText());
        assertEquals(markup, actual.getReplyMarkup());
    }

    @NotNull
    private static List<List<InlineKeyboardButton>> choosingShelter() {
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();

        InlineKeyboardButton butDog = new InlineKeyboardButton();
        butDog.setText("Собачий");
        butDog.setCallbackData(DOG_BUTTON);

        InlineKeyboardButton butCat = new InlineKeyboardButton();
        butCat.setText("Кошачий");
        butCat.setCallbackData(CAT_BUTTON);

        rowInLine.add(butDog);
        rowInLine.add(butCat);

        rowsInLine.add(rowInLine);
        return rowsInLine;
    }

}
