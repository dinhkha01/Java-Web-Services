package com.example.ss9.repository;

import com.example.ss9.model.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    // Optional: Custom query method for better performance (alternative to stream filtering)
//    @Query("SELECT m FROM Movie m WHERE LOWER(m.title) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
//    List<Movie> findByTitleContainingIgnoreCase(@Param("searchTerm") String searchTerm);

    // Optional: Another approach using Spring Data JPA method naming
    List<Movie> findByTitleContainingIgnoreCase(String title);
}