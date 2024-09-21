/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bosa.service;

import com.bosa.model.EndUser;
import java.util.List;

/**
 *
 * @author Abhishek
 */
public interface UserService {

    List<EndUser> getAllUsers(String role);

    List<EndUser> getAllAdminUsers(String role);

    EndUser updateUserDetails(EndUser user);

    EndUser getUserById(long id);
}
