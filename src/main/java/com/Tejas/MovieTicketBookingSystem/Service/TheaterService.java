package com.Tejas.MovieTicketBookingSystem.Service;

import com.Tejas.MovieTicketBookingSystem.DTOs.TheaterRequest;
import com.Tejas.MovieTicketBookingSystem.DTOs.TheaterResponse;
import com.Tejas.MovieTicketBookingSystem.DTOs.TheaterUpdateRequest;
import com.Tejas.MovieTicketBookingSystem.Entity.TheaterEntity;
import com.Tejas.MovieTicketBookingSystem.Enum.TheaterStatus;

import java.util.List;

public interface TheaterService {
    public TheaterResponse createTheaterProfile(TheaterRequest request);

    public TheaterResponse updateTheaterProfile(Long id, TheaterUpdateRequest request);

    public List<TheaterResponse> searchTheater(String name, String state, String city, Long id,int page, int size);

    public List<TheaterResponse> getAllTheater(int page, int size);

    public TheaterResponse updateTheaterStatus(Long id , TheaterStatus status);

    public String deleteTheaterProfile(Long id);

    public TheaterEntity mapToEntity(TheaterRequest request);

    public TheaterResponse mapToResponse(TheaterEntity entity);
}
