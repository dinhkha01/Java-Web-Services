package com.example.ss14.repository;

import com.example.ss14.model.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    // Tìm phim theo tên (không phân biệt hoa thường)
    List<Movie> findByTitleContainingIgnoreCase(String title);

    // Tìm phim theo ngày phát hành
    List<Movie> findByReleaseDate(Date releaseDate);

    // Tìm phim phát hành trong khoảng thời gian
    List<Movie> findByReleaseDateBetween(Date startDate, Date endDate);

    // Tìm phim theo thời lượng
    List<Movie> findByDurationBetween(Integer minDuration, Integer maxDuration);

    // Kiểm tra phim có tồn tại theo tên
    boolean existsByTitleIgnoreCase(String title);

    // Tìm phim kèm theo lịch chiếu
    @Query("SELECT m FROM Movie m LEFT JOIN FETCH m.showtimes WHERE m.id = :id")
    Optional<Movie> findByIdWithShowtimes(@Param("id") Long id);

    // Tìm tất cả phim có lịch chiếu
    @Query("SELECT DISTINCT m FROM Movie m INNER JOIN m.showtimes")
    List<Movie> findMoviesWithShowtimes();
}