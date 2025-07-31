// MovieController.java
package com.example.ss14.controller;

import com.example.ss14.model.dto.request.MovieRequest;
import com.example.ss14.model.dto.request.MovieUpdateRequest;
import com.example.ss14.model.dto.response.DataResponse;
import com.example.ss14.model.dto.response.MovieResponse;
import com.example.ss14.service.MovieService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    /**
     * GET /api/v1/movies - Xem danh sách phim (public)
     */
    @GetMapping("/movies")
    public ResponseEntity<DataResponse<List<MovieResponse>>> getAllMovies() {
        try {
            List<MovieResponse> movies = movieService.getAllMovies();
            DataResponse<List<MovieResponse>> response = DataResponse.<List<MovieResponse>>builder()
                    .code(200)
                    .key("success")
                    .data(movies)
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            DataResponse<List<MovieResponse>> response = DataResponse.<List<MovieResponse>>builder()
                    .code(500)
                    .key("error")
                    .data(null)
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * GET /api/v1/movies/{id} - Xem chi tiết phim (public)
     */
    @GetMapping("/movies/{id}")
    public ResponseEntity<DataResponse<MovieResponse>> getMovieById(@PathVariable Long id) {
        try {
            MovieResponse movie = movieService.getMovieById(id);
            DataResponse<MovieResponse> response = DataResponse.<MovieResponse>builder()
                    .code(200)
                    .key("success")
                    .data(movie)
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            DataResponse<MovieResponse> response = DataResponse.<MovieResponse>builder()
                    .code(404)
                    .key("not_found")
                    .data(null)
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    /**
     * GET /api/v1/movies/search - Tìm kiếm phim theo tên (public)
     */
    @GetMapping("/movies/search")
    public ResponseEntity<DataResponse<List<MovieResponse>>> searchMovies(@RequestParam String title) {
        try {
            List<MovieResponse> movies = movieService.searchMoviesByTitle(title);
            DataResponse<List<MovieResponse>> response = DataResponse.<List<MovieResponse>>builder()
                    .code(200)
                    .key("success")
                    .data(movies)
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            DataResponse<List<MovieResponse>> response = DataResponse.<List<MovieResponse>>builder()
                    .code(500)
                    .key("error")
                    .data(null)
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * POST /api/v1/admin/movies - Thêm phim (chỉ ADMIN)
     */
    @PostMapping("/admin/movies")
    public ResponseEntity<DataResponse<MovieResponse>> createMovie(@Valid @RequestBody MovieRequest movieRequest) {
        try {
            MovieResponse createdMovie = movieService.createMovie(movieRequest);
            DataResponse<MovieResponse> response = DataResponse.<MovieResponse>builder()
                    .code(201)
                    .key("created")
                    .data(createdMovie)
                    .build();
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            DataResponse<MovieResponse> response = DataResponse.<MovieResponse>builder()
                    .code(400)
                    .key("bad_request")
                    .data(null)
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            DataResponse<MovieResponse> response = DataResponse.<MovieResponse>builder()
                    .code(500)
                    .key("error")
                    .data(null)
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * PUT /api/v1/admin/movies/{id} - Sửa phim (chỉ ADMIN)
     */
    @PutMapping("/admin/movies/{id}")
    public ResponseEntity<DataResponse<MovieResponse>> updateMovie(
            @PathVariable Long id,
            @Valid @RequestBody MovieUpdateRequest movieUpdateRequest) {
        try {
            MovieResponse updatedMovie = movieService.updateMovie(id, movieUpdateRequest);
            DataResponse<MovieResponse> response = DataResponse.<MovieResponse>builder()
                    .code(200)
                    .key("updated")
                    .data(updatedMovie)
                    .build();
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            DataResponse<MovieResponse> response = DataResponse.<MovieResponse>builder()
                    .code(400)
                    .key("bad_request")
                    .data(null)
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            DataResponse<MovieResponse> response = DataResponse.<MovieResponse>builder()
                    .code(404)
                    .key("not_found")
                    .data(null)
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    /**
     * DELETE /api/v1/admin/movies/{id} - Xóa phim (chỉ ADMIN)
     */
    @DeleteMapping("/admin/movies/{id}")
    public ResponseEntity<DataResponse<String>> deleteMovie(@PathVariable Long id) {
        try {
            movieService.deleteMovie(id);
            DataResponse<String> response = DataResponse.<String>builder()
                    .code(200)
                    .key("deleted")
                    .data("Phim đã được xóa thành công")
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            DataResponse<String> response = DataResponse.<String>builder()
                    .code(404)
                    .key("not_found")
                    .data("Không tìm thấy phim để xóa")
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}