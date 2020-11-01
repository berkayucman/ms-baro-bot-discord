package com.baro.bot.discord.components;

import com.baro.bot.discord.commands.CommandCategory;
import com.baro.bot.discord.commands.CommandContext;
import com.baro.bot.discord.commands.ICommand;
import com.baro.bot.discord.commands.admin.*;
import com.baro.bot.discord.commands.information.EmoteCmd;
import com.baro.bot.discord.commands.information.HelpCmd;
import com.baro.bot.discord.commands.misc.PollCmd;
import com.baro.bot.discord.commands.moderation.LockCmd;
import com.baro.bot.discord.commands.music.*;
import com.baro.bot.discord.commands.music.dj.*;
import com.baro.bot.discord.commands.owner.*;
import com.baro.bot.discord.config.BotConfig;
import com.baro.bot.discord.config.FlagsConfig;
import com.baro.bot.discord.model.GuildSettingsEntity;
import com.baro.bot.discord.model.MusicSettingsEntity;
import com.baro.bot.discord.repository.GuildSettingsRepository;
import com.baro.bot.discord.repository.MusicSettingsRepository;
import com.baro.bot.discord.service.BaroBot;
import com.sun.istack.NotNull;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

@Component
public class CommandManager {

    private final static Logger LOGGER = LoggerFactory.getLogger(CommandManager.class);
    private final Map<String, ICommand> commands;
    private final BaroBot bot;
    private final BotConfig botConfig;
    private final FlagsConfig flagsConfig;
    private final GuildSettingsRepository guildSettingsRepository;
    private final MusicSettingsRepository musicSettingsRepository;

    public CommandManager(BaroBot bot, BotConfig botConfig, FlagsConfig flagsConfig, GuildSettingsRepository guildSettingsRepository, MusicSettingsRepository musicSettingsRepository) {
        this.bot = bot;
        this.botConfig = botConfig;
        this.flagsConfig = flagsConfig;
        this.guildSettingsRepository = guildSettingsRepository;
        this.musicSettingsRepository = musicSettingsRepository;
        this.commands = new HashMap();

        //ADMIN
        commands.put("prefix", new PrefixCmd(guildSettingsRepository, botConfig));
        commands.put("setdj", new MusicDjRoleIdCmd(musicSettingsRepository));
        commands.put("settc", new MusicTextChannelIdCmd(musicSettingsRepository));
        commands.put("setvc", new MusicVoiceChannelIdCmd(musicSettingsRepository));
        commands.put("ticket", new TicketCmd(flagsConfig));

        // INFORMATION
        commands.put("emote", new EmoteCmd());
        commands.put("help", new HelpCmd());

        // MUSIC
        commands.put("play", new PlayCmd());
        commands.put("nowplaying", new NowplayingCmd());
        commands.put("shuffle", new ShuffleCmd());
        commands.put("skip", new SkipCmd());
        commands.put("remove", new RemoveCmd());
        commands.put("queue", new QueueCmd(bot, musicSettingsRepository));
        commands.put("pplaylist", new PlayPlaylistCmd());
        commands.put("playlists", new PlaylistsCmd());
        commands.put("search", new SearchCmd(bot));
        commands.put("lyrics", new LyricsCmd());

        // MUSIC with DJ PERMISSIONS
        commands.put("stop", new StopCmd());
        commands.put("forceremove", new ForceRemoveCmd());
        commands.put("movetrack", new MoveTrackCmd());
        commands.put("forceskip", new ForceskipCmd());
        commands.put("pause", new PauseCmd());
        commands.put("playnext", new PlaynextCmd());
        commands.put("prepeat", new PlaylistRepeatCmd(musicSettingsRepository));
        commands.put("repeat", new RepeatCmd(musicSettingsRepository));
        commands.put("seek", new SeekCmd());
        commands.put("skipto", new SkiptoCmd());
        commands.put("volume", new VolumeCmd());

        // OWNER
        commands.put("evalj", new EvalCmd());
        commands.put("debug", new DebugCmd(botConfig));
        commands.put("guildlist", new GuildListCmd(bot));
        commands.put("playlist", new PlaylistCmd());
        commands.put("shutdownj", new ShutdownCmd());
        commands.put("setstatusj", new SetstatusCmd());
        commands.put("setnamej", new SetnameCmd());
        commands.put("setgamej", new SetgameCmd());
        commands.put("setavatarj", new SetavatarCmd());

        // MISC
        commands.put("poll", new PollCmd());

        // MODERATION
        commands.put("lock", new LockCmd());
    }

    @Nullable
    public ICommand getCommand(String search) {
        String searchLower = search.toLowerCase();
        for (ICommand cmd : commands.values()) {
            if (cmd.getName().equalsIgnoreCase(searchLower) || cmd.getAliases().contains(searchLower)) {
                return cmd;
            }
        }
        return null;
    }

