package by.chukotka.nursery.controllers;

import by.chukotka.nursery.model.Plant;
import by.chukotka.nursery.model.Variety;
import by.chukotka.nursery.services.PlantService;
import by.chukotka.nursery.services.VarietyService;
import by.chukotka.nursery.validators.UniqueVarietyValidator;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/nursery/varieties")
public class VarietiesController {
    private final PlantService plantService;
    private final VarietyService varietyService;
    private final UniqueVarietyValidator uniqueVarietyValidator;

    public VarietiesController(PlantService plantService, VarietyService varietyService, UniqueVarietyValidator uniqueVarietyValidator) {
        this.plantService = plantService;
        this.varietyService = varietyService;
        this.uniqueVarietyValidator = uniqueVarietyValidator;
    }

    @GetMapping
    public String showVarieties(@RequestParam(value = "page", required = false, defaultValue = "-1") int page,
            @RequestParam(value = "varietiesPerPage", required = false, defaultValue = "-1") int varietiesPerPage,
            @RequestParam(value = "sortNameType", required = false, defaultValue = "false") boolean sortNameType,
            Model model){
        List<Variety> varieties;
        if(page != -1 && varietiesPerPage != -1){
            varieties = sortNameType
                    ? varietyService.findAndPageAndSortByNameType(page, varietiesPerPage)
                    : varietyService.findAndPage(page, varietiesPerPage);
        }else if (page == -1 && varietiesPerPage == -1 && sortNameType){
            varieties = varietyService.findAndSortByNameType();
        }else {
            varieties = varietyService.findAll();
        }
        model.addAttribute("varieties", varieties);
        return "varieties/show";
    }

    @GetMapping("/{idVariety}")
    public String showVariety(@PathVariable("idVariety") int idVariety, Model model,
                              @ModelAttribute("plant") Plant plant){
        Variety variety = varietyService.findOne(idVariety);
        model.addAttribute("variety", variety);
        if(variety.getPlantA() != null){
            model.addAttribute("plantA", variety.getPlantA());
        } else {
            model.addAttribute("plants", plantService.findAll());
        }
        return "varieties/profile";
    }

    @GetMapping("/new")
    public String addVariety(@ModelAttribute("variety") Variety variety){
        return "varieties/new";
    }

    @PostMapping()
    public String createVariety(@ModelAttribute("variety") @Valid Variety variety, BindingResult bindingResult) {
        uniqueVarietyValidator.validate(variety, bindingResult);
        if(bindingResult.hasErrors()){
            return "varieties/new";
        }
        varietyService.save(variety);
        return "redirect:/nursery/varieties";
    }

    @GetMapping("/{idVariety}/edit")
    public String editVariety(@PathVariable("idVariety") int idVariety, Model model){
        model.addAttribute("variety", varietyService.findOne(idVariety));
        return "varieties/edit";
    }

    @PatchMapping("/{idVariety}")
    public String updateVariety(@ModelAttribute("variety") @Valid Variety variety, BindingResult bindingResult,
                                @PathVariable("idVariety") int idVariety){
        uniqueVarietyValidator.validate(variety, bindingResult);
        if (bindingResult.hasErrors()) {
            return "varieties/edit";
        }
        varietyService.update(idVariety, variety);
        return "redirect:/nursery/varieties";
    }

    @PatchMapping("/{idVariety}/plant")
    public String addVarietyToPlant(@ModelAttribute ("plant") Plant plant, @PathVariable ("idVariety") int idVariety) {
        varietyService.addVarietyToPlant(plant, idVariety);
        return "redirect:/nursery/varieties/{idVariety}";
    }


    @GetMapping("/search")
    public String searchVarieties(@RequestParam(required = false, defaultValue = "") String startString, Model model) {
        model.addAttribute("startString", startString);
        if (!startString.isEmpty()) {
            model.addAttribute("variants", varietyService.searchByTypePlant(startString));
             }
        return "varieties/search";
   }

    @DeleteMapping("/{idVariety}")
    public String deleteVariety(@PathVariable("idVariety") int idVariety){
        varietyService.deleteVariety(idVariety);
        return "redirect:/nursery/varieties";
    }

}





