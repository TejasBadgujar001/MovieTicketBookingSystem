package com.Tejas.MovieTicketBookingSystem.DTOs;

import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class TheaterUpdateRequest {

    @Size(min = 2,message = "Minimum 2 characters required")
    private String name;

    @Size(min = 2,message = "Minimum 2 characters required")
    private String city;

    @Size(min = 3,message = "Minimum 3 characters required")
    private String state;

    @Size(min = 15,message = "Minimum 15 characters required")
    private String address;
}
