package dk.example.repository;

import dk.example.entity.Showtime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShowtimeRepository extends JpaRepository<Showtime,String> {
    List<Showtime> findByMovie_Id(Long movieId);
    List<Showtime> findByScreenRoom_Id(Long screenRoomId);
}
