package com.example.ss7.service;

import com.example.ss7.entity.Seed;
import com.example.ss7.repository.SeedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SeedService {
    @Autowired
    private SeedRepository seedRepository;

    public List<Seed> getAllSeeds(){
        return seedRepository.findAll();
    }
    public Optional<Seed> getSeedById(Long id){
        return seedRepository.findById( id);
    }

    public Seed saveSeed(Seed request){
        return seedRepository.save(request);
    }
    public void deleteSeed(Long id){
        seedRepository.deleteById(id);
    }
    public Seed updateSeed(Long id, Seed request) {
        Optional<Seed> optionalSeed = seedRepository.findById(id);
        if(optionalSeed.isPresent()){
            Seed oldSeed = optionalSeed.get();
            oldSeed.setName(request.getName());
            oldSeed.setQuantity(request.getQuantity());
            return seedRepository.save(oldSeed);
        }
        return null;
    }
}
