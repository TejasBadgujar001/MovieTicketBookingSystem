package com.Tejas.MovieTicketBookingSystem.DTOs;

import com.Tejas.MovieTicketBookingSystem.Enum.Genre;
import com.Tejas.MovieTicketBookingSystem.Enum.Language;
import lombok.*;

import java.time.LocalDate;

@Setter
@AllArgsConstructor
@Getter
@NoArgsConstructor
@Builder
public class MovieResponse {
    private Long id;

    private String name;

    private String description;

    private LocalDate releaseDate;

    private Integer duration;

    private Genre genre;

    private Language language;

    private String postedBy;
}
