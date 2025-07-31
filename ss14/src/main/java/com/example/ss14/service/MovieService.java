package com.example.ss14.service;

import com.example.ss14.model.dto.request.MovieRequest;
import com.example.ss14.model.dto.request.MovieUpdateRequest;
import com.example.ss14.model.dto.response.MovieResponse;

import java.util.List;

public interface MovieService {
    List<MovieResponse> getAllMovies();
    MovieResponse getMovieById(Long id);
    MovieResponse createMovie(MovieRequest movieRequest);
    MovieResponse updateMovie(Long id, MovieUpdateRequest movieUpdateRequest);
    void deleteMovie(Long id);
    List<MovieResponse> searchMoviesByTitle(String title);
}