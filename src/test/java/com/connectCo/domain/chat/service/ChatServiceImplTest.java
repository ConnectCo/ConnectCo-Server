package com.connectCo.domain.chat.service;

import com.connectCo.domain.chat.entity.Chat;
import com.connectCo.domain.chat.entity.ChatRoom;
import com.connectCo.domain.chat.event.ChatCreatedEvent;
import com.connectCo.domain.chat.mapper.ChatMapper;
import com.connectCo.domain.chat.repository.ChatRepository;
import com.connectCo.domain.chat.repository.ChatRoomRepository;
import com.connectCo.domain.member.entity.Profile;
import com.connectCo.global.exception.CustomApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ChatServiceImplTest {

    ChatRepository chatRepository = mock(ChatRepository.class);
    ChatRoomRepository chatRoomRepository = mock(ChatRoomRepository.class);
    ApplicationEventPublisher publisher = mock(ApplicationEventPublisher.class);
    ChatServiceImpl service;
    ChatRoom room;

    @BeforeEach
    void setUp() {
        service = new ChatServiceImpl(chatRepository, new ChatMapper(), chatRoomRepository, publisher);
        Profile a = mock(Profile.class); when(a.getId()).thenReturn(1L);
        Profile b = mock(Profile.class); when(b.getId()).thenReturn(2L);
        room = ChatRoom.builder().id(7L).sender(a).receiver(b).build();
        when(chatRoomRepository.findById(7L)).thenReturn(Optional.of(room));
        when(chatRepository.save(any(Chat.class))).thenAnswer(inv -> {
            Chat c = inv.getArgument(0);
            return Chat.builder().id(500L).chatRoom(c.getChatRoom()).senderId(c.getSenderId()).message(c.getMessage()).build();
        });
    }

    @Test
    void 받는_사람은_요청값이_아니라_채팅방에서_정하고_푸시는_이벤트로_넘긴다() {
        service.createChat(2L, 7L, "안녕하세요");

        ArgumentCaptor<ChatCreatedEvent> captor = ArgumentCaptor.forClass(ChatCreatedEvent.class);
        verify(publisher).publishEvent(captor.capture());
        assertThat(captor.getValue().receiverProfileId()).isEqualTo(1L);
        assertThat(captor.getValue().chatId()).isEqualTo(500L);
        assertThat(room.getRecentMessage()).isEqualTo("안녕하세요");
    }

    @Test
    void 방에_참여하지_않은_사람은_메시지를_보낼_수_없다() {
        assertThatThrownBy(() -> service.createChat(3L, 7L, "x"))
                .isInstanceOf(CustomApiException.class);
        verify(chatRepository, never()).save(any());
        verifyNoInteractions(publisher);
    }
}
