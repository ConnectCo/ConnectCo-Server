package com.connectCo.domain.chat.event;

// 메시지 저장이 커밋된 뒤 상대에게 푸시를 보내기 위한 이벤트
public record ChatCreatedEvent(Long chatId, Long receiverProfileId, String message) {
}
