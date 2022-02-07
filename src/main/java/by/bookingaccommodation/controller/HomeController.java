package by.bookingaccommodation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping()
    public String preview() {
        return "preview";
    }
}
