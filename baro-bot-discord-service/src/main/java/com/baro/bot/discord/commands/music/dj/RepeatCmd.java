package com.baro.bot.discord.commands.music.dj;

import com.baro.bot.discord.commands.CommandCategory;
import com.baro.bot.discord.commands.CommandContext;
import com.baro.bot.discord.commands.ICommand;
import com.baro.bot.discord.commands.MusicCommand;
import com.baro.bot.discord.commands.music.QueueCmd;

import java.util.ArrayList;
import java.util.List;

/*
 TODO: This is playlist repeat
 * rename it to PlaylistRepeatCmd
 * Add only 1 song repeat (normal repeat)
 */
public class RepeatCmd extends MusicCommand implements ICommand {

    // args: "[on|off]"
    @Override
    public void execute(CommandContext ctx) {
        if (!init(ctx)) return;

        boolean value;
        if (ctx.getArgs().isEmpty()) {
            value = !QueueCmd.repeatMode;
        } else if (ctx.getArgs().equalsIgnoreCase("true") || ctx.getArgs().equalsIgnoreCase("on")) {
            value = true;
        } else if (ctx.getArgs().equalsIgnoreCase("false") || ctx.getArgs().equalsIgnoreCase("off")) {
            value = false;
        } else {
            ctx.getEvent().getChannel().sendMessage("Valid options are `on` or `off` (or leave empty to toggle)").queue();
            return;
        }
        QueueCmd.repeatMode = value;
        ctx.getEvent().getChannel().sendMessage("Repeat mode is now `" + (value ? "ON" : "OFF") + "`").queue();
    }

    @Override
    public String getName() {
        return "repeat";
    }

    @Override
    public String getDescription() {
        return "Re-adds music to the queue when finished";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.DJ;
    }

    @Override
    public List<String> getExamples() {
        List<String> samples = new ArrayList<>();
        samples.add("`repeat ON|OFF` - Puts the player in (or takes it out of) repeat mode. In repeat mode, when songs end naturally (not removed or skipped), they get put back into the queue.");
        return samples;
    }
}
