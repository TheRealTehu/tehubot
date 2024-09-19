package com.therealtehu.discordbot.TehuBot.service;

import com.therealtehu.discordbot.TehuBot.database.model.MemberData;
import com.therealtehu.discordbot.TehuBot.database.repository.MemberRepository;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.utils.concurrent.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class MemberServiceTest {
    private MemberService memberService;
    private final MemberRepository mockMemberRepository = Mockito.mock(MemberRepository.class);
    private final Member mockMember = Mockito.mock(Member.class);

    @BeforeEach
    void setup() {
        memberService = new MemberService(mockMemberRepository);
    }

    @Test
    void addNewMemberIfNotExistsWhenMemberIsInDbReturnsFalse() {
        when(mockMember.getIdLong()).thenReturn(1L);
        when(mockMemberRepository.existsByUserId(1L)).thenReturn(true);

        assertFalse(memberService.addNewMemberIfNotExists(mockMember));

        verify(mockMemberRepository, times(1)).existsByUserId(1L);
        verifyNoMoreInteractions(mockMemberRepository);
    }

    @Test
    void addNewMemberIfNotExistsWhenMemberIsNotInDbSavesMemberAndReturnsTrue() {
        when(mockMember.getIdLong()).thenReturn(1L);
        when(mockMemberRepository.existsByUserId(1L)).thenReturn(false);

        assertTrue(memberService.addNewMemberIfNotExists(mockMember));

        MemberData expectedMemberData = new MemberData();
        expectedMemberData.setUserId(1L);

        verify(mockMemberRepository, times(1)).existsByUserId(1L);
        verify(mockMemberRepository).save(expectedMemberData);
    }

    @Test
    void addMembersFromGuildAddsEveryNewMemberToGuild() {
        Guild mockGuild = Mockito.mock(Guild.class);
        Task<List<Member>> mockTask = Mockito.mock(Task.class);
        Member mockMember2 = Mockito.mock(Member.class);
        List<Member> memberList = List.of(mockMember, mockMember2);
        when(mockGuild.loadMembers()).thenReturn(mockTask);

        doAnswer(ans -> {
            Consumer<List<Member>> callback = (Consumer<List<Member>>) ans.getArgument(0);
            callback.accept(memberList);
            return null;
        }).when(mockTask).onSuccess(any(Consumer.class));

        when(mockMember.getIdLong()).thenReturn(1L);
        when(mockMember2.getIdLong()).thenReturn(2L);
        when(mockMemberRepository.existsByUserId(1L)).thenReturn(false);
        when(mockMemberRepository.existsByUserId(2L)).thenReturn(true);

        memberService.addMembersFromGuild(mockGuild);

        MemberData expectedMemberData = new MemberData();
        expectedMemberData.setUserId(1L);

        verify(mockMemberRepository, times(1)).existsByUserId(1L);
        verify(mockMemberRepository, times(1)).existsByUserId(2L);
        verify(mockMemberRepository).save(expectedMemberData);
        verifyNoMoreInteractions(mockMemberRepository);
    }
}