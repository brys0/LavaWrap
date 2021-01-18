package brys.dev.lavawrap.Track.Events

import brys.dev.lavawrap.Lib.Util.Time
import brys.dev.lavawrap.Log.TrackLog
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.TextChannel
import java.awt.Color
import java.lang.StringBuilder
import java.util.*


class TrackEvents(private val player: AudioPlayer, private val track: AudioTrack, private val text: TextChannel) {
    private val time = Time
    fun trackStart(queue: Queue<AudioTrack>, embed: EmbedBuilder?, log: Boolean) {
        when (log) {
            true -> TrackLog.trackStart(track,text.guild)
            }
            when (embed == null) {
                true -> {
                    val live = if(track.info.isStream) "LIVE" else time.formatMili(track.duration)
                    val next = if(queue.peek() == null) "None" else "[${queue.peek().info.title}](${queue.peek().info.uri})"
                    val picture = "https://i.ytimg.com/vi/${track.identifier}/maxresdefault.jpg"
                    val nowPlaying = EmbedBuilder()
                        .setAuthor("Now playing")
                        .setTitle(track.info.title,track.info.uri)
                        .setColor(Color.decode("#7289da"))
                        .addField("Duration",live,true)
                        .addField("Channel",track.info.author,true)
                        .addField("Next Up",next,true)
                        .setThumbnail(picture)
                        .build()
                        text.sendMessage(nowPlaying).queue()
                    }
                false -> text.sendMessage(embed.build()).queue()
              }
            }
         }

