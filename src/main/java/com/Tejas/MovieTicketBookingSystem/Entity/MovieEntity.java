package com.Tejas.MovieTicketBookingSystem.Entity;

import com.Tejas.MovieTicketBookingSystem.Enum.Genre;
import com.Tejas.MovieTicketBookingSystem.Enum.Language;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;


@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_movie")
@Builder
public class MovieEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private LocalDate releaseDate;

    @Enumerated(EnumType.STRING)
    private Language language;

    private Integer duration;

    @Enumerated(EnumType.STRING)
    private Genre genre;

    @ManyToOne
    @JoinColumn(name = "posted_by")
    private UserEntity userEntity;
}
