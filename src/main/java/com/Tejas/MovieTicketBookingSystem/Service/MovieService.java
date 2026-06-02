package com.Tejas.MovieTicketBookingSystem.Service;

import com.Tejas.MovieTicketBookingSystem.DTOs.MovieRequest;
import com.Tejas.MovieTicketBookingSystem.DTOs.MovieResponse;
import com.Tejas.MovieTicketBookingSystem.DTOs.MovieUpdateRequest;
import com.Tejas.MovieTicketBookingSystem.Entity.MovieEntity;
import com.Tejas.MovieTicketBookingSystem.Enum.Genre;
import com.Tejas.MovieTicketBookingSystem.Enum.Language;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MovieService {
    public MovieResponse postMovie(MovieRequest request);

    public List<MovieResponse> searchMovie(String name, Genre genre, Language language, int page, int size);

    public List<MovieResponse> getAllMovie(int page, int size);

    public MovieResponse updateMovieProfile(Long id, MovieUpdateRequest request);

    public String deleteMovie(Long id);

    public MovieResponse mapToResponse(MovieEntity entity);

    public MovieEntity mapToEntity(MovieRequest request);
}
