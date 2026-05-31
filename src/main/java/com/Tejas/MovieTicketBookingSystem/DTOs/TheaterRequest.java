package com.Tejas.MovieTicketBookingSystem.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class TheaterRequest {

    @NotBlank(message = "Theater name can't blank")
    @Size(min = 2,message = "Minimum 2 characters required")
    private String name;

    @NotBlank(message = "Theater name can't blank")
    @Size(min = 2,message = "Minimum 2 characters required")
    private String city;

    @NotBlank(message = "Theater name can't blank")
    @Size(min = 3,message = "Minimum 3 characters required")
    private String  state;

    @NotBlank(message = "Theater name can't blank")
    @Size(min = 15,message = "Minimum 15 characters required")
    private String address;
}
