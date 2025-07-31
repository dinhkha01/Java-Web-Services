package com.example.ss14.service;

import com.example.ss14.model.dto.request.ShowtimeRequest;
import com.example.ss14.model.dto.request.ShowtimeUpdateRequest;
import com.example.ss14.model.dto.response.ShowtimeResponse;
import com.example.ss14.model.dto.response.ShowtimeSimpleResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface ShowtimeService {
    List<ShowtimeResponse> getAllShowtimes();
    List<ShowtimeSimpleResponse> getAllShowtimesSimple();
    ShowtimeResponse getShowtimeById(Long id);
    ShowtimeResponse createShowtime(ShowtimeRequest showtimeRequest);
    ShowtimeResponse updateShowtime(Long id, ShowtimeUpdateRequest showtimeUpdateRequest);
    void deleteShowtime(Long id);
    List<ShowtimeResponse> getShowtimesByMovieId(Long movieId);
    List<ShowtimeResponse> getShowtimesByDate(LocalDateTime date);
}