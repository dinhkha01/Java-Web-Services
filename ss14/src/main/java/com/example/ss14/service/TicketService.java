package com.example.ss14.service;

import com.example.ss14.exception.BadRequestException;
import com.example.ss14.model.dto.request.TicketRequest;
import com.example.ss14.model.dto.response.TicketResponse;
import com.example.ss14.model.dto.response.TicketSimpleResponse;

import java.util.List;

public interface TicketService {

    /**
     * Đặt vé cho user hiện tại
     */
    TicketResponse bookTicket(TicketRequest ticketRequest, Long userId) throws BadRequestException;

    /**
     * Lấy danh sách vé của user hiện tại
     */
    List<TicketSimpleResponse> getMyTickets(Long userId);

    /**
     * Admin xem tất cả vé đã đặt
     */
    List<TicketResponse> getAllTickets();

    /**
     * Lấy danh sách ghế đã đặt theo showtime
     */
    List<String> getBookedSeats(Long showtimeId);

    /**
     * Đếm số vé đã đặt theo showtime
     */
    long countTicketsByShowtime(Long showtimeId);
}