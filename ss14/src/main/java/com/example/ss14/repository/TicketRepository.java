package com.example.ss14.repository;

import com.example.ss14.model.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    // Tìm vé theo user ID
    List<Ticket> findByUserId(Long userId);

    // Tìm vé theo user ID và sắp xếp theo thời gian đặt vé giảm dần
    List<Ticket> findByUserIdOrderByBookingTimeDesc(Long userId);

    // Tìm vé theo showtime ID
    List<Ticket> findByShowtimeId(Long showtimeId);

    // Kiểm tra ghế đã được đặt chưa
    boolean existsByShowtimeIdAndSeatNumber(Long showtimeId, String seatNumber);

    // Tìm tất cả vé kèm thông tin user và showtime (cho admin)
    @Query("SELECT t FROM Ticket t " +
            "JOIN FETCH t.user u " +
            "JOIN FETCH t.showtime s " +
            "JOIN FETCH s.movie m " +
            "ORDER BY t.bookingTime DESC")
    List<Ticket> findAllWithDetails();

    // Tìm vé của user kèm thông tin showtime và movie
    @Query("SELECT t FROM Ticket t " +
            "JOIN FETCH t.showtime s " +
            "JOIN FETCH s.movie m " +
            "WHERE t.user.id = :userId " +
            "ORDER BY t.bookingTime DESC")
    List<Ticket> findByUserIdWithDetails(@Param("userId") Long userId);

    // Đếm số vé đã đặt theo showtime
    long countByShowtimeId(Long showtimeId);

    // Tìm danh sách ghế đã đặt theo showtime
    @Query("SELECT t.seatNumber FROM Ticket t WHERE t.showtime.id = :showtimeId")
    List<String> findBookedSeatsByShowtimeId(@Param("showtimeId") Long showtimeId);
}