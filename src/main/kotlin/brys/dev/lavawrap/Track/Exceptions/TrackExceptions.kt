package brys.dev.lavawrap.Track.Exceptions

import brys.dev.lavawrap.Lib.Util.Time
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import net.dv8tion.jda.api.entities.TextChannel

class TrackExceptions(private val player: AudioPlayer, private val track: AudioTrack, private val channel: TextChannel) {
    private val time = Time
    fun trackException(e: FriendlyException, message: String?) {
        return when (message.isNullOrEmpty()) {
            true -> channel.sendMessage("Track encountered an exception. (${e.message}").queue()
            false -> channel.sendMessage(message).queue()
        }
    }
    fun trackStuck(ms: Long, message: String?) {
        val format = time.formatMili(ms)
        return when (message.isNullOrEmpty()) {
            true -> channel.sendMessage("Track got stuck at `$format").queue()
            false -> channel.sendMessage(message).queue()
        }
    }

}