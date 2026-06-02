package com.Tejas.MovieTicketBookingSystem.Controller;

import com.Tejas.MovieTicketBookingSystem.DTOs.AuthDto;
import com.Tejas.MovieTicketBookingSystem.DTOs.UserRequest;
import com.Tejas.MovieTicketBookingSystem.DTOs.UserResponse;
import com.Tejas.MovieTicketBookingSystem.DTOs.UserUpdateRequest;
import com.Tejas.MovieTicketBookingSystem.Service.UserService;
import com.Tejas.MovieTicketBookingSystem.ServiceImpl.UserServiceImplementation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "User Controller", description = "APIs for user management, authentication and profile operations")
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody UserRequest request){
        UserResponse userResponse = userService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String,Object>> login(@RequestBody AuthDto authDto){
        Map<String,Object> loginResult = userService.authenticateAndGenerateToken(authDto);
        return ResponseEntity.status(HttpStatus.OK).body(loginResult);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/search")
    public ResponseEntity<List<UserResponse>> searchUser(
            @RequestParam(required = false)String name,
            @RequestParam(required = false)String email,
            @RequestParam(required = false)Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ){
        List<UserResponse> userResponseList = userService.searchUser(email,name,id,page,size);
        return ResponseEntity.status(HttpStatus.OK).body(userResponseList);
    }

    @PreAuthorize("hasAnyRole('ADMIN,'THEATER_OWNER','CUSTOMER')")
    @PatchMapping("/{id}")
    public ResponseEntity<UserResponse> editUserProfile(@PathVariable Long id,@RequestBody UserUpdateRequest request){
        UserResponse userResponse = userService.updateUserProfile(id,request);
        return ResponseEntity.status(HttpStatus.OK).body(userResponse);
    }

    @PreAuthorize("hasAnyRole('ADMIN,'THEATER_OWNER','CUSTOMER')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> removeUserProfile(@PathVariable Long id){
        String message = userService.deleteUseProfile(id);
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }
}
