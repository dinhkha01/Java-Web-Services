package dk.example.service;

import dk.example.entity.Theater;
import dk.example.repository.TheaterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TheaterService implements IService<Theater,Long>{
    @Autowired
    private TheaterRepository theaterRepository;
    @Override
    public Theater save(Theater entity) {
        return theaterRepository.save(entity) ;
    }

    @Override
    public Optional<Theater> findById(Long aLong) {
        return theaterRepository.findById(aLong);
    }

    @Override
    public Theater update(Theater entity) {
        if (entity.getId() == null) {
            throw new IllegalArgumentException("Theater ID cannot be null for update");
        }
        if (!theaterRepository.existsById(entity.getId())) {
            throw new RuntimeException("Theater with ID " + entity.getId() + " not found");
        }
        return theaterRepository.save(entity);
    }

    @Override
    public void delete(Long aLong) {
        theaterRepository.deleteById(aLong);

    }

    @Override
    public List<Theater> findAll() {
        return theaterRepository.findAll();
    }
}
