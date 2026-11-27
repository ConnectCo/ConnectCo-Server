package com.connectCo.config.security;

import com.connectCo.config.security.auth.PrincipalDetails;
import com.connectCo.config.security.jwt.JwtTokenProvider;
import com.connectCo.domain.chat.entity.ChatRoom;
import com.connectCo.domain.chat.repository.ChatRoomRepository;
import com.connectCo.domain.member.entity.Member;
import com.connectCo.domain.member.entity.Profile;
import com.connectCo.domain.member.entity.ProfileType;
import com.connectCo.domain.member.repository.MemberRepository;
import com.connectCo.global.exception.CustomApiException;
import com.connectCo.global.exception.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.impl.DefaultClaims;
import org.junit.jupiter.api.Test;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.security.Principal;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class StompAuthChannelInterceptorTest {

    JwtTokenProvider jwt = mock(JwtTokenProvider.class);
    MemberRepository memberRepository = mock(MemberRepository.class);
    ChatRoomRepository chatRoomRepository = mock(ChatRoomRepository.class);
    StompAuthChannelInterceptor interceptor = new StompAuthChannelInterceptor(jwt, memberRepository, chatRoomRepository);
    MessageChannel channel = mock(MessageChannel.class);

    Message<byte[]> frame(StompCommand command, String auth, String destination, Principal user) {
        StompHeaderAccessor accessor = StompHeaderAccessor.create(command);
        if (auth != null) accessor.addNativeHeader("Authorization", auth);
        if (destination != null) accessor.setDestination(destination);
        if (user != null) accessor.setUser(user);
        accessor.setLeaveMutable(true);
        return MessageBuilder.createMessage(new byte[0], accessor.getMessageHeaders());
    }

    Principal user(Long profileId) {
        Member member = mock(Member.class);
        when(member.getRole()).thenReturn(com.connectCo.domain.member.entity.Role.USER);
        PrincipalDetails p = new PrincipalDetails(member, profileId, ProfileType.STORE);
        return new UsernamePasswordAuthenticationToken(p, null, p.getAuthorities());
    }

    @Test
    void 토큰_없이_연결하면_거부한다() {
        assertThatThrownBy(() -> interceptor.preSend(frame(StompCommand.CONNECT, null, null, null), channel))
                .isInstanceOf(MessageDeliveryException.class);
    }

    @Test
    void 유효하지_않은_토큰이면_거부한다() {
        doThrow(new CustomApiException(ErrorCode.UNAUTHORIZED)).when(jwt).validateToken("bad");
        assertThatThrownBy(() -> interceptor.preSend(frame(StompCommand.CONNECT, "Bearer bad", null, null), channel))
                .isInstanceOf(MessageDeliveryException.class);
    }

    @Test
    void 유효한_토큰이면_세션_사용자를_등록한다() {
        Claims claims = new DefaultClaims(Map.of("sub", "5", "profileId", 10L, "profileType", "STORE"));
        when(jwt.parseClaims("ok")).thenReturn(claims);
        Member member = mock(Member.class);
        when(member.getRole()).thenReturn(com.connectCo.domain.member.entity.Role.USER);
        when(memberRepository.getMember(5L)).thenReturn(member);

        Message<?> result = interceptor.preSend(frame(StompCommand.CONNECT, "Bearer ok", null, null), channel);

        Principal registered = StompHeaderAccessor.wrap(result).getUser();
        assertThat(StompAuthChannelInterceptor.requireProfileId(registered)).isEqualTo(10L);
    }

    @Test
    void 참여하지_않은_채팅방은_구독할_수_없다() {
        Profile a = mock(Profile.class); when(a.getId()).thenReturn(1L);
        Profile b = mock(Profile.class); when(b.getId()).thenReturn(2L);
        when(chatRoomRepository.findById(7L)).thenReturn(Optional.of(ChatRoom.builder().id(7L).sender(a).receiver(b).build()));

        interceptor.preSend(frame(StompCommand.SUBSCRIBE, null, "/topic/7", user(2L)), channel);
        assertThatThrownBy(() -> interceptor.preSend(frame(StompCommand.SUBSCRIBE, null, "/topic/7", user(3L)), channel))
                .isInstanceOf(MessageDeliveryException.class);
        assertThatThrownBy(() -> interceptor.preSend(frame(StompCommand.SUBSCRIBE, null, "/topic/abc", user(2L)), channel))
                .isInstanceOf(MessageDeliveryException.class);
    }

    @Test
    void 인증되지_않은_세션은_메시지를_보낼_수_없다() {
        assertThatThrownBy(() -> interceptor.preSend(frame(StompCommand.SEND, null, "/app/7", null), channel))
                .isInstanceOf(MessageDeliveryException.class);
    }
}
