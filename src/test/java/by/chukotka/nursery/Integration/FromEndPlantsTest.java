package by.chukotka.nursery.Integration;

import by.chukotka.nursery.model.Plant;
import by.chukotka.nursery.model.Saplings;
import by.chukotka.nursery.model.Variety;
import by.chukotka.nursery.services.PlantService;
import by.chukotka.nursery.util.H2databaseInitTest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@TestMethodOrder(MethodOrderer.Random.class)
@SuppressWarnings("unchecked")
public class FromEndPlantsTest extends H2databaseInitTest{

    private final MockMvc mockMvc;
    private MvcResult mvcResult;
    private ModelAndView modelAndView;

    @Autowired
    private PlantService plantService;

    @Autowired
    public FromEndPlantsTest(WebApplicationContext webApplicationContext) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testShowPlants() throws Exception {
        mvcResult = mockMvc.perform(get("/nursery/plants"))
                .andExpectAll(
                        model().size(1),
                        model().attributeExists("plants"),
                        status().isOk())
                .andReturn();

        modelAndView = mvcResult.getModelAndView();
        assertNotNull(modelAndView);
        assertEquals("plants/show", modelAndView.getViewName());

        List<Plant> coolPlants = (List<Plant>) modelAndView.getModel().get("plants");
        assertEquals(4, coolPlants.size());
    }

    @Test
    public void testShowPlantsWithVariety() throws Exception {
        mvcResult = mockMvc.perform(get("/nursery/plants/2"))
                .andExpectAll(
                        model().size(2),
                        model().attributeExists("plant"),
                        model().attributeExists("varieties"),
                        status().isOk())
                .andReturn();

        modelAndView = mvcResult.getModelAndView();
        assertNotNull(modelAndView);
        assertEquals("plants/profile", modelAndView.getViewName());

        Plant coolPlant = (Plant) modelAndView.getModel().get("plant");
        assertEquals("PEAR", String.valueOf(coolPlant.getSeedling()));
        assertEquals(20, coolPlant.getPriceSeedling());

        List<Variety> coolVariants = (List<Variety>) modelAndView.getModel().get("varieties");
        assertEquals(1, coolVariants.size());
        assertEquals("Хани Крисп", coolVariants.get(0).getNameType());
    }

    @Test
    public void testShowPlantsWithoutVariety() throws Exception {
        mvcResult = mockMvc.perform(get("/nursery/plants/4" ))
                .andExpectAll(
                        model().size(1),
                        model().attributeExists("plant"),
                        status().isOk())
                .andReturn();

        modelAndView = mvcResult.getModelAndView();
        assertNotNull(modelAndView);
        assertEquals("plants/profile", modelAndView.getViewName());

        Plant coolPlant = (Plant) modelAndView.getModel().get("plant");
        assertEquals("PEACH", String.valueOf(coolPlant.getSeedling()));
        assertEquals(17, coolPlant.getPriceSeedling());

        List<Variety> coolVariants = (List<Variety>) modelAndView.getModel().get("varieties");
        assertNull(coolVariants);
    }

    @Test
    public void testCreatePlant() throws Exception {
        List<Plant> coolPlantsBD = plantService.findAll();
        assertEquals(4, coolPlantsBD.size());

        Plant coolPlant = new Plant();
        coolPlant.setSeedling(Saplings.APRICOT);
        coolPlant.setPriceSeedling(19);

        mockMvc.perform(post("/nursery/plants")
                .flashAttr("plant", coolPlant))
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrl("/nursery/plants")
                );
        coolPlantsBD = plantService.findAll();
        assertEquals(5, coolPlantsBD.size());
        assertEquals(Saplings.APRICOT, coolPlantsBD.get(4).getSeedling());
        assertEquals(19, coolPlantsBD.get(4).getPriceSeedling());
    }

    @Test
    public void testUpdatePlant() throws Exception {
        Plant updatedPlant = plantService.findOneById(1);
        assertEquals(Saplings.APPLE, updatedPlant.getSeedling());
        assertEquals(15, updatedPlant.getPriceSeedling());

        updatedPlant.setPriceSeedling(16);
        mockMvc.perform(patch("/nursery/plants/{id}", updatedPlant.getIdPlant())
                .flashAttr("plant", updatedPlant))
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrl("/nursery/plants")
                );
        updatedPlant = plantService.findOneById(1);
        assertEquals(16, updatedPlant.getPriceSeedling());
    }

    @Test
    public void testDeletePlant() throws Exception {
        Plant deletedPlant = plantService.findOneById(4);

        List<Plant> coolPlantsBD = plantService.findAll();
        assertEquals(4, coolPlantsBD.size());
        assertTrue(coolPlantsBD.contains(deletedPlant));

        mockMvc.perform(delete("/nursery/plants/{id}", deletedPlant.getIdPlant()))
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrl("/nursery/plants")
                );
        coolPlantsBD = plantService.findAll();
        assertEquals(3, coolPlantsBD.size());
        assertFalse(coolPlantsBD.contains(deletedPlant));
    }
}
