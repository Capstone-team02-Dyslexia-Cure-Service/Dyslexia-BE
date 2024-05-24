package com.capstone.dyslexia.global.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Schema(description = "API 응답 템플릿")
@ToString
@Getter
public class ApiResponseTemplate<T> {

    @Schema(description = "HTTP 응답 코드")
    private Integer code;

    @Schema(description = "HTTP 응답 메세지")
    private String message;

    @Schema(description = "타임 스탬프")
    private LocalDateTime timestamp;

    @Schema(description = "응답 data", implementation = Object.class)
    private T data;

    public ApiResponseTemplate(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.data = data;
    }

    public ApiResponseTemplate(Integer code, String message) {
        this.code = code;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public static ApiResponseTemplate<Void> ok() {
        return ok(null);
    }

    public static <T> ApiResponseTemplate<T> ok(T data) {
        return new ApiResponseTemplate<>(200, "OK", data);
    }

    public static <T>ApiResponseTemplate<T> created(T data) {
        return new ApiResponseTemplate<>(201, "Created", data);
    }

}
