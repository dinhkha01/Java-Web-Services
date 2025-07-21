package com.example.ss6.service;

import com.example.ss6.entity.Employee;
import com.example.ss6.repository.IEmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService implements IEmployeeService{
    @Autowired
    IEmployeeRepository employeeRepository;
    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Override
    public void deleteEmployee(int id) {
        employeeRepository.deleteById(id);

    }

    @Override
    public Employee updateEmployee(int id, Employee request) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        if (optionalEmployee.isPresent()) {
            Employee employee = optionalEmployee.get();
            employee.setName(request.getName());
            employee.setEmail(request.getEmail());
            employee.setPosition(request.getPosition());
            employee.setSalary(request.getSalary());
            return employeeRepository.save(employee);
        }
        return null;
    }

    @Override
    public Optional<Employee> findById(int id) {
        return employeeRepository.findById(id);
    }
}
