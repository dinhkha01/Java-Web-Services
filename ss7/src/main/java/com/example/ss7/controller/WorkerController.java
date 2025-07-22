package com.example.ss7.controller;

import com.example.ss7.entity.Worker;
import com.example.ss7.service.WorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workers")
public class WorkerController {

    @Autowired
    private WorkerService workerService;

    @GetMapping
    public ResponseEntity<List<Worker>> getAllWorkers() {
        return new ResponseEntity<>(workerService.getAllWorkers(), HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity<Worker> addWorker(@RequestBody Worker request) {
        return new ResponseEntity<>(workerService.addWorker(request), HttpStatus.CREATED);
    }
    @PutMapping("/{id}")
    public  ResponseEntity<Worker> updateWorker(@PathVariable Long id, @RequestBody Worker request) {
        Worker updatedWorker = workerService.updateWorker(id, request);
        if (updatedWorker != null) {
            return new ResponseEntity<>(updatedWorker, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorker(@PathVariable Long id) {
        workerService.deleteWorker(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
