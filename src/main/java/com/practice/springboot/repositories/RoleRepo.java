package com.practice.springboot.repositories;

import com.practice.springboot.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepo extends JpaRepository<Role, Integer> {

    boolean existsByName(String name);

    Optional<Role> findByName(String roleName);
}
