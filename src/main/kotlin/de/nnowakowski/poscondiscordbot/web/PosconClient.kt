package de.nnowakowski.poscondiscordbot.web

import de.nnowakowski.poscondiscordbot.data.PosconMetarResponse
import de.nnowakowski.poscondiscordbot.data.PosconOnlineResponse
import de.nnowakowski.poscondiscordbot.data.PosconTafResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToFlux
import reactor.core.publisher.Flux
import reactor.util.retry.Retry
import java.time.Duration

@Component
class PosconClient(
    @Autowired private val webClientBuilder: WebClient.Builder,
    @Value("\${poscon.api.retry-count}") private val posconApiRetryCount: Long
) {
    private val webClient: WebClient = webClientBuilder.baseUrl("https://hqapi.poscon.net/").build()
    private val servicesWebClient: WebClient = webClientBuilder.baseUrl("https://services.poscon.com/").build()

    fun getOnlineUsers(): Flux<PosconOnlineResponse> {
        return webClient.get().uri("online.json").retrieve().bodyToFlux<PosconOnlineResponse>()
            .retryWhen(
                Retry.backoff(posconApiRetryCount, Duration.ofSeconds(2)).doBeforeRetry {
                    println("Couldn't reach API, retrying ...")
                }
            )
    }

    fun getMetarInfo(icao: String): Flux<PosconMetarResponse> {
        return servicesWebClient.get().uri("metar/$icao").retrieve().bodyToFlux<PosconMetarResponse>().retryWhen(
            Retry.backoff(posconApiRetryCount, Duration.ofSeconds(2)).doBeforeRetry {
                println("Couldn't reach API, retrying ...")
            }
        )
    }

    fun getTafInfo(icao: String): Flux<PosconTafResponse> {
        return servicesWebClient.get().uri("taf/$icao").retrieve().bodyToFlux<PosconTafResponse>().retryWhen(
            Retry.backoff(posconApiRetryCount, Duration.ofSeconds(2)).doBeforeRetry {
                println("Couldn't reach API, retrying ...")
            }
        )
    }
}
