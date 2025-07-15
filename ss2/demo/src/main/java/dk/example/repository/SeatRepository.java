package dk.example.repository;

import dk.example.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SeatRepository extends JpaRepository<Seat,String> {
    Optional<Seat> findBySeatNumber(String seatNumber);
    List<Seat> findByScreenRoom_Id(Long screenRoomId);
}
