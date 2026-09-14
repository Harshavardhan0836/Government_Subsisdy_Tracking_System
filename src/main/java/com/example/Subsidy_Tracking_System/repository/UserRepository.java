package com.example.Subsidy_Tracking_System.repository;

import com.example.Subsidy_Tracking_System.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}