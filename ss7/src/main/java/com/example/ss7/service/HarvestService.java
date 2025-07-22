package com.example.ss7.service;

import com.example.ss7.entity.Harvest;
import com.example.ss7.entity.HarvestDto;
import com.example.ss7.repository.HarvestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class HarvestService {

    @Autowired
    private HarvestRepository harvestRepository;

    public List<HarvestDto> getAllHarvests(){
        List<Harvest> harvests= harvestRepository.findAll();
        return harvests.stream()
                .map(harvest -> new HarvestDto(
                        harvest.getId(),
                        harvest.getName(),
                        harvest.getQuantity(),
                        harvest.getTotalPrice()))
                .toList();
    }

    public Optional<HarvestDto> getHarvestById(Long id){
        Optional<Harvest> harvestOptional = harvestRepository.findById(id);
        return harvestOptional.map(harvest -> new HarvestDto(
                harvest.getId(),
                harvest.getName(),
                harvest.getQuantity(),
                harvest.getTotalPrice()));
    }
    public Harvest addHarvest(HarvestDto request){
        Harvest harvest = new Harvest();
        harvest.setName(request.getName());
        harvest.setTotalPrice(request.getTotalPrice());
        harvest.setQuantity(request.getQuantity());
        harvest.setCreatedAt(LocalDateTime.now());
        return harvestRepository.save(harvest) ;
    }
}
