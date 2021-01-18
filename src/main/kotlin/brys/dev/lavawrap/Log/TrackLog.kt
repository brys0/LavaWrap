package brys.dev.lavawrap.Log

import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import net.dv8tion.jda.api.entities.Guild

/**
 * Lavawrap log module for track events
 * @author [Brys](http://brys.tk)
 */
object TrackLog {
    fun trackStart(track: AudioTrack, guild: Guild) {
        println("┌────────────────────Track────────────────────┐")
        println("Track ${track.info?.title}, now playing in ${guild.name}")
        println("└─────────────────────────────────────────────┘")
    }
}