package com.Tejas.MovieTicketBookingSystem.ServiceImpl;

import com.Tejas.MovieTicketBookingSystem.DTOs.AuthDto;
import com.Tejas.MovieTicketBookingSystem.DTOs.UserRequest;
import com.Tejas.MovieTicketBookingSystem.DTOs.UserResponse;
import com.Tejas.MovieTicketBookingSystem.DTOs.UserUpdateRequest;
import com.Tejas.MovieTicketBookingSystem.Entity.UserEntity;
import com.Tejas.MovieTicketBookingSystem.Exceptions.InvalidSearchParameterException;
import com.Tejas.MovieTicketBookingSystem.Exceptions.UnauthorizedException;
import com.Tejas.MovieTicketBookingSystem.Exceptions.UserNotFoundException;
import com.Tejas.MovieTicketBookingSystem.Repository.UserRepository;
import com.Tejas.MovieTicketBookingSystem.Service.UserService;
import com.Tejas.MovieTicketBookingSystem.Util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;

@Service
@RequiredArgsConstructor
public class UserServiceImplementation implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final Logger logger = LoggerFactory.getLogger(UserServiceImplementation.class);

    @Override
    public UserResponse registerUser(UserRequest request) {
        UserEntity userEntity = mapToEntity(request);
        userEntity = userRepository.save(userEntity);
        logger.info("User with email: {} is saved successfully ",request.getEmail());
        return mapToResponse(userEntity);
    }

    @Override
    public List<UserResponse> searchUser(String email, String name, Long id,int page, int size) {
        Pageable pageable = PageRequest.of(page,size);
        if(name != null){
            Page<UserEntity> list = userRepository.findByNameContainingIgnoreCase(name,pageable);
            logger.info("fetching user with name: {}", name);
            return list.stream()
                    .map(this::mapToResponse)
                    .toList();
        }
        if(id != null){
            UserEntity userEntity= userRepository.findById(id)
                    .orElseThrow(()-> {
                        return new UserNotFoundException("User not exist for id: "+id);
                    });
            logger.info("fetching user with id: {}", id);
            return List.of(mapToResponse(userEntity));
        }
        if(email != null){
            UserEntity userEntity= userRepository.findByEmail(email)
                    .orElseThrow(()->{
                        return new UserNotFoundException("User not exist for email: "+email);
                    });
            logger.info("fetching user with email: {}", email);
            return List.of(mapToResponse(userEntity));
        }
        logger.warn("Provide valid search parameter for fetching user");
        throw new InvalidSearchParameterException("Provide valid search parameter");
    }

    @Override
    public Map<String, Object> authenticateAndGenerateToken(AuthDto authDto) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authDto.getEmail(), authDto.getPassword()));
            String token = jwtUtil.generateToken(authDto.getEmail());
            logger.info("Logging user with email id: {} "+authDto.getEmail());
            return Map.of(
                    "Token" , token,
                    "User" , getPublicProfile(authDto.getEmail())
            );
        }catch (Exception e){
            logger.warn("Authentication failed for user :{}.",authDto.getEmail());
            throw new UnauthorizedException("Invalid email or password");
        }
    }

    @Override
    public UserResponse getPublicProfile(String email) {
        UserEntity entity = userRepository.findByEmail(email)
                .orElseThrow(()-> new UserNotFoundException("User not exist for email: "+email));
        return mapToResponse(entity);
    }

    @Override
    public UserEntity getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(()-> new UserNotFoundException("User not exist for email: "+email));
    }

    @Override
    public UserResponse updateUserProfile(Long id, UserUpdateRequest request) {
        logger.info("Attempting to update user with id:{}", id);
        UserEntity existingUser = userRepository.findById(id)
                .orElseThrow(()->{
                    logger.warn("User not exist for id: {}",id);
                    return new UserNotFoundException("No User exist for id: "+id);
                });
        UserEntity entity = getLoggedInUser();
        if(entity.getId().equals(id)){
            if(request.getName() != null)
                entity.setName(request.getName());
            if(request.getEmail() !=null)
                entity.setEmail(request.getEmail());
            if(request.getPassword() != null)
                entity.setPassword(passwordEncoder.encode(request.getPassword()));
            userRepository.save(entity);
            logger.info("user updated successfully for id: {}", id);
            return mapToResponse(entity);
        }else{
            logger.warn("Unauthorized profile trying to update user with id: {}", id);
            throw new UnauthorizedException("Unauthorized profile try to edit user profile with id: "+id);
        }
    }

    @Override
    public String deleteUseProfile(Long id) {
        logger.info("Attempting to delete user with id:{}", id);
        UserEntity existingUser = userRepository.findById(id)
                .orElseThrow(()->{
                    logger.warn("User not exist for id: {}",id);
                    return new UserNotFoundException("No User exist for id: "+id);
                });
        UserEntity userEntity = getLoggedInUser();
        if(userEntity.getId().equals(id)){
            userRepository.deleteById(id);
            logger.info("user deleted successfully for id: {}", id);
            return "User deleted successfully.";
        }else {
            logger.warn("Unauthorized profile trying to delete user with id: {}", id);
            throw new UnauthorizedException("Unauthorized profile try to edit user profile with id: "+id);
        }
    }

    @Override
    public UserResponse mapToResponse(UserEntity entity) {
        return UserResponse.builder()
                .name(entity.getName())
                .email(entity.getEmail())
                .id(entity.getId())
                .role(entity.getRole())
                .build();
    }

    @Override
    public UserEntity mapToEntity(UserRequest request) {
        return UserEntity.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();
    }
}
