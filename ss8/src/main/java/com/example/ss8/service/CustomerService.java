package com.example.ss8.service;

import com.example.ss8.exception.BadRequestException;
import com.example.ss8.model.dto.request.CustomerDTO;
import com.example.ss8.model.dto.response.DataResponse;
import com.example.ss8.model.entity.Customer;
import com.example.ss8.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@RequiredArgsConstructor
@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    public DataResponse<List<Customer>> getAllCustomers() {
        return DataResponse.<List<Customer>>builder()
                .key("customers")
                .data(customerRepository.findAll())
                .build();
    }
    @Transactional
    public DataResponse<Customer> createCustomer(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        customer.setName(customerDTO.getName());
        customer.setEmail(customerDTO.getEmail());
        customer.setPhone(customerDTO.getPhone());
        customer.setNumberOfPayments(customerDTO.getNumberOfPayments());
        return DataResponse.<Customer>builder()
                .key("customer")
                .data(customerRepository.save(customer))
                .build();
    }
    @Transactional
    public DataResponse<Customer> updateCustomer(Long id, CustomerDTO customerDTO) throws BadRequestException {
        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Không tìm thấy khách hàng với ID: " + id));

        // Cập nhật thông tin khách hàng
        existingCustomer.setName(customerDTO.getName());
        existingCustomer.setEmail(customerDTO.getEmail());
        existingCustomer.setPhone(customerDTO.getPhone());
        existingCustomer.setNumberOfPayments(customerDTO.getNumberOfPayments());

        return DataResponse.<Customer>builder()
                .key("customer")
                .data(customerRepository.save(existingCustomer))
                .build();
    }
    @Transactional
    public void deleteCustomer(Long id) throws BadRequestException {
        if (!customerRepository.existsById(id)) {
            throw new BadRequestException("Không tìm thấy khách hàng với ID: " + id);
        }
        customerRepository.deleteById(id);
    }


}
