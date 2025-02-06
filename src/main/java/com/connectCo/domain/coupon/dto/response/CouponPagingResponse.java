package com.connectCo.domain.coupon.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CouponPagingResponse<T> {
    private List<T> coupons;

    @Schema(description = "현재 페이지 번호", example = "1")
    private int page;

    @Schema(description = "총 페이지 수", example = "10")
    private int totalPages;

    @Schema(description = "총 요소 수", example = "100")
    private int totalElements;

    @Schema(description = "첫 번째 페이지 여부", example = "true")
    private Boolean isFirst;

    @Schema(description = "마지막 페이지 여부", example = "false")
    private Boolean isLast;
}
