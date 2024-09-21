/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bosa.service;

import com.bosa.model.EndUser;
import com.bosa.repository.EnduserRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 *
 * @author Abhishek
 */
@Service
public class EndUserDetailService implements UserDetailsService {

    @Autowired
    EnduserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<EndUser> userByName = userRepository.findByUsername(username);
        if (userByName.isPresent()) {
            EndUser user = userByName.get();
            return User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .roles(getRoles(user))
                    .build();
        } else {
            Optional<EndUser> userByEmail = userRepository.findByEmail(username);
            if (userByEmail.isPresent()) {
                EndUser user = userByEmail.get();
                return User.builder()
                        .username(user.getUsername())
                        .password(user.getPassword())
                        .roles(getRoles(user))
                        .build();
            } else {
                throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
            }
        }
    }

    private String[] getRoles(EndUser user) {
        if (user.getRole() == null) {
            return new String[]{"USER"};
        }
        return user.getRole().split(",");
    }
}
