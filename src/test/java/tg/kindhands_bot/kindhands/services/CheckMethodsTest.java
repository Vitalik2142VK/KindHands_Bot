package tg.kindhands_bot.kindhands.services;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tg.kindhands_bot.kindhands.components.CheckMethods;
import tg.kindhands_bot.kindhands.components.ProcessingBotMessages;
import tg.kindhands_bot.kindhands.entities.User;
import tg.kindhands_bot.kindhands.enums.BotState;
import tg.kindhands_bot.kindhands.exceptions.IncorrectDataExceptionAndSendMessage;
import tg.kindhands_bot.kindhands.repositories.UserRepository;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static tg.kindhands_bot.kindhands.AdditionalMethods.createUser;
//import static tg.kindhands_bot.kindhands.services.AdditionalMethods.getUpdateButton;

@ExtendWith(MockitoExtension.class)
public class CheckMethodsTest {

    @Mock
    private CheckMethods checkMethods;

    @Mock
    ProcessingBotMessages botMessages;

    @Mock
    UserRepository userRepository;



    @Test
    public void checkNumberPhone() {

        String phone = "88005558089";
        User user = createUser(1L, 102030L, "Name", false, null, BotState.NULL);
        user.setPhone(CheckMethods.checkNumberPhone(phone));
        User actual = user;

        assertEquals("+7(800)555-80-89" ,actual.getPhone());
    }


    @Test
    public void checkFullName() {

        String fullName = "Жан-клод Ван Дамм";
        User user = createUser(1L, 102030L, "Name", false, null, BotState.NULL);

        List<String> arrFullName;
        arrFullName = CheckMethods.checkFullName(fullName);
        user.setLastName(arrFullName.get(0));
        user.setFirstName(arrFullName.get(1));
        user.setPatronymic(arrFullName.get(2));

        User actual = user;

        assertEquals(1, actual.getId());
        assertEquals(102030, actual.getChatId());
        assertEquals("Ван", actual.getFirstName());
        assertEquals("Дамм", actual.getPatronymic());
        assertEquals("Жан-Клод", actual.getLastName());

    }

    @Test
    public void checkFullNameNegative() {

        String fullName = "Жан";
        User user = createUser(1L, 102030L, "Name", false, null, BotState.NULL);

        List<String> arrFullName;
        try {
            arrFullName = CheckMethods.checkFullName(fullName);
        } catch (IncorrectDataExceptionAndSendMessage | NullPointerException e) {
            System.out.println(e);
        }

        User actual = user;

        assertEquals("Name", actual.getFirstName());
        assertEquals(null, actual.getLastName());
        assertEquals("", actual.getPatronymic());

    }
}
