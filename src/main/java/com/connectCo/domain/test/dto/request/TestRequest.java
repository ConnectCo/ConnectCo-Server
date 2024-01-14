package com.connectCo.domain.test.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TestRequest {
    public class TestDto {
        @NotNull
        private String id;

        @NotBlank
        private String name;

        @Min(0)
        private int age;

    }
}
