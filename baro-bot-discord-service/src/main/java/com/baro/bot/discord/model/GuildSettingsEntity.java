package com.baro.bot.discord.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "guild_settings")
@Table(name = "guild_settings")
public class GuildSettingsEntity {

    @Id
    @Column(unique = true, nullable = false)
    private Long serverId;
    @Column(nullable = false)
    private String prefix;
    @Column(nullable = false)
    private String serverName;
    @Column(nullable = false)
    private boolean welcomeMessage;
    @Column(nullable = false)
    private String welcomeAvatar;
    @Column(nullable = false)
    private String ticketMessage;
    @Column(nullable = false)
    private String welcomeDm;
    @Column(nullable = false)
    private String serverAvatar;
    @Column(nullable = false)
    private String djRoleId;
    @Column(nullable = false)
    private String musicTextChannelId;
    @Column(nullable = false)
    private String musicVoiceChannelId;
    @Column(nullable = false)
    private boolean repeatMode;
    @Column(nullable = false)
    private String defaultPlaylist;

    public GuildSettingsEntity() {
    }

    public GuildSettingsEntity(Long serverId, String prefix, String serverName, boolean welcomeMessage, String welcomeAvatar, String ticketMessage, String welcomeDm, String serverAvatar, String djRoleId, String musicTextChannelId, String musicVoiceChannelId, boolean repeatMode, String defaultPlaylist) {
        this.serverId = serverId;
        this.prefix = prefix;
        this.serverName = serverName;
        this.welcomeMessage = welcomeMessage;
        this.welcomeAvatar = welcomeAvatar;
        this.ticketMessage = ticketMessage;
        this.welcomeDm = welcomeDm;
        this.serverAvatar = serverAvatar;
        this.djRoleId = djRoleId;
        this.musicTextChannelId = musicTextChannelId;
        this.musicVoiceChannelId = musicVoiceChannelId;
        this.repeatMode = repeatMode;
        this.defaultPlaylist = defaultPlaylist;
    }


    public Long getServerId() {
        return serverId;
    }

    public void setServerId(Long serverId) {
        this.serverId = serverId;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public boolean isWelcomeMessage() {
        return welcomeMessage;
    }

    public void setWelcomeMessage(boolean welcomeMessage) {
        this.welcomeMessage = welcomeMessage;
    }

    public String getWelcomeAvatar() {
        return welcomeAvatar;
    }

    public void setWelcomeAvatar(String welcomeAvatar) {
        this.welcomeAvatar = welcomeAvatar;
    }

    public String getTicketMessage() {
        return ticketMessage;
    }

    public void setTicketMessage(String ticketMessage) {
        this.ticketMessage = ticketMessage;
    }

    public String getWelcomeDm() {
        return welcomeDm;
    }

    public void setWelcomeDm(String welcomeDm) {
        this.welcomeDm = welcomeDm;
    }

    public String getServerAvatar() {
        return serverAvatar;
    }

    public void setServerAvatar(String serverAvatar) {
        this.serverAvatar = serverAvatar;
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

    public boolean isRepeatMode() {
        return repeatMode;
    }

    public void setRepeatMode(boolean repeatMode) {
        this.repeatMode = repeatMode;
    }

    public String getDefaultPlaylist() {
        return defaultPlaylist;
    }

    public void setDefaultPlaylist(String defaultPlaylist) {
        this.defaultPlaylist = defaultPlaylist;
    }
}
