package com.Tejas.MovieTicketBookingSystem.DTOs;

import com.Tejas.MovieTicketBookingSystem.Enum.Genre;
import com.Tejas.MovieTicketBookingSystem.Enum.Language;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovieRequest {
    @NotBlank(message = "Movie name can't be blank")
    @Size(min = 2,message = "Movie name at least contains 2 characters")
    private String name;

    @NotBlank(message = "Movie description can't be blank")
    @Size(min = 15, max = 500 ,message = "Movie name at least contains 2 characters")
    private String description;

    @NotBlank(message = "Movie release date required")
    @Future(message = "Release date should be in future")
    private LocalDate releaseDate;

    @NotNull(message = "duration required")
    private Integer duration;

    @NotNull(message = "Language can't be null")
    private Language language;

    @NotBlank(message = "Genre required")
    private Genre genre;
}
