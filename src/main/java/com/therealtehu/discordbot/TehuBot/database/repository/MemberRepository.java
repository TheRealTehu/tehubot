package com.therealtehu.discordbot.TehuBot.database.repository;

import com.therealtehu.discordbot.TehuBot.database.model.MemberData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<MemberData, Long> {
    Optional<MemberData> findByUserId(long userId);
    boolean existsByUserId(long userId);
}
