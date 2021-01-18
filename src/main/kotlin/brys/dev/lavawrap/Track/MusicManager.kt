package brys.dev.lavawrap.Track

import brys.dev.lavawrap.Handler.AudioSendHandler
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.entities.User

class GuildManager(val manager: AudioPlayerManager, guild: Guild, channel: TextChannel, requester: User) {
    val player: AudioPlayer = manager.createPlayer()
    var trackManager: TrackManager? = null
    val sendHandler: AudioSendHandler
    init {
        trackManager = TrackManager(player, guild, channel, requester)
        player.addListener(this.trackManager)
        sendHandler = AudioSendHandler(this.player)
    }
}