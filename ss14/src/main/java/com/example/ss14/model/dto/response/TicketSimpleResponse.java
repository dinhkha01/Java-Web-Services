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
public class TicketSimpleResponse {
    private Long id;
    private String seatNumber;
    private LocalDateTime bookingTime;
    private BigDecimal price;
    private String movieTitle;
    private LocalDateTime showStartTime;
    private String room;
}