package com.ssg_order.ssg_order_api.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<Map<String, String>> errorDetails = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> {
                    ErrorCode code = resolveFieldCode(error.getField());
                    return Map.of(
                            "field", error.getField(),
                            "code", code.getCode(),
                            "message", code.getMessage()
                    );
                })
                .toList();

        Map<String, Object> body = Map.of(
                "timestamp", LocalDateTime.now().toString(),
                "status", 400,
                "code", resolveErrorGroupCode(request.getRequestURI()),
                "path", request.getRequestURI(),
                "message", "요청값이 잘못되었습니다.",
                "errors", errorDetails
        );

        return ResponseEntity.badRequest().body(body);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(BusinessException.class)
    public ResponseEntity<?> handleBusinessException(BusinessException ex, HttpServletRequest request) {
        ErrorCode code = ex.getErrorCode();

        Map<String, Object> body = Map.of(
                "timestamp", LocalDateTime.now().toString(),
                "status", code.getStatus(),
                "code", code.getCode(),
                "path", request.getRequestURI(),
                "message", code.getMessage()
        );

        return ResponseEntity.status(code.getStatus()).body(body);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleUnexpectedException(Exception ex, HttpServletRequest request) {
        Map<String, Object> body = Map.of(
                "timestamp", LocalDateTime.now().toString(),
                "status", 500,
                "code", "GEN_500",
                "path", request.getRequestURI(),
                "message", "서버 내부 오류가 발생했습니다."
        );

        return ResponseEntity.status(500).body(body);
    }

    private ErrorCode resolveFieldCode(String field) {
        if (field.matches("orderCreateRequestItemList\\[\\d+\\]\\.ordQty")) {
            return ErrorCode.INVALID_QTY;
        }
        if (field.matches("orderCreateRequestItemList\\[\\d+\\]\\.productNo")) {
            return ErrorCode.INVALID_PRODUCT_NO;
        }
        return ErrorCode.INVALID_PARAM;
    }

    private String resolveErrorGroupCode(String path) {
        if (path.contains("createOrder")) return "ORD_400";
        if (path.contains("cancelOrder")) return "CLM_400";
        return "GEN_400";
    }
}
