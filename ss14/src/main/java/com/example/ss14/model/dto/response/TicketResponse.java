package com.example.ss14.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TicketResponse {
    private Long id;
    private String seatNumber;
    private LocalDateTime bookingTime;
    private BigDecimal price;

    // Thông tin user
    private Long userId;
    private String username;

    // Thông tin showtime
    private Long showtimeId;
    private LocalDateTime startTime;
    private String room;

    // Thông tin movie
    private Long movieId;
    private String movieTitle;
}