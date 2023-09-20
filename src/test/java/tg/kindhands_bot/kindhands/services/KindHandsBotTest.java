package tg.kindhands_bot.kindhands.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import tg.kindhands_bot.kindhands.config.BotConfig;
import tg.kindhands_bot.kindhands.repositories.ReportAnimalRepository;
import tg.kindhands_bot.kindhands.repositories.UserRepository;

@ExtendWith(MockitoExtension.class)
public class KindHandsBotTest {

    @Mock
    private BotConfig botConfig;

    @Mock
    private UserRepository userRepository;
    @Mock
    private ReportAnimalRepository reportAnimalRepository;

    @InjectMocks
    private VolunteerService volunteerService;

    @InjectMocks
    private KindHandsBot bot;

    @BeforeEach
    public void beforeEach() {
        Mockito.when(botConfig.getBotName()).thenReturn("nameBot");
        Mockito.when(botConfig.getToken()).thenReturn("0000000000:AAaaaaaaaaaaa_AaAaAAAAAAAAAAAAAAAA");
        bot = new KindHandsBot(userRepository, reportAnimalRepository, volunteerService, botConfig);
    }

    @Test
    public void onUpdateReceived() {

    }
}
