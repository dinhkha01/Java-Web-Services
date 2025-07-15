package com.example.ss3.respository;

import com.example.ss3.dto.EmployeeDTO;
import com.example.ss3.dto.EmployeeInfo;
import com.example.ss3.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee,Long> {
    Optional<Employee> findByPhone(String phone);

    @Query("SELECT e FROM Employee e WHERE e.salary = ?1")
    List<Employee> findEmployeeBySalary(Double salary);

    @Query("SELECT new com.example.ss3.dto.EmployeeDTO(e.id, e.name, e.email, e.salary) FROM Employee e")
    List<EmployeeDTO> findAllEmployeesDTO();

    @Query("SELECT e.name as name, e.phone as phone, e.salary as salary  FROM Employee e ")
    List<EmployeeInfo> findAllEmployeeInfo();
    Page<Employee> findByPhoneContaining(String phone, Pageable pageable);
}
