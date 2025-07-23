package com.example.ss8.service;

import com.example.ss8.exception.BadRequestException;
import com.example.ss8.exception.NotFoundException;
import com.example.ss8.model.dto.request.OrderDTO;
import com.example.ss8.model.dto.request.OrderDetailDTO;
import com.example.ss8.model.dto.response.DataResponse;
import com.example.ss8.model.entity.*;
import com.example.ss8.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final CustomerRepository customerRepository;
    private final EmployeeRepository employeeRepository;
    private final DishRepository dishRepository;

    @Transactional
    public DataResponse<Order> createOrder(OrderDTO orderDTO) throws NotFoundException, BadRequestException {
        // Kiểm tra customer tồn tại
        Customer customer = customerRepository.findById(orderDTO.getCustomerId())
                .orElseThrow(() -> new NotFoundException("Không tìm thấy khách hàng với ID: " + orderDTO.getCustomerId()));

        // Kiểm tra employee tồn tại
        Employee employee = employeeRepository.findById(orderDTO.getEmployeeId())
                .orElseThrow(() -> new NotFoundException("Không tìm thấy nhân viên với ID: " + orderDTO.getEmployeeId()));

        // Tạo đơn hàng mới
        Order order = Order.builder()
                .customer(customer)
                .employee(employee)
                .totalMoney(0.0)
                .build();

        double totalMoney = 0.0;
        List<OrderDetail> orderDetails = new ArrayList<>();

        // Xử lý từng chi tiết đơn hàng
        for (OrderDetailDTO detailDTO : orderDTO.getOrderDetails()) {
            // Kiểm tra món ăn tồn tại
            Dish dish = dishRepository.findById(detailDTO.getDishId())
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy món ăn với ID: " + detailDTO.getDishId()));

            // Kiểm tra trạng thái món ăn
            if (!"AVAILABLE".equalsIgnoreCase(dish.getStatus())) {
                throw new BadRequestException("Món ăn '" + dish.getName() + "' hiện không có sẵn");
            }

            // Tạo chi tiết đơn hàng
            OrderDetail orderDetail = OrderDetail.builder()
                    .order(order)
                    .dish(dish)
                    .quantity(detailDTO.getQuantity())
                    .priceBuy(dish.getPrice())
                    .build();

            orderDetails.add(orderDetail);
            totalMoney += dish.getPrice() * detailDTO.getQuantity();
        }

        // Cập nhật tổng tiền
        order.setTotalMoney(totalMoney);

        // Lưu đơn hàng
        Order savedOrder = orderRepository.save(order);

        // Cập nhật order cho các orderDetail và lưu
        orderDetails.forEach(detail -> detail.setOrder(savedOrder));
        orderDetailRepository.saveAll(orderDetails);

        // Cập nhật số lần thanh toán của khách hàng
        customer.setNumberOfPayments(customer.getNumberOfPayments() + 1);
        customerRepository.save(customer);

        // Lấy lại đơn hàng với đầy đủ thông tin
        Order orderWithDetails = orderRepository.findByIdWithFullDetails(savedOrder.getId());

        return DataResponse.<Order>builder()
                .key("order")
                .data(orderWithDetails)
                .build();
    }

    public DataResponse<List<Order>> getAllOrders() {
        List<Order> orders = orderRepository.findAllWithDetails();
        return DataResponse.<List<Order>>builder()
                .key("orders")
                .data(orders)
                .build();
    }

    public DataResponse<Order> getOrderById(Long id) throws NotFoundException {
        Order order = orderRepository.findByIdWithFullDetails(id);
        if (order == null) {
            throw new NotFoundException("Không tìm thấy đơn hàng với ID: " + id);
        }

        return DataResponse.<Order>builder()
                .key("order")
                .data(order)
                .build();
    }
}