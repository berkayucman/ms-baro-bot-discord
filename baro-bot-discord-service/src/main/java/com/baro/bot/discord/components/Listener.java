package com.baro.bot.discord.components;

import com.baro.bot.discord.config.BotConfig;
import com.baro.bot.discord.service.BaroBot;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.ShutdownEvent;
import net.dv8tion.jda.api.events.channel.category.CategoryCreateEvent;
import net.dv8tion.jda.api.events.channel.category.CategoryDeleteEvent;
import net.dv8tion.jda.api.events.channel.category.update.CategoryUpdateNameEvent;
import net.dv8tion.jda.api.events.channel.category.update.CategoryUpdatePermissionsEvent;
import net.dv8tion.jda.api.events.channel.category.update.CategoryUpdatePositionEvent;
import net.dv8tion.jda.api.events.channel.text.TextChannelCreateEvent;
import net.dv8tion.jda.api.events.channel.text.TextChannelDeleteEvent;
import net.dv8tion.jda.api.events.channel.text.update.*;
import net.dv8tion.jda.api.events.channel.voice.VoiceChannelCreateEvent;
import net.dv8tion.jda.api.events.channel.voice.VoiceChannelDeleteEvent;
import net.dv8tion.jda.api.events.channel.voice.update.VoiceChannelUpdateBitrateEvent;
import net.dv8tion.jda.api.events.channel.voice.update.VoiceChannelUpdateNameEvent;
import net.dv8tion.jda.api.events.channel.voice.update.VoiceChannelUpdatePositionEvent;
import net.dv8tion.jda.api.events.channel.voice.update.VoiceChannelUpdateUserLimitEvent;
import net.dv8tion.jda.api.events.emote.EmoteAddedEvent;
import net.dv8tion.jda.api.events.emote.EmoteRemovedEvent;
import net.dv8tion.jda.api.events.emote.update.EmoteUpdateNameEvent;
import net.dv8tion.jda.api.events.emote.update.EmoteUpdateRolesEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateBoostTimeEvent;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateNicknameEvent;
import net.dv8tion.jda.api.events.guild.update.*;
import net.dv8tion.jda.api.events.guild.voice.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageDeleteEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageUpdateEvent;
import net.dv8tion.jda.api.events.role.RoleCreateEvent;
import net.dv8tion.jda.api.events.role.RoleDeleteEvent;
import net.dv8tion.jda.api.events.role.update.*;
import net.dv8tion.jda.api.events.user.UserActivityStartEvent;
import net.dv8tion.jda.api.events.user.UserTypingEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateAvatarEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateDiscriminatorEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateNameEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateOnlineStatusEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Component
public class Listener extends ListenerAdapter {

    private final static Logger LOGGER = LoggerFactory.getLogger(Listener.class);
    private final BotConfig botConfig;
    private final BaroBot bot;
    private final CommandManager manager;

    public Listener(@Lazy BaroBot bot, BotConfig botConfig, @Lazy CommandManager manager) {
        this.botConfig = botConfig;
        this.bot = bot;
        this.manager = manager;
    }

    //JDA Events
    @Override
    public void onReady(ReadyEvent event) {
        if (event.getJDA().getGuilds().isEmpty()) {
            LOGGER.warn("This bot is not on any guilds! Use the following link to add the bot to your guilds!");
            LOGGER.warn(event.getJDA().getInviteUrl(Permission.ADMINISTRATOR));
        }
//        event.getJDA().getGuilds().forEach(guild -> bot.getPlayerManager().setUpHandler(guild));
    }

    @Override
    public void onMessageReceived(@Nullable MessageReceivedEvent event) {
        if (event.isWebhookMessage() || event.getAuthor().isBot()) return;
        String prefix = botConfig.getPrefix();
        String raw = event.getMessage().getContentRaw();
        if (raw.startsWith(prefix)) {
            manager.handle(event);
        }
//        new VoteSystem().handleVotes(bot, event);
    }

    @Override
    public void onShutdown(ShutdownEvent event) {
        bot.shutdown();
    }

    //User Events
    @Override
    public void onUserUpdateName(@Nonnull UserUpdateNameEvent event) {
//        if (botConfig.isProduction())
//            new Logging().onUserUpdateName(event);
    }

    @Override
    public void onUserUpdateDiscriminator(@Nonnull UserUpdateDiscriminatorEvent event) {
//        if (botConfig.isProduction())
//            new Logging().onUserUpdateDiscriminator(event);
    }

    @Override
    public void onUserUpdateAvatar(@Nonnull UserUpdateAvatarEvent event) {
//        if (botConfig.isProduction())
//            new Logging().onUserUpdateAvatar(event);
    }

    @Override
    public void onUserUpdateOnlineStatus(@Nonnull UserUpdateOnlineStatusEvent event) {
//        if (botConfig.isProduction())
//            new Logging().onUserUpdateOnlineStatus(event, bot);
    }

    @Override
    public void onUserTyping(@Nonnull UserTypingEvent event) {
//        if (botConfig.isProduction())
//            new Logging().onUserTyping(event, bot);
    }

