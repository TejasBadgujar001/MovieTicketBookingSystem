package com.Tejas.MovieTicketBookingSystem.ServiceImpl;

import com.Tejas.MovieTicketBookingSystem.Entity.UserEntity;
import com.Tejas.MovieTicketBookingSystem.Exceptions.UserNotFoundException;
import com.Tejas.MovieTicketBookingSystem.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;
    private final Logger logger = LoggerFactory.getLogger(CustomUserDetailService.class);

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       UserEntity userEntity = userRepository.findByEmail(username)
               .orElseThrow(()->{
                   logger.warn("User not exist for username: {}",username);
                   return new UserNotFoundException("User not exist with username: "+username);
               });
       return User.builder()
               .username(userEntity.getEmail())
               .password(userEntity.getPassword())
               .authorities(new SimpleGrantedAuthority("ROLE"+userEntity.getRole().name()))
               .build();
    }
}
