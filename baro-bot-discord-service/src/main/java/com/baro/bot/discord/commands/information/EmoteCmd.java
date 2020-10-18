
package com.baro.bot.discord.commands.information;

import com.baro.bot.discord.commands.ACommand;
import com.baro.bot.discord.commands.CommandCategory;
import com.baro.bot.discord.commands.CommandContext;
import com.baro.bot.discord.commands.ICommand;
import com.baro.bot.discord.util.ColorUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emote;

import java.util.ArrayList;
import java.util.List;

public class EmoteCmd extends ACommand implements ICommand {

    @Override
    public void execute(CommandContext ctx) {
        String str = ctx.getArgs();
        ColorUtil colorUtil = new ColorUtil();
        EmbedBuilder eb = new EmbedBuilder().setColor(colorUtil.getRandomColor());
        if (str.matches("<:.*:\\d+>")) {
            String id = str.replaceAll("<:.*:(\\d+)>", "$1");
            Emote emote = ctx.getEvent().getJDA().getEmoteById(id);
            if (emote == null) {
                sendError(ctx, "invalid characters");
                return;
            }
            eb.setTitle("Unknown emote:")
                    .addField("ID", "emote.getId()", false)
                    .addField("Guild", (emote.getGuild() == null ? "Unknown" : "**" + emote.getGuild().getName() + "**"), false)
                    .addField("URL", emote.getImageUrl(), false);
            ctx.getEvent().getChannel().sendMessage(eb.build()).queue();
            return;
        }
        if (str.codePoints().count() > 10) {
            ctx.getEvent().getChannel().sendMessage("Invalid emote, or input is too long").queue();
            return;
        }
        eb.setTitle("Emoji/Character info");
        List<String> fullString = new ArrayList<>();
        str.codePoints().forEachOrdered(code -> {
            char[] chars = Character.toChars(code);
            String hex = Integer.toHexString(code).toUpperCase();
            while (hex.length() < 4)
                hex = "0" + hex;
            eb.addField("HEX", "`\\u" + hex + "`\t" + Character.getName(code) + "\t" + String.valueOf(chars), false);
            fullString.add("\\u" + hex);
            if (chars.length > 1) {
                String hex0 = Integer.toHexString(chars[0]).toUpperCase();
                String hex1 = Integer.toHexString(chars[1]).toUpperCase();
                while (hex0.length() < 4)
                    hex0 = "0" + hex0;
                while (hex1.length() < 4)
                    hex1 = "0" + hex1;
            }
        });
        eb.addBlankField(false);
        eb.addField("Unicode", "`" + String.join("", fullString) + "`", false);
        ctx.getEvent().getChannel().sendMessage(eb.build()).queue();
    }

    @Override
    public String getName() {
        return "emote";
    }

    @Override
    public String getDescription() {
        return "Views info on an emote or characters";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.INFORMATION;
    }

    @Override
    public boolean getArgs() {
        return true;
    }

    @Override
    public List<String> getExamples() {
        List<String> samples = new ArrayList<>();
        samples.add("`emote <emoji|character>`");
        return samples;
    }
}
