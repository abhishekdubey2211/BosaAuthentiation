/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bosa.repository;

import com.bosa.model.EndUser;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Abhishek
 */
@Repository
public interface EnduserRepository extends JpaRepository<EndUser, Long> {

    Optional<EndUser> findByUsername(String username);

    Optional<EndUser> findByEmail(String email);

    List<EndUser> findByRole(String role);
}
