package com.example.ss8.exception;

import com.example.ss8.model.dto.ApiResponse;
import com.example.ss8.model.dto.DataError;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Xử lý lỗi đối số truyền vào không hợp lệ (validation)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<DataError>> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex) {

        Map<String, String> details = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            details.put(error.getField(), error.getDefaultMessage());
        });

        DataError dataError = DataError.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message("Dữ liệu đầu vào không hợp lệ")
                .details(details)
                .build();

        ApiResponse<DataError> response = ApiResponse.<DataError>builder()
                .status(false)
                .message("Validation failed")
                .data(dataError)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // Xử lý lỗi vi phạm ràng buộc
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<DataError>> handleConstraintViolation(
            ConstraintViolationException ex) {

        Map<String, String> details = new HashMap<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            String fieldName = violation.getPropertyPath().toString();
            String message = violation.getMessage();
            details.put(fieldName, message);
        }

        DataError dataError = DataError.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message("Vi phạm ràng buộc dữ liệu")
                .details(details)
                .build();

        ApiResponse<DataError> response = ApiResponse.<DataError>builder()
                .status(false)
                .message("Constraint violation")
                .data(dataError)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // Xử lý lỗi không tìm thấy phần tử
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ApiResponse<DataError>> handleNoSuchElement(
            NoSuchElementException ex) {

        Map<String, String> details = new HashMap<>();
        details.put("error", ex.getMessage() != null ? ex.getMessage() : "Phần tử không tồn tại");

        DataError dataError = DataError.builder()
                .code(HttpStatus.NOT_FOUND.value())
                .message("Không tìm thấy dữ liệu")
                .details(details)
                .build();

        ApiResponse<DataError> response = ApiResponse.<DataError>builder()
                .status(false)
                .message("Element not found")
                .data(dataError)
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // Xử lý lỗi phân tích, chuyển đổi ngày giờ không hợp lệ
    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<ApiResponse<DataError>> handleDateTimeParse(
            DateTimeParseException ex) {

        Map<String, String> details = new HashMap<>();
        details.put("parsedString", ex.getParsedString());
        details.put("errorIndex", String.valueOf(ex.getErrorIndex()));
        details.put("error", "Định dạng ngày giờ không hợp lệ");

        DataError dataError = DataError.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message("Lỗi phân tích ngày giờ")
                .details(details)
                .build();

        ApiResponse<DataError> response = ApiResponse.<DataError>builder()
                .status(false)
                .message("Date time parse error")
                .data(dataError)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // Xử lý lỗi không tìm thấy tài nguyên
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiResponse<DataError>> handleNoResourceFound(
            NoResourceFoundException ex) {

        Map<String, String> details = new HashMap<>();
        details.put("resourcePath", ex.getResourcePath());
        details.put("httpMethod", ex.getHttpMethod().name());
        details.put("error", "Tài nguyên không tồn tại");

        DataError dataError = DataError.builder()
                .code(HttpStatus.NOT_FOUND.value())
                .message("Không tìm thấy tài nguyên")
                .details(details)
                .build();

        ApiResponse<DataError> response = ApiResponse.<DataError>builder()
                .status(false)
                .message("Resource not found")
                .data(dataError)
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // Xử lý lỗi yêu cầu không hợp lệ (BadRequestException tùy chỉnh)
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse<DataError>> handleBadRequest(
            BadRequestException ex) {

        Map<String, String> details = new HashMap<>();
        details.put("error", ex.getMessage() != null ? ex.getMessage() : "Yêu cầu không hợp lệ");

        DataError dataError = DataError.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message("Yêu cầu không hợp lệ")
                .details(details)
                .build();

        ApiResponse<DataError> response = ApiResponse.<DataError>builder()
                .status(false)
                .message("Bad request")
                .data(dataError)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // Xử lý các lỗi chung khác
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<DataError>> handleGeneral(Exception ex) {

        Map<String, String> details = new HashMap<>();
        details.put("error", ex.getMessage() != null ? ex.getMessage() : "Đã xảy ra lỗi không xác định");

        DataError dataError = DataError.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("Lỗi hệ thống")
                .details(details)
                .build();

        ApiResponse<DataError> response = ApiResponse.<DataError>builder()
                .status(false)
                .message("Internal server error")
                .data(dataError)
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}