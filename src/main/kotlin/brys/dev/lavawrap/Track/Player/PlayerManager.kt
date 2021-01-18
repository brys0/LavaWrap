package brys.dev.lavawrap.Track.Player

import brys.dev.lavawrap.Lib.Util.Time
import brys.dev.lavawrap.Track.GuildManager
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers
import com.sedmelluq.discord.lavaplayer.source.twitch.TwitchStreamAudioSourceManager
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.entities.User
import java.awt.Color
import java.util.HashMap
import java.util.function.Consumer
/**
 * Lavawraps player manager handling the playing of the physical tracks.
 * [play]
 */
class PlayerManager private constructor() {
   private val PlayerManager: AudioPlayerManager
   private val guildManagers: MutableMap<Long, GuildManager>
   private val time = Time
   fun getGuildManager(guild: Guild, channel: TextChannel, requester: User): GuildManager {
       val id = guild.idLong
       var guildManager = guildManagers[id]
       when (guildManager) {
           null -> {
               guildManager = GuildManager(PlayerManager, guild, channel, requester)
               guildManagers[id] = guildManager
           }
       }
       guild.audioManager.sendingHandler = guildManager!!.sendHandler
       return guildManager
   }
    fun play(channel: TextChannel, url: String, requester: User, embed: EmbedBuilder?) {
        val guildManager = getGuildManager(channel.guild,channel,requester)
        val player = guildManager.player
        this.PlayerManager.loadItemOrdered(guildManager,url,object:AudioLoadResultHandler {
            override fun trackLoaded(track: AudioTrack) {
                play(guildManager, track, channel)
                when (guildManager.trackManager!!.queue.peek()) {
                    null -> return
                    else -> {
                        when (embed) {
                            null -> {
                                val picture = "https://i.ytimg.com/vi/${track.identifier}/maxresdefault.jpg"
                                val live = if(track.info.isStream) "LIVE" else time.formatMili(track.duration)
                                val trackEmbed = EmbedBuilder()
                                    .setTitle("Added Track")
                                    .addField("Track","[${track.info.title}](${track.info.uri})",true)
                                    .addField("Channel",track.info.author,true)
                                    .setThumbnail(picture)
                                    .addField("Duration",live,true)
                                    .setColor(Color.decode("#7289da"))
                                    .setFooter("Requested By: ${requester.asTag}",requester.effectiveAvatarUrl)
                                channel.sendMessage(trackEmbed.build()).queue()
                            }
                            else -> channel.sendMessage(embed.build()).queue()
                        }
                    }
                }
            }

            override fun playlistLoaded(playlist: AudioPlaylist) {
                var firstTrack = playlist.selectedTrack
                if (firstTrack == null) {
                    firstTrack = playlist.tracks.removeAt(0)
                }
                val count = playlist.tracks.size.coerceAtMost(10)
                val tracks: List<AudioTrack> = ArrayList(playlist.tracks)
                val picture = "https://i.ytimg.com/vi/${firstTrack.identifier}/maxresdefault.jpg"
                val playlistEmbed = EmbedBuilder()
                    .setTitle(firstTrack.info.title,firstTrack.info.uri)
                    .setDescription("Showing first **$count** Tracks.\n")
                    .setThumbnail(picture)
                    .setColor(Color.decode("#7289da"))
                    for (i in 0 until count) {
                        val track = tracks[i]
                        val info = track.info
                        playlistEmbed.appendDescription(
                            String.format(
                            "%s `%s`\n",
                            "`#${i + 1}` - [${info.title}](${info.uri})",
                            time.formatMili(info.length)
                            )
                        )
                    }
                channel.sendMessage(playlistEmbed.build()).queue()
                play(guildManager, firstTrack, channel)
                playlist.tracks.forEach(Consumer { track: AudioTrack ->
                    guildManager.trackManager!!.queue(track, channel)
                })
            }
            override fun noMatches() {
                channel.sendMessage("Nothing found perhaps this url is hidden $url").queue()
            }
            override fun loadFailed(exception: FriendlyException?) {
                channel.sendMessage("Couldn't play the track. (`${exception!!.message}`)").queue()
            }
        })

    }
    private fun play(guildManager: GuildManager, track: AudioTrack, channel: TextChannel) {
       guildManager.trackManager!!.queue(track, channel)
    }
    companion object {
        private var PLAYERMANAGER: PlayerManager? = null

        @JvmStatic
        @get:Synchronized
        val instance: PlayerManager?
            get() {
                if (PLAYERMANAGER == null) {
                    PLAYERMANAGER = PlayerManager()
                }
                return PLAYERMANAGER
            }
    }
init {
    guildManagers = HashMap()
    PlayerManager = DefaultAudioPlayerManager()
    AudioSourceManagers.registerRemoteSources(PlayerManager)
    AudioSourceManagers.registerLocalSource(PlayerManager)
    PlayerManager.registerSourceManager(TwitchStreamAudioSourceManager())
    }
}
