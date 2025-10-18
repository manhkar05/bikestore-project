package com.example.bikestore.bikestore.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping("/")
    public String home() {
        return "forward:/html/Trang Chủ.html";
    }
}
