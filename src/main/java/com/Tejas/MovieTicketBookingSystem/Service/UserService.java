package com.Tejas.MovieTicketBookingSystem.Service;

import com.Tejas.MovieTicketBookingSystem.DTOs.AuthDto;
import com.Tejas.MovieTicketBookingSystem.DTOs.UserRequest;
import com.Tejas.MovieTicketBookingSystem.DTOs.UserResponse;
import com.Tejas.MovieTicketBookingSystem.DTOs.UserUpdateRequest;
import com.Tejas.MovieTicketBookingSystem.Entity.UserEntity;

import java.util.List;
import java.util.Map;

public interface UserService {

    public UserResponse registerUser(UserRequest request);

    public List<UserResponse> searchUser(String email, String name, Long id ,int page,int size);

    public Map<String, Object> authenticateAndGenerateToken(AuthDto authDto);

    public UserResponse getPublicProfile(String email);

    public UserEntity getLoggedInUser();

    public UserResponse updateUserProfile(Long id, UserUpdateRequest request);

    public String deleteUseProfile(Long id);

    public UserResponse mapToResponse(UserEntity entity);

    public UserEntity mapToEntity(UserRequest request);
}
