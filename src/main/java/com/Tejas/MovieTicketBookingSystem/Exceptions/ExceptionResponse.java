package com.Tejas.MovieTicketBookingSystem.Exceptions;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class ExceptionResponse {
    private LocalDateTime timeStamp;
    private int statusCode;
    private String message;
    private String error;
}
