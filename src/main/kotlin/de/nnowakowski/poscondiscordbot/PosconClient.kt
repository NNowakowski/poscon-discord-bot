package de.nnowakowski.poscondiscordbot

import de.nnowakowski.poscondiscordbot.data.PosconOnlineResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToFlux
import reactor.core.publisher.Flux
import reactor.util.retry.Retry
import java.time.Duration

@Component
class PosconClient(@Autowired private val webClientBuilder: WebClient.Builder) {
    private val webClient: WebClient = webClientBuilder.baseUrl("https://hqapi.poscon.net/").build()

    fun getOnlineUsers(): Flux<PosconOnlineResponse> {
        return webClient.get().uri("online.json").retrieve().bodyToFlux<PosconOnlineResponse>()
            .retryWhen(Retry.backoff(5, Duration.ofSeconds(2)).doBeforeRetry {
                println("Couldn't reach API, retrying ...")
            })
    }
}