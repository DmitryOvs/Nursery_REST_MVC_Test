package by.chukotka.nursery.controllers;

import by.chukotka.nursery.model.Plant;
import by.chukotka.nursery.model.Saplings;
import by.chukotka.nursery.model.Variety;
import by.chukotka.nursery.services.PlantService;
import by.chukotka.nursery.services.VarietyService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.Random.class)
public class PantsControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private PlantService plantService;

    @MockBean
    private VarietyService varietyService;

    @Autowired
    private PantsController pantsController;



    private Plant testPlant;
    private List<Plant> testPlants;
    private List<Variety> testVarieties;


    @BeforeEach
    public void setUp() {
        testPlant = new Plant();
        testPlants = new ArrayList<>();
        testPlant.setIdPlant(new Random().nextInt(50));

        testVarieties = new ArrayList<>();

        mockMvc = MockMvcBuilders.standaloneSetup(pantsController).build();
    }

    @Test
    void testShowPlants() throws Exception {
        when(plantService.findAll()).thenReturn(testPlants);
        mockMvc.perform(get("/nursery/plants"))
               .andDo(print())
                .andExpectAll(
                        model().size(1),
                        model().attribute("plants", testPlants),
                       status().isOk(),
                       forwardedUrl("plants/show")
               );
       verify(plantService, times(1)).findAll();
    }

    @Test
    public void showPlantTest() throws Exception {
        when(plantService.findOneById(anyInt())).thenReturn(testPlant);
        when(varietyService.findByPlant(any(Plant.class))).thenReturn(testVarieties);

        mockMvc.perform(get("/nursery/plants/{id}", anyInt()))
                .andDo(print())
                .andExpectAll(
                        model().size(1),
                        model().attribute("plant", testPlant),
                        model().attributeDoesNotExist("varieties"),
                        status().isOk(),
                        forwardedUrl("plants/profile")
                );
        testVarieties.add(new Variety());
        mockMvc.perform(get("/nursery/plants/" + testPlant.getIdPlant()))
                .andDo(print())
                .andExpectAll(
                        model().size(2),
                        model().attribute("plant", testPlant),
                        model().attribute("varieties", testVarieties),
                        status().isOk(),
                        forwardedUrl("plants/profile")
                );

        verify(plantService, times(2)).findOneById(anyInt());
        verify(varietyService, times(2)).findByPlant(any(Plant.class));
    }

    @Test
    public void testAddPlant() throws Exception {
        mockMvc.perform(get("/nursery/plants/new"))
                .andDo(print())
                .andExpectAll(
                        model().size(1),
                        model().attributeExists("plant"),
                        status().isOk(),
                        forwardedUrl("plants/new")
                );
    }

    @Test
    public void testCreatePlant() throws Exception {

        mockMvc.perform(post("/nursery/plants")
                .flashAttr("plant", testPlant))
                .andDo(print())
                .andExpectAll(
                        model().attribute("plant", testPlant),
                        model().attributeErrorCount("plant", 1),
                        status().isOk(),
                        forwardedUrl("plants/new")
                );

        testPlant.setPriceSeedling(1);
        testPlant.setSeedling(Saplings.APRICOT);
        mockMvc.perform(post("/nursery/plants")
                        .flashAttr("plant", testPlant))
                .andDo(print())
                .andExpectAll(
                        model().attribute("plant", testPlant),
                        model().attributeErrorCount("plant", 1),
                        status().isOk(),
                        forwardedUrl("plants/new")
                );

        testPlant.setSeedling(Saplings.PLUM);
        mockMvc.perform(post("/nursery/plants")
                        .flashAttr("plant", testPlant))
                .andDo(print())
                .andExpectAll(
                        model().attribute("plant", testPlant),
                        model().attributeErrorCount("plant", 1),
                        status().isOk(),
                        forwardedUrl("plants/new")
                );

        testPlant.setPriceSeedling(25);
        mockMvc.perform(post("/nursery/plants")
                        .flashAttr("plant", testPlant))
                .andDo(print())
                .andExpectAll(
                        model().attribute("plant", testPlant),
                        model().attributeErrorCount("plant", 1),
                        status().isOk(),
                        forwardedUrl("plants/new")
                );



        testPlant.setPriceSeedling(15);
        mockMvc.perform(post("/nursery/plants")
                        .flashAttr("plant", testPlant))
                .andDo(print())
                .andExpectAll(
                        model().size(2),
                        model().attribute("plant", testPlant),
                        status().is3xxRedirection(),
                        redirectedUrl("/nursery/plants")
                );
        verify(plantService, times(1)).save(any(Plant.class));

    }

    @Test
    public void testEditPlant() throws Exception {
        when(plantService.findOneById(anyInt())).thenReturn(testPlant);
        mockMvc.perform(get("/nursery/plants/{id}/edit", anyInt()))
                .andDo(print())
                .andExpectAll(
                        model().size(1),
                        model().attribute("plant", testPlant),
                        status().isOk(),
                        forwardedUrl("plants/edit")
                );
        verify(plantService, times(1)).findOneById(anyInt());
    }


    @Test
    public void testUpdatePlant() throws Exception {
        testPlant.setIdPlant(new Random().nextInt(100));
        testPlant.setSeedling(Saplings.PLUM);

        testPlant.setPriceSeedling(1);
        mockMvc.perform(patch("/nursery/plants/{id}", testPlant.getIdPlant())
                        .flashAttr("plant", testPlant))
                .andDo(print())
                .andExpectAll(
                        model().size(1),
                        model().attribute("plant", testPlant),
                        model().attributeErrorCount("plant", 1),
                        status().isOk(),
                        forwardedUrl("plants/edit")
                );

        testPlant.setPriceSeedling(25);
        mockMvc.perform(patch("/nursery/plants/{id}", testPlant.getIdPlant())
                        .flashAttr("plant", testPlant))
                .andDo(print())
                .andExpectAll(
                        model().size(1),
                        model().attribute("plant", testPlant),
                        model().attributeErrorCount("plant", 1),
                        status().isOk(),
                        forwardedUrl("plants/edit")
                );

        testPlant.setPriceSeedling(15);
        mockMvc.perform(patch("/nursery/plants/{id}", testPlant.getIdPlant())
                        .flashAttr("plant", testPlant))
                .andDo(print())
                .andExpectAll(
                        model().size(1),
                        model().attribute("plant", testPlant),
                        status().is3xxRedirection(),
                        redirectedUrl("/nursery/plants")
                );
        verify(plantService, times(1)).update(anyInt(), any(Plant.class));
    }

    @Test
    public void testDeletePlant() throws Exception {
        for (int i = 0; i < 4; i++) {
            mockMvc.perform(delete("/nursery/plants/{id}", anyInt()))
                    .andDo(print())
                    .andExpectAll(
                            status().is3xxRedirection(),
                            redirectedUrl("/nursery/plants")

                    );
        }
        verify(plantService, times(4)).delete(anyInt());
    }

    @AfterEach
    public void tearDown() {
        verifyNoMoreInteractions(plantService, varietyService);
    }
}
