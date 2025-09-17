package com.jsalva.gymsystem.repository;

import com.jsalva.gymsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByUsernameAndIsActiveTrue(String username);
    boolean existsByEmail(String email);
    // Custom query for username generation
    @Query("SELECT u.username FROM User u WHERE u.username LIKE :baseUsername")
    List<String> findUsernamesLike(@Param("baseUsername") String baseUsername);
}
