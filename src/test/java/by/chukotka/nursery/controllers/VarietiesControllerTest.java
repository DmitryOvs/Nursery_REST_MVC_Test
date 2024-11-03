package by.chukotka.nursery.controllers;

import by.chukotka.nursery.model.Plant;
import by.chukotka.nursery.model.Variety;
import by.chukotka.nursery.services.PlantService;
import by.chukotka.nursery.services.VarietyService;
import by.chukotka.nursery.validators.UniqueVarietyValidator;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.anyInt;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;




@SpringBootTest
@Import(UniqueVarietyValidator.class)
@TestMethodOrder(MethodOrderer.Random.class)
public class VarietiesControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private PlantService plantService;

    @MockBean
    private VarietyService varietyService;

    @Autowired
    private VarietiesController varietiesController;

    private Plant testPlant;
    private List<Plant> testPlants;
    private Variety testVariety;
    private List<Variety> testVarieties;



    @BeforeEach
    public void setUp() {
        testPlant = new Plant();
        testPlants = new ArrayList<>();
        testVariety = new Variety();
        testVariety.setIdVariety(new Random().nextInt(150));
        testVarieties = new ArrayList<>();

        testVariety.setCreateDate(null);
        testVariety.setModifyDate(null);
        testVariety.setPlantA(null);

        mockMvc = MockMvcBuilders.standaloneSetup(varietiesController).build();
    }

    @Test
    public void testShowVarieties() throws Exception {

        when(varietyService.findAll()).thenReturn(testVarieties);

        mockMvc.perform(get("/nursery/varieties"))
                .andDo(print())
                .andExpectAll(
                        model().size(1),
                        model().attribute("varieties", testVarieties),
                        status().isOk(),
                        forwardedUrl("varieties/show")
                );

        mockMvc.perform(get("/nursery/varieties")
                .param("page", "2"))
                .andDo(print())
                .andExpectAll(
                        model().size(1),
                        model().attribute("varieties", testVarieties),
                        status().isOk(),
                        forwardedUrl("varieties/show")
                );

        mockMvc.perform(get("/nursery/varieties")
                        .param("varietiesPerPage", "2"))
                .andDo(print())
                .andExpectAll(
                        model().size(1),
                        model().attribute("varieties", testVarieties),
                        status().isOk(),
                        forwardedUrl("varieties/show")
                );
        verify(varietyService, times(3)).findAll(); //??????????????"3"

        when(varietyService.findAndSortByNameType()).thenReturn(testVarieties);

        mockMvc.perform(get("/nursery/varieties")
                        .param("sortNameType", "true"))
                .andDo(print())
                .andExpectAll(
                        model().size(1),
                        model().attribute("varieties", testVarieties),
                        status().isOk(),
                        forwardedUrl("varieties/show")
                );
        verify(varietyService, times(1)).findAndSortByNameType();

        when(varietyService.findAndPage(anyInt(), anyInt())).thenReturn(testVarieties);

        mockMvc.perform(get("/nursery/varieties")
                .param("page", "1")
                .param("varietiesPerPage", "4"))
                .andDo(print())
                .andExpectAll(
                        model().size(1),
                        model().attribute("varieties", testVarieties),
                        status().isOk(),
                        forwardedUrl("varieties/show")
                );
        verify(varietyService, times(1)).findAndPage(anyInt(), anyInt());

        when(varietyService.findAndPageAndSortByNameType(anyInt(), anyInt())).thenReturn(testVarieties);

        mockMvc.perform(get("/nursery/varieties")
                        .param("page", "1")
                        .param("varietiesPerPage", "4")
                        .param("sortNameType", "true"))
                .andDo(print())
                .andExpectAll(
                        model().size(1),
                        model().attribute("varieties", testVarieties),
                        status().isOk(),
                        forwardedUrl("varieties/show")
                );
        verify(varietyService, times(1)).findAndPageAndSortByNameType(anyInt(), anyInt());
    }

    @Test
    public void testShowVariety() throws Exception {
        when(varietyService.findOne(anyInt())).thenReturn(testVariety);
        when(plantService.findAll()).thenReturn(testPlants);

        mockMvc.perform(get("/nursery/varieties/{id}", anyInt()))
                .andDo(print())
                .andExpectAll(
                        model().size(3),
                        model().attributeExists("plant"),
                        model().attributeExists("variety"),
                        model().attributeExists("plants"),
                        model().attributeDoesNotExist("plantA"),
                        status().isOk(),
                        forwardedUrl("varieties/profile")
                );

        testVariety.setPlantA(testPlant);
        mockMvc.perform(get("/nursery/varieties/{id}", anyInt()))
                .andDo(print())
                .andExpectAll(
                        model().size(3),
                        model().attributeExists("plant"),
                        model().attributeExists("variety"),
                        model().attributeDoesNotExist("plants"),
                        model().attributeExists("plantA"),
                        status().isOk(),
                        forwardedUrl("varieties/profile")
                );
        verify(varietyService, times(2)).findOne(anyInt());
        verify(plantService, times(1)).findAll();
    }

    @Test
    public void testAddVariety() throws Exception {
        mockMvc.perform(get("/nursery/varieties/new"))
                .andDo(print())
                .andExpectAll(
                        model().size(1),
                        model().attributeExists("variety"),
                        status().isOk(),
                        forwardedUrl("varieties/new")
                );
    }
    @Test
    public void testCreateVariety() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(new VarietiesController(
                plantService, varietyService, mock(UniqueVarietyValidator.class))).build();


        mockMvc.perform(post("/nursery/varieties")
                        .flashAttr("variety", testVariety))
                .andDo(print())
                .andExpectAll(
                        model().attribute("variety", testVariety),
                        model().errorCount(3),
                        model().attributeHasFieldErrors("variety", "nameType", "year", "quantity"),
                        status().isOk(),
                        forwardedUrl("varieties/new")
                );

        testVariety.setNameType("Прага");
        testVariety.setYear(2017);
        testVariety.setQuantity(0);

        mockMvc.perform(post("/nursery/varieties")
                        .flashAttr("variety", testVariety))
                .andDo(print())
                .andExpectAll(
                        model().attribute("variety", testVariety),
                        model().errorCount(2),
                        model().attributeHasFieldErrors("variety", "year", "quantity"),
                        status().isOk(),
                        forwardedUrl("varieties/new")
                );

        testVariety.setNameType("Прага");
        testVariety.setYear(2017);
        testVariety.setQuantity(7);
        mockMvc.perform(post("/nursery/varieties")
                        .flashAttr("variety", testVariety))
                .andDo(print())
                .andExpectAll(
                        model().attribute("variety", testVariety),
                        model().errorCount(1),
                        model().attributeHasFieldErrors("variety",  "year"),
                        status().isOk(),
                        forwardedUrl("varieties/new")
                );

        testVariety.setNameType("Прага");
        testVariety.setYear(2030);
        testVariety.setQuantity(7);
        mockMvc.perform(post("/nursery/varieties")
                        .flashAttr("variety", testVariety))
                .andDo(print())
                .andExpectAll(
                        model().attribute("variety", testVariety),
                        model().errorCount(1),
                        model().attributeHasFieldErrors("variety",  "year"),
                        status().isOk(),
                        forwardedUrl("varieties/new")
                );

        testVariety.setNameType("Прага");
        testVariety.setYear(2023);
        testVariety.setQuantity(7);
        mockMvc.perform(post("/nursery/varieties")
                        .flashAttr("variety", testVariety))
                .andDo(print())
                .andExpectAll(
                        model().attribute("variety", testVariety),
                        status().is3xxRedirection(),
                        redirectedUrl("/nursery/varieties")
                );
        verify(varietyService, times(1)).save(any(Variety.class));
    }

    @Test
    public void testEditVariety() throws Exception {
        when(varietyService.findOne(anyInt())).thenReturn(testVariety);
        mockMvc.perform(get("/nursery/varieties/{idVariety}/edit", anyInt()))
                .andDo(print())
                .andExpectAll(
                        model().size(1),
                        model().attribute("variety", testVariety),
                        status().isOk(),
                        forwardedUrl("varieties/edit")
                );
        verify(varietyService, times(1)).findOne(anyInt());
    }

    @Test
    public void testUpdateVariety() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(new VarietiesController(
                plantService, varietyService, mock(UniqueVarietyValidator.class))).build();

        when(varietyService.findOne(anyInt())).thenReturn(testVariety);
        mockMvc.perform(patch("/nursery/varieties/{idVariety}", testVariety.getIdVariety())
                        .flashAttr("variety", testVariety))
                .andDo(print())
                .andExpectAll(
                        model().size(1),
                        model().attribute("variety", testVariety),
                        status().isOk(),
                        forwardedUrl("varieties/edit")
                );

        testVariety.setNameType("Any body");
        testVariety.setYear(2017);
        testVariety.setQuantity(-10);
        mockMvc.perform(patch("/nursery/varieties/{idVariety}", testVariety.getIdVariety())
                        .flashAttr("variety", testVariety))
                .andDo(print())
                .andExpectAll(
                        model().size(1),
                        model().attribute("variety", testVariety),
                        model().attributeErrorCount("variety", 3),
                        status().isOk(),
                        forwardedUrl("varieties/edit")
                );

        testVariety.setNameType("Мальвина");
        testVariety.setYear(2021);
        testVariety.setQuantity(50);
        mockMvc.perform(patch("/nursery/varieties/{idVariety}", testVariety.getIdVariety())
                        .flashAttr("variety", testVariety))
                .andDo(print())
                .andExpectAll(
                        model().size(1),
                        model().attribute("variety", testVariety),
                        status().is3xxRedirection(),
                        redirectedUrl("/nursery/varieties")
                );
        verify(varietyService, times(1)).update(anyInt(), any(Variety.class));
    }

    @Test
    public void testAddVarietyToPlant() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(new VarietiesController(
                plantService, varietyService, mock(UniqueVarietyValidator.class))).build();

        mockMvc.perform(patch("/nursery/varieties/{idVariety}/plant", testVariety.getIdVariety())
                        .flashAttr("plant", testPlant))
                .andDo(print())
                .andExpectAll(
                        model().size(1),
                        model().attribute("plant", testPlant),
                        status().is3xxRedirection(),
                        redirectedUrl("/nursery/varieties/" + testVariety.getIdVariety())
                );
        verify(varietyService, times(1)).addVarietyToPlant(any(Plant.class), anyInt());
    }

    @Test
    public void testSearchVarieties() throws Exception {
        mockMvc.perform(get("/nursery/varieties/search")
                .param("startString", ""))
                .andDo(print())
                .andExpectAll(
                        model().size(1),
                        model().attribute("startString", ""),
                        model().attributeDoesNotExist("variants"),
                        status().isOk(),
                        forwardedUrl("varieties/search")
                );

        mockMvc.perform(get("/nursery/varieties/search")
                        .param("startString", "Сорт"))
                .andDo(print())
                .andExpectAll(
                        model().size(2),
                        model().attribute("startString", "Сорт"),
                        model().attribute("variants", testVarieties),
                        status().isOk(),
                        forwardedUrl("varieties/search")
                );
        verify(varietyService, times(1)).searchByTypePlant(anyString());
    }

    @Test
    public void testDeleteVariety() throws Exception {
        for (int i = 0; i < 6; i++){
            mockMvc.perform(delete("/nursery/varieties/{idVariety}", anyInt()))
                    .andDo(print())
                    .andExpectAll(
                            status().is3xxRedirection(),
                            redirectedUrl("/nursery/varieties")
                    );
        }
        verify(varietyService, times(6)).deleteVariety(anyInt());
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(plantService, varietyService);
    }
}