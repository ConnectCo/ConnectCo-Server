package com.connectCo.domain.store.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "가게 ID 응답", example = "{ \"storeId\": 1 }")
public class StoreIdResponse {
    private Long storeId;
}
