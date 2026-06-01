package com.Tejas.MovieTicketBookingSystem.DTOs;

import com.Tejas.MovieTicketBookingSystem.Enum.Genre;
import com.Tejas.MovieTicketBookingSystem.Enum.Language;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Setter
@AllArgsConstructor
@Getter
@NoArgsConstructor
@Builder
public class MovieUpdateRequest {
    @Size(min = 2,message = "Movie name at least contains 2 characters")
    private String name;

    @Size(min = 15, max = 500 ,message = "Movie name at least contains 2 characters")
    private String description;


    private LocalDate releaseDate;

    private Integer duration;

    private Genre genre;

    private Language language;
}
