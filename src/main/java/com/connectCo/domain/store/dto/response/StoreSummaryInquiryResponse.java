package com.connectCo.domain.store.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class StoreSummaryInquiryResponse {
    private Long storeId;
    private String name;
    private String description;
    private String thumbnail;
}
