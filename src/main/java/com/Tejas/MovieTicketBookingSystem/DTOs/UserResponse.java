package com.Tejas.MovieTicketBookingSystem.DTOs;

import com.Tejas.MovieTicketBookingSystem.Enum.Role;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class UserResponse {

    private Long id;

    private String name;

    private String email;

    private Role role;
}
