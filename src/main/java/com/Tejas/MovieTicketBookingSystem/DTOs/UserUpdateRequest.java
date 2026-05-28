package com.Tejas.MovieTicketBookingSystem.DTOs;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class UserUpdateRequest {
    private String name;
    private String email;
    private String password;
}
