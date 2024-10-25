package com.therealtehu.discordbot.TehuBot.service;

import com.therealtehu.discordbot.TehuBot.database.model.MemberData;
import com.therealtehu.discordbot.TehuBot.database.repository.MemberRepository;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.utils.concurrent.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {
    private MemberService memberService;
    @Mock
    private MemberRepository memberRepositoryMock;
    @Mock
    private Member memberMock;
    @Mock
    private MemberData memberDataMock;

    @BeforeEach
    void setup() {
        memberService = new MemberService(memberRepositoryMock);
    }

    @Test
    void addNewMemberIfNotExistsWhenMemberIsInDbReturnsFalse() {
        when(memberMock.getIdLong()).thenReturn(1L);
        when(memberRepositoryMock.existsByUserId(1L)).thenReturn(true);

        assertFalse(memberService.addNewMemberIfNotExists(memberMock));

        verify(memberRepositoryMock, times(1)).existsByUserId(1L);
        verifyNoMoreInteractions(memberRepositoryMock);
    }

    @Test
    void addNewMemberIfNotExistsWhenMemberIsNotInDbSavesMemberAndReturnsTrue() {
        when(memberMock.getIdLong()).thenReturn(1L);
        when(memberRepositoryMock.existsByUserId(1L)).thenReturn(false);

        assertTrue(memberService.addNewMemberIfNotExists(memberMock));

        MemberData expectedMemberData = new MemberData();
        expectedMemberData.setUserId(1L);

        verify(memberRepositoryMock, times(1)).existsByUserId(1L);
        verify(memberRepositoryMock).save(expectedMemberData);
    }

    @Test
    void addMembersFromGuildAddsEveryNewMemberToGuild() {
        Guild mockGuild = Mockito.mock(Guild.class);
        Task<List<Member>> mockTask = Mockito.mock(Task.class);
        Member mockMember2 = Mockito.mock(Member.class);
        List<Member> memberList = List.of(memberMock, mockMember2);
        when(mockGuild.loadMembers()).thenReturn(mockTask);

        doAnswer(ans -> {
            Consumer<List<Member>> callback = (Consumer<List<Member>>) ans.getArgument(0);
            callback.accept(memberList);
            return null;
        }).when(mockTask).onSuccess(any(Consumer.class));

        when(memberMock.getIdLong()).thenReturn(1L);
        when(mockMember2.getIdLong()).thenReturn(2L);
        when(memberRepositoryMock.existsByUserId(1L)).thenReturn(false);
        when(memberRepositoryMock.existsByUserId(2L)).thenReturn(true);

        memberService.addMembersFromGuild(mockGuild);

        MemberData expectedMemberData = new MemberData();
        expectedMemberData.setUserId(1L);

        verify(memberRepositoryMock, times(1)).existsByUserId(1L);
        verify(memberRepositoryMock, times(1)).existsByUserId(2L);
        verify(memberRepositoryMock).save(expectedMemberData);
        verifyNoMoreInteractions(memberRepositoryMock);
    }

    @Test
    void getMemberDataWhenMemberIsNotInDbThrowsException() {
        when(memberRepositoryMock.findByUserId(1L)).thenThrow(NoSuchElementException.class);

        assertThrows(NoSuchElementException.class, () -> memberService.getMemberData(1L));
    }

    @Test
    void getMemberDataWhenMemberIsInDbReturnsMemberData() {
        when(memberRepositoryMock.findByUserId(1L)).thenReturn(Optional.of(memberDataMock));

        assertEquals(memberDataMock, memberService.getMemberData(1L));
    }
}