package com.Tejas.MovieTicketBookingSystem.DTOs;

import com.Tejas.MovieTicketBookingSystem.Enum.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class UserRequest {
    @NotBlank(message = "Name can't be black")
    @Size(min = 2,message = "Minimum 2 characters required")
    private String name;

    @NotBlank
    @Email(message = "Enter valid email")
    private String email;

    @NotBlank
    @Size(min = 8, message = "Password at least contains 8 characters")
    private String password;

    @NotNull
    private Role role;
}
