package dk.example.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
@Getter
@Entity
@Table(name = "screen_rooms")
public class ScreenRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer capacity;

    // Quan hệ Many-to-One với Theater
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theater_id", nullable = false)
    private Theater theater;

    // Quan hệ One-to-Many với Seat
    @OneToMany(mappedBy = "screenRoom", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Seat> seats = new ArrayList<>();

    // Quan hệ One-to-Many với Showtime
    @OneToMany(mappedBy = "screenRoom", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Showtime> showtimes = new ArrayList<>();
}