    public void handle(MessageReceivedEvent event) {

        String prefix = event.getChannelType() == ChannelType.PRIVATE ? botConfig.getPrefix() : getPrefix(event.getGuild().getIdLong());

        if (!event.getMessage().getContentRaw().startsWith(prefix)) {
            return;
        }

        String[] args = event.getMessage().getContentRaw()
                .replaceFirst("(?i)" + Pattern.quote(prefix), "")
                .split("\\s+");

        String invoke = args[0].toLowerCase();
        ICommand cmd = getCommand(invoke);

        if (cmd == null) return;

        CommandContext ctx = new CommandContext(bot, prefix, args, event, invoke, this);
        if (!argsProvided(ctx, cmd)) {
            event.getChannel().sendMessage("Please provide arguments").queue();
            return;
        }
        if (isGuildOnlyViolation(ctx, cmd)) {
            event.getChannel().sendMessage("This command is not available in private channels").queue();
            return;
        }
        if (isNsfwViolation(ctx, cmd)) {
            event.getChannel().sendMessage("This is a nsfw command. Please execute it in a nsfw channel.").queue();
            return;
        }
        if (!hasAllMemberPermissions(ctx, cmd)) {
            event.getChannel().sendMessage("You don't have needed permissions to run this command!" +
                    "\nPermissions needed:\n" + cmd.getMemberPermissions().toString()).queue();
            return;
        }
        if (!hasAllBotPermissions(ctx, cmd)) {
            event.getChannel().sendMessage("I don't have needed permissions to run this command!" +
                    "\nI need the following permissions:\n" + cmd.getBotPermissions().toString()).queue();
            return;
        }

        if (!hasOwnerPermissions(ctx, cmd)) return;
        // TODO - Cooldown
        cmd.execute(ctx);
    }

    private boolean hasOwnerPermissions(CommandContext ctx, ICommand cmd) {
        if (cmd.getCategory().equals(CommandCategory.OWNER) &&
                !botConfig.getBotOwnerIds().contains(ctx.getEvent().getAuthor().getId())) {
            ctx.getEvent().getChannel().sendMessage("Only a bot owner can execute this command!").queue();
            return false;
        }
        return true;
    }

    private boolean hasAllMemberPermissions(CommandContext ctx, ICommand cmd) {
        if (!ctx.getEvent().getChannel().getType().equals(ChannelType.TEXT)) {
            return true;
        }
        EnumSet<Permission> neededPerms = cmd.getMemberPermissions();
        EnumSet<Permission> memberPermissions = ctx.getEvent().getMember().getPermissions();

        if (neededPerms == null) return true;

        if (memberPermissions.contains(Permission.ADMINISTRATOR) ||
                memberPermissions.containsAll(neededPerms) ||
                botConfig.getBotOwnerIds().contains(ctx.getEvent().getAuthor().getId())
        )
            return true;

        return false;
    }

    private boolean hasAllBotPermissions(CommandContext ctx, ICommand cmd) {
        if (!ctx.getEvent().getChannel().getType().equals(ChannelType.TEXT)) {
            return true;
        }
        EnumSet<Permission> neededPermissions = cmd.getBotPermissions();
        EnumSet<Permission> botPermissions = ctx.getEvent().getGuild()
                .getMemberById(ctx.getEvent().getJDA().getSelfUser().getId()).getPermissions();

        if (neededPermissions == null) {
            return true;
        }

        if (botPermissions.containsAll(neededPermissions)) {
            return true;
        }
        return false;
    }

    private boolean argsProvided(CommandContext ctx, ICommand cmd) {
        return !ctx.getArgs().isEmpty() || !cmd.getArgs();
    }

    private boolean isGuildOnlyViolation(CommandContext ctx, ICommand cmd) {
        return !ctx.getEvent().getChannel().getType().equals(ChannelType.TEXT) && cmd.guildOnly();
    }

    private boolean isNsfwViolation(CommandContext ctx, ICommand cmd) {
        boolean notTextChannel = !ctx.getEvent().getChannel().getType().equals(ChannelType.TEXT);
        if (notTextChannel) return false;
        return !ctx.getEvent().getTextChannel().isNSFW() && cmd.isNsfw();
    }

    @NotNull
    public String getPrefix(Long serverId) {
        Optional<GuildSettingsEntity> settings = guildSettingsRepository.findById(serverId);
        if (settings.isPresent()) {
            return settings.get().getPrefix();
        }
        return botConfig.getPrefix();
    }

    @NotNull
    public String getMusicTextChannelId(Long serverId) {
        Optional<MusicSettingsEntity> settings = musicSettingsRepository.findById(serverId);
        return settings.map(MusicSettingsEntity::getTextChannelId).orElse(null);
    }

    @NotNull
    public String getMusicVoiceChannelId(Long serverId) {
        Optional<MusicSettingsEntity> settings = musicSettingsRepository.findById(serverId);
        return settings.map(MusicSettingsEntity::getVoiceChannelId).orElse(null);
    }

    @NotNull
    public String getDjRoleId(Long serverId) {
        Optional<MusicSettingsEntity> settings = musicSettingsRepository.findById(serverId);
        return settings.map(MusicSettingsEntity::getDjRoleId).orElse(null);
    }

    public Map<String, ICommand> getCommands() {
        return commands;
    }

    public BotConfig getBotConfig() {
        return botConfig;
    }

    public GuildSettingsRepository getGuildSettingsReository() {
        return guildSettingsRepository;
    }

    public MusicSettingsRepository getMusicSettingsRepository() {
        return musicSettingsRepository;
    }
}
