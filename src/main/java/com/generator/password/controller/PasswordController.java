package com.generator.password.controller;

import com.generator.password.generate.PasswordGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@Controller
public class PasswordController {

    @GetMapping("/password")
    public String generatePassword(Model model) {
        final String password = new PasswordGenerator()
                .generateRandomPassword(64, 2, 2, 2, 2);
        model.addAttribute("password", password);
        return "show-password";
    }

    @PostMapping("/password")
    public String generatePassword(Model model, @RequestBody Map<String, Integer> requestParams) {
        final String password = new PasswordGenerator().generateRandomPassword(
                        requestParams.get("length"), requestParams.get("lowercase"),
                        requestParams.get("uppercase"), requestParams.get("digit"),
                        requestParams.get("specialChar"));
        model.addAttribute("password", password);
        return "show-password";
    }

}
