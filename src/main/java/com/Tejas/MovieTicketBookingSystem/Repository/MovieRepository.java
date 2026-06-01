package com.Tejas.MovieTicketBookingSystem.Repository;

import com.Tejas.MovieTicketBookingSystem.Entity.MovieEntity;
import com.Tejas.MovieTicketBookingSystem.Enum.Genre;
import com.Tejas.MovieTicketBookingSystem.Enum.Language;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<MovieEntity,Long> {
    Optional<MovieEntity> findByNameIgnoreCase(String name);

    Page<MovieEntity> findByGenre(Genre genre, Pageable pageable);

    Page<MovieEntity> findByLanguage(Language language, Pageable pageable);

    Page<MovieEntity> findByUserEntityId(Long id, Pageable pageable);
}
