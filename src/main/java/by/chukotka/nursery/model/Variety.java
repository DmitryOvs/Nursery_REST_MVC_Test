package by.chukotka.nursery.model;

import by.chukotka.nursery.validators.CurrentYear;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.util.Date;
import java.util.Objects;

@ToString
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Variety")
public class Variety {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_variety")
    private int idVariety;

    @NotNull(message = "Название сорта не может быть пустым")
    @Pattern(regexp = "[0-9А-ЯЁ][0-9a-zA-Zа-яА-ЯёЁ\\-\\s]+",
            message = "Должен быть формат \"Название\" на русском языке")
    @Column(name = "name_type", unique = true, nullable = false)
    private String nameType;

    @Min(value = 2020, message = "Год высадки саженца должен быть больше 2019")
    @CurrentYear
    @Column(name = "year")
    private int year;

    @Min(value = 1, message = "Количество саженцев более 0")
    @Column(name = "quantity")
    private int quantity;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date")
    private Date createDate;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modify_date")
    private Date modifyDate;

    @ManyToOne
    @Cascade(org.hibernate.annotations.CascadeType.PERSIST)
    @JoinColumn(name = "seedling_id", referencedColumnName = "id_plant")
    private Plant plantA;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Variety variety = (Variety) o;
        return year == variety.year && Objects.equals(nameType, variety.nameType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nameType, year);
    }

}


