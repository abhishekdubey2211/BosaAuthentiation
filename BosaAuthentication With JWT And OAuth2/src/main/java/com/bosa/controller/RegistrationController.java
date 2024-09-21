/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bosa.controller;

import com.bosa.jwtservice.JWTUtil;
import com.bosa.model.EndUser;
import com.bosa.model.LoginForm;
import com.bosa.repository.EnduserRepository;
import com.bosa.service.EndUserDetailService;
import com.bosa.service.UserServiceImpl;
import java.util.List;
import java.util.Optional;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Abhishek
 */
@RestController
@RequestMapping("/bosa")
public class RegistrationController {

    @Autowired
    EnduserRepository enduserRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JWTUtil jwtService;

    @Autowired
    EndUserDetailService userDetailService;

    @Autowired
    private UserServiceImpl endUserService;

    @GetMapping("/users")
    public List<EndUser> getUsersByRole() {
        return endUserService.getAllUsers("USER");
    }

    @GetMapping("/admin")
    public List<EndUser> getAdminByRole() {
        return endUserService.getAllUsers("ADMIN");
    }
    
    @PostMapping("/authenticate")
    public String authenticate(@RequestBody LoginForm loginform) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginform.getUsername(), loginform.getPassword()));
        if (authentication.isAuthenticated()) {
            UserDetails user = userDetailService.loadUserByUsername(loginform.getUsername());
            return jwtService.generteToken(user);
        } else {
            throw new UsernameNotFoundException("User Credentials Not Found");
        }
    }

    @PostMapping("/register/user")
    public EndUser registration(@RequestBody EndUser user) {

        Optional<EndUser> checkuserexists = enduserRepository.findByEmail(user.getEmail());
        if (checkuserexists.isPresent()) {
            throw new UsernameNotFoundException("User Already Exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return enduserRepository.save(user);
    }
}
