/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bosa.service;

import com.bosa.model.EndUser;
import com.bosa.repository.EnduserRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Abhishek
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    EnduserRepository userRepository;

    @Override
     public List<EndUser> getAllUsers(String role) {
        List<EndUser> userList = userRepository.findByRole(role);
        if (userList != null && !userList.isEmpty()) {
            return userList;
        } else {
            throw new RuntimeException("No users found with the role: " + role);
        }
    }

    @Override
    public List<EndUser> getAllAdminUsers(String role) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public EndUser updateUserDetails(EndUser user) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public EndUser getUserById(long id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
