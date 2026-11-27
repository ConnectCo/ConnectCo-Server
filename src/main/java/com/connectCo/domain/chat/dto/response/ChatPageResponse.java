package com.connectCo.domain.chat.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ChatPageResponse {
    @Schema(description = "메시지 목록(오래된 순)")
    private List<ChatResponse> chatList;

    @Schema(description = "더 오래된 메시지를 조회할 때 넘길 커서. 없으면 null")
    private Long nextCursor;

    @Schema(description = "더 오래된 메시지가 있는지")
    private boolean hasNext;
}
