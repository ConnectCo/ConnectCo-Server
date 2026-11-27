package com.connectCo.domain.chat.service;

import com.connectCo.domain.chat.dto.response.ChatPageResponse;
import com.connectCo.domain.chat.dto.response.ChatResponse;
import com.connectCo.domain.chat.entity.Chat;
import com.connectCo.domain.chat.entity.ChatRoom;
import com.connectCo.domain.chat.mapper.ChatMapper;
import com.connectCo.domain.chat.mapper.ChatRoomMapper;
import com.connectCo.domain.chat.repository.ChatRepository;
import com.connectCo.domain.chat.repository.ChatRoomRepository;
import com.connectCo.domain.member.entity.Profile;
import com.connectCo.domain.member.repository.ProfileRepository;
import com.connectCo.global.exception.CustomApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.stream.LongStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class ChatRoomServiceImplTest {

    ChatRoomRepository chatRoomRepository = mock(ChatRoomRepository.class);
    ChatRepository chatRepository = mock(ChatRepository.class);
    ChatRoomServiceImpl service;
    ChatRoom room;

    @BeforeEach
    void setUp() {
        service = new ChatRoomServiceImpl(chatRoomRepository, chatRepository, new ChatRoomMapper(),
                mock(ProfileRepository.class), new ChatMapper());
        room = ChatRoom.builder().id(7L).sender(profile(1L)).receiver(profile(2L)).build();
        when(chatRoomRepository.findById(7L)).thenReturn(Optional.of(room));
    }

    static Profile profile(Long id) {
        Profile p = mock(Profile.class);
        when(p.getId()).thenReturn(id);
        return p;
    }

    static List<Chat> chatsDesc(long from, long to) {
        return LongStream.iterate(from, i -> i >= to, i -> i - 1)
                .mapToObj(i -> Chat.builder().id(i).senderId(1L).message("m" + i).build())
                .toList();
    }

    @Test
    void 첫_페이지는_최신_size개를_오래된_순으로_주고_다음_커서를_준다() {
        // 저장소는 size+1개(100~70)를 최신순으로 돌려준다
        when(chatRepository.findLatest(eq(7L), any(Pageable.class))).thenReturn(chatsDesc(100, 70));

        ChatPageResponse page = service.getChatsByChatRoom(1L, 7L, null, 30);

        assertThat(page.getChatList()).extracting(ChatResponse::getChatId)
                .containsExactlyElementsOf(LongStream.rangeClosed(71, 100).boxed().toList());
        assertThat(page.isHasNext()).isTrue();
        assertThat(page.getNextCursor()).isEqualTo(71L);
        verify(chatRepository).findLatest(7L, org.springframework.data.domain.PageRequest.of(0, 31));
    }

    @Test
    void 커서를_넘기면_그보다_오래된_메시지를_읽고_마지막_페이지면_커서가_없다() {
        when(chatRepository.findBefore(eq(7L), eq(71L), any(Pageable.class))).thenReturn(chatsDesc(70, 61));

        ChatPageResponse page = service.getChatsByChatRoom(2L, 7L, 71L, 30);

        assertThat(page.getChatList()).hasSize(10);
        assertThat(page.getChatList().get(0).getChatId()).isEqualTo(61L);
        assertThat(page.isHasNext()).isFalse();
        assertThat(page.getNextCursor()).isNull();
    }

    @Test
    void 참여하지_않은_방의_메시지는_조회할_수_없다() {
        assertThatThrownBy(() -> service.getChatsByChatRoom(3L, 7L, null, 30))
                .isInstanceOf(CustomApiException.class);
        verifyNoInteractions(chatRepository);
    }

    @Test
    void 조회_개수는_1에서_100_사이만_허용한다() {
        assertThatThrownBy(() -> service.getChatsByChatRoom(1L, 7L, null, 0)).isInstanceOf(CustomApiException.class);
        assertThatThrownBy(() -> service.getChatsByChatRoom(1L, 7L, null, 101)).isInstanceOf(CustomApiException.class);
    }

    @Test
    void 빌더로_만든_새_채팅방도_메시지_목록이_null이_아니다() {
        assertThat(ChatRoom.builder().build().getChats()).isNotNull();
    }
}
