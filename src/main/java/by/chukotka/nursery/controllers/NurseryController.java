package by.chukotka.nursery.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NurseryController {

    @GetMapping("/nursery")
    public String showNursery() {
        return "index";
    }

    @GetMapping("/nursery/owner")
    public String showOwner() {
        return "owner";
    }

    @GetMapping("/nursery/contact")
    public String showContact() {
        return "contact";
    }

}
