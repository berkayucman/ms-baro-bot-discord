package com.baro.bot.discord.commands.admin;

import com.baro.bot.discord.commands.ACommand;
import com.baro.bot.discord.commands.CommandCategory;
import com.baro.bot.discord.commands.CommandContext;
import com.baro.bot.discord.commands.ICommand;
import com.baro.bot.discord.repository.GuildSettingsReository;
import com.baro.bot.discord.util.FormatUtil;
import com.jagrosh.jdautilities.commons.utils.FinderUtil;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.VoiceChannel;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class MusicVoiceChannelIdCmd extends ACommand implements ICommand {

    private final GuildSettingsReository guildSettingsReository;

    public MusicVoiceChannelIdCmd(GuildSettingsReository guildSettingsReository) {
        this.guildSettingsReository = guildSettingsReository;
    }

    @Override
    public void execute(CommandContext ctx) {
        String guildId = ctx.getEvent().getGuild().getId();
        if (ctx.getArgs().equalsIgnoreCase("none")) {
            guildSettingsReository.setMusicVoiceChannelId("", guildId);
            sendSuccess(ctx, "Music can now be played in any channel");
        } else {
            List<VoiceChannel> list = FinderUtil.findVoiceChannels(ctx.getArgs(), ctx.getEvent().getGuild());
            if (list.isEmpty()) {
                sendError(ctx, "No Voice Channels found matching \"" + ctx.getArgs() + "\"");
            } else if (list.size() > 1) {
                ctx.getEvent().getChannel().sendMessage(FormatUtil.listOfVChannels(list, ctx.getArgs())).queue();
            } else {
                guildSettingsReository.setMusicVoiceChannelId(list.get(0).getId(), guildId);
                sendSuccess(ctx, "Music can now only be played in **" + list.get(0).getName() + "**");
            }
        }
    }

    @Override
    public String getName() {
        return "setvc";
    }

    @Override
    public String getDescription() {
        return "Sets the voice channel for playing music";
    }

    @Override
    public boolean getArgs() {
        return true;
    }

    @Override
    public boolean guildOnly() {
        return true;
    }

    @Override
    public boolean isNsfw() {
        return false;
    }

    @Nullable
    @Override
    public EnumSet<Permission> getMemberPermissions() {
        return EnumSet.of(Permission.ADMINISTRATOR);
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.ADMIN;
    }

    @Override
    public List<String> getExamples() {
        List<String> samples = new ArrayList<>();
        samples.add("`setvc none` - Clears the voice channel for playing music. This means that users can play music from any channel that the bot can connect to (if the bot is not already in a different channel)");
        samples.add("`setvc <channel_id | channel_name>` - Sets the voice channel for playing music. When set, the bot will only connect to the specified channel when users attempt to play music.");
        return samples;
    }
}
