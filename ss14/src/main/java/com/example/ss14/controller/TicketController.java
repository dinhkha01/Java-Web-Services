package com.example.ss14.controller;

import com.example.ss14.exception.BadRequestException;
import com.example.ss14.model.dto.request.TicketRequest;

import com.example.ss14.model.dto.response.DataResponse;
import com.example.ss14.model.dto.response.TicketResponse;
import com.example.ss14.model.dto.response.TicketSimpleResponse;
import com.example.ss14.service.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class TicketController {

    private final TicketService ticketService;

    /**
     * Đặt vé - Chỉ USER
     * POST /api/tickets/book
     */
    @PostMapping("/tickets/book")
    public ResponseEntity<DataResponse<TicketResponse>> bookTicket(
            @Valid @RequestBody TicketRequest ticketRequest,
            Authentication authentication) throws BadRequestException {
        try {
            // Lấy user ID từ authentication
            Long userId = getUserIdFromAuth(authentication);

            TicketResponse ticket = ticketService.bookTicket(ticketRequest, userId);

            DataResponse<TicketResponse> response = DataResponse.<TicketResponse>builder()
                    .status(201)
                    .message("Đặt vé thành công")
                    .data(ticket)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (BadRequestException e) {
            throw e;
        } catch (NoSuchElementException e) {
            throw e;
        } catch (Exception e) {
            log.error("Lỗi khi đặt vé", e);
            throw new RuntimeException("Đã xảy ra lỗi khi đặt vé");
        }
    }

    /**
     * Xem lịch sử vé của user hiện tại - Chỉ USER
     * GET /api/tickets/my
     */
    @GetMapping("/tickets/my")
    public ResponseEntity<DataResponse<List<TicketSimpleResponse>>> getMyTickets(
            Authentication authentication) {
        try {
            // Lấy user ID từ authentication
            Long userId = getUserIdFromAuth(authentication);

            List<TicketSimpleResponse> tickets = ticketService.getMyTickets(userId);

            DataResponse<List<TicketSimpleResponse>> response = DataResponse.<List<TicketSimpleResponse>>builder()
                    .status(200)
                    .message("Lấy danh sách vé thành công")
                    .data(tickets)
                    .build();

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Lỗi khi lấy danh sách vé của user", e);
            throw new RuntimeException("Không thể lấy danh sách vé");
        }
    }

    /**
     * ADMIN xem danh sách toàn bộ vé đã đặt
     * GET /api/admin/tickets
     */
    @GetMapping("/admin/tickets")
    public ResponseEntity<DataResponse<List<TicketResponse>>> getAllTickets() {
        try {
            List<TicketResponse> tickets = ticketService.getAllTickets();

            DataResponse<List<TicketResponse>> response = DataResponse.<List<TicketResponse>>builder()
                    .status(200)
                    .message("Lấy danh sách tất cả vé thành công")
                    .data(tickets)
                    .build();

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Lỗi khi lấy tất cả vé", e);
            throw new RuntimeException("Không thể lấy danh sách vé");
        }
    }

    /**
     * Lấy danh sách ghế đã đặt theo showtime - Public để user có thể xem ghế trống
     * GET /api/showtimes/{showtimeId}/booked-seats
     */
    @GetMapping("/showtimes/{showtimeId}/booked-seats")
    public ResponseEntity<DataResponse<List<String>>> getBookedSeats(
            @PathVariable Long showtimeId) {
        try {
            List<String> bookedSeats = ticketService.getBookedSeats(showtimeId);

            DataResponse<List<String>> response = DataResponse.<List<String>>builder()
                    .status(200)
                    .message("Lấy danh sách ghế đã đặt thành công")
                    .data(bookedSeats)
                    .build();

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Lỗi khi lấy danh sách ghế đã đặt cho showtime {}", showtimeId, e);
            throw new RuntimeException("Không thể lấy danh sách ghế đã đặt");
        }
    }

    /**
     * Đếm số vé đã đặt theo showtime - Public
     * GET /api/showtimes/{showtimeId}/ticket-count
     */
    @GetMapping("/showtimes/{showtimeId}/ticket-count")
    public ResponseEntity<DataResponse<Long>> getTicketCount(
            @PathVariable Long showtimeId) {
        try {
            long ticketCount = ticketService.countTicketsByShowtime(showtimeId);

            DataResponse<Long> response = DataResponse.<Long>builder()
                    .status(200)
                    .message("Đếm số vé thành công")
                    .data(ticketCount)
                    .build();

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Lỗi khi đếm vé cho showtime {}", showtimeId, e);
            throw new RuntimeException("Không thể đếm số vé");
        }
    }

    /**
     * Helper method để lấy user ID từ Authentication
     * Giả sử UserDetails có method getId() hoặc username chính là ID
     */
    private Long getUserIdFromAuth(Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new RuntimeException("Người dùng chưa được xác thực");
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            // Giả sử username chính là user ID hoặc có cách lấy ID khác
            // Bạn có thể cần điều chỉnh phần này tùy theo cách implement UserDetails
            try {
                return Long.parseLong(userDetails.getUsername());
            } catch (NumberFormatException e) {
                // Nếu username không phải là ID, cần implement cách khác
                // Ví dụ: tạo custom UserDetails với method getId()
                throw new RuntimeException("Không thể xác định ID người dùng");
            }
        }

        throw new RuntimeException("Định dạng principal không hợp lệ");
    }
}