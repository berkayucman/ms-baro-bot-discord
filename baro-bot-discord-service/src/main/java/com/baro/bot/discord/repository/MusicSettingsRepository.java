package com.baro.bot.discord.repository;

import com.baro.bot.discord.model.MusicSettingsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface MusicSettingsRepository extends JpaRepository<MusicSettingsEntity, Long> {

    @Transactional
    @Modifying
    @Query("UPDATE music_settings SET dj_role_id = :dj_role_id WHERE server_id = :server_id")
    int setMusicDjRoleId(@Param("dj_role_id") String dj_role_id, @Param("server_id") Long server_id);

    @Transactional
    @Modifying
    @Query("UPDATE music_settings SET music_text_channel_id = :music_text_channel_id WHERE server_id = :server_id")
    int setMusicTextChannelId(@Param("music_text_channel_id") String music_channel_id, @Param("server_id") Long server_id);

    @Transactional
    @Modifying
    @Query("UPDATE music_settings SET music_voice_channel_id = :music_voice_channel_id WHERE server_id = :server_id")
    int setMusicVoiceChannelId(@Param("music_voice_channel_id") String music_voice_channel_id, @Param("server_id") Long server_id);
}
