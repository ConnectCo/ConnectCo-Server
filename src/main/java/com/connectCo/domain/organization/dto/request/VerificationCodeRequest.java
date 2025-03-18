package com.connectCo.domain.organization.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class VerificationCodeRequest {

    @Schema(description = "이메일 인증코드", example = "1234")
    @NotNull(message = "인증코드는 필수 입력값입니다.")
    @Min(value = 1000, message = "인증코드는 4자리 숫자여야 합니다.")
    @Max(value = 9999, message = "인증코드는 4자리 숫자여야 합니다.")
    private Integer code;
} 