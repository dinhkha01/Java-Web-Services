package com.example.ss14.repository;

import com.example.ss14.model.entity.Showtime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ShowtimeRepository extends JpaRepository<Showtime, Long> {

    // Tìm lịch chiếu theo phim
    List<Showtime> findByMovieId(Long movieId);

    // Tìm lịch chiếu theo phòng
    List<Showtime> findByRoom(String room);

    // Tìm lịch chiếu trong khoảng thời gian
    List<Showtime> findByStartTimeBetween(LocalDateTime startTime, LocalDateTime endTime);

    // Tìm lịch chiếu từ thời điểm hiện tại trở đi
    List<Showtime> findByStartTimeAfter(LocalDateTime startTime);

    // Tìm lịch chiếu theo phòng và thời gian (để kiểm tra trung lặp)
    List<Showtime> findByRoomAndStartTimeBetween(String room, LocalDateTime startTime, LocalDateTime endTime);

    // Tìm lịch chiếu kèm thông tin phim
    @Query("SELECT s FROM Showtime s JOIN FETCH s.movie WHERE s.id = :id")
    Showtime findByIdWithMovie(@Param("id") Long id);

    // Tìm tất cả lịch chiếu kèm thông tin phim
    @Query("SELECT s FROM Showtime s JOIN FETCH s.movie ORDER BY s.startTime ASC")
    List<Showtime> findAllWithMovie();

    // Tìm lịch chiếu theo ngày cụ thể
    @Query("SELECT s FROM Showtime s JOIN FETCH s.movie WHERE DATE(s.startTime) = DATE(:date) ORDER BY s.startTime ASC")
    List<Showtime> findByDate(@Param("date") LocalDateTime date);

    // Kiểm tra xung đột lịch chiếu (cùng phòng, cùng thời gian)
    @Query("SELECT COUNT(s) > 0 FROM Showtime s WHERE s.room = :room AND s.startTime BETWEEN :startTime AND :endTime AND s.id != :excludeId")
    boolean existsConflictingShowtime(@Param("room") String room,
                                      @Param("startTime") LocalDateTime startTime,
                                      @Param("endTime") LocalDateTime endTime,
                                      @Param("excludeId") Long excludeId);
}