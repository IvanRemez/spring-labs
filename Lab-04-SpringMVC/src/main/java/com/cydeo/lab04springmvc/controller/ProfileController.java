package com.cydeo.lab04springmvc.controller;

import com.cydeo.lab04springmvc.model.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;

@Controller
public class ProfileController {

    @RequestMapping("/profile")
    public String profileInfo(Model model) {

        Profile profile = new Profile("msmith@gmail.com", "+1256342345",
                "mike", "smith", "msmith", LocalDateTime.now());

        model.addAttribute(profile);

        return "/profile/profile-info";
    }
}
