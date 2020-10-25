package com.baro.bot.discord.commands.music;

import com.baro.bot.discord.commands.CommandCategory;
import com.baro.bot.discord.commands.CommandContext;
import com.baro.bot.discord.commands.ICommand;
import com.baro.bot.discord.commands.MusicCommand;
import com.baro.bot.discord.commands.music.audio.AudioHandler;
import com.baro.bot.discord.commands.music.audio.QueuedTrack;
import com.baro.bot.discord.util.EmoteUtil;
import com.baro.bot.discord.util.FormatUtil;
import com.baro.bot.discord.util.MusicUtils;
import com.jagrosh.jdautilities.menu.ButtonMenu;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException.Severity;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.exceptions.PermissionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PlayCmd extends MusicCommand implements ICommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlayCmd.class);

    @Override
    public void execute(CommandContext ctx) {

        if (!init(ctx)) return;

        if (ctx.getArgs().isEmpty() && ctx.getEvent().getMessage().getAttachments().isEmpty()) {
            AudioHandler handler = (AudioHandler) ctx.getEvent().getGuild().getAudioManager().getSendingHandler();
            if (handler.getPlayer().getPlayingTrack() != null && handler.getPlayer().isPaused()) {
                handler.getPlayer().setPaused(false);
                ctx.getEvent().getChannel().sendMessage(handler.getPlayer().getPlayingTrack().getInfo().title + "**.").queue();
                return;
            }
            StringBuilder builder = new StringBuilder("Play Commands:\n");
            builder.append("\n`").append(ctx.getPrefix()).append(getName()).append(" <song title>` - plays the first result from Youtube");
            builder.append("\n`").append(ctx.getPrefix()).append(getName()).append(" <URL>` - plays the provided song, playlist, or stream");
            ctx.getEvent().getChannel().sendMessage(builder.toString()).queue();
            return;
        }
        String args = ctx.getArgs().startsWith("<") && ctx.getArgs().endsWith(">")
                ? ctx.getArgs().substring(1, ctx.getArgs().length() - 1)
                : ctx.getArgs().isEmpty() ? ctx.getEvent().getMessage().getAttachments().get(0).getUrl() : ctx.getArgs();

        if (!FormatUtil.isUrl(args)) {
            args = "ytsearch:" + args;
        }

        String track = args;

        if (ctx.getEvent().getMessage().getContentRaw().split(" ")[0].toLowerCase().contains("pplay")) {
            ctx.getBot().getPlayerManager().loadItemOrdered(ctx.getEvent().getGuild(), track, new ResultHandler(ctx.getEvent().getMessage(), ctx, false, true));
        } else {
            ctx.getEvent().getChannel().sendMessage(" Loading... `[" + args + "]`").queue(m ->
                    ctx.getBot().getPlayerManager().loadItemOrdered(ctx.getEvent().getGuild(), track,
                            new ResultHandler(m, ctx, false, false)));
        }
    }

    @Override
    public String getName() {
        return "play";
    }

    @Override
    public String getDescription() {
        return "Play a track";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.MUSIC;
    }

    @Override
    public List<String> getAliases() {
        List<String> aliases = new ArrayList<>();
        aliases.add("p");
        aliases.add("pplay");
        return aliases;
    }

    @Override
    public List<String> getExamples() {
        List<String> samples = new ArrayList<>();
        samples.add("`play <song_name>`");
        samples.add("`play <URL>`");
        samples.add("");
        samples.add("Plays the song or stream at the provided URL. Supported locations include (but are not limited to):" +
                " YouTube (and playlists), SoundCloud, BandCamp, Vimeo, and Twitch. Local files or URLs of the following" +
                " formats are also supported: MP3, FLAC, WAV, Matroska/WebM (AAC, Opus or Vorbis codecs), " +
                "MP4/M4A (AAC codec), OGG streams (Opus, Vorbis and FLAC codecs), AAC streams, Stream playlists (M3U and PLS)");
        return samples;
    }

    private class ResultHandler implements AudioLoadResultHandler {
        private final Message m;
        private final CommandContext ctx;
        private final boolean ytsearch;
        private final boolean pplay;

        private ResultHandler(Message m, CommandContext ctx, boolean ytsearch, boolean pplay) {
            this.m = m;
            this.ctx = ctx;
            this.ytsearch = ytsearch;
            this.pplay = pplay;
        }

        private void loadSingle(AudioTrack track, AudioPlaylist playlist) {
//            if (bot.getConfig().isTooLong(track)) {
//                m.editMessage(FormatUtil.filter(event.getClient().getWarning() + " This track (**" + track.getInfo().title + "**) is longer than the allowed maximum: `"
//                        + FormatUtil.formatTime(track.getDuration()) + "` > `" + FormatUtil.formatTime(bot.getConfig().getMaxSeconds() * 1000) + "`")).queue();
//                return;
//            }
            AudioHandler handler = (AudioHandler) ctx.getEvent().getGuild().getAudioManager().getSendingHandler();

            int pos = handler.addTrack(new QueuedTrack(track, ctx.getEvent().getAuthor())) + 1;
            String addMsg = FormatUtil.filter("Added **" + track.getInfo().title
                    + "** (`" + FormatUtil.formatTime(track.getDuration()) + "`) " + (pos == 0 ? "to begin playing" : " to the queue at position " + pos));
            if (playlist == null)
                m.editMessage(new MusicUtils(ctx.getBot()).addMusicEmbed(track, m.getAuthor()).build()).queue();
            else {
                EmoteUtil eu = new EmoteUtil(ctx.getBot());
                new ButtonMenu.Builder()
                        .setText(addMsg + "\n" + "This track has a playlist of **" +
                                playlist.getTracks().size() + "** tracks attached. Select " +
                                eu.getEmojiAsString("download") + " to load playlist.")
                        .setChoices(eu.getEmote("download"), eu.getEmote("xmark"))
                        .setEventWaiter(ctx.getBot().getEventWaiter())
                        .setTimeout(30, TimeUnit.SECONDS)
                        .setAction(re -> {
                            if (re.getName().equals("download"))
                                m.editMessage(addMsg + "\n" + "Loaded **" + loadPlaylist(playlist, track) + "** additional tracks!").queue();
                            else
                                m.editMessage(addMsg).queue();
                        })
                        .setFinalAction(m -> {
                            try {
                                m.clearReactions().queue();
                            } catch (PermissionException ignore) {
                            }
                        }).build().display(m);
            }
        }

        private int loadPlaylist(AudioPlaylist playlist, AudioTrack exclude) {
            int[] count = {0};
            playlist.getTracks().stream().forEach((track) -> {
//                if (!bot.getConfig().isTooLong(track) && !track.equals(exclude)) {
                if (!track.equals(exclude)) {
                    AudioHandler handler = (AudioHandler) ctx.getEvent().getGuild().getAudioManager().getSendingHandler();
                    handler.addTrack(new QueuedTrack(track, ctx.getEvent().getAuthor()));
                    count[0]++;
                }
            });
            return count[0];
        }

        @Override
        public void trackLoaded(AudioTrack track) {
            LOGGER.info("[PlayerManager] Track Loaded");
            loadSingle(track, null);
        }

        @Override
        public void playlistLoaded(AudioPlaylist playlist) {
            LOGGER.info("[PlayerManager] Playlist Loaded");
            if (pplay) {
                new MusicUtils(ctx.getBot()).addPlaylistEmbed(playlist, ctx.getEvent().getMessage());
                loadPlaylist(playlist, null);
            } else if (playlist.getTracks().size() == 1 || playlist.isSearchResult()) {
                AudioTrack single = playlist.getSelectedTrack() == null ? playlist.getTracks().get(0) : playlist.getSelectedTrack();
                loadSingle(single, null);
            } else if (playlist.getSelectedTrack() != null) { // needed for tracks with attached playlists
                AudioTrack single = playlist.getSelectedTrack();
                loadSingle(single, playlist);
            } else {
                int count = loadPlaylist(playlist, null);
                if (count == 0) {
                    m.editMessage(FormatUtil.filter("All entries in this playlist " + (playlist.getName() == null ? "" : "(**" + playlist.getName()
                            + "**) ") + "were longer than the allowed maximum (`NaN`)")).queue();
                } else {
                    m.editMessage(FormatUtil.filter("Found "
                            + (playlist.getName() == null ? "a playlist" : "playlist **" + playlist.getName() + "**") + " with `"
                            + playlist.getTracks().size() + "` entries; added to the queue!"
                            + (count < playlist.getTracks().size() ? "\n" + "Tracks longer than the allowed maximum (`"
                            + "NaN`) have been omitted." : ""))).queue();
                }
            }
        }

        @Override
        public void noMatches() {
            LOGGER.info("[PlayerManager] No Matches");
            if (ytsearch)
                m.editMessage(FormatUtil.filter("No results found for `" + ctx.getArgs() + "`.")).queue();
            else
                ctx.getBot().getPlayerManager().loadItemOrdered(
                        ctx.getEvent().getGuild(),
                        "ytsearch:" + ctx.getArgs(),
                        new ResultHandler(m, ctx, true, false));
        }

        @Override
        public void loadFailed(FriendlyException throwable) {
            LOGGER.info("[PlayerManager] Load Failed");
            if (throwable.severity == Severity.COMMON)
                m.editMessage("Error loading: " + throwable.getMessage()).queue();
            else
                m.editMessage("Error loading track.").queue();
        }
    }
}
