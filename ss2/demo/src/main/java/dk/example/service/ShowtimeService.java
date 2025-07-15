package dk.example.service;

import dk.example.entity.Showtime;
import dk.example.repository.ShowtimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ShowtimeService implements IService<Showtime, String> {

    @Autowired
    private ShowtimeRepository showtimeRepository;

    @Override
    public Showtime save(Showtime entity) {
        return showtimeRepository.save(entity);
    }

    @Override
    public Optional<Showtime> findById(String id) {
        return showtimeRepository.findById(id);
    }

    @Override
    public Showtime update(Showtime entity) {
        if (entity.getId() == null) {
            throw new IllegalArgumentException("Showtime ID cannot be null for update");
        }
        if (!showtimeRepository.existsById(String.valueOf(entity.getId()))) {
            throw new RuntimeException("Showtime with ID " + entity.getId() + " not found");
        }
        return showtimeRepository.save(entity);
    }

    @Override
    public void delete(String id) {
        if (!showtimeRepository.existsById(id)) {
            throw new RuntimeException("Showtime with ID " + id + " not found");
        }
        showtimeRepository.deleteById(id);
    }

    @Override
    public List<Showtime> findAll() {
        return showtimeRepository.findAll();
    }

    // Custom methods specific to Showtime
    public List<Showtime> findByMovieId(Long movieId) {
        return showtimeRepository.findByMovie_Id(movieId);
    }

    public List<Showtime> findByScreenRoomId(Long screenRoomId) {
        return showtimeRepository.findByScreenRoom_Id(screenRoomId);
    }
}