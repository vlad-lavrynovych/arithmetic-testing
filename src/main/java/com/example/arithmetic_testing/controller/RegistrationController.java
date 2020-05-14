package com.example.arithmetic_testing.controller;

import com.example.arithmetic_testing.domain.Role;
import com.example.arithmetic_testing.domain.User;
import com.example.arithmetic_testing.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collections;
import java.util.Map;

@Controller
public class RegistrationController {
    @Autowired
    private UserRepo userRepo;

    @GetMapping("/registration")
    public String registration() {
        return "registration.html";
    }

    @GetMapping("/login")
    public String login() {
        return "login.html";
    }


    @PostMapping("/registration")
    public String addUser(User user, Map<String, Object> model) {
        User userFromDb = userRepo.findByUsername(user.getUsername());

        if (userFromDb != null) {
            model.put("message", "User exists!");
            return "registration.html";
        }

        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        userRepo.save(user);

        return "redirect:/login";
    }


    @PostMapping("/login")
    public String loginUser(User user, Model model) {
        User userFromDb = userRepo.findByUsername(user.getUsername());
        if (userFromDb == null) {
            model.addAttribute("message", "User doesn`t exists!");
            return "registration.html";
        }
        if (userFromDb.getPassword() != user.getPassword() ){
            model.addAttribute("message", "Incorect login credentials");
            return "login.html";
        }

       return  "redirect:/";
    }
}
