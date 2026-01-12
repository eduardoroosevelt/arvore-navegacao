package com.example.menu.repository;

import com.example.menu.entity.UserEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    @EntityGraph(attributePaths = "permissions")
    Optional<UserEntity> findByUsername(String username);
}
