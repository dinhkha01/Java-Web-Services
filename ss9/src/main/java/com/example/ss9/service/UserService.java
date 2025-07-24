package com.example.ss9.service;

import com.example.ss9.exception.BadRequestException;
import com.example.ss9.exception.NotFoundException;
import com.example.ss9.model.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Service
@Slf4j
public class UserService {

    // Simulate database với HashMap
    private final Map<Long, User> userDatabase = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public UserService() {
        // Khởi tạo một số user mẫu
        initSampleData();
    }

    private void initSampleData() {
        log.info("Initializing sample user data");

        User user1 = User.builder()
                .id(idGenerator.getAndIncrement())
                .name("Nguyễn Văn A")
                .email("nguyenvana@example.com")
                .phone("0901234567")
                .age(25)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        User user2 = User.builder()
                .id(idGenerator.getAndIncrement())
                .name("Trần Thị B")
                .email("tranthib@example.com")
                .phone("0912345678")
                .age(30)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        userDatabase.put(user1.getId(), user1);
        userDatabase.put(user2.getId(), user2);

        log.info("Sample data initialized with {} users", userDatabase.size());
    }

    public List<User> getAllUsers() {
        log.debug("Getting all users from database");
        List<User> users = new ArrayList<>(userDatabase.values());
        log.debug("Retrieved {} users from database", users.size());
        return users;
    }

    public User getUserById(Long id) throws NotFoundException {
        log.debug("Getting user by id: {}", id);

        User user = userDatabase.get(id);
        if (user == null) {
            log.warn("User not found in database with id: {}", id);
            throw new NotFoundException("Người dùng không tồn tại với ID: " + id);
        }

        log.debug("Found user: {} with email: {}", user.getName(), user.getEmail());
        return user;
    }

    public User createUser(User user) throws BadRequestException {
        log.debug("Creating new user with email: {}", user.getEmail());

        // Kiểm tra email đã tồn tại
        if (isEmailExists(user.getEmail())) {
            log.warn("Attempt to create user with existing email: {}", user.getEmail());
            Map<String, String> details = new HashMap<>();
            details.put("email", "Email đã tồn tại trong hệ thống");
            throw new BadRequestException("Email đã được sử dụng", details);
        }

        // Validation bổ sung
        validateUserData(user);

        // Tạo user mới
        Long newId = idGenerator.getAndIncrement();
        user.setId(newId);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        userDatabase.put(newId, user);

        log.info("Created new user with id: {} and email: {}", newId, user.getEmail());
        return user;
    }

    public User updateUser(Long id, User updatedUser) throws NotFoundException, BadRequestException {
        log.debug("Updating user with id: {}", id);

        User existingUser = getUserById(id); // Throws NotFoundException if not found

        // Kiểm tra email trùng lặp (nếu email thay đổi)
        if (!existingUser.getEmail().equals(updatedUser.getEmail()) &&
                isEmailExists(updatedUser.getEmail())) {
            log.warn("Attempt to update user {} with existing email: {}", id, updatedUser.getEmail());
            Map<String, String> details = new HashMap<>();
            details.put("email", "Email đã tồn tại trong hệ thống");
            throw new BadRequestException("Email đã được sử dụng bởi người dùng khác", details);
        }

        // Validation bổ sung
        validateUserData(updatedUser);

        // Cập nhật thông tin
        existingUser.setName(updatedUser.getName());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setPhone(updatedUser.getPhone());
        existingUser.setAge(updatedUser.getAge());
        existingUser.setUpdatedAt(LocalDateTime.now());

        userDatabase.put(id, existingUser);

        log.info("Updated user with id: {} and email: {}", id, existingUser.getEmail());
        return existingUser;
    }

    public void deleteUser(Long id) throws NotFoundException {
        log.debug("Deleting user with id: {}", id);

        User user = getUserById(id); // Throws NotFoundException if not found
        userDatabase.remove(id);

        log.info("Deleted user with id: {} and email: {}", id, user.getEmail());
    }

    public User findUserByEmail(String email) throws NotFoundException {
        log.debug("Searching user by email: {}", email);

        User user = userDatabase.values().stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);

        if (user == null) {
            log.warn("User not found with email: {}", email);
            throw new NotFoundException("Không tìm thấy người dùng với email: " + email);
        }

        log.debug("Found user: {} with email: {}", user.getName(), user.getEmail());
        return user;
    }

    private boolean isEmailExists(String email) {
        return userDatabase.values().stream()
                .anyMatch(user -> user.getEmail().equalsIgnoreCase(email));
    }

    private void validateUserData(User user) throws BadRequestException {
        Map<String, String> errors = new HashMap<>();

        // Validation tuổi
        if (user.getAge() != null && (user.getAge() < 0 || user.getAge() > 150)) {
            errors.put("age", "Tuổi phải từ 0 đến 150");
        }

        // Validation số điện thoại
        if (user.getPhone() != null && !user.getPhone().matches("^0\\d{9,11}$")) {
            errors.put("phone", "Số điện thoại phải bắt đầu bằng 0 và có 10-12 chữ số");
        }

        if (!errors.isEmpty()) {
            log.warn("User data validation failed: {}", errors);
            throw new BadRequestException("Dữ liệu không hợp lệ", errors);
        }
    }
}