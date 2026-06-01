package com.Tejas.MovieTicketBookingSystem.ServiceImpl;

import com.Tejas.MovieTicketBookingSystem.DTOs.TheaterRequest;
import com.Tejas.MovieTicketBookingSystem.DTOs.TheaterResponse;
import com.Tejas.MovieTicketBookingSystem.DTOs.TheaterUpdateRequest;
import com.Tejas.MovieTicketBookingSystem.Entity.TheaterEntity;
import com.Tejas.MovieTicketBookingSystem.Entity.UserEntity;
import com.Tejas.MovieTicketBookingSystem.Enum.Role;
import com.Tejas.MovieTicketBookingSystem.Enum.TheaterStatus;
import com.Tejas.MovieTicketBookingSystem.Exceptions.InvalidSearchParameterException;
import com.Tejas.MovieTicketBookingSystem.Exceptions.ResourceNotFoundException;
import com.Tejas.MovieTicketBookingSystem.Exceptions.UnauthorizedException;
import com.Tejas.MovieTicketBookingSystem.Repository.TheaterRepository;
import com.Tejas.MovieTicketBookingSystem.Service.TheaterService;
import com.Tejas.MovieTicketBookingSystem.Service.UserService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import javax.management.BadAttributeValueExpException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TheaterServiceImplementation implements TheaterService {
    private  final TheaterRepository theaterRepository;

    private final UserService userServiceImplementation;

    private final Logger logger = LoggerFactory.getLogger(TheaterServiceImplementation.class);

    public UserEntity getLoggedInUser(){
        return userServiceImplementation.getLoggedInUser();
    }


    @Override
    public TheaterResponse createTheaterProfile(TheaterRequest request) {
        UserEntity user = getLoggedInUser();
        if(user.getRole() != Role.THEATER_OWNER){
            throw new UnauthorizedException("Only theater owners can create theater profiles");
        }
        TheaterEntity theaterEntity = mapToEntity(request);
        theaterEntity = theaterRepository.save(theaterEntity);
        logger.info("Theater profile is created with name: {} by {}",request.getName(),getLoggedInUser().getName());
        return mapToResponse(theaterEntity);
    }

    @Override
    public TheaterResponse updateTheaterProfile(Long id, TheaterUpdateRequest request) {
        logger.info("Attempting to update theater profile");
       UserEntity loggedInUser = getLoggedInUser();
       TheaterEntity theater = theaterRepository.findById(id)
               .orElseThrow(()->{
                   logger.warn("No theater exist with id: {}",id);
                   return  new ResourceNotFoundException("No theater exist with id: "+id);
               });
       if(loggedInUser.getId().equals(theater.getUserEntity().getId())){
           if(request.getName() != null)
               theater.setName(request.getName());
           if(request.getCity() != null)
               theater.setCity(request.getCity());
           if(request.getAddress() != null)
               theater.setAddress(request.getAddress());
           if(request.getState() != null)
               theater.setState(request.getState());
           theater = theaterRepository.save(theater);
           logger.info("Theater Profile is updated successfully with id: {}",id);
           return mapToResponse(theater);
       }else{
           logger.warn("Unauthorized user attempting to update theater with id : {}",id);
           throw new UnauthorizedException("Unauthorized user attempting to update theater with id :"+id);
       }
    }

    @Override
    public List<TheaterResponse> searchTheater(String name, String state, String city, Long id, int page, int size) {
        UserEntity userEntity = getLoggedInUser();
        Pageable pageable = PageRequest.of(page,size);
        if(name != null){
            logger.info("Fetching Theater of name: {}", name);
            Page<TheaterEntity> page1 = theaterRepository.findByNameContainingIgnoreCase(name,pageable);
            return page1.stream().map(this::mapToResponse).collect(Collectors.toList());
        }
        else if(state != null){
            logger.info("Fetching Theater in state: {}", state);
            Page<TheaterEntity> page1 = theaterRepository.findByStateContainingIgnoreCase(state,pageable);
            return page1.stream().map(this::mapToResponse).collect(Collectors.toList());
        }
        else if(city != null){
            logger.info("Fetching Theater in city: {}", city);
            Page<TheaterEntity> page1 = theaterRepository.findByCityContainingIgnoreCase(city,pageable);
            return page1.stream().map(this::mapToResponse).collect(Collectors.toList());
        }
        else if(id != null){
            if(userEntity.getRole().equals(Role.ADMIN) || userEntity.getRole().equals(Role.THEATER_OWNER)){
                TheaterEntity theater = theaterRepository.findById(id)
                        .orElseThrow(()->{
                            logger.info("No theatre exist with id: {}", id);
                            return new ResourceNotFoundException("No theater exist with id: "+id);
                        });
                return List.of(mapToResponse(theater));
            }else{
                logger.warn("User are not allowed to fetch theater using id with role CUSTOMER");
                throw new UnauthorizedException("You are not allowed to fetch theater using id");
            }
        }else{
            logger.warn("Provide valid search parameter for fetching user");
            throw new InvalidSearchParameterException("Provide valid search parameter");
        }
    }

    @Override
    public List<TheaterResponse> getAllTheater(int page, int size) {
        logger.info("Fetching all registered theater profiles");
        Pageable pageable = (Pageable) PageRequest.of(page,size);
        Page<TheaterEntity> page1 = theaterRepository.findAll(pageable);
        return page1.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Override
    public TheaterResponse updateTheaterStatus(Long id, TheaterStatus status) {
        logger.info("Attempting to update theater profile status");
        if(getLoggedInUser().getRole() == Role.ADMIN) {
            TheaterEntity theater = theaterRepository.findById(id)
                    .orElseThrow(() -> {
                        logger.warn("No theater exist with id: {}", id);
                        return new ResourceNotFoundException("No theater exist with id: " + id);
                    });
            if (theater.getStatus() != status) {
                theater.setStatus(status);
                theater = theaterRepository.save(theater);
                logger.info("Theater profile status update to {} for theater id: {}", theater.getStatus(), id);
                return mapToResponse(theater);
            } else {
                logger.warn("Theater profile status unable to update to PENDING again");
                throw new InvalidSearchParameterException("Theater already has status " + status);
            }
        }else{
            logger.warn("Unauthorized user attempting to update theater profile status");
            throw new UnauthorizedException("Unauthorized user attempting to update theater profile status");
        }
    }

    @Override
    public String deleteTheaterProfile(Long id) {
        logger.info("Attempting to delete theater profile");
        if(getLoggedInUser().getRole() == Role.ADMIN) {
            TheaterEntity theater = theaterRepository.findById(id)
                    .orElseThrow(() -> {
                        logger.warn("No theater exist with id: {}", id);
                        return new ResourceNotFoundException("No theater exist with id: " + id);
                    });
            theaterRepository.deleteById(id);
            logger.info("Theater profile with id: {} deleted successfully",id);
            return "Theater profile delete successfully";
        }else{
            logger.warn("Unauthorized user attempting to delete theater profile status");
            throw new UnauthorizedException("Unauthorized user attempting to update theater profile status");
        }
    }

    @Override
    public TheaterEntity mapToEntity(TheaterRequest request) {
        return TheaterEntity.builder()
                .name(request.getName())
                .city(request.getCity())
                .state(request.getState())
                .address(request.getAddress())
                .userEntity(getLoggedInUser())
                .build();
    }

    @Override
    public TheaterResponse mapToResponse(TheaterEntity entity) {
        return TheaterResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .city(entity.getCity())
                .state(entity.getState())
                .address(entity.getAddress())
                .owner(entity.getUserEntity().getName())
                .status(entity.getStatus())
                .build();
    }
}
