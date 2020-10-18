package com.baro.bot.discord.commands;

import com.baro.bot.discord.commands.music.audio.AudioHandler;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.exceptions.PermissionException;

public abstract class MusicCommand extends ACommand {

    public boolean isPlayingMusic(CommandContext ctx) {
        AudioHandler handler = (AudioHandler) ctx.getEvent().getGuild().getAudioManager().getSendingHandler();
        return handler.isMusicPlaying(ctx.getEvent().getJDA());
    }

    private void setupHandler(CommandContext ctx) {
        ctx.getBot().getPlayerManager().setUpHandler(ctx.getEvent().getGuild());
    }

    public boolean init(CommandContext ctx) {
        boolean result = validVoiceState(ctx);
        if (result)
            setupHandler(ctx);
        return result;
    }

    private boolean validVoiceState(CommandContext ctx) {
        VoiceChannel current = ctx.getEvent().getGuild().getSelfMember().getVoiceState().getChannel();
        GuildVoiceState userState = ctx.getEvent().getMember().getVoiceState();
        if (!userState.inVoiceChannel() || userState.isDeafened() || (current != null && !userState.getChannel().equals(current))) {
            ctx.getEvent().getChannel().sendMessage("You must be listening in " +
                    (current == null ? "a voice channel" : "**" + current.getName() + "**") + " to use that!")
                    .queue();
            return false;
        }

        VoiceChannel afkChannel = userState.getGuild().getAfkChannel();
        if (afkChannel != null && afkChannel.equals(userState.getChannel())) {
            ctx.getEvent().getChannel().sendMessage("You cannot use that command in an AFK channel!").queue();
            return false;
        }

        if (!ctx.getEvent().getGuild().getSelfMember().getVoiceState().inVoiceChannel()) {
            try {
                ctx.getEvent().getGuild().getAudioManager().openAudioConnection(userState.getChannel());
                return true;
            } catch (PermissionException ex) {
                ctx.getEvent().getChannel().sendMessage("I am unable to connect to **" + userState.getChannel().getName() + "**!").queue();
                return false;
            }
        }
        return true;
    }
}
