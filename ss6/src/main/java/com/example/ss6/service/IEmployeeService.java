package com.example.ss6.service;

import com.example.ss6.entity.Employee;

import java.util.List;
import java.util.Optional;

public interface IEmployeeService {
    List<Employee> getAllEmployees();
    Employee saveEmployee(Employee employee);
    void deleteEmployee(int id);
    Employee updateEmployee(int id, Employee employee);
    Optional<Employee> findById(int id);
}
