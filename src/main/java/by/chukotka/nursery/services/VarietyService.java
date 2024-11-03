package by.chukotka.nursery.services;


import by.chukotka.nursery.model.Plant;
import by.chukotka.nursery.model.Variety;
import by.chukotka.nursery.repositories.PlantsRepository;
import by.chukotka.nursery.repositories.VarietiesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@Transactional(readOnly = true)
public class VarietyService {
    private final VarietiesRepository varietiesRepository;

    @Autowired
    private PlantsRepository plantsRepository;


    @Autowired
    public VarietyService(VarietiesRepository varietiesRepository) {
        this.varietiesRepository = varietiesRepository;

    }


    public List<Variety> findAll() {
    return varietiesRepository.findAll();
}

    public List<Variety> findAndSortByNameType(){
    return varietiesRepository.findAll(Sort.by("nameType"));
}

    public List<Variety> findAndPage(int page, int itemOnPage){
    return  varietiesRepository.findAll(PageRequest.of(page, itemOnPage)).getContent();
}

    public List<Variety> findAndPageAndSortByNameType(int page, int itemOnPage) {
        return varietiesRepository.findAll(PageRequest.of(page, itemOnPage, Sort.by("nameType"))).getContent();
}

    public Variety findOne(int idVariety){
    Optional<Variety> variety = varietiesRepository.findById(idVariety);
    return variety.orElse(null);
}

    public List<Variety> findByPlant(Plant plant){
    return varietiesRepository.findByPlantA(plant);
}

    public Variety findOneByNameType(String nameType) {
        return varietiesRepository.findByNameType(nameType).orElse(null);
    }

    @Transactional
    public void save(Variety variety) {
    varietiesRepository.save(variety);
}

    @Transactional
    public void update(int id, Variety variety) {
        Variety varietyInBase = varietiesRepository.findById(id).orElse(null);
        if (varietyInBase != null) {
            variety.setPlantA(varietyInBase.getPlantA());
            }
        variety.setIdVariety(id);
        varietiesRepository.save(variety);

        // varietiesRepository.findById(idVariety).ifPresent(varietyOld -> variety.setPlantA(varietyOld.getPlantA()));
        // variety.setIdVariety(idVariety);
        //varietiesRepository.save(variety);
}

    @Transactional
    public void addVarietyToPlant(Plant plant, int idVariety) {
    Variety variety = varietiesRepository.findById(idVariety).orElse(null);
    Plant p = plantsRepository.findById(plant.getIdPlant()).orElse(null);
    if(variety != null && p != null){
        variety.setPlantA(p);
        varietiesRepository.save(variety);
    }
}

    public List<Variety> searchByTypePlant(String startString){
    return varietiesRepository.findByNameTypeStartingWithIgnoreCase(startString);
    }

    @Transactional
    public void deleteVariety(int idVariety) {
    varietiesRepository.deleteById(idVariety);
    }
}

