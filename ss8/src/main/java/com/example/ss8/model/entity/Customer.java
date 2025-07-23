package com.example.ss8.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String phone;
    private String email;
    private int numberOfPayments;;
    private Boolean status;
    private LocalDateTime createdAt;
    @PrePersist
    public void prePersist() {
        if (status == null) {
            this.status = true;
        }
        this.createdAt= LocalDateTime.now();
    }
}
