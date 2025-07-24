package com.example.ss9.service;

import com.example.ss9.exception.NotFoundException;
import com.example.ss9.model.dto.response.DataResponse;
import com.example.ss9.model.entity.Movie;
import com.example.ss9.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MovieService {
    private final MovieRepository movieRepository;

    @Transactional
    public DataResponse<Movie> createMovie(Movie movie) {
        Movie savedMovie = movieRepository.save(movie);
        log.debug("Ten phim: {}, Thoi luong: {}", savedMovie.getTitle(), savedMovie.getReleaseDate());
        return DataResponse.<Movie>builder()
                .key("movie")
                .data(savedMovie)
                .build();
    }

    public DataResponse<Movie> updateMovie(Long id, Movie movie) throws NotFoundException {
        if (!movieRepository.existsById(id)) {
            log.error("Movie with id {} not found", id);
            throw new NotFoundException("Không tim thấy phim với id: " + id);
        }
        movie.setId(id);
        log.debug("Movie to update: {}", movieRepository.findById(id).get());
        Movie updatedMovie = movieRepository.save(movie);
        log.debug("Updated movie: {}", updatedMovie);
        return DataResponse.<Movie>builder()
                .key("movie")
                .data(updatedMovie)
                .build();
    }

    public void deleteMovie(Long id) throws NotFoundException {
        if (!movieRepository.existsById(id)) {
            log.error("Movie with id {} not found", id);
            throw new NotFoundException("Không tìm thấy phim với id: " + id);
        }
        movieRepository.deleteById(id);
        log.info("Deleted movie with id {}", id);
    }

    public DataResponse<List<Movie>> getMovies(String searchMovie) {
        List<Movie> movies;
        if (searchMovie != null && !searchMovie.trim().isEmpty()) {
            movies = movieRepository.findByTitleContainingIgnoreCase(searchMovie.trim());
            log.info("\u001B[36m🎬 MOVIE SEARCH ANALYTICS - Search Term: '{}', Results Found: {}",
                    searchMovie, movies.size());
        } else {
            // Lấy tất cả phim khi không có từ khóa tìm kiếm
            movies = movieRepository.findAll();
            log.info("\u001B[36m🎬 MOVIE LIST ANALYTICS - Total Movies: {}",
                    movies.size());
        }
        return DataResponse.<List<Movie>>builder()
                .key("movies")
                .data(movies)
                .build();
    }
}