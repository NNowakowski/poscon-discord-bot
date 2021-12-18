package de.nnowakowski.poscondiscordbot.kord.embedbuilder

import de.nnowakowski.poscondiscordbot.data.PosconMetarResponse
import dev.kord.core.behavior.reply
import dev.kord.core.event.message.MessageCreateEvent
import org.springframework.stereotype.Component

@Component
class PosconMetarEmbedBuilder {
    suspend fun createMetarEmbed(
        messageCreateEvent: MessageCreateEvent,
        posconMetarResponse: PosconMetarResponse
    ) {
        messageCreateEvent.message.reply {
            embed {
                title = "Recorded METAR for ${posconMetarResponse.icao}"

                field {
                    name = "METAR"
                    inline = false
                    value = posconMetarResponse.metar ?: "No METAR received"
                }
            }
        }
    }
}
