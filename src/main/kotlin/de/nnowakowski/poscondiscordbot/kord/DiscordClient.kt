package de.nnowakowski.poscondiscordbot.kord

import de.nnowakowski.poscondiscordbot.kord.listener.MessageCreateEventListener
import de.nnowakowski.poscondiscordbot.web.PosconClient
import dev.kord.core.Kord
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.core.on
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.reactive.awaitSingle
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class DiscordClient(
    @Value("\${discord.bot.token}") private val discordBotToken: String,
    @Value("\${discord.status-update.interval-in-ms}") private val discordStatusUpdateIntervalInMs: Long,
    @Autowired private val messageCreateEventListener: MessageCreateEventListener,
    @Autowired private val posconClient: PosconClient
) {
    @EventListener(ApplicationReadyEvent::class)
    fun createDiscordClient() {
        runBlocking {
            val kord: Kord = Kord(discordBotToken)

            kord.on<MessageCreateEvent> {
                messageCreateEventListener.handle(kord, this)
            }

            launch {
                while (isActive) {
                    val posconOnlineResponse = posconClient.getOnlineUsers().awaitSingle()

                    kord.editPresence {
                        watching("${posconOnlineResponse.atc.size} ATC & ${posconOnlineResponse.flights.size} pilots!")
                    }

                    delay(discordStatusUpdateIntervalInMs)
                }
            }

            kord.login()
        }
    }
}
