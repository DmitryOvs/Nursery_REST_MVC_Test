package by.chukotka.nursery.repositories;

import by.chukotka.nursery.model.Plant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface PlantsRepository extends JpaRepository<Plant, Integer> {


}
