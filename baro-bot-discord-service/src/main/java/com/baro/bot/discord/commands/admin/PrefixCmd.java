package com.baro.bot.discord.commands.admin;

import com.baro.bot.discord.commands.ACommand;
import com.baro.bot.discord.commands.CommandCategory;
import com.baro.bot.discord.commands.CommandContext;
import com.baro.bot.discord.commands.ICommand;
import com.baro.bot.discord.config.BotConfig;
import com.baro.bot.discord.repository.GuildSettingsReository;
import net.dv8tion.jda.api.Permission;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class PrefixCmd extends ACommand implements ICommand {

    private final GuildSettingsReository guildSettingsReository;
    private static final int MAX_PREFIX_LENGTH = 3;
    private final BotConfig botConfig;

    public PrefixCmd(GuildSettingsReository guildSettingsReository, BotConfig botConfig) {
        this.guildSettingsReository = guildSettingsReository;
        this.botConfig = botConfig;
    }

    @Override
    public void execute(CommandContext ctx) {

        if (ctx.getArgs().length() > MAX_PREFIX_LENGTH) {
            sendError(ctx, "Prefix can not have more than 3 characters");

            return;
        }

        String guildId = ctx.getEvent().getGuild().getId();
        if (ctx.getArgs().isEmpty()) {
            guildSettingsReository.setPrefix(botConfig.getPrefix(), guildId);
            sendSuccess(ctx, "Prefix cleared.");
        } else {
            guildSettingsReository.setPrefix(ctx.getArgs(), guildId);
            sendSuccess(ctx, "Custom prefix set to `" + ctx.getArgs() + "` on *" + ctx.getEvent().getGuild().getName() + "*");
        }
    }

    @Override
    public String getName() {
        return "prefix";
    }

    @Override
    public String getDescription() {
        return "Change bot's prefix";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.ADMIN;
    }

    @Override
    public boolean getArgs() {
        return false;
    }

    @Override
    public boolean guildOnly() {
        return true;
    }

    @Override
    public EnumSet<Permission> getMemberPermissions() {
        return EnumSet.of(Permission.ADMINISTRATOR);
    }

    @Override
    public List<String> getExamples() {
        List<String> samples = new ArrayList<>();
        samples.add("`prefix` - Clears the server-specific prefix and set the default prefix");
        samples.add("`prefix <PREFIX>` - Sets the server-specific prefix");
        samples.add("");
        samples.add("**HINT:** prefix max length is 3");
        return samples;
    }
}
