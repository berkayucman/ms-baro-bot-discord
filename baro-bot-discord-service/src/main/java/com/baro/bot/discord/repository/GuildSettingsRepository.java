package com.baro.bot.discord.repository;

import com.baro.bot.discord.model.GuildSettingsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface GuildSettingsRepository extends JpaRepository<GuildSettingsEntity, Long> {

    @Transactional
    @Modifying
    @Query("UPDATE guild_settings SET prefix = :prefix WHERE server_id = :server_id")
    int setPrefix(@Param("prefix") String prefix, @Param("server_id") Long server_id);
}
