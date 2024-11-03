package by.chukotka.nursery.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.validator.constraints.Range;
import java.util.List;
import java.util.Objects;

@ToString
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Plant")
public class Plant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_plant")
    private int idPlant;

    @Enumerated(EnumType.STRING)
    private Saplings seedling;

    @Range(min = 8, max = 20, message = "Стоимость саженца должна быть не менее 8 руб. и не более 20")
    @Column(name = "price_seedling")
    private int priceSeedling;

    @Cascade(org.hibernate.annotations.CascadeType.PERSIST)
    @OneToMany(mappedBy = "plantA")
    private List<Variety> typePlant;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Plant plant = (Plant) o;
        return priceSeedling == plant.priceSeedling && seedling == plant.seedling;
    }

    @Override
    public int hashCode() {
        return Objects.hash(seedling, priceSeedling);
    }

}



