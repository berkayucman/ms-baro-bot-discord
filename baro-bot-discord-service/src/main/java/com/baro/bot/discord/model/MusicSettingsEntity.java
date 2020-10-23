package com.baro.bot.discord.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "music_settings")
@Table(name = "music_settings")
public class MusicSettingsEntity {

    @Id
    @Column(unique = true, nullable = false)
    private Long serverId;

    @Column(nullable = false)
    private String djRoleId;
    @Column(nullable = false)
    private String musicTextChannelId;
    @Column(nullable = false)
    private String musicVoiceChannelId;
    @Column(nullable = false)
    private boolean trackRepeat;
    @Column(nullable = false)
    private boolean playlistRepeat;
    @Column(nullable = false)
    private String defaultPlaylist;

    public MusicSettingsEntity() {
    }

    public MusicSettingsEntity(Long serverId, String djRoleId, String musicTextChannelId, String musicVoiceChannelId, String defaultPlaylist, boolean trackRepeat, boolean playlistRepeat) {
        this.serverId = serverId;
        this.djRoleId = djRoleId;
        this.musicTextChannelId = musicTextChannelId;
        this.musicVoiceChannelId = musicVoiceChannelId;
        this.defaultPlaylist = defaultPlaylist;
        this.trackRepeat = trackRepeat;
        this.playlistRepeat = playlistRepeat;
    }

    public Long getServerId() {
        return serverId;
    }

    public void setServerId(Long serverId) {
        this.serverId = serverId;
    }

    public String getDjRoleId() {
        return djRoleId;
    }

    public void setDjRoleId(String djRoleId) {
        this.djRoleId = djRoleId;
    }

    public String getMusicTextChannelId() {
        return musicTextChannelId;
    }

    public void setMusicTextChannelId(String musicTextChannelId) {
        this.musicTextChannelId = musicTextChannelId;
    }

    public String getMusicVoiceChannelId() {
        return musicVoiceChannelId;
    }

    public void setMusicVoiceChannelId(String musicVoiceChannelId) {
        this.musicVoiceChannelId = musicVoiceChannelId;
    }

    public String getDefaultPlaylist() {
        return defaultPlaylist;
    }

    public void setDefaultPlaylist(String defaultPlaylist) {
        this.defaultPlaylist = defaultPlaylist;
    }

    public boolean isTrackRepeat() {
        return trackRepeat;
    }

    public void setTrackRepeat(boolean trackRepeat) {
        this.trackRepeat = trackRepeat;
    }

    public boolean isPlaylistRepeat() {
        return playlistRepeat;
    }

    public void setPlaylistRepeat(boolean playlistRepeat) {
        this.playlistRepeat = playlistRepeat;
    }
}
