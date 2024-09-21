/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bosa.controller;

import com.bosa.jwtservice.JWTUtil;
import com.bosa.model.EndUser;
import com.bosa.repository.EnduserRepository;
import com.bosa.service.EndUserDetailService;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Abhishek
 */
@RestController
@RequestMapping("/bosa")
public class BosaController {

    @GetMapping("/version")
    public String version() {
        return "BosaAuth Handler v0.0.1 ";
    }

    @GetMapping("/home")
    public String welcomePage() {
        return "Welcome TO Bosa TeleCalling";
    }

    @GetMapping("/admin/home")
    public String adminWelcomePage() {
        return "Welcome TO Bosa Admin HomePage";
    }

    @GetMapping("/user/home")
    public String userWelcomePage() {
        return "Welcome TO Bosa User HomePage";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
