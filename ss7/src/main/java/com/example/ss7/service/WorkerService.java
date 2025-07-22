package com.example.ss7.service;

import com.example.ss7.entity.Worker;
import com.example.ss7.repository.WorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WorkerService {

    @Autowired
    private WorkerRepository workerRepository;

    public List<Worker> getAllWorkers() {
        return workerRepository.findAll();
    }
    public Worker addWorker(Worker request) {
        return workerRepository.save(request);
    }
    public Optional<Worker> getWorkerById(Long id) {
        return workerRepository.findById(id);
    }
    public void deleteWorker(Long id) {
        workerRepository.deleteById(id);
    }
    public Worker updateWorker(Long id, Worker request){
        Optional<Worker> workerOptional = workerRepository.findById(id);
    if (workerOptional.isPresent()) {
        Worker worker = workerOptional.get();
        worker.setName(request.getName());
        worker.setPhone(request.getPhone());
        worker.setSalary(request.getSalary());
        worker.setAddress(request.getAddress());
        return workerRepository.save(worker);
    }
        return null;
    }


}
