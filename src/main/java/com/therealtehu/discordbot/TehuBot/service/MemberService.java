package com.therealtehu.discordbot.TehuBot.service;

import com.therealtehu.discordbot.TehuBot.database.model.MemberData;
import com.therealtehu.discordbot.TehuBot.database.repository.MemberRepository;
import com.therealtehu.discordbot.TehuBot.service.display.MessageSender;
import com.therealtehu.discordbot.TehuBot.service.poll.MessageReactionEventWithText;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class MemberService {
    private final MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public boolean addNewMemberIfNotExists(Member discordMember) {
        if (!memberRepository.existsByUserId(discordMember.getIdLong())) {
            MemberData memberData = new MemberData();
            memberData.setUserId(discordMember.getIdLong());
            memberRepository.save(memberData);
            return true;
        }
        return false;
    }

    public void addMembersFromGuild(Guild guild) {
        guild.loadMembers()
                .onSuccess(memberList -> memberList
                        .forEach(this::addNewMemberIfNotExists)
                );
    }

    public MemberData getMemberData(long id) throws NoSuchElementException { //TODO: WRITE TESTS
        Optional<MemberData> optionalMember = memberRepository.findByUserId(id);
        if (optionalMember.isPresent()) {
            return optionalMember.get();
        } else {
            throw new NoSuchElementException();
        }
    }
}
