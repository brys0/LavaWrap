package brys.dev.lavawrap.Track

import brys.dev.lavawrap.Track.Events.TrackEvents
import brys.dev.lavawrap.Track.Exceptions.TrackExceptions
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventListener
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.entities.User
import java.util.*

/**
 * The Lavawrap track manager class managing all track events and exceptions / providing util for other methods
 * [nextTrack] [queueRepeat] [trackRepeat] [queueRepeating] [trackRepeating] [shuffleQueue] [removeTrack] [skipTracks]
 * @author [Brys](http://brys.tk)
 */
class TrackManager(private val player: AudioPlayer, private val guild: Guild, private val tx: TextChannel, private val requester: User): AudioEventAdapter(), AudioEventListener {
    var lastPlay: AudioTrack? = null
    var queue: Queue<AudioTrack> = LinkedList()
    var currentTrack: AudioTrack? = null
    var trackRepeat: Boolean = false
    var queueRepeat: Boolean = false
    fun queue(track: AudioTrack, channel: TextChannel) {
        try {
            if (!player.startTrack(track, true)) {
                queue.offer(track)
            }
        } catch (e: FriendlyException) {
            channel.sendMessage("An error occurred most likely this is because the requested song is age restricted.")
        }
    }
       private fun nextTrack(): AudioTrack {
           player.startTrack(queue.poll(), false)
           return queue.peek();
       }
    override fun onTrackException(player: AudioPlayer, track: AudioTrack, exception: FriendlyException) {
        TrackExceptions(player, track, tx).trackException(exception,null)
    }
    override fun onTrackStuck(player: AudioPlayer, track: AudioTrack, thresholdMs: Long) {
        TrackExceptions(player, track, tx).trackStuck(thresholdMs,null)
    }
    override fun onTrackEnd(player: AudioPlayer?, track: AudioTrack?, endReason: AudioTrackEndReason) {
        this.lastPlay = track
        if (endReason.mayStartNext) {
            if (trackRepeat)
                player!!.startTrack(lastPlay!!.makeClone(),false)
            else
                nextTrack()
        }
    }
    override fun onTrackStart(player: AudioPlayer, track: AudioTrack) {
        TrackEvents(player, track, tx).trackStart(queue,null,false)
    }
    fun queueRepeat(repeating: Boolean) {
        this.queueRepeat = repeating
    }
    fun trackRepeat(repeating: Boolean) {
        this.trackRepeat = repeating
    }
    fun queueRepeating(): Boolean {
        return queueRepeat
    }
    fun trackRepeating(): Boolean {
        return trackRepeat
    }
    fun shuffleQueue(): Queue<AudioTrack> {
      val list = queue.toList().shuffled()
        queue.clear()
        queue.addAll(list)
        return queue
    }
    fun removeTrack(i: Int): AudioTrack {
        val list = queue.toMutableList()
        val removedTrack = list.removeAt(i-1)
        queue.clear()
        queue.addAll(list)
        return removedTrack
    }
    fun skipTracks(number: Int) {
        for (i in 0 until number) removeElement(0)
    }
    private fun removeElement(index: Int): AudioTrack? {
        val list = queue.toMutableList()
        val removeElement = list.removeAt(index)
        queue.clear()
        queue.addAll(list)
        return removeElement
    }
}