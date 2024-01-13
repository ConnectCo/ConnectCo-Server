package com.connectCo.domain.test.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TestRequest {
    @NotNull(message = "아이디는 필수 입력값입니다.")
    private String id;

    @NotBlank(message = "이름은 필수 입력값입니다.")
    private String name;

    @Min(value = 1, message = "나이는 0보다 커야합니다.")
    private int age;
}
