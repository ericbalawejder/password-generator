package com.generator.password.controller;

import com.generator.password.generate.PasswordGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PasswordController {

    @GetMapping("/password")
    public String generatePassword(Model model) {
        final String password = new PasswordGenerator()
                .generateRandomPassword(64, 2, 2, 2, 2);
        model.addAttribute("password", password);
        return "show-password";
    }

}
