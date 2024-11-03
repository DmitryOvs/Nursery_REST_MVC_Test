package by.chukotka.nursery.services;


import by.chukotka.nursery.model.Plant;
import by.chukotka.nursery.repositories.PlantsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PlantService {

    private final PlantsRepository plantsRepository;

    public PlantService(PlantsRepository plantsRepository) {
        this.plantsRepository = plantsRepository;
    }

    public List<Plant> findAll() {
        return plantsRepository.findAll();
    }

    public Plant findOneById(int idPlant) {
        Optional<Plant> person = plantsRepository.findById(idPlant);
        return person.orElse(null);
    }

    @Transactional
    public void save(Plant plant) {
        plantsRepository.save(plant);
    }

    @Transactional
    public void update(int idPlant, Plant plant) {
        plant.setIdPlant(idPlant);
        plantsRepository.save(plant);

    }

    @Transactional
    public void delete(int idPlant) {
        plantsRepository.deleteById(idPlant);
    }


}
