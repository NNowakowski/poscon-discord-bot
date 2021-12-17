package de.nnowakowski.poscondiscordbot

import de.nnowakowski.poscondiscordbot.data.Atc
import de.nnowakowski.poscondiscordbot.data.Flight
import de.nnowakowski.poscondiscordbot.data.PosconOnlineResponse
import dev.kord.core.Kord
import dev.kord.core.behavior.channel.createEmbed
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.rest.builder.message.EmbedBuilder
import kotlinx.coroutines.reactive.awaitSingle
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.format.DateTimeFormatter

@Component
class KordMessageCreateEventListener(
    @Value("\${discord.oauth2.client-id}") private val discordOauth2ClientId: String,
    @Value("\${discord.oauth2.permissions}") private val discordOauth2Permissions: String,
    @Value("\${discord.oauth2.scope}") private val discordOauth2Scope: String,
    @Autowired private val posconClient: PosconClient
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
                    messageCreateEvent.message.channel.createMessage("https://discord.com/api/oauth2/authorize?client_id=${discordOauth2ClientId}&permissions=${discordOauth2Permissions}&scope=${discordOauth2Scope}")
                }
                "!atc" -> {
                    val posconOnlineResponse = posconClient.getOnlineUsers().awaitSingle()
                    messageCreateEvent.message.channel.createEmbed {
                        createAtcEmbed(this, posconOnlineResponse, posconOnlineResponse.atc)
                    }
                }
                "!pilots" -> {
                    val posconOnlineResponse = posconClient.getOnlineUsers().awaitSingle()
                    messageCreateEvent.message.channel.createEmbed {
                        createFlightEmbed(this, posconOnlineResponse, posconOnlineResponse.flights)
                    }
                }
                "!online" -> {
                    val posconOnlineResponse = posconClient.getOnlineUsers().awaitSingle()
                    messageCreateEvent.message.channel.createEmbed {
                        createAtcEmbed(this, posconOnlineResponse, posconOnlineResponse.atc, true)
                    }
                    messageCreateEvent.message.channel.createEmbed {
                        createFlightEmbed(this, posconOnlineResponse, posconOnlineResponse.flights, true)
                    }
                }
                "!upcoming" -> {
                    val posconOnlineResponse = posconClient.getOnlineUsers().awaitSingle()
                    messageCreateEvent.message.channel.createEmbed {
                        createAtcEmbed(this, posconOnlineResponse, posconOnlineResponse.upcomingAtc, false)
                    }
                    messageCreateEvent.message.channel.createEmbed {
                        createFlightEmbed(this, posconOnlineResponse, posconOnlineResponse.upcomingFlights, false)
                    }
                }
            }
        }
    }

    fun createFlightEmbed(
        embedBuilder: EmbedBuilder,
        posconOnlineResponse: PosconOnlineResponse,
        flightArray: Array<Flight>,
        showCurrent: Boolean = true
    ) {
        embedBuilder.title = if (showCurrent) {
            "Online pilots: ${flightArray.size}"
        } else {
            "Upcoming flights: ${flightArray.size}"
        }

        flightArray.forEach {
            embedBuilder.field {
                name = if (showCurrent) {
                    "${it.callsign ?: "N/A"} (${it.acTpe ?: "N/A"})"
                } else {
                    "${it.callsign ?: "N/A"} (${it.acTpe ?: "N/A"}) - ${
                        it.std?.format(
                            DateTimeFormatter.ofPattern("dd.MM.yyyy - HH:mm (z)")
                        )
                    }"
                }
                inline = false
                value =
                    "From ${it.flightplan?.dep ?: "N/A"} to ${it.flightplan?.dest ?: "N/A"} (Alternate ${it.flightplan?.altnt ?: "N/A"})"
            }
        }

        embedBuilder.footer {
            text = "Fetched at: ${
                posconOnlineResponse.lastUpdated?.format(
                    DateTimeFormatter.ofPattern("dd.MM.yyyy - HH:mm (z)")
                )
            }"
        }
    }

    fun createAtcEmbed(
        embedBuilder: EmbedBuilder,
        posconOnlineResponse: PosconOnlineResponse,
        atcArray: Array<Atc>,
        showCurrent: Boolean = true
    ) {
        embedBuilder.title = if (showCurrent) {
            "Online ATC: ${atcArray.size}"
        } else {
            "Upcoming ATC: ${atcArray.size}"
        }

        atcArray.forEach {
            embedBuilder.field {
                name = if (showCurrent) {
                    "${it.telephony ?: "N/A"} - ${it.type ?: "N/A"} (${it.fir ?: "N/A"})"
                } else {
                    "${it.telephony ?: "N/A"} - ${it.type ?: "N/A"} (${it.fir ?: "N/A"}) - ${
                        it.start?.format(
                            DateTimeFormatter.ofPattern("dd.MM.yyyy - HH:mm (z)")
                        )
                    }"
                }
                inline = false
                value = "VHF ${it.vhfFreq ?: "N/A"}"
            }
        }

        embedBuilder.footer {
            text = "Fetched at: ${
                posconOnlineResponse.lastUpdated?.format(
                    DateTimeFormatter.ofPattern("dd.MM.yyyy - HH:mm (z)")
                )
            }"
        }
    }
}