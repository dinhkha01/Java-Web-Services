package com.example.ss14.service;

import com.example.ss14.exception.BadRequestException;
import com.example.ss14.exception.NotFoundException;
import com.example.ss14.model.dto.request.TicketRequest;
import com.example.ss14.model.dto.response.TicketResponse;
import com.example.ss14.model.dto.response.TicketSimpleResponse;
import com.example.ss14.model.entity.Showtime;
import com.example.ss14.model.entity.Ticket;
import com.example.ss14.model.entity.User;
import com.example.ss14.repository.IAccountRepository;
import com.example.ss14.repository.ShowtimeRepository;
import com.example.ss14.repository.TicketRepository;
import com.example.ss14.service.TicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final IAccountRepository accountRepository;
    private final ShowtimeRepository showtimeRepository;

    @Override
    @Transactional
    public TicketResponse bookTicket(TicketRequest ticketRequest, Long userId) throws BadRequestException {
        try {
            // Kiểm tra user tồn tại
            User user = accountRepository.findById(userId)
                    .orElseThrow(() -> new NoSuchElementException("Không tìm thấy user với ID: " + userId));

            // Kiểm tra showtime tồn tại
            Showtime showtime = showtimeRepository.findByIdWithMovie(ticketRequest.getShowtimeId());
            if (showtime == null) {
                throw new NoSuchElementException("Không tìm thấy suất chiếu với ID: " + ticketRequest.getShowtimeId());
            }

            // Kiểm tra suất chiếu chưa bắt đầu
            if (showtime.getStartTime().isBefore(LocalDateTime.now())) {
                throw new BadRequestException("Không thể đặt vé cho suất chiếu đã bắt đầu",
                        Map.of("showtime", "Suất chiếu đã bắt đầu lúc " + showtime.getStartTime()));
            }

            // Kiểm tra ghế chưa được đặt
            if (ticketRepository.existsByShowtimeIdAndSeatNumber(ticketRequest.getShowtimeId(), ticketRequest.getSeatNumber())) {
                throw new BadRequestException("Ghế đã được đặt",
                        Map.of("seatNumber", "Ghế " + ticketRequest.getSeatNumber() + " đã có người đặt"));
            }

            // Tạo vé mới
            Ticket ticket = Ticket.builder()
                    .user(user)
                    .showtime(showtime)
                    .seatNumber(ticketRequest.getSeatNumber())
                    .bookingTime(LocalDateTime.now())
                    .price(ticketRequest.getPrice())
                    .build();

            // Lưu vé
            Ticket savedTicket = ticketRepository.save(ticket);
            log.info("User {} đã đặt vé thành công cho suất chiếu {} ghế {}",
                    userId, ticketRequest.getShowtimeId(), ticketRequest.getSeatNumber());

            // Trả về response
            return mapToTicketResponse(savedTicket);

        } catch (NoSuchElementException e) {
            log.error("Lỗi khi đặt vé: {}", e.getMessage());
            throw e;
        } catch (BadRequestException e) {
            log.error("Lỗi validation khi đặt vé: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Lỗi không xác định khi đặt vé", e);
            throw new RuntimeException("Đã xảy ra lỗi khi đặt vé: " + e.getMessage());
        }
    }

    @Override
    public List<TicketSimpleResponse> getMyTickets(Long userId) {
        try {
            List<Ticket> tickets = ticketRepository.findByUserIdWithDetails(userId);
            return tickets.stream()
                    .map(this::mapToTicketSimpleResponse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Lỗi khi lấy danh sách vé của user {}", userId, e);
            throw new RuntimeException("Không thể lấy danh sách vé: " + e.getMessage());
        }
    }

    @Override
    public List<TicketResponse> getAllTickets() {
        try {
            List<Ticket> tickets = ticketRepository.findAllWithDetails();
            return tickets.stream()
                    .map(this::mapToTicketResponse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Lỗi khi lấy tất cả vé", e);
            throw new RuntimeException("Không thể lấy danh sách tất cả vé: " + e.getMessage());
        }
    }

    @Override
    public List<String> getBookedSeats(Long showtimeId) {
        try {
            return ticketRepository.findBookedSeatsByShowtimeId(showtimeId);
        } catch (Exception e) {
            log.error("Lỗi khi lấy danh sách ghế đã đặt cho showtime {}", showtimeId, e);
            throw new RuntimeException("Không thể lấy danh sách ghế đã đặt: " + e.getMessage());
        }
    }

    @Override
    public long countTicketsByShowtime(Long showtimeId) {
        try {
            return ticketRepository.countByShowtimeId(showtimeId);
        } catch (Exception e) {
            log.error("Lỗi khi đếm vé cho showtime {}", showtimeId, e);
            throw new RuntimeException("Không thể đếm số vé: " + e.getMessage());
        }
    }

    /**
     * Mapping Ticket entity to TicketResponse
     */
    private TicketResponse mapToTicketResponse(Ticket ticket) {
        return TicketResponse.builder()
                .id(ticket.getId())
                .seatNumber(ticket.getSeatNumber())
                .bookingTime(ticket.getBookingTime())
                .price(ticket.getPrice())
                .userId(ticket.getUser().getId())
                .username(ticket.getUser().getUsername())
                .showtimeId(ticket.getShowtime().getId())
                .startTime(ticket.getShowtime().getStartTime())
                .room(ticket.getShowtime().getRoom())
                .movieId(ticket.getShowtime().getMovie().getId())
                .movieTitle(ticket.getShowtime().getMovie().getTitle())
                .build();
    }

    /**
     * Mapping Ticket entity to TicketSimpleResponse
     */
    private TicketSimpleResponse mapToTicketSimpleResponse(Ticket ticket) {
        return TicketSimpleResponse.builder()
                .id(ticket.getId())
                .seatNumber(ticket.getSeatNumber())
                .bookingTime(ticket.getBookingTime())
                .price(ticket.getPrice())
                .movieTitle(ticket.getShowtime().getMovie().getTitle())
                .showStartTime(ticket.getShowtime().getStartTime())
                .room(ticket.getShowtime().getRoom())
                .build();
    }
}