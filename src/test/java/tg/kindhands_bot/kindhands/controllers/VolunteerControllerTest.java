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
import tg.kindhands_bot.kindhands.entities.Volunteer;
import tg.kindhands_bot.kindhands.repositories.VolunteersRepository;
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

        @SpyBean
        private VolunteerService volunteerService;

        @InjectMocks
        private VolunteerController volunteerController;

    @Test
    public void getAllVolunteersTest() throws Exception {
        Volunteer volunteer = new Volunteer();
        volunteer.setId(1L);
        volunteer.setName("Bob");
        volunteer.setAdopted(true);
        volunteer.setPhone("8800");
        volunteer.setChatId(123L);

        Volunteer volunteer1 = new Volunteer();
        volunteer1.setId(2L);
        volunteer1.setName("God");
        volunteer1.setAdopted(true);
        volunteer1.setPhone("8800");
        volunteer1.setChatId(123L);



        List<Volunteer> list = List.of(volunteer, volunteer1);

        when(volunteersRepository.findAll()).thenReturn(list);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/volunteer")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[0].id").value(volunteer.getId()))
                .andExpect(jsonPath("$.[0].name").value(volunteer.getName()))
                .andExpect(jsonPath("$.[0].adopted").value(volunteer.getAdopted()))
                .andExpect(jsonPath("$.[0].phone").value(volunteer.getPhone()))
                .andExpect(jsonPath("$.[0].chatId").value(volunteer.getChatId()))
                .andExpect(jsonPath("$.[1].id").value(volunteer1.getId()))
                .andExpect(jsonPath("$.[1].name").value(volunteer1.getName()))
                .andExpect(jsonPath("$.[1].adopted").value(volunteer.getAdopted()))
                .andExpect(jsonPath("$.[1].phone").value(volunteer.getPhone()))
                .andExpect(jsonPath("$.[1].chatId").value(volunteer.getChatId()));

    }
    @Test
    public void deleteVolunteerTest() throws Exception {
        final String name = "Bob";
        final Long id = 1L;

        JSONObject volunteerObject = new JSONObject();
        volunteerObject.put("name", name);

        Volunteer volunteer = new Volunteer();
        volunteer.setId(id);
        volunteer.setName(name);

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
