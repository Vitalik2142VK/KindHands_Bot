package tg.kindhands_bot.kindhands.controllers;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import tg.kindhands_bot.kindhands.entities.User;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private UserRepository userRepository;
//
//    @SpyBean
//    private UserService userService;
//
//    @InjectMocks
//    private UserController userController;

//    @Test
//    public void addUserBlacklistTest() throws Exception {
//        User user1 = new User();
//        user1.setId(1L);
//        user1.setName("Bob");
//        user1.setDenialReason("denialReason");
//
//        User user2 = new User();
//        user2.setId(2L);
//        user2.setName("Bobee");
//        user2.setDenialReason("NoDenialReason");
//
//        List<User> list = List.of(user1, user2);
//
//
//
//        when(userRepository.findAll()).thenReturn(list);
//
//        mockMvc.perform(MockMvcRequestBuilders
//                        .put("/volunteer/user/blacklist/add/")
//                        .param("id", "blockMessage")
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$",hasSize(2)))
//                .andExpect(jsonPath("$.[0].id").value(user1.getId()))
//                .andExpect(jsonPath("$.[0].denialReason").value(user1.getDenialReason()))
//                .andExpect(jsonPath("$.[1].id").value(user2.getId()))
//                .andExpect(jsonPath("$.[1].denialReason").value(user2.getDenialReason()));
//
//
//    }
// @LocalServerPort
//private int port;
//    @Autowired
//    private UserController userController;
//
//    @Autowired
//    private TestRestTemplate restTemplate;
//
//    @Test
//    void contextLoads() throws Exception {
//
//    }
//    @Test
//    public void addUserBlacklistTest() throws Exception {
//        User user = new User();
//        user.setId(1L);
//        user.setName("Ivan");
//        user.setDenialReason("asdasda");
//        Assertions.assertThat()
//
//
//    }
}
