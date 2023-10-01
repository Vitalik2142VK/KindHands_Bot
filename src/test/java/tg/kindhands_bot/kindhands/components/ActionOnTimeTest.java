package tg.kindhands_bot.kindhands.components;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import tg.kindhands_bot.kindhands.AdditionalMethods;
import tg.kindhands_bot.kindhands.entities.User;
import tg.kindhands_bot.kindhands.entities.tamed.TamedAnimal;
import tg.kindhands_bot.kindhands.entities.tamed.TamedCat;
import tg.kindhands_bot.kindhands.entities.tamed.TamedDog;
import tg.kindhands_bot.kindhands.enums.BotState;
import tg.kindhands_bot.kindhands.repositories.tamed.TamedAnimalRepository;
import tg.kindhands_bot.kindhands.repositories.tamed.TamedCatRepository;
import tg.kindhands_bot.kindhands.repositories.tamed.TamedDogRepository;
import tg.kindhands_bot.kindhands.services.KindHandsBot;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static tg.kindhands_bot.kindhands.AdditionalMethods.*;

@ExtendWith(MockitoExtension.class)
public class ActionOnTimeTest {

    @Mock
    private TamedAnimalRepository tamedAnimalRepository;
    @Mock
    private TamedCatRepository tamedCatRepository;
    @Mock
    private TamedDogRepository tamedDogRepository;

    @Mock
    private KindHandsBot bot;

    @InjectMocks
    private ActionOnTime actionOnTime;

    private List<TamedDog> tamedDogs;
    private List<TamedCat> tamedCats;

    private LocalDate date;

    @BeforeEach
    public void beforeEach() {
        date = LocalDate.now();
        tamedDogs = new ArrayList<>(List.of(
                createTamedDog(1L, createUser(1L, 102030L, "LastName1", "FirstName1",
                        "8800", false, "", BotState.NULL),
                        null, date.minusDays(2), date),
                createTamedDog(2L, createUser(2L, 102031L, "LastName2", "FirstName2",
                        "8801", false, "", BotState.NULL),
                        null, date.minusDays(2), date.minusDays(2))
        ));
        tamedCats = new ArrayList<>(List.of(
                createTamedCat(3L, createUser(3L, 102032L, "LastName3", "FirstName3",
                        "8802", false, "", BotState.NULL),
                        null, date.minusDays(2), date.minusDays(1))
        ));
    }

    @Test
    public void sendDailyReportReminder() {
        assertFalse(tamedDogs.get(0).getDateLastReport().isBefore(date));
        assertTrue(tamedDogs.get(1).getDateLastReport().isBefore(date));
        assertTrue(tamedCats.get(0).getDateLastReport().isBefore(date));

        Mockito.when(tamedDogRepository.findAllUsersDateLastReportBefore(date)).thenReturn(new ArrayList<>(List.of(tamedDogs.get(1).getUser())));
        Mockito.when(tamedCatRepository.findAllUsersDateLastReportBefore(date)).thenReturn(new ArrayList<>(List.of(tamedCats.get(0).getUser())));

        actionOnTime.sendDailyReportReminder();

        Mockito.when(tamedDogRepository.findAllUsersDateLastReportBefore(date)).thenReturn(new ArrayList<>(List.of(tamedDogs.get(1).getUser())));
        Mockito.when(tamedCatRepository.findAllUsersDateLastReportBefore(date)).thenReturn(new ArrayList<>(List.of(tamedCats.get(0).getUser())));

        List<User> getsUsers = getAllUser();

        assertEquals(getsUsers.get(0), tamedCats.get(0).getUser());
        assertEquals(getsUsers.get(1), tamedDogs.get(1).getUser());
    }

    @Test
    public void sendDailyReportNotReceived() {
        tamedCats.get(0).setDateLastReport(date);

        assertFalse(tamedDogs.get(0).getDateLastReport().isBefore(date));
        assertFalse(tamedCats.get(0).getDateLastReport().isBefore(date));
        assertTrue(tamedDogs.get(1).getDateLastReport().isBefore(date));

        Mockito.when(tamedDogRepository.findAllUsersDateLastReportBefore(date)).thenReturn(new ArrayList<>(List.of(tamedDogs.get(1).getUser())));
        Mockito.when(tamedCatRepository.findAllUsersDateLastReportBefore(date)).thenReturn(new ArrayList<>());

        actionOnTime.sendDailyReportNotReceived();

        Mockito.when(tamedDogRepository.findAllUsersDateLastReportBefore(date)).thenReturn(new ArrayList<>(List.of(tamedDogs.get(1).getUser())));
        Mockito.when(tamedCatRepository.findAllUsersDateLastReportBefore(date)).thenReturn(new ArrayList<>());

        List<User> getsUsers = getAllUser();

        assertEquals(getsUsers.get(0), tamedDogs.get(1).getUser());

        SendMessage actual = getEditMessageTextActual();

        assertEquals("102031", actual.getChatId());
        assertEquals("Сегодня мы не получили от Вас отчет! Напоминаем, что, если отчеты не будут отправляться, то мы будем вынуждены забрать " +
                "питомца обратно.\n\nЕсли у вас возникают проблемы с отправкой отчета, то необходимо обратиться к волонтерам. " +
                "\n\nСпасибо за понимание!", actual.getText());
    }

    @Test
    public void checkDailyReportReceived() {
        assertFalse(Period.between(tamedDogs.get(0).getDateLastReport(), date).getDays() >= 2);
        assertFalse(Period.between(tamedCats.get(0).getDateLastReport(), date).getDays() >= 2);
        assertTrue(Period.between(tamedDogs.get(1).getDateLastReport(), date).getDays() >= 2);

        Mockito.when(tamedAnimalRepository.findByDateLastReportBefore(date)).thenReturn(new ArrayList<>(List.of(tamedDogs.get(1), tamedCats.get(0))));

        actionOnTime.checkDailyReportReceived();

        SendMessage actual = getEditMessageTextActual();

        assertEquals("102031", actual.getChatId());
        assertEquals("Вы не отправляли отчеты более 2-х дней. Пожалуйста, отправьте отчет в ближайшее время, " +
                "иначе мы будем вынуждены забрать питомца обратно!" +
                "\n\nВ скором времени с Вами свяжутся для уточнения.", actual.getText());
    }

    // Дополнительные методы

    public List<User> getAllUser() {
        Method method;
        try {
            method = actionOnTime.getClass().getDeclaredMethod("getAllUser");
            method.setAccessible(true);
            return (List<User>) method.invoke(actionOnTime);

        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private SendMessage getEditMessageTextActual() {
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(bot).sendMessage(argumentCaptor.capture());
        return argumentCaptor.getValue();
    }
}
