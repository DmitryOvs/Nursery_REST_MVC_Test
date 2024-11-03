package by.chukotka.nursery.services;

import by.chukotka.nursery.model.Plant;
import by.chukotka.nursery.model.Variety;
import by.chukotka.nursery.repositories.PlantsRepository;
import by.chukotka.nursery.repositories.VarietiesRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.Random.class)
public class VarietyServiceTest {

    @MockBean
    VarietiesRepository varietiesRepository;

    @Autowired
    VarietyService varietyService;

    Variety testVariety;
    List<Variety> testVarieties;
    Plant testPlant;

    Random random;
    @Autowired
    private PlantsRepository plantsRepository;

    @BeforeEach
    public void setUp(){
        testPlant = new Plant();

        testVariety = new Variety();
        testVarieties = new ArrayList<>();
        testVarieties.add(testVariety);

        random = new Random();
    }

    @Test
    public void testFindAll(){
        when(varietiesRepository.findAll()).thenReturn(testVarieties);
        List<Variety> coolVarieties = varietyService.findAll();
        assertIterableEquals(testVarieties, coolVarieties);
        verify(varietiesRepository, times(1)).findAll();
    }

    @Test
    public void testFindAndSortByNameType(){
        when(varietiesRepository.findAll(Sort.by("nameType"))).thenReturn(testVarieties);
        List<Variety> coolVarieties = varietyService.findAndSortByNameType();
        assertIterableEquals(testVarieties, coolVarieties);
        assertEquals(testVariety, coolVarieties.get(0));
        verify(varietiesRepository, times(1)).findAll(Sort.by("nameType"));
    }

    @Test
    public void testFindAndPage(){
        PageImpl<Variety> varietyPage = new PageImpl<>(testVarieties);
        when(varietiesRepository.findAll(isA(Pageable.class))).thenReturn(varietyPage);
        List<Variety> coolVarieties = varietyService.findAndPage(random.nextInt(50), random.nextInt(50));
        assertIterableEquals(testVarieties, coolVarieties);
        verify(varietiesRepository, times(1)).findAll(isA(Pageable.class));
    }

    @Test
    public void testFindAndPageAndSortByNameType(){
        PageImpl<Variety> varietyPage = new PageImpl<>(testVarieties);
        when(varietiesRepository.findAll(isA(Pageable.class))).thenReturn(varietyPage);
        for (int i = 1; i <= 9; i++) {
            assertEquals(testVarieties, varietyService.findAndPageAndSortByNameType(i, i));
        }
        verify(varietiesRepository, times(9)).findAll(isA(Pageable.class));
    }

    @Test
    public void testFindOne(){
        when(varietiesRepository.findById(anyInt())).thenReturn(Optional.of(testVariety));
        Variety coolVariety = varietyService.findOne(anyInt());
        assertEquals(testVariety, coolVariety);
        verify(varietiesRepository, times(1)).findById(anyInt());
    }

    @Test
    public void testFindByPlant(){
        when(varietiesRepository.findByPlantA(any(Plant.class))).thenReturn(testVarieties);
        List<Variety> coolVariety = varietyService.findByPlant(testPlant);
        assertEquals(1, coolVariety.size());
        assertEquals(testVariety, coolVariety.get(0));
        verify(varietiesRepository, times(1)).findByPlantA(any(Plant.class));
    }

    @Test
    public void testFindOneByNameType(){
        when(varietiesRepository.findByNameType(any(String.class))).thenReturn(Optional.of(testVariety));
        Variety coolVariety = varietyService.findOneByNameType(anyString());
        assertEquals(testVariety, coolVariety);
        verify(varietiesRepository, times(1)).findByNameType(anyString());
    }

    @Test
    public void testSave(){
        for (int i = 0; i < 5; i++) varietyService.save(testVariety);
        verify(varietiesRepository, times(5)).save(any(Variety.class));
    }

    @Test
    public void testUpdate(){
        testVariety.setPlantA(testPlant);
        when(varietiesRepository.findById(anyInt())).thenReturn(Optional.of(testVariety));
        int testVarietyId = random.nextInt(100);
        varietyService.update(testVarietyId, testVariety);
        assertEquals(testVarietyId, testVariety.getIdVariety());
       verify(varietiesRepository, times(1)).findById(testVarietyId);
       verify(varietiesRepository, times(1)).save(any(Variety.class));
    }

    @Test
    public void testAddVarietyToPlant(){
        when(varietiesRepository.findById(anyInt())).thenReturn(Optional.of(testVariety));
        int testVarietyId = random.nextInt(100);
        varietyService.addVarietyToPlant(testPlant,testVarietyId);
        assertEquals(testPlant, testVariety.getPlantA());
        verify(varietiesRepository, times(1)).findById(testVarietyId);
        verify(varietiesRepository, times(1)).save(any(Variety.class));
    }

    @Test
    public void testSearchByTypePlant(){
        when(varietiesRepository.findByNameTypeStartingWithIgnoreCase(anyString())).thenReturn(testVarieties);
        List<Variety> coolVarieties = varietyService.searchByTypePlant(anyString());
        assertIterableEquals(testVarieties, coolVarieties);
        verify(varietiesRepository, times(1)).findByNameTypeStartingWithIgnoreCase(anyString());
    }

    @Test
    public void testDeleteVariety(){
        varietyService.deleteVariety(random.nextInt(100));
        verify(varietiesRepository, times(1)).deleteById(anyInt());
    }

    @AfterEach
    public void tearDown(){
        verifyNoMoreInteractions(varietiesRepository);
    }
}