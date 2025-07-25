package com.example.demo.service;

import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.dto.response.DataResponse;
import com.example.demo.model.entity.Account;
import com.example.demo.model.entity.Transaction;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final NotificationService notificationService;

    @Transactional
    public DataResponse<Transaction> transferMoney(UUID senderId, UUID receiverId, Double amount, String note)
            throws NotFoundException, BadRequestException {

        try {
            // Kiểm tra tài khoản gửi
            Account sender = accountRepository.findById(senderId)
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy tài khoản gửi với ID: " + senderId));

            // Kiểm tra tài khoản nhận
            Account receiver = accountRepository.findById(receiverId)
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy tài khoản nhận với ID: " + receiverId));

            // Kiểm tra số tiền hợp lệ
            if (amount == null || amount <= 0) {
                throw new BadRequestException("Số tiền chuyển phải lớn hơn 0");
            }

            // Kiểm tra số dư
            if (sender.getMoney() == null || sender.getMoney() < amount) {
                // Tạo giao dịch thất bại
                Transaction failedTransaction = createTransaction(sender, receiver, amount, note, "thất bại");
                log.error("Giao dịch thất bại: Số dư không đủ. Tài khoản {}, số dư hiện tại: {}, số tiền cần chuyển: {}",
                        senderId, sender.getMoney(), amount);

                return DataResponse.<Transaction>builder()
                        .code(201)
                        .key("transaction")
                        .data(failedTransaction)
                        .build();
            }

            // Thực hiện chuyển tiền
            sender.setMoney(sender.getMoney() - amount);
            receiver.setMoney((receiver.getMoney() != null ? receiver.getMoney() : 0.0) + amount);

            // Lưu cập nhật tài khoản
            accountRepository.save(sender);
            accountRepository.save(receiver);

            // Tạo giao dịch thành công
            Transaction successTransaction = createTransaction(sender, receiver, amount, note, "thành công");

            // Gửi thông báo
            String senderNotification = String.format("Bạn đã chuyển %.2f VND cho %s. Số dư hiện tại: %.2f VND",
                    amount, receiver.getFullName(), sender.getMoney());
            String receiverNotification = String.format("Bạn đã nhận %.2f VND từ %s. Số dư hiện tại: %.2f VND",
                    amount, sender.getFullName(), receiver.getMoney());

            notificationService.createNotification(sender, senderNotification);
            notificationService.createNotification(receiver, receiverNotification);

            log.info("Giao dịch thành công: {} chuyển {} VND cho {}",
                    sender.getFullName(), amount, receiver.getFullName());

            return DataResponse.<Transaction>builder()
                    .code(201)
                    .key("transaction")
                    .data(successTransaction)
                    .build();

        } catch (NotFoundException | BadRequestException e) {
            throw e;
        } catch (Exception e) {
            log.error("Lỗi hệ thống khi thực hiện giao dịch: {}", e.getMessage(), e);
            throw new RuntimeException("Lỗi hệ thống khi thực hiện giao dịch");
        }
    }

    private Transaction createTransaction(Account sender, Account receiver, Double amount, String note, String status) {
        Transaction transaction = new Transaction();
        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setMoney(amount);
        transaction.setNote(note);
        transaction.setStatus(status);
        transaction.setCreatedAt(LocalDateTime.now());

        return transactionRepository.save(transaction);
    }
}