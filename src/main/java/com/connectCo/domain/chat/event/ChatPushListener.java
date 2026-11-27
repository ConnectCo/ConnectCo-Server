package com.connectCo.domain.chat.event;

import com.connectCo.domain.fcm.service.FcmService;
import com.connectCo.domain.member.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/*
 * 기존에는 메시지 저장 트랜잭션 안에서 FCM을 호출했다.
 * FCM이 실패하면 예외가 나면서 이미 저장한 메시지까지 롤백됐고, FCM 응답을 기다리는 동안 DB 커넥션을 잡고 있었다.
 * 푸시는 메시지가 커밋된 뒤 별도 스레드에서 보내고, 실패해도 메시지에는 영향을 주지 않는다.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ChatPushListener {

    private final ProfileRepository profileRepository;
    private final FcmService fcmService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendPush(ChatCreatedEvent event) {
        try {
            profileRepository.findById(event.receiverProfileId())
                    .map(profile -> profile.getFcmToken())
                    .filter(token -> token != null && !token.isBlank())
                    .ifPresent(token -> fcmService.sendPushNotification(token, "새로운 메시지", event.message()));
        } catch (Exception e) {
            log.warn("채팅 푸시 전송 실패 chatId={} cause={}", event.chatId(), e.getClass().getSimpleName());
        }
    }
}
