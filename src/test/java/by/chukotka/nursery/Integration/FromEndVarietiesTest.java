package by.chukotka.nursery.Integration;


import by.chukotka.nursery.model.Plant;
import by.chukotka.nursery.model.Variety;
import by.chukotka.nursery.services.PlantService;
import by.chukotka.nursery.services.VarietyService;
import by.chukotka.nursery.util.H2databaseInitTest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@TestMethodOrder(MethodOrderer.Random.class)
@SuppressWarnings("unchecked")
public class FromEndVarietiesTest extends H2databaseInitTest {

    private final MockMvc mockMvc;
    private MvcResult mvcResult;
    private ModelAndView modelAndView;

    @Autowired
    private PlantService plantService;

    @Autowired
    private VarietyService varietyService;

    @Autowired
    public FromEndVarietiesTest(WebApplicationContext webApplicationContext) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testShowVarietiesWithoutSearchingAndPagingAndSorted() throws Exception {
        mvcResult = mockMvc.perform(get("/nursery/varieties"))
                .andExpectAll(
                        model().size(1),
                        model().attributeExists("varieties"),
                        status().isOk())
                .andReturn();
        modelAndView = mvcResult.getModelAndView();
        assertNotNull(modelAndView);
        assertEquals("varieties/show", modelAndView.getViewName());

        List<Variety> coolVarieties = (List<Variety>) modelAndView.getModel().get("varieties");
        assertEquals(5, coolVarieties.size());
    }

    @Test
    public void testShowVarietiesWithPaging() throws Exception {
        mvcResult = mockMvc.perform(get("/nursery/varieties")
                        .param("page", "1"))
                .andExpectAll(
                        model().size(1),
                        model().attributeExists("varieties"),
                        status().isOk())
                .andReturn();

        modelAndView = mvcResult.getModelAndView();
        assertNotNull(modelAndView);
        assertEquals("varieties/show", modelAndView.getViewName());

        List<Variety> coolVarietiesPage = (List<Variety>) modelAndView.getModel().get("varieties");
        assertEquals(5, coolVarietiesPage.size());


        mvcResult = mockMvc.perform(get("/nursery/varieties")
                        .param("varietiesPerPage", "7"))
                .andExpectAll(
                        model().size(1),
                        model().attributeExists("varieties"),
                        status().isOk())
                .andReturn();

        modelAndView = mvcResult.getModelAndView();
        assertNotNull(modelAndView);
        assertEquals("varieties/show", modelAndView.getViewName());

        List<Variety> coolVarietiesPerPage = (List<Variety>) modelAndView.getModel().get("varieties");
        assertEquals(5, coolVarietiesPerPage.size());
    }

    @Test
    public void testShowVarietiesWithTwinPaging() throws Exception {
        mvcResult = mockMvc.perform(get("/nursery/varieties")
                        .param("page", "0")
                        .param("varietiesPerPage", "1"))
                .andExpectAll(
                        model().size(1),
                        model().attributeExists("varieties"),
                        status().isOk())
                .andReturn();

        modelAndView = mvcResult.getModelAndView();
        assertNotNull(modelAndView);
        assertEquals("varieties/show", modelAndView.getViewName());

        List<Variety> coolVarietiesPagingOne = (List<Variety>) modelAndView.getModel().get("varieties");
        assertEquals(1, coolVarietiesPagingOne.size());
        assertEquals("Принц", coolVarietiesPagingOne.get(0).getNameType());


        mvcResult = mockMvc.perform(get("/nursery/varieties")
                        .param("page", "1")
                        .param("varietiesPerPage", "6"))
                .andExpectAll(
                        model().size(1),
                        model().attributeExists("varieties"),
                        status().isOk())
                .andReturn();

        modelAndView = mvcResult.getModelAndView();
        assertNotNull(modelAndView);
        assertEquals("varieties/show", modelAndView.getViewName());

        List<Variety> coolVarietiesPagingEmpty = (List<Variety>) modelAndView.getModel().get("varieties");
        assertTrue(coolVarietiesPagingEmpty.isEmpty());
    }

    @Test
    public void testShowVarietiesWithSortingByNameType() throws Exception {
        mvcResult = mockMvc.perform(get("/nursery/varieties")
                        .param("sortNameType", "true"))
                .andExpectAll(
                        model().size(1),
                        model().attributeExists("varieties"),
                        status().isOk())
                .andReturn();

        modelAndView = mvcResult.getModelAndView();
        assertNotNull(modelAndView);
        assertEquals("varieties/show", modelAndView.getViewName());

        ArrayList<Variety> coolVarietiesSortingByNameType = (ArrayList<Variety>) modelAndView.getModel().get("varieties");
        assertEquals(5, coolVarietiesSortingByNameType.size());
        assertEquals("Антоновка", coolVarietiesSortingByNameType.get(0).getNameType());
        assertEquals("Бриз", coolVarietiesSortingByNameType.get(1).getNameType());
        assertEquals("Елена", coolVarietiesSortingByNameType.get(2).getNameType());
        assertEquals("Принц", coolVarietiesSortingByNameType.get(3).getNameType());
        assertEquals("Хани Крисп", coolVarietiesSortingByNameType.get(4).getNameType());




    }

