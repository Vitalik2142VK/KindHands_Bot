package tg.kindhands_bot.kindhands.controllers;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import tg.kindhands_bot.kindhands.entities.Animal;
import tg.kindhands_bot.kindhands.entities.photo.AnimalPhoto;
import tg.kindhands_bot.kindhands.enums.TypeAnimal;
import tg.kindhands_bot.kindhands.repositories.AnimalsRepository;
import tg.kindhands_bot.kindhands.repositories.photo.AnimalPhotoRepository;
import tg.kindhands_bot.kindhands.services.AnimalService;

import java.util.List;
import java.util.Optional;

@WebMvcTest(controllers = AnimalController.class)
public class AnimalControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AnimalsRepository animalsRepository;
    @MockBean
    private AnimalPhotoRepository animalPhotoRepository;

    @SpyBean
    private AnimalService animalService;

    @InjectMocks
    private AnimalController animalController;

    @Test
    public void getAllAnimalsTest() throws Exception {
        Animal animal1 = new Animal();
        animal1.setId(1L);
        animal1.setName("One");
        animal1.setRation("good");
        animal1.setRecommendation("no");
        animal1.setTypeAnimal(TypeAnimal.CAT);



        Animal animal2 = new Animal();
        animal2.setId(2L);
        animal2.setName("Two");
        animal2.setRation("bad");
        animal2.setRecommendation("yes");
        animal2.setTypeAnimal(TypeAnimal.CAT);

        List<Animal> list = List.of(animal1, animal2);

        when(animalsRepository.findAll()).thenReturn(list);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/volunteer/animal/getAll")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[0].id").value(animal1.getId()))
                .andExpect(jsonPath("$.[0].name").value(animal1.getName()))
                .andExpect(jsonPath("$.[0].ration").value(animal1.getRation()))
                .andExpect(jsonPath("$.[0].recommendation").value(animal1.getRecommendation()))
                .andExpect(jsonPath("$.[1].id").value(animal2.getId()))
                .andExpect(jsonPath("$.[1].name").value(animal2.getName()))
                .andExpect(jsonPath("$.[1].ration").value(animal2.getRation()))
                .andExpect(jsonPath("$.[1].recommendation").value(animal2.getRecommendation()));

    }

    @Test
    public void addAnimalTest() throws Exception {
        final Long id = 1L;
        final String name = "Dog";
        final String ration = "normal";
        final String recommendation = "no";


        JSONObject animalObject = new JSONObject();
        animalObject.put("name", name);
        animalObject.put("ration", ration);
        animalObject.put("recommendation", recommendation);

        Animal animal = new Animal();
        animal.setId(id);
        animal.setName(name);
        animal.setRation(ration);
        animal.setRecommendation(recommendation);

        when(animalsRepository.save(any(Animal.class))).thenReturn(animal);
        when(animalsRepository.findById(any(Long.class))).thenReturn(Optional.of(animal));

        mockMvc.perform(MockMvcRequestBuilders
                .post("/volunteer/animal/add")
                .content(animalObject.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.ration").value(ration))
                .andExpect(jsonPath("$.recommendation").value(recommendation));
    }

    @Test
    public void removeAnimalTest() throws Exception {
        final Long id = 1L;
        final String name = "Dog";
        final String ration = "normal";
        final String recommendation = "no";
        final AnimalPhoto animalPhoto = new AnimalPhoto();

        JSONObject animalObject = new JSONObject();
        animalObject.put("name", name);
        animalObject.put("ration", ration);
        animalObject.put("recommendation", recommendation);
        animalObject.put("animalfoto", animalPhoto);

        Animal animal = new Animal();
        animal.setId(id);
        animal.setName(name);
        animal.setRation(ration);
        animal.setRecommendation(recommendation);
        animal.setAnimalPhoto(animalPhoto);

        when(animalsRepository.findById(any(Long.class))).thenReturn(Optional.of(animal));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/volunteer/animal/" + id)
                        .content(animalObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());



    }


}
