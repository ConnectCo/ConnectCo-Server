package com.connectCo.domain.store.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class StoreIdResponse {
    private UUID storeId;
}
