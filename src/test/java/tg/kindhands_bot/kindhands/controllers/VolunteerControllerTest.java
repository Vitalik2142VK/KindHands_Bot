package tg.kindhands_bot.kindhands.controllers;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import tg.kindhands_bot.kindhands.components.MessagesBotFromControllers;
import tg.kindhands_bot.kindhands.entities.User;
import tg.kindhands_bot.kindhands.entities.Volunteer;
import tg.kindhands_bot.kindhands.enums.BotState;
import tg.kindhands_bot.kindhands.repositories.AnimalsRepository;
import tg.kindhands_bot.kindhands.repositories.ReportAnimalRepository;
import tg.kindhands_bot.kindhands.repositories.UserRepository;
import tg.kindhands_bot.kindhands.repositories.VolunteersRepository;
import tg.kindhands_bot.kindhands.repositories.tamed.TamedAnimalRepository;
import tg.kindhands_bot.kindhands.services.KindHandsBot;
import tg.kindhands_bot.kindhands.services.VolunteerService;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = VolunteerController.class)
public class VolunteerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VolunteersRepository volunteersRepository;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private AnimalsRepository animalsRepository;
    @MockBean
    private TamedAnimalRepository tamedAnimalRepository;
    @MockBean
    private ReportAnimalRepository reportAnimalRepository;
    @MockBean
    private KindHandsBot bot;

    @SpyBean
    private VolunteerService volunteerService;
    @SpyBean
    private MessagesBotFromControllers messagesBot;

    @InjectMocks
    private VolunteerController volunteerController;

//    @Test
//    public void getAllVolunteersTest() throws Exception {
//        Volunteer volunteer = new Volunteer();
//        User user = new User();
//        user.setFirstName("Bob");
//        user.setLastName("Singler");
//        user.setChatId(123L);
//        user.setPatronymic("");
//        user.setPhone("");
//        user.setBlocked(false);
//        user.setDenialReason(null);
//        user.setNeedHelp(false);
//        user.setBotState(BotState.NULL);
//        volunteer.setId(1L);
//        volunteer.setAdopted(true);
//        volunteer.setPhone("8800");
//        volunteer.setUser(user);
//
//        Volunteer volunteer1 = new Volunteer();
//        User user1 = new User();
//        user1.setFirstName("God");
//        user1.setLastName("My");
//        user1.setChatId(123L);
//        user.setPatronymic("");
//        user.setPhone("");
//        user.setBlocked(false);
//        user.setDenialReason(null);
//        user.setNeedHelp(false);
//        user.setBotState(BotState.NULL);
//        volunteer1.setId(2L);
//        volunteer1.setAdopted(true);
//        volunteer1.setPhone("8800");
//        volunteer1.setUser(user1);
//
//        List<Volunteer> list = List.of(volunteer, volunteer1);
//
//        when(volunteersRepository.findAll()).thenReturn(list);
//
//        mockMvc.perform(MockMvcRequestBuilders
//                        .get("/volunteer")
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(2)))
//                .andExpect(jsonPath("$.[0].id").value(volunteer.getId()))
//                .andExpect(jsonPath("$.[0].adopted").value(volunteer.getAdopted()))
//                .andExpect(jsonPath("$.[0].phone").value(volunteer.getPhone()))
//                .andExpect(jsonPath("$.[0].user.chatId").value(volunteer.getUser().getChatId()))
//                .andExpect(jsonPath("$.[0].user.firstName").value(volunteer.getUser().getFirstName()))
//                .andExpect(jsonPath("$.[0].user.lastName").value(volunteer.getUser().getLastName()))
//                .andExpect(jsonPath("$.[0].user.patronymic").value(volunteer.getUser().getPatronymic()))
//                .andExpect(jsonPath("$.[0].user.phone").value(volunteer.getUser().getPhone()))
//                .andExpect(jsonPath("$.[0].user.blocked").value(volunteer.getUser().getBlocked()))
//                .andExpect(jsonPath("$.[0].user.denialReason").value(volunteer.getUser().getDenialReason()))
//                .andExpect(jsonPath("$.[0].user.needHelp").value(volunteer.getUser().getNeedHelp()))
//                .andExpect(jsonPath("$.[0].user.botState").value(volunteer.getUser().getBotState()))
//                .andExpect(jsonPath("$.[1].id").value(volunteer1.getId()))
//                .andExpect(jsonPath("$.[1].adopted").value(volunteer1.getAdopted()))
//                .andExpect(jsonPath("$.[1].phone").value(volunteer1.getPhone()))
//                .andExpect(jsonPath("$.[1].user.chatId").value(volunteer1.getUser().getChatId()))
//                .andExpect(jsonPath("$.[1].user.firstName").value(volunteer1.getUser().getFirstName()))
//                .andExpect(jsonPath("$.[1].user.lastName").value(volunteer1.getUser().getLastName()))
//                .andExpect(jsonPath("$.[1].user.patronymic").value(volunteer.getUser().getPatronymic()))
//                .andExpect(jsonPath("$.[1].user.phone").value(volunteer.getUser().getPhone()))
//                .andExpect(jsonPath("$.[1].user.blocked").value(volunteer.getUser().getBlocked()))
//                .andExpect(jsonPath("$.[1].user.denialReason").value(volunteer.getUser().getDenialReason()))
//                .andExpect(jsonPath("$.[1].user.needHelp").value(volunteer.getUser().getNeedHelp()))
//                .andExpect(jsonPath("$.[1].user.botState").value(volunteer.getUser().getBotState()));
//
//    }
//
    @Test
    public void deleteVolunteerTest() throws Exception {
        final String firstName = "Bob";
        final Long id = 1L;

        JSONObject volunteerObject = new JSONObject();
        volunteerObject.put("firstName", firstName);

        Volunteer volunteer = new Volunteer();
        User user = new User();
        user.setFirstName(firstName);
        volunteer.setId(id);
        volunteer.setUser(user);

        when(volunteersRepository.findById(any(Long.class))).thenReturn(Optional.of(volunteer));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/volunteer/" + id)
                        .content(volunteerObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());


    }
//    @Test
//    public void getReportsTest() throws Exception {
//
//    }
}
