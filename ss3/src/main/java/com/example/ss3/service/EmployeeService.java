package com.example.ss3.service;

import com.example.ss3.entity.Employee;
import com.example.ss3.respository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;


@Service
public class EmployeeService {
     @Autowired
    private EmployeeRepository employeeRepository;

    public Page<Employee> getAllEmployeesSortedById(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        return employeeRepository.findAll(pageable);
    }
    public Page<Employee> getAllEmployeesPaginated(int page, int size, String sortBy, String sortDir){
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() :Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page,size,sort);
        return employeeRepository.findAll(pageable);
    }
    public Page<Employee> searchByPhone(String phone, int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() :
                Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        return employeeRepository.findByPhoneContaining(phone, pageable);
    }

    public Employee saveEmployee(Employee em){
        em.setCreated_at(new Date());
        return employeeRepository.save(em);
    }

    public Employee getEmployeeById(Long id) {
        Optional<Employee> employee = employeeRepository.findById((id));
        if (employee.isPresent()) {
            return employee.get();
        }
        throw new RuntimeException("Không tìm thấy nhân viên với ID: " + id);
    }

    public Employee updateEmployee(Employee em){
        Optional<Employee> existingEmployee = employeeRepository.findById(em.getId());
            Employee updatedEmployee = existingEmployee.get();
            updatedEmployee.setName(em.getName());
            updatedEmployee.setEmail(em.getEmail());
            updatedEmployee.setPhone(em.getPhone());
            updatedEmployee.setSalary(em.getSalary());
            return employeeRepository.save(updatedEmployee);
    }
    public void deleteEmployee(Long id){
        employeeRepository.deleteById(id);
    }

}
