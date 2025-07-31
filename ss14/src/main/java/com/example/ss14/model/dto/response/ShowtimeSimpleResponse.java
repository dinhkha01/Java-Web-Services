package com.example.ss14.model.dto.response;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShowtimeSimpleResponse {
    private Long id;
    private Long movieId;
    private String movieTitle;
    private LocalDateTime startTime;
    private String room;
}
