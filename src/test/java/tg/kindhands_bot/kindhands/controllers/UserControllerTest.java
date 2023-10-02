package tg.kindhands_bot.kindhands.controllers;


import org.assertj.core.api.Assertions;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import tg.kindhands_bot.kindhands.AdditionalMethods;
import tg.kindhands_bot.kindhands.components.MessagesBotFromControllers;
import tg.kindhands_bot.kindhands.entities.Animal;
import tg.kindhands_bot.kindhands.entities.ReportAnimal;
import tg.kindhands_bot.kindhands.entities.User;
import tg.kindhands_bot.kindhands.entities.Volunteer;
import tg.kindhands_bot.kindhands.entities.tamed.TamedAnimal;
import tg.kindhands_bot.kindhands.entities.tamed.TamedCat;
import tg.kindhands_bot.kindhands.enums.BotState;
import tg.kindhands_bot.kindhands.repositories.AnimalsRepository;
import tg.kindhands_bot.kindhands.repositories.ReportAnimalRepository;
import tg.kindhands_bot.kindhands.repositories.UserRepository;
import tg.kindhands_bot.kindhands.repositories.VolunteersRepository;
import tg.kindhands_bot.kindhands.repositories.photo.ReportAnimalPhotoRepository;
import tg.kindhands_bot.kindhands.repositories.tamed.TamedAnimalRepository;
import tg.kindhands_bot.kindhands.services.KindHandsBot;
import tg.kindhands_bot.kindhands.services.UserService;
import tg.kindhands_bot.kindhands.services.VolunteerService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;
    @MockBean
    private VolunteersRepository volunteersRepository;
    @MockBean
    private AnimalsRepository animalsRepository;
    @MockBean
    private TamedAnimalRepository tamedAnimalRepository;
    @MockBean
    private ReportAnimalRepository reportAnimalRepository;
    @MockBean
    private KindHandsBot bot;
    @MockBean
    private ReportAnimalPhotoRepository reportAnimalPhotoRepository;

    @SpyBean
    private UserService userService;
    @SpyBean
    private MessagesBotFromControllers messagesBot;
    @InjectMocks
    private UserController userController;

    @Test
    public void needVolunteerHelperTest() throws Exception {
        User user = new User();
        user.setFirstName("ds");
        user.setLastName("sdf");
        user.setNeedHelp(true);
        user.setId(1L);

        User user1 = new User();
        user1.setFirstName("err");
        user1.setLastName("er");
        user1.setNeedHelp(true);
        user1.setId(2L);

        List<User> list = List.of(user, user1);

        when(userRepository.findByNeedHelpTrue()).thenReturn(list);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/volunteer/user/help")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[0].id").value(user.getId()))
                .andExpect(jsonPath("$.[0].needHelp").value(user.getNeedHelp()))
                .andExpect(jsonPath("$.[0].firstName").value(user.getFirstName()))
                .andExpect(jsonPath("$.[0].lastName").value(user.getLastName()))
                .andExpect(jsonPath("$.[1].id").value(user1.getId()))
                .andExpect(jsonPath("$.[1].needHelp").value(user1.getNeedHelp()))
                .andExpect(jsonPath("$.[1].firstName").value(user1.getFirstName()))
                .andExpect(jsonPath("$.[1].lastName").value(user1.getLastName()));

        when(userRepository.findByNeedHelpTrue()).thenReturn(new ArrayList<>());
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/volunteer/user/help")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

//    @Test
//    public void changeIsNeedHelpTest() throws Exception {
//        final String firstName = "Bob";
//        final Long id = 1L;
//
//        JSONObject userObject = new JSONObject();
//        userObject.put("firstName", firstName);
//        userObject.put("id", id);
//
//        User user = AdditionalMethods.createUser(1L,1L,"asd",false,"sdf", BotState.NULL);
//        user.setNeedHelp(true);
//
//        when(userRepository.save(any(User.class))).thenReturn(user);
//        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(user));
//
//
//        mockMvc.perform(MockMvcRequestBuilders
//                        .put("/volunteer/user/help/change/" + id)
//                        .accept(MediaType.APPLICATION_JSON)
//                        .content(userObject.toString())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk());

//        mockMvc.perform(MockMvcRequestBuilders
//                        .put("/volunteer/user/help/change/" + id)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(id))
//                .andExpect(jsonPath("$.firstname").value(firstName))
//                .andExpect(jsonPath("$.needHelp").value(user.getNeedHelp()));

 //   }

//    @Test
//    public void addUserAnimalTest() throws Exception {
//        final Long id = 1L;
//        final Long idAnimal = 1L;
//
//        Animal animal = new Animal();
//        animal.setId(idAnimal);
//
//        User user = new User();
//        user.setId(id);
//
//
//
//    }
}