    @Override
    public void onUserActivityStart(@Nonnull UserActivityStartEvent event) {
//        if (botConfig.isProduction())
//            new Logging().onUserActivityStart(event, bot);
    }

    //Guild (TextChannel) Message Events
    @Override
    public void onGuildMessageDelete(GuildMessageDeleteEvent event) {
//        if (botConfig.isProduction())
//            new Logging().onGuildMessageDelete(event, bot, messages);
    }

    @Override
    public void onGuildMessageUpdate(GuildMessageUpdateEvent event) {
//        if (botConfig.isProduction())
//            new Logging().onGuildMessageUpdate(event, messages);
    }

    //TextChannel Events
    @Override
    public void onTextChannelDelete(@Nonnull TextChannelDeleteEvent event) {
//        if (botConfig.isProduction())
//            new Logging().onTextChannelDelete(event);
    }

    @Override
    public void onTextChannelUpdateName(@Nonnull TextChannelUpdateNameEvent event) {
//        if (botConfig.isProduction())
//            new Logging().onTextChannelUpdateName(event);
    }

    @Override
    public void onTextChannelUpdateTopic(@Nonnull TextChannelUpdateTopicEvent event) {
//        if (botConfig.isProduction())
//            new Logging().onTextChannelUpdateTopic(event);
    }

    @Override
    public void onTextChannelUpdatePosition(@Nonnull TextChannelUpdatePositionEvent event) {
//        if (botConfig.isProduction())
//            new Logging().onTextChannelUpdatePosition(event);
    }

    @Override
    public void onTextChannelUpdateNSFW(@Nonnull TextChannelUpdateNSFWEvent event) {
//        if (botConfig.isProduction())
//            new Logging().onTextChannelUpdateNSFW(event, bot);
    }

    @Override
    public void onTextChannelUpdateSlowmode(@Nonnull TextChannelUpdateSlowmodeEvent event) {
//        if (botConfig.isProduction())
//            new Logging().onTextChannelUpdateSlowmode(event, bot);
    }

    @Override
    public void onTextChannelCreate(@Nonnull TextChannelCreateEvent event) {
//        if (botConfig.isProduction())
//            new Logging().onTextChannelCreate(event);
    }

    //VoiceChannel Events
    @Override
    public void onVoiceChannelDelete(@Nonnull VoiceChannelDeleteEvent event) {
//        if (botConfig.isProduction())
//            new Logging().onVoiceChannelDelete(event);
    }

    @Override
    public void onVoiceChannelUpdateName(@Nonnull VoiceChannelUpdateNameEvent event) {
//        if (botConfig.isProduction())
//            new Logging().onVoiceChannelUpdateName(event);
    }

    @Override
    public void onVoiceChannelUpdatePosition(@Nonnull VoiceChannelUpdatePositionEvent event) {
//        if (botConfig.isProduction())
//            new Logging().onVoiceChannelUpdatePosition(event);
    }

    @Override
    public void onVoiceChannelUpdateUserLimit(@Nonnull VoiceChannelUpdateUserLimitEvent event) {
//        if (botConfig.isProduction())
//            new Logging().onVoiceChannelUpdateUserLimit(event);
    }

    @Override
    public void onVoiceChannelUpdateBitrate(@Nonnull VoiceChannelUpdateBitrateEvent event) {
//        if (botConfig.isProduction())
//            new Logging().onVoiceChannelUpdateBitrate(event);
    }

    @Override
    public void onVoiceChannelCreate(@Nonnull VoiceChannelCreateEvent event) {
//        if (botConfig.isProduction())
//            new Logging().onVoiceChannelCreate(event);
    }

    //Category Events
    public void onCategoryDelete(@Nonnull CategoryDeleteEvent event) {
    }

    public void onCategoryUpdateName(@Nonnull CategoryUpdateNameEvent event) {
    }

    public void onCategoryUpdatePosition(@Nonnull CategoryUpdatePositionEvent event) {
    }

    public void onCategoryUpdatePermissions(@Nonnull CategoryUpdatePermissionsEvent event) {
    }

    public void onCategoryCreate(@Nonnull CategoryCreateEvent event) {
    }

    //Guild Update Events
    public void onGuildUpdateAfkChannel(@Nonnull GuildUpdateAfkChannelEvent event) {
    }

    public void onGuildUpdateSystemChannel(@Nonnull GuildUpdateSystemChannelEvent event) {
    }

    public void onGuildUpdateAfkTimeout(@Nonnull GuildUpdateAfkTimeoutEvent event) {
    }

    public void onGuildUpdateExplicitContentLevel(@Nonnull GuildUpdateExplicitContentLevelEvent event) {
    }

    public void onGuildUpdateIcon(@Nonnull GuildUpdateIconEvent event) {
    }

    public void onGuildUpdateMFALevel(@Nonnull GuildUpdateMFALevelEvent event) {
    }

    public void onGuildUpdateName(@Nonnull GuildUpdateNameEvent event) {
    }

