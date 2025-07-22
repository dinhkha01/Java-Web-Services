package com.example.ss7.repository;

import com.example.ss7.entity.Harvest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HarvestRepository extends JpaRepository<Harvest , Long> {
}
