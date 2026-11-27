package com.connectCo.config.security;

import com.connectCo.config.security.auth.PrincipalDetails;
import com.connectCo.config.security.jwt.JwtTokenProvider;
import com.connectCo.domain.chat.repository.ChatRoomRepository;
import com.connectCo.domain.member.entity.Member;
import com.connectCo.domain.member.entity.ProfileType;
import com.connectCo.domain.member.repository.MemberRepository;
import com.connectCo.global.exception.CustomApiException;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import java.security.Principal;

/*
 * 기존에는 /ws가 permitAll이고 STOMP 연결에 인증이 없어, 메시지의 senderId를 아무 값으로나 보낼 수 있었다.
 * - CONNECT: Authorization 헤더의 JWT를 검증하고 세션 사용자로 등록한다.
 * - SUBSCRIBE /topic/{roomId}: 채팅방 참여자만 구독할 수 있다(남의 대화 수신 차단).
 * - SEND: 연결 시 인증된 사용자만 보낼 수 있다. 보낸 사람은 컨트롤러에서 세션 사용자로 정한다.
 */
@Component
@RequiredArgsConstructor
public class StompAuthChannelInterceptor implements ChannelInterceptor {

    private static final String TOPIC_PREFIX = "/topic/";

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final ChatRoomRepository chatRoomRepository;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor == null || accessor.getCommand() == null) {
            return message;
        }
        StompCommand command = accessor.getCommand();
        if (command == StompCommand.CONNECT) {
            accessor.setUser(authenticate(accessor.getFirstNativeHeader("Authorization")));
        } else if (command == StompCommand.SUBSCRIBE) {
            checkSubscribe(requireProfileId(accessor.getUser()), accessor.getDestination());
        } else if (command == StompCommand.SEND) {
            requireProfileId(accessor.getUser());
        }
        return message;
    }

    private UsernamePasswordAuthenticationToken authenticate(String header) {
        if (header == null || !header.startsWith("Bearer ")) {
            throw new MessageDeliveryException("인증 토큰이 없습니다.");
        }
        try {
            String token = header.substring(7);
            jwtTokenProvider.validateToken(token);
            Claims claims = jwtTokenProvider.parseClaims(token);
            Long profileId = claims.get("profileId", Long.class);
            String profileType = claims.get("profileType", String.class);
            if (profileId == null || profileType == null) {
                throw new MessageDeliveryException("프로필이 선택되지 않은 토큰입니다.");
            }
            Member member = memberRepository.getMember(Long.valueOf(claims.getSubject()));
            PrincipalDetails principal = new PrincipalDetails(member, profileId, ProfileType.valueOf(profileType));
            return new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
        } catch (CustomApiException | IllegalArgumentException e) {
            throw new MessageDeliveryException("유효하지 않은 인증 토큰입니다.");
        }
    }

    private void checkSubscribe(Long profileId, String destination) {
        if (destination == null || !destination.startsWith(TOPIC_PREFIX)) {
            throw new MessageDeliveryException("구독할 수 없는 경로입니다.");
        }
        Long roomId;
        try {
            roomId = Long.valueOf(destination.substring(TOPIC_PREFIX.length()));
        } catch (NumberFormatException e) {
            throw new MessageDeliveryException("구독할 수 없는 경로입니다.");
        }
        boolean participant = chatRoomRepository.findById(roomId)
                .map(room -> room.isParticipant(profileId))
                .orElse(false);
        if (!participant) {
            throw new MessageDeliveryException("참여하지 않은 채팅방입니다.");
        }
    }

    public static Long requireProfileId(Principal user) {
        if (user instanceof UsernamePasswordAuthenticationToken auth
                && auth.getPrincipal() instanceof PrincipalDetails principal) {
            return principal.profileId();
        }
        throw new MessageDeliveryException("인증되지 않은 연결입니다.");
    }
}
