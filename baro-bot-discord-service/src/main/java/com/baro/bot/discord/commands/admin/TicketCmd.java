package com.baro.bot.discord.commands.admin;

import com.baro.bot.discord.commands.ACommand;
import com.baro.bot.discord.commands.CommandCategory;
import com.baro.bot.discord.commands.CommandContext;
import com.baro.bot.discord.commands.ICommand;
import com.baro.bot.discord.util.ColorUtil;
import com.baro.bot.discord.util.EmoteUtil;
import com.baro.bot.discord.util.Flags;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class TicketCmd extends ACommand implements ICommand {

    private void createTicket(CommandContext ctx) {
        ctx.getEvent().getGuild().createCategory("Tickets").queue(category -> category.createTextChannel("ticket").queue(channel -> {
            sendSuccess(ctx, "Your new ticket channel is " + channel.getAsMention());
            channel.getManager().setTopic(Flags.BAROBOT_TICKET_CHANNEL.toString()).setSlowmode(channel.MAX_SLOWMODE).queue();
            Role everyone = ctx.getEvent().getGuild().getPublicRole();
            List<Permission> deny = new ArrayList<>();
            deny.add(Permission.MESSAGE_ADD_REACTION);
            deny.add(Permission.MESSAGE_WRITE);
            channel.getManager().putPermissionOverride(everyone, null, deny).queue();
            channel.sendMessage(getTicketEmbed(ctx.getBot().getJda().getSelfUser().getName(), ctx.getBot().getJda().getSelfUser().getEffectiveAvatarUrl()).build()).queue(message -> {
                message.addReaction(new EmoteUtil(ctx.getBot()).getEmote("paladin")).queue();
            });
        }));
    }

    private void deleteTicket(CommandContext ctx) {
        if (ctx.getEvent().getChannel().getName().toLowerCase().contains("ticket-")) {
            try {
                ctx.getEvent().getTextChannel().delete().queue();
            } catch (Exception ex) {
                // channel already deleted - ignore
            }
        } else {
            sendError(ctx, "This is not a ticket channel");
        }
    }

    private EmbedBuilder getTicketEmbed(String name, String avatar) {
        ColorUtil colorUtil = new ColorUtil();
        return new EmbedBuilder().setColor(colorUtil.getRandomColor())
                .setDescription("```http\nReact to open a ticket!\n```")
                .setImage("https://cdn.discordapp.com/attachments/396964573007052800/547342790904774671/Loading.gif")
                .setAuthor(name + " Ticket System", avatar, avatar);
    }

    @Override
    public void execute(CommandContext ctx) {
        switch (ctx.getArgs().toLowerCase()) {
            case "delete":
            case "remove":
            case "close":
                deleteTicket(ctx);
                break;
            case "create":
            case "add":
            case "make":
                createTicket(ctx);
                break;
            default:
                sendError(ctx, "First argument must be **create** or **close**");
        }
    }

    @Override
    public String getName() {
        return "ticket";
    }

    @Override
    public String getDescription() {
        return "Enable or disable ticket system in your server";
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

    @Nullable
    @Override
    public EnumSet<Permission> getBotPermissions() {
        return EnumSet.of(Permission.MANAGE_CHANNEL);
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.ADMIN;
    }

    @Override
    public List<String> getExamples() {
        List<String> samples = new ArrayList<>();
        samples.add("`ticket create` - creates your ticket channel");
        samples.add("`ticket close` - closes an existing user ticket/channel");
        return samples;
    }
}
