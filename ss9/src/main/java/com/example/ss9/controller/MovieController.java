package com.example.ss9.controller;

import com.example.ss9.exception.NotFoundException;
import com.example.ss9.model.dto.response.DataResponse;
import com.example.ss9.model.entity.Movie;
import com.example.ss9.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/movies")
public class MovieController {
    private final MovieService movieService;

    @PostMapping
    public ResponseEntity<DataResponse<Movie>> createMovie(@RequestBody Movie movie) {
        return new ResponseEntity<>(movieService.createMovie(movie), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DataResponse<Movie>> updateMovie(@PathVariable Long id, @RequestBody Movie movie) throws NotFoundException {
        return new ResponseEntity<>(movieService.updateMovie(id, movie), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) throws NotFoundException {
        movieService.deleteMovie(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<DataResponse<List<Movie>>> getMovies(@RequestParam(required = false) String searchMovie) {
        return new ResponseEntity<>(movieService.getMovies(searchMovie), HttpStatus.OK);
    }
}