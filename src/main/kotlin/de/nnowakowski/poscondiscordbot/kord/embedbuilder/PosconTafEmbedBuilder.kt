package de.nnowakowski.poscondiscordbot.kord.embedbuilder

import de.nnowakowski.poscondiscordbot.data.PosconTafResponse
import dev.kord.core.behavior.reply
import dev.kord.core.event.message.MessageCreateEvent
import org.springframework.stereotype.Component

@Component
class PosconTafEmbedBuilder {
    suspend fun createTafEmbed(
        messageCreateEvent: MessageCreateEvent,
        posconTafResponse: PosconTafResponse
    ) {
        messageCreateEvent.message.reply {
            embed {
                title = "Recorded TAF for ${posconTafResponse.icao}"

                field {
                    name = "TAF"
                    inline = false
                    value = posconTafResponse.taf ?: "No TAF received"
                }
            }
        }
    }
}
