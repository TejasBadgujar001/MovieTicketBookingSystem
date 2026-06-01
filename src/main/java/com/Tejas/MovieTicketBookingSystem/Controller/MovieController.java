package com.Tejas.MovieTicketBookingSystem.Controller;

import com.Tejas.MovieTicketBookingSystem.DTOs.MovieRequest;
import com.Tejas.MovieTicketBookingSystem.DTOs.MovieResponse;
import com.Tejas.MovieTicketBookingSystem.DTOs.MovieUpdateRequest;
import com.Tejas.MovieTicketBookingSystem.Enum.Genre;
import com.Tejas.MovieTicketBookingSystem.Enum.Language;
import com.Tejas.MovieTicketBookingSystem.Service.MovieService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/movie")
@Tag(name = "Movie Controller", description = "APIs for posting, fetching, update and delete movies")
public class MovieController {
    private final MovieService movieService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MovieResponse> postMovie(@Valid @RequestBody MovieRequest movieRequest){
        MovieResponse response = movieService.postMovie(movieRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MovieResponse> updateMovieProfile(@PathVariable Long id, @Valid @RequestBody MovieUpdateRequest request){
        MovieResponse response = movieService.updateMovieProfile(id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','THEATER_OWNER','CUSTOMER')")
    public ResponseEntity<List<MovieResponse>> fetchAllMovies(
            @RequestParam(required = false, defaultValue = "0")int page,
            @RequestParam(required = false, defaultValue = "5")int size
    ){
        List<MovieResponse> list = movieService.getAllMovie(page,size);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN','THEATER_OWNER','CUSTOMER')")
    public ResponseEntity<List<MovieResponse>> searchMovies(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Language language,
            @RequestParam(required = false)Genre genre,
            @RequestParam(required = false, defaultValue = "0")int page,
            @RequestParam(required = false, defaultValue = "5")int size
    ){
        List<MovieResponse> list = movieService.searchMovie(name,genre,language,page,size);
        return ResponseEntity.ok(list);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteMovieProfile(@PathVariable Long id){
        return new ResponseEntity<>(movieService.deleteMovie(id),HttpStatus.OK);
    }
    @GetMapping("/my-movies")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<MovieResponse>> getMyMovies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ){
        return ResponseEntity.ok(
                movieService.getMoviePostedByUser(page, size)
        );
    }
}
