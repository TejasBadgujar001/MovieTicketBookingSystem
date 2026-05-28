package com.Tejas.MovieTicketBookingSystem.Repository;

import com.Tejas.MovieTicketBookingSystem.Entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long> {
    Optional<UserEntity> findByEmail(String email);
    Page<UserEntity> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
