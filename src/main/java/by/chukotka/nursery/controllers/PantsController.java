package by.chukotka.nursery.controllers;

import by.chukotka.nursery.model.Plant;
import by.chukotka.nursery.model.Variety;
import by.chukotka.nursery.services.PlantService;
import by.chukotka.nursery.services.VarietyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/nursery/plants")
public class PantsController {
    private final PlantService plantService;
    private final VarietyService varietyService;

    @Autowired
    public PantsController(PlantService plantService, VarietyService varietyService) {
        this.plantService = plantService;
        this.varietyService = varietyService;
    }

    @GetMapping
    public String showPlants(Model model) {
        model.addAttribute("plants", plantService.findAll());
        return "plants/show";
    }

    @GetMapping("/{idPlant}")
    public String showPlant(@PathVariable int idPlant, Model model) {
        Plant plant = plantService.findOneById(idPlant);
        List<Variety> varieties = varietyService.findByPlant(plant);
        model.addAttribute("plant", plant);
        if (!varieties.isEmpty()) {
            model.addAttribute("varieties", varieties);
        }
        return "plants/profile";
    }

    @GetMapping("/new")
    public String addPlant(@ModelAttribute("plant") Plant plant) {
        return "plants/new";
    }

    @PostMapping()
    public String createPlant(@ModelAttribute("plant") @Valid Plant plant, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return "plants/new";
        plantService.save(plant);
        return "redirect:/nursery/plants";
    }

    @GetMapping("/{idPlant}/edit")
    public String editPlant(@PathVariable ("idPlant") int idPlant, Model model) {
        model.addAttribute("plant", plantService.findOneById(idPlant));
        return "plants/edit";
    }

    @PatchMapping("/{idPlant}")
    public String updatePlant(@ModelAttribute("plant") @Valid Plant plant, BindingResult bindingResult,
                              @PathVariable ("idPlant") int idPlant) {
        if (bindingResult.hasErrors()) return "plants/edit";
        plantService.update(idPlant, plant);
        return "redirect:/nursery/plants";
    }

    @DeleteMapping("/{idPlant}")
    public String deletePlant(@PathVariable ("idPlant") int idPlant) {
        plantService.delete(idPlant);
        return "redirect:/nursery/plants";
    }
}

