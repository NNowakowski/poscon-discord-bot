package de.nnowakowski.poscondiscordbot.kord.embedbuilder

import de.nnowakowski.poscondiscordbot.data.PosconOnlineResponse
import dev.kord.core.behavior.channel.createEmbed
import dev.kord.core.event.message.MessageCreateEvent
import org.springframework.stereotype.Component
import java.time.format.DateTimeFormatter

@Component
class PosconOnlineEmbedBuilder {
    suspend fun createFlightEmbed(
        messageCreateEvent: MessageCreateEvent,
        posconOnlineResponse: PosconOnlineResponse
    ) {
        messageCreateEvent.message.channel.createEmbed {
            title = "Online pilots: ${posconOnlineResponse.flights.size}"

            posconOnlineResponse.flights.forEach {
                field {
                    name = if (it.flightplan != null) {
                        "${it.flightplan.callsign ?: "N/A"} (${it.flightplan.acType ?: "N/A"})"
                    } else {
                        "${it.callsign ?: "N/A"} (${it.acType ?: "N/A"})"
                    }

                    inline = false

                    value = if (it.flightplan != null) {
                        "From ${it.flightplan.dep ?: "N/A"} to ${it.flightplan.dest ?: "N/A"}"
                    } else {
                        "No active flightplan"
                    }
                }
            }

            footer {
                text = "Fetched at: ${
                posconOnlineResponse.lastUpdated?.format(
                    DateTimeFormatter.ofPattern("dd.MM.yyyy - HH:mm z")
                )
                }"
            }
        }
    }

    suspend fun createUpcomingFlightEmbed(
        messageCreateEvent: MessageCreateEvent,
        posconOnlineResponse: PosconOnlineResponse
    ) {
        messageCreateEvent.message.channel.createEmbed {
            title = "Upcoming flights: ${posconOnlineResponse.upcomingFlights.size}"

            posconOnlineResponse.upcomingFlights.forEach {
                field {
                    name = "${it.callsign ?: "N/A"} (${it.acType ?: "N/A"}) - ${
                    it.std?.format(
                        DateTimeFormatter.ofPattern("dd.MM.yyyy - HH:mm z")
                    )
                    }"
                    inline = false
                    value = "From ${it.dep ?: "N/A"} to ${it.dest ?: "N/A"}"
                }
            }

            footer {
                text = "Fetched at: ${
                posconOnlineResponse.lastUpdated?.format(
                    DateTimeFormatter.ofPattern("dd.MM.yyyy - HH:mm z")
                )
                }"
            }
        }
    }

    suspend fun createAtcEmbed(
        messageCreateEvent: MessageCreateEvent,
        posconOnlineResponse: PosconOnlineResponse
    ) {
        messageCreateEvent.message.channel.createEmbed {
            title = "Online ATC: ${posconOnlineResponse.atc.size}"

            posconOnlineResponse.atc.forEach {
                field {
                    name = "${it.telephony ?: "N/A"} - ${it.type ?: "N/A"} (${it.fir ?: "N/A"})"
                    inline = false
                    value = "VHF ${it.vhfFreq ?: "N/A"}"
                }
            }

            footer {
                text = "Fetched at: ${
                posconOnlineResponse.lastUpdated?.format(
                    DateTimeFormatter.ofPattern("dd.MM.yyyy - HH:mm z")
                )
                }"
            }
        }
    }

    suspend fun createUpcomingAtcEmbed(
        messageCreateEvent: MessageCreateEvent,
        posconOnlineResponse: PosconOnlineResponse
    ) {
        messageCreateEvent.message.channel.createEmbed {
            title = "Upcoming ATC: ${posconOnlineResponse.upcomingAtc.size}"

            posconOnlineResponse.upcomingAtc.forEach {
                field {
                    name = "${it.telephony ?: "N/A"} - ${it.type ?: "N/A"} (${it.fir ?: "N/A"}) - ${
                    it.start?.format(
                        DateTimeFormatter.ofPattern("dd.MM.yyyy - HH:mm z")
                    )
                    }"
                    inline = false
                    value = "VHF ${it.vhfFreq ?: "N/A"}"
                }
            }

            footer {
                text = "Fetched at: ${
                posconOnlineResponse.lastUpdated?.format(
                    DateTimeFormatter.ofPattern("dd.MM.yyyy - HH:mm z")
                )
                }"
            }
        }
    }
}
