package com.Tejas.MovieTicketBookingSystem.Security;

import com.Tejas.MovieTicketBookingSystem.ServiceImpl.CustomUserDetailService;
import com.Tejas.MovieTicketBookingSystem.Util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final CustomUserDetailService customUserDetailService;
    private final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String path = request.getServletPath();

        if(path.equals("/user/login") || path.equals("/user/register") || path.equals("/theater/search")|| path.equals("/movie")||path.equals("/movie/search")){
            logger.info("Public endpoint accessed: {}", path);
            filterChain.doFilter(request,response);
            return;
        }

        String email = null;
        String token = null;

        String authHeader = request.getHeader("Authorization");
        if(email == null && authHeader.startsWith("Bearer")){
            token = authHeader.substring(7);
            email = jwtUtil.extractUsername(token);
            logger.info("JWT token extracted successfully for user: {}", email);
        }else{
            logger.warn("Authorization header missing or invalid");
        }


        if(email != null && SecurityContextHolder.getContext().getAuthentication()==null){
            UserDetails userDetails = customUserDetailService.loadUserByUsername(email);
            if(jwtUtil.validateToken(token,userDetails)){
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                logger.info("User authenticated successfully with email: {}",email);
            }else{
                logger.warn("JWT token validation failed for user: {}", email);
            }
            filterChain.doFilter(request,response);
        }

    }
}
