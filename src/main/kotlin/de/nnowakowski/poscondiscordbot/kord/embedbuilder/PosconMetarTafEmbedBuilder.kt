package de.nnowakowski.poscondiscordbot.kord.embedbuilder

import de.nnowakowski.poscondiscordbot.data.PosconMetarTafResponse
import dev.kord.core.behavior.reply
import dev.kord.core.event.message.MessageCreateEvent
import org.springframework.stereotype.Component

@Component
class PosconMetarTafEmbedBuilder {
    suspend fun createMetarTafEmbed(
        messageCreateEvent: MessageCreateEvent,
        posconMetarTafResponse: PosconMetarTafResponse
    ) {
        messageCreateEvent.message.reply {
            embed {
                title = "Recorded METAR & TAF for ${posconMetarTafResponse.icao}"

                field {
                    name = "METAR"
                    inline = false
                    value = posconMetarTafResponse.metar ?: "No METAR received"
                }

                field {
                    name = "TAF"
                    inline = false
                    value = posconMetarTafResponse.taf ?: "No TAF received"
                }
            }
        }
    }
}
