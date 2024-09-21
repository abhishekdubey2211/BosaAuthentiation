/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bosa.security;

import com.bosa.model.EndUser;
import com.bosa.repository.EnduserRepository;
import com.bosa.service.EndUserDetailService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import org.hibernate.validator.internal.util.logging.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

/**
 *
 * @author Abhishek
 */
@Component
public class OAuth2SucessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private EnduserRepository userRepository;

    @Autowired
    EndUserDetailService endUserdetailService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        AuthenticationSuccessHandler.super.onAuthenticationSuccess(request, response, chain, authentication); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        DefaultOAuth2User defaultOAuth2User = (DefaultOAuth2User) authentication.getPrincipal();
        String picture = defaultOAuth2User.getAttribute("picture").toString();
        String name = defaultOAuth2User.getAttribute("name").toString();
        String email = defaultOAuth2User.getAttribute("email").toString();

        Optional<EndUser> optionalUser = userRepository.findByEmail(email);
        EndUser user;
        if (optionalUser.isPresent()) {
            user = optionalUser.get();
        } else {
            user = new EndUser();
            user.setEmail(email);
            user.setUsername(name);
            user.setProfileimg(picture);
            user.setRole("USER");
            user.setPassword("Test1234!");
            userRepository.save(user);
        }
        new DefaultRedirectStrategy().sendRedirect(request, response, "/bosa/home");
    }

}
