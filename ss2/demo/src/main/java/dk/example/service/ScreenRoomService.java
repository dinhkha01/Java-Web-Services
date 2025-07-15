package dk.example.service;

import dk.example.entity.ScreenRoom;
import dk.example.repository.ScreenRoomRepository;
import jakarta.persistence.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class ScreenRoomService implements IService<ScreenRoom,Long>{

    @Autowired
    private ScreenRoomRepository screenRoomRepository;
    @Override
    public ScreenRoom save(ScreenRoom entity) {
        return screenRoomRepository.save(entity);
    }

    @Override
    public Optional<ScreenRoom> findById(Long aLong) {
        return screenRoomRepository.findById(aLong);
    }

    @Override
    public ScreenRoom update(ScreenRoom entity) {
        if (entity.getId() == null) {
            throw new IllegalArgumentException("ScreenRoom ID cannot be null for update");
        }
        if (!screenRoomRepository.existsById(entity.getId())) {
            throw new RuntimeException("ScreenRoom with ID " + entity.getId() + " not found");
        }
        return screenRoomRepository.save(entity);
    }

    @Override
    public void delete(Long aLong) {
        if (!screenRoomRepository.existsById(aLong)) {
            throw new RuntimeException("ScreenRoom with ID " + aLong + " not found");
        }
        screenRoomRepository.deleteById(aLong);

    }

    @Override
    public List<ScreenRoom> findAll() {
        return screenRoomRepository.findAll();
    }
}
