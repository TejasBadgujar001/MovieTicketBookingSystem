package com.Tejas.MovieTicketBookingSystem.Controller;

import com.Tejas.MovieTicketBookingSystem.DTOs.TheaterRequest;
import com.Tejas.MovieTicketBookingSystem.DTOs.TheaterResponse;
import com.Tejas.MovieTicketBookingSystem.DTOs.TheaterUpdateRequest;
import com.Tejas.MovieTicketBookingSystem.Enum.TheaterStatus;
import com.Tejas.MovieTicketBookingSystem.Service.TheaterService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/theater")
@Tag(name = "Theater Controller", description = "APIs for Theater creation, management and deletion")
public class TheaterController {
    private final TheaterService theaterService;

    @PreAuthorize("hasRole('THEATER_OWNER')")
    @PostMapping()
    public ResponseEntity<TheaterResponse> createTheaterProfile(@Valid @RequestBody TheaterRequest request){
        TheaterResponse response = theaterService.createTheaterProfile(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('THEATER_OWNER','ADMIN','CUSTOMER')")
    @GetMapping()
    public ResponseEntity<List<TheaterResponse>> searchTheaterProfile(
            @RequestParam(required = false)String name,
            @RequestParam(required = false)String state,
            @RequestParam(required = false)String city,
            @RequestParam(required = false)Long id,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "5") @Min(0) @Max(50) int size
            ){
        List<TheaterResponse> theaterResponses = theaterService.searchTheater(name,state,city,id,page,size);
        return ResponseEntity.ok(theaterResponses);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<TheaterResponse>> fetchAllTheaterProfile(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ){
        return ResponseEntity.ok(theaterService.getAllTheater(page, size));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/status/{id}")
    public ResponseEntity<TheaterResponse> updateTheaterProfileStatus(
            @PathVariable Long id,
            @RequestParam TheaterStatus status
    ){
        return ResponseEntity.ok(theaterService.updateTheaterStatus(id,status));
    }

    @PreAuthorize("hasRole('THEATER_OWNER')")
    @PatchMapping("/{id}")
    public ResponseEntity<TheaterResponse> updateTheaterProfile(
            @PathVariable Long id,
            @Valid @RequestBody TheaterUpdateRequest request
    ){
        return ResponseEntity.ok(theaterService.updateTheaterProfile(id,request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTheaterProfile(@PathVariable Long id){
        return ResponseEntity.ok(theaterService.deleteTheaterProfile(id));
    }
}
