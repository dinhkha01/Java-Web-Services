package dk.example.service;

import dk.example.entity.Movie;
import dk.example.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class MovieService implements IService<Movie,Long> {

    @Autowired
    private MovieRepository movieRepository;
    @Override
    public Movie save(Movie movie) {
        return movieRepository.save(movie);
    }

    @Override
    public Optional<Movie> findById(Long id ) {
        return movieRepository.findById(id);
    }

    @Override
    public Movie update(Movie movie) {
        if (movie.getId() == null) {
            throw new IllegalArgumentException("Movie ID cannot be null for update");
        }
        if (!movieRepository.existsById(movie.getId())) {
            throw new RuntimeException("Movie with ID " + movie.getId() + " not found");
        }
        return movieRepository.save(movie);
    }

    @Override
    public void delete(Long id) {
        movieRepository.deleteById(id);
    }

    @Override
    public List<Movie> findAll() {
        return movieRepository.findAll();
    }
}
