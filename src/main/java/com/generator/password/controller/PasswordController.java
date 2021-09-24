package com.generator.password.controller;

import com.generator.password.entity.PasswordGenerator;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PasswordController {

    @GetMapping("/password")
    public String generatePassword(Model model) {
        model.addAttribute("password", new PasswordGenerator());
        return "create-policy";
    }

    @CrossOrigin("http://localhost:4000")
    @PostMapping(path = "/show", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PasswordGenerator> showPassword(@ModelAttribute("generator") PasswordGenerator generator) {
        return new ResponseEntity<>(generator, HttpStatus.OK);
    }

    /* For Thymeleaf form
    @CrossOrigin("http://localhost:4000")
    @PostMapping("/show")
    public String showPassword(@ModelAttribute("generator") PasswordGenerator generator) {
        return "show-password";
    }*/

}
