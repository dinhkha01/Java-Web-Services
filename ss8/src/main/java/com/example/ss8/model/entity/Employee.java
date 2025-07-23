package com.example.ss8.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Setter
@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Name khong dc de trong")
    private String name;
    @NotBlank
    @Size(min = 10, max = 11, message = "Phone number must be between 10 and 11 characters")
    private String phone;
    @NotBlank(message = "Address khong dc de trong")
    private String address;
    @NotBlank(message = "Position khong dc de trong")
    private String position;
    private Double salary;


}
