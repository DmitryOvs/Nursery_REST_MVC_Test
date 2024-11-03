package by.chukotka.nursery.validators;

import by.chukotka.nursery.model.Variety;
import by.chukotka.nursery.repositories.VarietiesRepository;
import by.chukotka.nursery.services.VarietyService;
import by.chukotka.nursery.util.H2databaseInitTest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
public class UniqueVarietyValidatorTest extends H2databaseInitTest {

    private final UniqueVarietyValidator validator;
    private final Variety testVariety;
    private final VarietyService varietyService;
    private final Errors errors;


    @Autowired
    public UniqueVarietyValidatorTest(VarietiesRepository  varietiesRepository) {
        testVariety = new Variety();
        varietyService = new VarietyService(varietiesRepository);
        validator = new UniqueVarietyValidator(varietyService);
        errors = new BeanPropertyBindingResult(testVariety, "variety");
    }

    @Test
    public void testSupports(){
        assertTrue(validator.supports(Variety.class));
        assertFalse(validator.supports(Object.class));
    }

    @Test
    void testVarietyNotUniqueName() {
        testVariety.setNameType("Принц");
        validator.validate(testVariety, errors);
        assertTrue(errors.hasErrors());
        assertEquals(1, errors.getErrorCount());
    }

    @Test
    public void testSameId(){
        int sameId = varietyService.findOneByNameType("Елена").getIdVariety();
        testVariety.setIdVariety(sameId);
        testVariety.setNameType("Елена");
        validator.validate(testVariety, errors);
        assertFalse(errors.hasErrors());
    }

    @Test
    public void testVarietyUniqueName(){
        testVariety.setNameType("Димон");
        validator.validate(testVariety, errors);
        assertFalse(errors.hasErrors());
    }
}