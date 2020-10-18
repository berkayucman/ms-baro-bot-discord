package com.baro.bot.discord.repository;

import com.baro.bot.discord.model.GuildSettingsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface GuildSettingsReository extends JpaRepository<GuildSettingsEntity, Long> {

    @Transactional
    @Modifying
    @Query("UPDATE guild_settings SET prefix = :prefix WHERE server_id = :server_id")
    int setPrefix(@Param("prefix") String prefix, @Param("server_id") String server_id);

    @Transactional
    @Modifying
    @Query("UPDATE guild_settings SET dj_role_id = :dj_role_id WHERE server_id = :server_id")
    int setMusicDjRoleId(@Param("dj_role_id") String dj_role_id, @Param("server_id") String server_id);

    @Transactional
    @Modifying
    @Query("UPDATE guild_settings SET music_channel_id = :music_channel_id WHERE server_id = :server_id")
    int setMusicTextChannelId(@Param("music_channel_id") String music_channel_id, @Param("server_id") String server_id);

    @Transactional
    @Modifying
    @Query("UPDATE guild_settings SET music_voice_channel_id = :music_voice_channel_id WHERE server_id = :server_id")
    int setMusicVoiceChannelId(@Param("music_voice_channel_id") String music_voice_channel_id, @Param("server_id") String server_id);
}
