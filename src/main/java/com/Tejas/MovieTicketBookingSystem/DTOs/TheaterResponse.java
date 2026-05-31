package com.Tejas.MovieTicketBookingSystem.DTOs;

import com.Tejas.MovieTicketBookingSystem.Enum.TheaterStatus;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class TheaterResponse {

    private Long id;

    private String name;

    private String city;

    private String address;

    private String owner;

    private String state;

    private TheaterStatus status;
}
