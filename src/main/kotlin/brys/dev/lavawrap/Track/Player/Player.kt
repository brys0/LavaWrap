package brys.dev.lavawrap.Track.Player

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.entities.User
import java.util.*

object Player {
    fun queue(track: AudioTrack, channel: TextChannel, requester: User, player: AudioPlayer, queue: Queue<AudioTrack>) {
        when(player.startTrack(track,true)) {
            true -> return
            false -> queue.offer(track)
        }
    }
}