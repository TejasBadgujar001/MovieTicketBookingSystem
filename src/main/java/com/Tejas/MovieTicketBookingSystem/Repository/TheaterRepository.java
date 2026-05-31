package com.Tejas.MovieTicketBookingSystem.Repository;

import com.Tejas.MovieTicketBookingSystem.Entity.TheaterEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TheaterRepository extends JpaRepository<TheaterEntity,Long> {
    Page<TheaterEntity> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Page<TheaterEntity> findByCityContainingIgnoreCase(String city, Pageable pageable);

    Page<TheaterEntity> findByStateContainingIgnoreCase(String state, Pageable pageable);

    List<TheaterEntity> findByUserEntityId(Long id);
}
