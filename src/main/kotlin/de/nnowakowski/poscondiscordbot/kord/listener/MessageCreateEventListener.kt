package de.nnowakowski.poscondiscordbot.kord.listener

import de.nnowakowski.poscondiscordbot.kord.embedbuilder.PosconMetarEmbedBuilder
import de.nnowakowski.poscondiscordbot.kord.embedbuilder.PosconMetarTafEmbedBuilder
import de.nnowakowski.poscondiscordbot.kord.embedbuilder.PosconOnlineEmbedBuilder
import de.nnowakowski.poscondiscordbot.kord.embedbuilder.PosconTafEmbedBuilder
import de.nnowakowski.poscondiscordbot.web.PosconClient
import dev.kord.core.behavior.reply
import dev.kord.core.event.message.MessageCreateEvent
import kotlinx.coroutines.reactive.awaitSingle
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
@SuppressWarnings("LongParameterList", "NestedBlockDepth", "LongMethod", "ComplexMethod")
class MessageCreateEventListener(
    @Value("\${discord.oauth2.client-id}") private val discordOauth2ClientId: String,
    @Value("\${discord.oauth2.permissions}") private val discordOauth2Permissions: String,
    @Value("\${discord.oauth2.scope}") private val discordOauth2Scope: String,
    @Value("\${poscon.api.icao-length}") private val posconApiIcaoLength: Int,
    @Autowired private val posconClient: PosconClient,
    @Autowired private val posconOnlineEmbedBuilder: PosconOnlineEmbedBuilder,
    @Autowired private val posconMetarEmbedBuilder: PosconMetarEmbedBuilder,
    @Autowired private val posconTafEmbedBuilder: PosconTafEmbedBuilder,
    @Autowired private val posconMetarTafEmbedBuilder: PosconMetarTafEmbedBuilder
) {
    suspend fun handle(messageCreateEvent: MessageCreateEvent) {
        if (messageCreateEvent.message.author?.isBot == false) {
            with(messageCreateEvent.message.content) {
                when {
                    equals("!invite") -> {
                        messageCreateEvent.message.reply {
                            content = "https://discord.com/api/oauth2/authorize" +
                                "?client_id=$discordOauth2ClientId" +
                                "&permissions=$discordOauth2Permissions" +
                                "&scope=$discordOauth2Scope"
                        }
                    }
                    equals("!atc") -> {
                        val posconOnlineResponse = posconClient.getOnlineUsers().awaitSingle()
                        posconOnlineEmbedBuilder.createAtcEmbed(messageCreateEvent, posconOnlineResponse)
                    }
                    equals("!pilots") -> {
                        val posconOnlineResponse = posconClient.getOnlineUsers().awaitSingle()
                        posconOnlineEmbedBuilder.createFlightEmbed(messageCreateEvent, posconOnlineResponse)
                    }
                    equals("!online") -> {
                        val posconOnlineResponse = posconClient.getOnlineUsers().awaitSingle()
                        posconOnlineEmbedBuilder.createAtcEmbed(messageCreateEvent, posconOnlineResponse)
                        posconOnlineEmbedBuilder.createFlightEmbed(messageCreateEvent, posconOnlineResponse)
                    }
                    equals("!upcoming") -> {
                        val posconOnlineResponse = posconClient.getOnlineUsers().awaitSingle()
                        posconOnlineEmbedBuilder.createUpcomingAtcEmbed(messageCreateEvent, posconOnlineResponse)
                        posconOnlineEmbedBuilder.createUpcomingFlightEmbed(messageCreateEvent, posconOnlineResponse)
                    }
                    contains("!metartaf") -> {
                        val icao: String = split(" ").last()

                        if (icao.length != posconApiIcaoLength) {
                            messageCreateEvent.message.reply {
                                content = "Entered parameter must be $posconApiIcaoLength characters long!"
                            }
                        } else {
                            val posconMetarTafResponse = posconClient.getMetarTafInfo(icao).awaitSingle()
                            posconMetarTafEmbedBuilder.createMetarTafEmbed(messageCreateEvent, posconMetarTafResponse)
                        }
                    }
                    contains("!metar") -> {
                        val icao: String = split(" ").last()

                        if (icao.length != posconApiIcaoLength) {
                            messageCreateEvent.message.reply {
                                content = "Entered parameter must be $posconApiIcaoLength characters long!"
                            }
                        } else {
                            val posconMetarResponse = posconClient.getMetarInfo(icao).awaitSingle()
                            posconMetarEmbedBuilder.createMetarEmbed(messageCreateEvent, posconMetarResponse)
                        }
                    }
                    contains("!taf") -> {
                        val icao: String = split(" ").last()

                        if (icao.length != posconApiIcaoLength) {
                            messageCreateEvent.message.reply {
                                content = "Entered parameter must be $posconApiIcaoLength characters long!"
                            }
                        } else {
                            val posconTafResponse = posconClient.getTafInfo(icao).awaitSingle()
                            posconTafEmbedBuilder.createTafEmbed(messageCreateEvent, posconTafResponse)
                        }
                    }
                    else -> {}
                }
            }
        }
    }
}