    @Test
    public void testShowVarietiesWithSortingByNameAndPaging() throws Exception {
        mvcResult = mockMvc.perform(get("/nursery/varieties")
                        .param("page", "0")
                        .param("perPage", "2")
                        .param("sortNameType", "true"))
                .andExpectAll(
                        model().size(1),
                        model().attributeExists("varieties"),
                        status().isOk())
                .andReturn();

        modelAndView = mvcResult.getModelAndView();
        assertNotNull(modelAndView);
        assertEquals("varieties/show", modelAndView.getViewName());

        List<Variety> coolVarietiesSortingByNameType = (List<Variety>) modelAndView.getModel().get("varieties");
        assertEquals(5, coolVarietiesSortingByNameType.size());
        assertEquals("Антоновка", coolVarietiesSortingByNameType.get(4).getNameType());
        assertEquals("Бриз", coolVarietiesSortingByNameType.get(3).getNameType());
    }

    @Test
    public void testNewCreateVariety() throws Exception {
        List<Variety> coolVarieties = varietyService.findAll();
        assertEquals(5, coolVarieties.size());

        Variety newVariety = new Variety();
        newVariety.setNameType("Мираж");
        newVariety.setQuantity(25);
        newVariety.setYear(2021);

        mockMvc.perform(post("/nursery/varieties")
                .flashAttr("variety", newVariety))
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrl("/nursery/varieties"));

        coolVarieties = varietyService.findAll();
        assertEquals(6, coolVarieties.size());
    }

    @Test
    public void testUpdateVariety() throws Exception {
        Variety updatedVariety = varietyService.findOne(3);
        assertEquals("Елена", updatedVariety.getNameType());
        assertEquals(2024, updatedVariety.getYear());
        assertEquals(30, updatedVariety.getQuantity());

        updatedVariety.setNameType("Гриб");
        updatedVariety.setQuantity(25);
        updatedVariety.setYear(2021);

        mockMvc.perform(patch("/nursery/varieties/{id}", updatedVariety.getIdVariety())
                .flashAttr("variety", updatedVariety))
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrl("/nursery/varieties")
                );
        updatedVariety = varietyService.findOne(3);
        assertEquals("Гриб", updatedVariety.getNameType());
        assertEquals(2021, updatedVariety.getYear());
        assertEquals(25, updatedVariety.getQuantity());
    }

    @Test
    public void testAddVarietyToPlant() throws Exception {
        Variety updatedVariety = varietyService.findOne(3);
        assertNull(updatedVariety.getPlantA());

        Plant plantA = plantService.findOneById(new Random().nextInt(4) + 1);

        mockMvc.perform(patch("/nursery/varieties/{id}/plant", updatedVariety.getIdVariety())
                .flashAttr("plantA", plantA))
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrl("/nursery/varieties" + updatedVariety.getIdVariety())
                );
        updatedVariety = varietyService.findOne(3);
        assertNull(updatedVariety.getPlantA());
    }

    @Test
    public void testSearchExistingVariety() throws Exception {
        mvcResult = mockMvc.perform(get("/nursery/varieties/search")
                        .param("startString", "Еле"))
                .andExpectAll(
                        model().size(2),
                        model().attribute("startString", "Еле"),
                        model().attributeExists("variants"),
                        status().isOk())
                .andReturn();

        modelAndView = mvcResult.getModelAndView();
        assertNotNull(modelAndView);
        assertEquals("varieties/search", modelAndView.getViewName());

        List<Variety> coolVarieties = (List<Variety>) modelAndView.getModel().get("varieties");
        assertEquals(1, coolVarieties.size());
        assertEquals("Елена", coolVarieties.get(1).getNameType());
    }

    @Test
    public void testSearchNotExistingVariety() throws Exception {
        mvcResult = mockMvc.perform(get("/nursery/varieties/search")
                        .param("startString", "Тетрадь"))
                .andExpectAll(
                        model().size(2),
                        model().attribute("startString", "Тетрадь"),
                        model().attributeExists("variants"),
                        status().isOk())
                .andReturn();

        modelAndView = mvcResult.getModelAndView();
        assertNotNull(modelAndView);
        assertEquals("varieties/search", modelAndView.getViewName());

        List<Variety> coolVarieties = (List<Variety>) modelAndView.getModel().get("varieties");
        assertNull(coolVarieties);
    }

    @Test
    public void testDeleteVariety() throws Exception {
        List<Variety> coolVarieties = varietyService.findAll();
        assertEquals(5, coolVarieties.size());

        Variety deletedVariety = coolVarieties.get(2);
        assertEquals("Елена", deletedVariety.getNameType());

        mockMvc.perform(delete("/nursery/varieties/{id}", deletedVariety.getIdVariety()))
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrl("/nursery/varieties")
                );

        coolVarieties = varietyService.findAll();
        assertEquals(4, coolVarieties.size());
        assertFalse(coolVarieties.contains(deletedVariety));
    }
}