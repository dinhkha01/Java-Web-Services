package com.example.ss14.service;

import com.example.ss14.model.dto.request.ShowtimeRequest;
import com.example.ss14.model.dto.request.ShowtimeUpdateRequest;
import com.example.ss14.model.dto.response.MovieResponse;
import com.example.ss14.model.dto.response.ShowtimeResponse;
import com.example.ss14.model.dto.response.ShowtimeSimpleResponse;
import com.example.ss14.model.entity.Movie;
import com.example.ss14.model.entity.Showtime;
import com.example.ss14.repository.MovieRepository;
import com.example.ss14.repository.ShowtimeRepository;
import com.example.ss14.service.ShowtimeService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ShowtimeServiceImpl implements ShowtimeService {

    private final ShowtimeRepository showtimeRepository;
    private final MovieRepository movieRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ShowtimeResponse> getAllShowtimes() {
        return showtimeRepository.findAllWithMovie()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShowtimeSimpleResponse> getAllShowtimesSimple() {
        return showtimeRepository.findAllWithMovie()
                .stream()
                .map(this::convertToSimpleResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ShowtimeResponse getShowtimeById(Long id) {
        Showtime showtime = showtimeRepository.findByIdWithMovie(id);
        if (showtime == null) {
            throw new EntityNotFoundException("Không tìm thấy lịch chiếu với ID: " + id);
        }
        return convertToResponse(showtime);
    }

    @Override
    public ShowtimeResponse createShowtime(ShowtimeRequest showtimeRequest) {
        // Kiểm tra phim có tồn tại
        Movie movie = movieRepository.findById(showtimeRequest.getMovieId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy phim với ID: " + showtimeRequest.getMovieId()));

        // Kiểm tra xung đột lịch chiếu (giả sử mỗi phim chiếu khoảng movie.duration + 30 phút)
        LocalDateTime endTime = showtimeRequest.getStartTime().plusMinutes(movie.getDuration() + 30);
        boolean hasConflict = showtimeRepository.existsConflictingShowtime(
                showtimeRequest.getRoom(),
                showtimeRequest.getStartTime().minusMinutes(30),
                endTime,
                0L // excludeId = 0 vì đây là tạo mới
        );

        if (hasConflict) {
            throw new IllegalArgumentException("Đã có lịch chiếu khác trong phòng " + showtimeRequest.getRoom() +
                    " vào thời gian này");
        }

        Showtime showtime = Showtime.builder()
                .movie(movie)
                .startTime(showtimeRequest.getStartTime())
                .room(showtimeRequest.getRoom())
                .build();

        Showtime savedShowtime = showtimeRepository.save(showtime);
        return convertToResponse(savedShowtime);
    }

    @Override
    public ShowtimeResponse updateShowtime(Long id, ShowtimeUpdateRequest showtimeUpdateRequest) {
        Showtime existingShowtime = showtimeRepository.findByIdWithMovie(id);
        if (existingShowtime == null) {
            throw new EntityNotFoundException("Không tìm thấy lịch chiếu với ID: " + id);
        }

        // Kiểm tra phim có tồn tại
        Movie movie = movieRepository.findById(showtimeUpdateRequest.getMovieId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy phim với ID: " + showtimeUpdateRequest.getMovieId()));

        // Kiểm tra xung đột lịch chiếu (trừ chính nó)
        LocalDateTime endTime = showtimeUpdateRequest.getStartTime().plusMinutes(movie.getDuration() + 30);
        boolean hasConflict = showtimeRepository.existsConflictingShowtime(
                showtimeUpdateRequest.getRoom(),
                showtimeUpdateRequest.getStartTime().minusMinutes(30),
                endTime,
                id // excludeId = id hiện tại
        );

        if (hasConflict) {
            throw new IllegalArgumentException("Đã có lịch chiếu khác trong phòng " + showtimeUpdateRequest.getRoom() +
                    " vào thời gian này");
        }

        existingShowtime.setMovie(movie);
        existingShowtime.setStartTime(showtimeUpdateRequest.getStartTime());
        existingShowtime.setRoom(showtimeUpdateRequest.getRoom());

        Showtime updatedShowtime = showtimeRepository.save(existingShowtime);
        return convertToResponse(updatedShowtime);
    }

    @Override
    public void deleteShowtime(Long id) {
        if (!showtimeRepository.existsById(id)) {
            throw new EntityNotFoundException("Không tìm thấy lịch chiếu với ID: " + id);
        }
        showtimeRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShowtimeResponse> getShowtimesByMovieId(Long movieId) {
        return showtimeRepository.findByMovieId(movieId)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShowtimeResponse> getShowtimesByDate(LocalDateTime date) {
        return showtimeRepository.findByDate(date)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // Helper methods để convert Entity sang Response DTO
    private ShowtimeResponse convertToResponse(Showtime showtime) {
        MovieResponse movieResponse = MovieResponse.builder()
                .id(showtime.getMovie().getId())
                .title(showtime.getMovie().getTitle())
                .description(showtime.getMovie().getDescription())
                .duration(showtime.getMovie().getDuration())
                .releaseDate(showtime.getMovie().getReleaseDate())
                .build();

        return ShowtimeResponse.builder()
                .id(showtime.getId())
                .movie(movieResponse)
                .startTime(showtime.getStartTime())
                .room(showtime.getRoom())
                .build();
    }

    private ShowtimeSimpleResponse convertToSimpleResponse(Showtime showtime) {
        return ShowtimeSimpleResponse.builder()
                .id(showtime.getId())
                .movieId(showtime.getMovie().getId())
                .movieTitle(showtime.getMovie().getTitle())
                .startTime(showtime.getStartTime())
                .room(showtime.getRoom())
                .build();
    }
}