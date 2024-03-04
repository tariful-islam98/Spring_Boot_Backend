package com.practice.springboot.repositories;

import com.practice.springboot.entities.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {
    // You can add custom query methods here if needed
}