    public void onGuildUpdateNotificationLevel(@Nonnull GuildUpdateNotificationLevelEvent event) {
    }

    public void onGuildUpdateOwner(@Nonnull GuildUpdateOwnerEvent event) {
    }

    public void onGuildUpdateRegion(@Nonnull GuildUpdateRegionEvent event) {
    }

    public void onGuildUpdateSplash(@Nonnull GuildUpdateSplashEvent event) {
    }

    public void onGuildUpdateVerificationLevel(@Nonnull GuildUpdateVerificationLevelEvent event) {
    }

    public void onGuildUpdateFeatures(@Nonnull GuildUpdateFeaturesEvent event) {
    }

    public void onGuildUpdateVanityCode(@Nonnull GuildUpdateVanityCodeEvent event) {
    }

    public void onGuildUpdateBanner(@Nonnull GuildUpdateBannerEvent event) {
    }

    public void onGuildUpdateDescription(@Nonnull GuildUpdateDescriptionEvent event) {
    }

    public void onGuildUpdateBoostTier(@Nonnull GuildUpdateBoostTierEvent event) {
    }

    public void onGuildUpdateBoostCount(@Nonnull GuildUpdateBoostCountEvent event) {
    }

    public void onGuildUpdateMaxMembers(@Nonnull GuildUpdateMaxMembersEvent event) {
    }

    public void onGuildUpdateMaxPresences(@Nonnull GuildUpdateMaxPresencesEvent event) {
    }

    //Guild Member Events
    public void onGuildMemberJoin(@Nonnull GuildMemberJoinEvent event) {
//        if (botConfig.isProduction())
//            new Logging().onGuildMemberJoin(event);
    }

    public void onGuildMemberRemove(@Nonnull GuildMemberRemoveEvent event) {
//        if (botConfig.isProduction())
//            new Logging().onGuildMemberLeave(event);
    }

    public void onGuildMemberRoleAdd(@Nonnull GuildMemberRoleAddEvent event) {
    }

    public void onGuildMemberRoleRemove(@Nonnull GuildMemberRoleRemoveEvent event) {
    }

    //Guild Member Update Events
    public void onGuildMemberUpdateNickname(@Nonnull GuildMemberUpdateNicknameEvent event) {
    }

    public void onGuildMemberUpdateBoostTime(@Nonnull GuildMemberUpdateBoostTimeEvent event) {
    }

    //Guild Voice Events
    public void onGuildVoiceUpdate(@Nonnull GuildVoiceUpdateEvent event) {
    }

    public void onGuildVoiceJoin(GuildVoiceJoinEvent event) {
//        if (botConfig.isProduction())
//            new Logging().onGuildVoiceJoin(event);
    }

    public void onGuildVoiceMove(@Nonnull GuildVoiceMoveEvent event) {
    }

    public void onGuildVoiceLeave(GuildVoiceLeaveEvent event) {
//        if (botConfig.isProduction())
//            new Logging().onGuildVoiceLeave(event);
    }

    public void onGuildVoiceMute(@Nonnull GuildVoiceMuteEvent event) {
    }

    public void onGuildVoiceDeafen(@Nonnull GuildVoiceDeafenEvent event) {
    }

    public void onGuildVoiceGuildMute(@Nonnull GuildVoiceGuildMuteEvent event) {
    }

    public void onGuildVoiceGuildDeafen(@Nonnull GuildVoiceGuildDeafenEvent event) {
    }

    public void onGuildVoiceSelfMute(@Nonnull GuildVoiceSelfMuteEvent event) {
    }

    public void onGuildVoiceSelfDeafen(@Nonnull GuildVoiceSelfDeafenEvent event) {
    }

    public void onGuildVoiceSuppress(@Nonnull GuildVoiceSuppressEvent event) {
    }

    //Role events
    public void onRoleCreate(@Nonnull RoleCreateEvent event) {
    }

    public void onRoleDelete(@Nonnull RoleDeleteEvent event) {
    }

    //Role Update Events
    public void onRoleUpdateColor(@Nonnull RoleUpdateColorEvent event) {
    }

    public void onRoleUpdateHoisted(@Nonnull RoleUpdateHoistedEvent event) {
    }

    public void onRoleUpdateMentionable(@Nonnull RoleUpdateMentionableEvent event) {
    }

    public void onRoleUpdateName(@Nonnull RoleUpdateNameEvent event) {
    }

    public void onRoleUpdatePermissions(@Nonnull RoleUpdatePermissionsEvent event) {
    }

    public void onRoleUpdatePosition(@Nonnull RoleUpdatePositionEvent event) {
    }

    //Emote Events
    public void onEmoteAdded(@Nonnull EmoteAddedEvent event) {
    }

    public void onEmoteRemoved(@Nonnull EmoteRemovedEvent event) {
    }

    //Emote Update Events
    public void onEmoteUpdateName(@Nonnull EmoteUpdateNameEvent event) {
    }

    public void onEmoteUpdateRoles(@Nonnull EmoteUpdateRolesEvent event) {
    }
}
