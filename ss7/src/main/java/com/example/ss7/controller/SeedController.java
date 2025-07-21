package com.example.ss7.controller;

import com.example.ss7.entity.Seed;
import com.example.ss7.service.SeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seeds")
public class SeedController {
    @Autowired
    private SeedService seedService;

    @GetMapping
    public ResponseEntity<List<Seed>> getAllSeeds() {
        return new ResponseEntity<>(seedService.getAllSeeds(), HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity<Seed> saveSeed(@RequestBody Seed request) {
        return new ResponseEntity<>( seedService.saveSeed(request), HttpStatus.CREATED);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Seed> updateSeed(@PathVariable Long id,@RequestBody Seed request) {
        Seed updatedSeed = seedService.updateSeed(id, request);
        if (updatedSeed != null) {
            return new ResponseEntity<>(updatedSeed, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeed(@PathVariable Long id) {
        seedService.deleteSeed(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
