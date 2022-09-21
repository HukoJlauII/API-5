package com.example.api5.controller;

import com.example.api5.entity.Contact;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @GetMapping("/")
    public String showIndex(Model model)
    {
        Contact contact=new Contact();
        model.addAttribute("contact",contact);
        return "index";
    }
}
