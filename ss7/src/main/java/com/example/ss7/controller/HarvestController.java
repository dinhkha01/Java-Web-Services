package com.example.ss7.controller;

import com.example.ss7.entity.Harvest;
import com.example.ss7.entity.HarvestDto;
import com.example.ss7.service.HarvestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/harvests")
public class HarvestController {
    @Autowired
    private HarvestService harvestService;

    @GetMapping
    public ResponseEntity<List<HarvestDto>> getAllHarvests() {
        return new ResponseEntity<>(harvestService.getAllHarvests(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Harvest> addHarvest(@RequestBody HarvestDto request) {
        Harvest harvest = harvestService.addHarvest(request);
        return new ResponseEntity<>(harvest, HttpStatus.CREATED);
    }


    @GetMapping("/{id}")
    public ResponseEntity<HarvestDto> getHarvestById(@PathVariable Long id) {
        Optional<HarvestDto> harvest = harvestService.getHarvestById(id);
        if (harvest.isPresent()) {
            return new ResponseEntity<>(harvest.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
