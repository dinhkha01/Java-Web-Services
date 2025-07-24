package com.example.ss9.controller;

import com.example.ss9.exception.BadRequestException;
import com.example.ss9.exception.NotFoundException;
import com.example.ss9.model.entity.User;
import com.example.ss9.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    /**
     * Lấy danh sách tất cả users
     */
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        log.info("Receiving request to get all users");
        try {
            List<User> users = userService.getAllUsers();
            log.info("Successfully retrieved {} users", users.size());
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            log.error("Error occurred while getting all users", e);
            throw e;
        }
    }

    /**
     * Lấy user theo ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) throws NotFoundException {
        log.info("Receiving request to get user with id: {}", id);
        try {
            User user = userService.getUserById(id);
            log.info("Successfully retrieved user: {}", user.getEmail());
            return ResponseEntity.ok(user);
        } catch (NotFoundException e) {
            log.warn("User not found with id: {}", id);
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error occurred while getting user with id: {}", id, e);
            throw e;
        }
    }

    /**
     * Tạo user mới
     */
    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) throws BadRequestException {
        log.info("Receiving request to create user with email: {}", user.getEmail());
        try {
            User createdUser = userService.createUser(user);
            log.info("Successfully created user with id: {} and email: {}",
                    createdUser.getId(), createdUser.getEmail());
            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        } catch (BadRequestException e) {
            log.warn("Bad request while creating user: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error occurred while creating user with email: {}",
                    user.getEmail(), e);
            throw e;
        }
    }

    /**
     * Cập nhật user
     */
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id,
                                           @Valid @RequestBody User user)
            throws NotFoundException, BadRequestException {
        log.info("Receiving request to update user with id: {}", id);
        try {
            User updatedUser = userService.updateUser(id, user);
            log.info("Successfully updated user with id: {} and email: {}",
                    updatedUser.getId(), updatedUser.getEmail());
            return ResponseEntity.ok(updatedUser);
        } catch (NotFoundException e) {
            log.warn("Cannot update - user not found with id: {}", id);
            throw e;
        } catch (BadRequestException e) {
            log.warn("Bad request while updating user with id: {} - {}", id, e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error occurred while updating user with id: {}", id, e);
            throw e;
        }
    }

    /**
     * Xóa user
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) throws NotFoundException {
        log.info("Receiving request to delete user with id: {}", id);
        try {
            userService.deleteUser(id);
            log.info("Successfully deleted user with id: {}", id);
            return ResponseEntity.noContent().build();
        } catch (NotFoundException e) {
            log.warn("Cannot delete - user not found with id: {}", id);
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error occurred while deleting user with id: {}", id, e);
            throw e;
        }
    }

    /**
     * Tìm kiếm user theo email
     */
    @GetMapping("/search")
    public ResponseEntity<User> findUserByEmail(@RequestParam String email)
            throws BadRequestException, NotFoundException {
        log.info("Receiving request to search user by email: {}", email);

        if (email == null || email.trim().isEmpty()) {
            log.warn("Invalid email parameter: {}", email);
            throw new BadRequestException("Email parameter is required",
                    Map.of("email", "Email không được để trống"));
        }

        try {
            User user = userService.findUserByEmail(email);
            log.info("Successfully found user by email: {}", email);
            return ResponseEntity.ok(user);
        } catch (NotFoundException e) {
            log.warn("User not found with email: {}", email);
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error occurred while searching user by email: {}", email, e);
            throw e;
        }
    }
}