package de.nnowakowski.poscondiscordbot.kord.listener

import de.nnowakowski.poscondiscordbot.kord.embedbuilder.PosconOnlineEmbedBuilder
import de.nnowakowski.poscondiscordbot.web.PosconClient
import dev.kord.core.Kord
import dev.kord.core.event.message.MessageCreateEvent
import kotlinx.coroutines.reactive.awaitSingle
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class MessageCreateEventListener(
    @Value("\${discord.oauth2.client-id}") private val discordOauth2ClientId: String,
    @Value("\${discord.oauth2.permissions}") private val discordOauth2Permissions: String,
    @Value("\${discord.oauth2.scope}") private val discordOauth2Scope: String,
    @Autowired private val posconClient: PosconClient,
    @Autowired private val posconOnlineEmbedBuilder: PosconOnlineEmbedBuilder
) {
    suspend fun handle(kord: Kord, messageCreateEvent: MessageCreateEvent) {
        if (messageCreateEvent.message.author?.isBot == false) {
            when (messageCreateEvent.message.content) {
                "!ping" -> {
                    messageCreateEvent.message.channel.createMessage("pong!")
                    kord.editPresence {
                        watching("pong!")
                    }
                }
                "!invite" -> {
                    messageCreateEvent.message.channel.createMessage(
                        "https://discord.com/api/oauth2/authorize" +
                            "?client_id=$discordOauth2ClientId" +
                            "&permissions=$discordOauth2Permissions" +
                            "&scope=$discordOauth2Scope"
                    )
                }
                "!atc" -> {
                    val posconOnlineResponse = posconClient.getOnlineUsers().awaitSingle()
                    posconOnlineEmbedBuilder.createAtcEmbed(messageCreateEvent, posconOnlineResponse)
                }
                "!pilots" -> {
                    val posconOnlineResponse = posconClient.getOnlineUsers().awaitSingle()
                    posconOnlineEmbedBuilder.createFlightEmbed(messageCreateEvent, posconOnlineResponse)
                }
                "!online" -> {
                    val posconOnlineResponse = posconClient.getOnlineUsers().awaitSingle()
                    posconOnlineEmbedBuilder.createAtcEmbed(messageCreateEvent, posconOnlineResponse)
                    posconOnlineEmbedBuilder.createFlightEmbed(messageCreateEvent, posconOnlineResponse)
                }
                "!upcoming" -> {
                    val posconOnlineResponse = posconClient.getOnlineUsers().awaitSingle()
                    posconOnlineEmbedBuilder.createUpcomingAtcEmbed(messageCreateEvent, posconOnlineResponse)
                    posconOnlineEmbedBuilder.createUpcomingFlightEmbed(messageCreateEvent, posconOnlineResponse)
                }
            }
        }
    }
}
