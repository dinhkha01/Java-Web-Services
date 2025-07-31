// ShowtimeController.java
package com.example.ss14.controller;

import com.example.ss14.model.dto.request.ShowtimeRequest;
import com.example.ss14.model.dto.request.ShowtimeUpdateRequest;
import com.example.ss14.model.dto.response.DataResponse;
import com.example.ss14.model.dto.response.ShowtimeResponse;
import com.example.ss14.model.dto.response.ShowtimeSimpleResponse;
import com.example.ss14.service.ShowtimeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ShowtimeController {

    private final ShowtimeService showtimeService;

    /**
     * GET /api/v1/showtimes - Xem lịch chiếu (public)
     */
    @GetMapping("/showtimes")
    public ResponseEntity<DataResponse<List<ShowtimeResponse>>> getAllShowtimes() {
        try {
            List<ShowtimeResponse> showtimes = showtimeService.getAllShowtimes();
            DataResponse<List<ShowtimeResponse>> response = DataResponse.<List<ShowtimeResponse>>builder()
                    .code(200)
                    .key("success")
                    .data(showtimes)
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            DataResponse<List<ShowtimeResponse>> response = DataResponse.<List<ShowtimeResponse>>builder()
                    .code(500)
                    .key("error")
                    .data(null)
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * GET /api/v1/showtimes/simple - Xem lịch chiếu đơn giản (public)
     */
    @GetMapping("/showtimes/simple")
    public ResponseEntity<DataResponse<List<ShowtimeSimpleResponse>>> getAllShowtimesSimple() {
        try {
            List<ShowtimeSimpleResponse> showtimes = showtimeService.getAllShowtimesSimple();
            DataResponse<List<ShowtimeSimpleResponse>> response = DataResponse.<List<ShowtimeSimpleResponse>>builder()
                    .code(200)
                    .key("success")
                    .data(showtimes)
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            DataResponse<List<ShowtimeSimpleResponse>> response = DataResponse.<List<ShowtimeSimpleResponse>>builder()
                    .code(500)
                    .key("error")
                    .data(null)
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * GET /api/v1/showtimes/{id} - Xem chi tiết lịch chiếu (public)
     */
    @GetMapping("/showtimes/{id}")
    public ResponseEntity<DataResponse<ShowtimeResponse>> getShowtimeById(@PathVariable Long id) {
        try {
            ShowtimeResponse showtime = showtimeService.getShowtimeById(id);
            DataResponse<ShowtimeResponse> response = DataResponse.<ShowtimeResponse>builder()
                    .code(200)
                    .key("success")
                    .data(showtime)
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            DataResponse<ShowtimeResponse> response = DataResponse.<ShowtimeResponse>builder()
                    .code(404)
                    .key("not_found")
                    .data(null)
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    /**
     * GET /api/v1/showtimes/movie/{movieId} - Xem lịch chiếu theo phim (public)
     */
    @GetMapping("/showtimes/movie/{movieId}")
    public ResponseEntity<DataResponse<List<ShowtimeResponse>>> getShowtimesByMovieId(@PathVariable Long movieId) {
        try {
            List<ShowtimeResponse> showtimes = showtimeService.getShowtimesByMovieId(movieId);
            DataResponse<List<ShowtimeResponse>> response = DataResponse.<List<ShowtimeResponse>>builder()
                    .code(200)
                    .key("success")
                    .data(showtimes)
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            DataResponse<List<ShowtimeResponse>> response = DataResponse.<List<ShowtimeResponse>>builder()
                    .code(500)
                    .key("error")
                    .data(null)
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * GET /api/v1/showtimes/date - Xem lịch chiếu theo ngày (public)
     * @param date Format: yyyy-MM-dd'T'HH:mm:ss (VD: 2024-01-15T00:00:00)
     */
    @GetMapping("/showtimes/date")
    public ResponseEntity<DataResponse<List<ShowtimeResponse>>> getShowtimesByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {
        try {
            List<ShowtimeResponse> showtimes = showtimeService.getShowtimesByDate(date);
            DataResponse<List<ShowtimeResponse>> response = DataResponse.<List<ShowtimeResponse>>builder()
                    .code(200)
                    .key("success")
                    .data(showtimes)
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            DataResponse<List<ShowtimeResponse>> response = DataResponse.<List<ShowtimeResponse>>builder()
                    .code(500)
                    .key("error")
                    .data(null)
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * POST /api/v1/admin/showtimes - Thêm lịch chiếu (chỉ ADMIN)
     */
    @PostMapping("/admin/showtimes")
    public ResponseEntity<DataResponse<ShowtimeResponse>> createShowtime(@Valid @RequestBody ShowtimeRequest showtimeRequest) {
        try {
            ShowtimeResponse createdShowtime = showtimeService.createShowtime(showtimeRequest);
            DataResponse<ShowtimeResponse> response = DataResponse.<ShowtimeResponse>builder()
                    .code(201)
                    .key("created")
                    .data(createdShowtime)
                    .build();
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            DataResponse<ShowtimeResponse> response = DataResponse.<ShowtimeResponse>builder()
                    .code(400)
                    .key("bad_request")
                    .data(null)
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            DataResponse<ShowtimeResponse> response = DataResponse.<ShowtimeResponse>builder()
                    .code(500)
                    .key("error")
                    .data(null)
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * PUT /api/v1/admin/showtimes/{id} - Sửa lịch chiếu (chỉ ADMIN)
     */
    @PutMapping("/admin/showtimes/{id}")
    public ResponseEntity<DataResponse<ShowtimeResponse>> updateShowtime(
            @PathVariable Long id,
            @Valid @RequestBody ShowtimeUpdateRequest showtimeUpdateRequest) {
        try {
            ShowtimeResponse updatedShowtime = showtimeService.updateShowtime(id, showtimeUpdateRequest);
            DataResponse<ShowtimeResponse> response = DataResponse.<ShowtimeResponse>builder()
                    .code(200)
                    .key("updated")
                    .data(updatedShowtime)
                    .build();
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            DataResponse<ShowtimeResponse> response = DataResponse.<ShowtimeResponse>builder()
                    .code(400)
                    .key("bad_request")
                    .data(null)
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            DataResponse<ShowtimeResponse> response = DataResponse.<ShowtimeResponse>builder()
                    .code(404)
                    .key("not_found")
                    .data(null)
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    /**
     * DELETE /api/v1/admin/showtimes/{id} - Xóa lịch chiếu (chỉ ADMIN)
     */
    @DeleteMapping("/admin/showtimes/{id}")
    public ResponseEntity<DataResponse<String>> deleteShowtime(@PathVariable Long id) {
        try {
            showtimeService.deleteShowtime(id);
            DataResponse<String> response = DataResponse.<String>builder()
                    .code(200)
                    .key("deleted")
                    .data("Lịch chiếu đã được xóa thành công")
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            DataResponse<String> response = DataResponse.<String>builder()
                    .code(404)
                    .key("not_found")
                    .data("Không tìm thấy lịch chiếu để xóa")
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}