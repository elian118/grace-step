package com.postelian.backend.domain.user.repository;

import com.postelian.backend.domain.user.entity.Role;
import com.postelian.backend.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    @Query("SELECT u FROM User u WHERE " +
           "(:name IS NULL OR u.name LIKE %:name%) AND " +
           "(:email IS NULL OR u.email LIKE %:email%) AND " +
           "(:role IS NULL OR u.role = :role)")
    Page<User> search(
            @Param("name") String name,
            @Param("email") String email,
            @Param("role") Role role,
            Pageable pageable);
}
