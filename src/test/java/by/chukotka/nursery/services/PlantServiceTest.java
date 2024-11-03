package by.chukotka.nursery.services;


import by.chukotka.nursery.model.Plant;
import by.chukotka.nursery.model.Saplings;
import by.chukotka.nursery.repositories.PlantsRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.Random.class)
public class PlantServiceTest {

    @MockBean
    PlantsRepository plantsRepository;

    @Autowired
    PlantService plantService;

    Plant testPlant;
    List<Plant> testPlants;

    @BeforeEach
    public void setUp() {
        testPlant = new Plant();
        testPlant.setSeedling(Saplings.APPLE);

        testPlants = new ArrayList<>();
        testPlants.add(testPlant);
    }

    @Test
    public void testFindAll(){
        when(plantsRepository.findAll()).thenReturn(testPlants);
        List<Plant> coolPlants = plantService.findAll();
        assertIterableEquals(testPlants, coolPlants);
        verify(plantsRepository).findAll();
    }

    @Test
    public void testFindOneById(){
        when(plantsRepository.findById(anyInt())).thenReturn(Optional.of(testPlant));
        Plant coolPlant = plantService.findOneById(anyInt());
        assertNotNull(coolPlant);
        assertEquals(Saplings.APPLE, coolPlant.getSeedling());
        verify(plantsRepository, times(1)).findById(anyInt());
    }

    @Test
    public void testSave(){
        for (int i = 0; i < 6; i++) plantService.save(new Plant());
       verify(plantsRepository, times(6)).save(any(Plant.class));
    }

    @Test
    public void testUpdate(){
        plantService.update(anyInt(), testPlant);
        verify(plantsRepository, times(1)).save(testPlant);
    }

    @Test
    public void testDelete(){
        for (int i = 0; i < 5; i++) plantService.delete(anyInt());
        verify(plantsRepository, times(5)).deleteById(anyInt());
    }

    @AfterEach
    void tearDown(){
        verifyNoMoreInteractions(plantsRepository);
    }
}
