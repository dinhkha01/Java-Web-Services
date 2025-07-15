package dk.example.service;

import dk.example.entity.Seat;
import dk.example.repository.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SeatService implements IService<Seat, String> {

    @Autowired
    private SeatRepository seatRepository;

    @Override
    public Seat save(Seat entity) {
        return seatRepository.save(entity);
    }

    @Override
    public Optional<Seat> findById(String id) {
        return seatRepository.findById(id);
    }

    @Override
    public Seat update(Seat entity) {
        if (entity.getId() == null) {
            throw new IllegalArgumentException("Seat ID cannot be null for update");
        }
        if (!seatRepository.existsById(String.valueOf(entity.getId()))){
            throw new RuntimeException("Seat with ID " + entity.getId() + " not found");
        }
        return seatRepository.save(entity);
    }

    @Override
    public void delete(String id) {
        if (!seatRepository.existsById(id)) {
            throw new RuntimeException("Seat with ID " + id + " not found");
        }
        seatRepository.deleteById(id);
    }

    @Override
    public List<Seat> findAll() {
        return seatRepository.findAll();
    }

    // Custom methods specific to Seat
    public Optional<Seat> findBySeatNumber(String seatNumber) {
        return seatRepository.findBySeatNumber(seatNumber);
    }

    public List<Seat> findByScreenRoomId(Long screenRoomId) {
        return seatRepository.findByScreenRoom_Id(screenRoomId);
    }
}