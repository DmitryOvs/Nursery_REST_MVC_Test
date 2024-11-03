package by.chukotka.nursery.validators;


import by.chukotka.nursery.model.Variety;
import by.chukotka.nursery.services.VarietyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UniqueVarietyValidator implements Validator {

    private final VarietyService varietyService;

@Autowired
    public UniqueVarietyValidator(VarietyService varietyService) {
        this.varietyService = varietyService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Variety.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Variety variety = (Variety) target;
        Variety varietyName = varietyService.findOneByNameType(variety.getNameType());
        if (varietyName != null && variety.getIdVariety() != varietyName.getIdVariety()) {
            errors.rejectValue("nameType", "", "Саженец этого типа с таким названием уже существует");
        }
    }
}
