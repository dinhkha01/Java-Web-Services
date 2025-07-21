package com.example.ss7.repository;

import com.example.ss7.entity.Seed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeedRepository extends JpaRepository<Seed,Long> {
}
