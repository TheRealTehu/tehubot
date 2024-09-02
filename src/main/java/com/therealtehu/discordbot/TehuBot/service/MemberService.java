package com.therealtehu.discordbot.TehuBot.service;

import com.therealtehu.discordbot.TehuBot.database.model.MemberData;
import com.therealtehu.discordbot.TehuBot.database.repository.MemberRepository;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private final MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public boolean addNewMemberIfNotExists(Member jpaMember) {
        if(!memberRepository.existsByUserId(jpaMember.getIdLong())) {
            MemberData memberData = new MemberData();
            memberData.setUserId(jpaMember.getIdLong());
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
}
