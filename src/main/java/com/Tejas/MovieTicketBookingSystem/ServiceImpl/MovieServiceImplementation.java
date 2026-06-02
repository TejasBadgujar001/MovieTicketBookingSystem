package com.Tejas.MovieTicketBookingSystem.ServiceImpl;

import com.Tejas.MovieTicketBookingSystem.DTOs.MovieRequest;
import com.Tejas.MovieTicketBookingSystem.DTOs.MovieResponse;
import com.Tejas.MovieTicketBookingSystem.DTOs.MovieUpdateRequest;
import com.Tejas.MovieTicketBookingSystem.Entity.MovieEntity;
import com.Tejas.MovieTicketBookingSystem.Entity.UserEntity;
import com.Tejas.MovieTicketBookingSystem.Enum.Genre;
import com.Tejas.MovieTicketBookingSystem.Enum.Language;
import com.Tejas.MovieTicketBookingSystem.Exceptions.InvalidSearchParameterException;
import com.Tejas.MovieTicketBookingSystem.Exceptions.ResourceNotFoundException;
import com.Tejas.MovieTicketBookingSystem.Exceptions.UnauthorizedException;
import com.Tejas.MovieTicketBookingSystem.Repository.MovieRepository;
import com.Tejas.MovieTicketBookingSystem.Service.MovieService;
import com.Tejas.MovieTicketBookingSystem.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieServiceImplementation implements MovieService {

    private final MovieRepository movieRepository;
    private final UserService userServiceImplementation;
    private final Logger logger = LoggerFactory.getLogger(MovieServiceImplementation.class);

    public UserEntity getLoggedInUser(){
        return userServiceImplementation.getLoggedInUser();
    }

    @Override
    public MovieResponse postMovie(MovieRequest request) {
        MovieEntity entity = mapToEntity(request);
        entity.setUserEntity(getLoggedInUser());
        entity = movieRepository.save(entity);
        return mapToResponse(entity);
    }

    @Override
    public List<MovieResponse> searchMovie(String name, Genre genre, Language language, int page, int size) {
        Pageable pageable = PageRequest.of(page,size);
        if(name != null){
            MovieEntity movieEntity = movieRepository.findByNameIgnoreCase(name)
                    .orElseThrow(()->{
                        return new ResourceNotFoundException("No movie found with name :"+name);
                    });
            return List.of(mapToResponse(movieEntity));
        }
        else if(genre != null){
            Page<MovieEntity> page1 = movieRepository.findByGenre(genre,pageable);
            return page1.stream().map(this::mapToResponse).collect(Collectors.toList());
        }
        else if(language != null){
            Page<MovieEntity> page1 = movieRepository.findByLanguage(language,pageable);
            return page1.stream().map(this::mapToResponse).collect(Collectors.toList());
        }else{
            throw new InvalidSearchParameterException("Invalid search parameter");
        }
    }

    @Override
    public List<MovieResponse> getAllMovie(int page, int size) {
        Pageable pageable = PageRequest.of(page,size);
        Page<MovieEntity> page1 = movieRepository.findAll(pageable);
        return page1.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Override
    public MovieResponse updateMovieProfile(Long id, MovieUpdateRequest request) {
        UserEntity userEntity = getLoggedInUser();
        MovieEntity movieEntity = movieRepository.findById(id)
                .orElseThrow(()->{
                    return new ResourceNotFoundException("No movie found with id: "+id);
                });
        if(userEntity.getId().equals(movieEntity.getUserEntity().getId())) {
            if (request.getName() != null)
                movieEntity.setName(request.getName());
            if (request.getDescription() != null)
                movieEntity.setDescription(request.getDescription());
            if (request.getDuration() != null)
                movieEntity.setDuration(request.getDuration());
            if (request.getGenre() != null)
                movieEntity.setGenre(request.getGenre());
            if (request.getLanguage() != null)
                movieEntity.setLanguage(request.getLanguage());
            if (request.getReleaseDate() != null)
                movieEntity.setReleaseDate(request.getReleaseDate());
            movieRepository.save(movieEntity);
            return mapToResponse(movieEntity);
        }else {
            throw new UnauthorizedException("Unauthorised user attempting to update movie profile with id: "+ id);
        }
    }

    @Override
    public String deleteMovie(Long id) {
        UserEntity userEntity = getLoggedInUser();
        MovieEntity movieEntity = movieRepository.findById(id)
                .orElseThrow(()->{
                    return new ResourceNotFoundException("No movie found with id: "+id);
                });
        if(userEntity.getId().equals(movieEntity.getUserEntity().getId())){
            movieRepository.deleteById(id);
            return "Movie deleted successfully.";
        }else {
            throw new UnauthorizedException("Unauthorised user attempting to delete movie profile with id: "+ id);
        }
    }


    @Override
    public MovieResponse mapToResponse(MovieEntity entity) {
       return MovieResponse.builder()
               .id(entity.getId())
               .name(entity.getName())
               .description(entity.getDescription())
               .duration(entity.getDuration())
               .releaseDate(entity.getReleaseDate())
               .postedBy(entity.getUserEntity().getName())
               .language(entity.getLanguage())
               .genre(entity.getGenre())
               .build();
    }

    @Override
    public MovieEntity mapToEntity(MovieRequest request) {
        return MovieEntity.builder()
                .name(request.getName())
                .description(request.getDescription())
                .releaseDate(request.getReleaseDate())
                .duration(request.getDuration())
                .language(request.getLanguage())
                .genre(request.getGenre())
                .build();
    }
}
