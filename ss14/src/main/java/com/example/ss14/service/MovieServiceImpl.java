package com.example.ss14.service;

import com.example.ss14.model.dto.request.MovieRequest;
import com.example.ss14.model.dto.request.MovieUpdateRequest;
import com.example.ss14.model.dto.response.MovieResponse;
import com.example.ss14.model.entity.Movie;
import com.example.ss14.repository.MovieRepository;
import com.example.ss14.service.MovieService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;

    @Override
    @Transactional(readOnly = true)
    public List<MovieResponse> getAllMovies() {
        return movieRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public MovieResponse getMovieById(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy phim với ID: " + id));
        return convertToResponse(movie);
    }

    @Override
    public MovieResponse createMovie(MovieRequest movieRequest) {
        // Kiểm tra trùng tên phim
        if (movieRepository.existsByTitleIgnoreCase(movieRequest.getTitle())) {
            throw new IllegalArgumentException("Phim với tên '" + movieRequest.getTitle() + "' đã tồn tại");
        }

        Movie movie = Movie.builder()
                .title(movieRequest.getTitle())
                .description(movieRequest.getDescription())
                .duration(movieRequest.getDuration())
                .releaseDate(movieRequest.getReleaseDate())
                .build();

        Movie savedMovie = movieRepository.save(movie);
        return convertToResponse(savedMovie);
    }

    @Override
    public MovieResponse updateMovie(Long id, MovieUpdateRequest movieUpdateRequest) {
        Movie existingMovie = movieRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy phim với ID: " + id));

        // Kiểm tra trùng tên phim (trừ chính nó)
        if (!existingMovie.getTitle().equalsIgnoreCase(movieUpdateRequest.getTitle()) &&
                movieRepository.existsByTitleIgnoreCase(movieUpdateRequest.getTitle())) {
            throw new IllegalArgumentException("Phim với tên '" + movieUpdateRequest.getTitle() + "' đã tồn tại");
        }

        existingMovie.setTitle(movieUpdateRequest.getTitle());
        existingMovie.setDescription(movieUpdateRequest.getDescription());
        existingMovie.setDuration(movieUpdateRequest.getDuration());
        existingMovie.setReleaseDate(movieUpdateRequest.getReleaseDate());

        Movie updatedMovie = movieRepository.save(existingMovie);
        return convertToResponse(updatedMovie);
    }

    @Override
    public void deleteMovie(Long id) {
        if (!movieRepository.existsById(id)) {
            throw new EntityNotFoundException("Không tìm thấy phim với ID: " + id);
        }
        movieRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovieResponse> searchMoviesByTitle(String title) {
        return movieRepository.findByTitleContainingIgnoreCase(title)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // Helper method để convert Entity sang Response DTO
    private MovieResponse convertToResponse(Movie movie) {
        return MovieResponse.builder()
                .id(movie.getId())
                .title(movie.getTitle())
                .description(movie.getDescription())
                .duration(movie.getDuration())
                .releaseDate(movie.getReleaseDate())
                .build();
    }
}