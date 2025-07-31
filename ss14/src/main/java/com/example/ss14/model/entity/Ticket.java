package com.example.ss14.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tickets")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Quan hệ Many-to-One với User
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Quan hệ Many-to-One với Showtime
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "showtime_id", nullable = false)
    private Showtime showtime;

    @Column(name = "seat_number", nullable = false)
    private String seatNumber;

    @Column(name = "booking_time", nullable = false)
    private LocalDateTime bookingTime;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    // Ràng buộc unique để tránh đặt trùng ghế trong cùng suất chiếu
    @Table(uniqueConstraints = {
            @jakarta.persistence.UniqueConstraint(columnNames = {"showtime_id", "seat_number"})
    })
    public static class UniqueConstraint {
    }
}