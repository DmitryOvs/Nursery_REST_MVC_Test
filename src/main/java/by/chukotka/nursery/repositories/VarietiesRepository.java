package by.chukotka.nursery.repositories;

import by.chukotka.nursery.model.Plant;
import by.chukotka.nursery.model.Variety;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VarietiesRepository extends JpaRepository<Variety, Integer> {

    List<Variety> findByPlantA(Plant plant);

    List<Variety> findByNameTypeStartingWithIgnoreCase(String startString);

    Optional<Variety> findByNameType(String nameType);

}
