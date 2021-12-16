package de.nnowakowski.poscondiscordbot.data

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.ZonedDateTime

data class PosconOnlineResponse(
    val lastUpdated: ZonedDateTime?,
    val atc: Array<Atc>,
    val upcomingAtc: Array<Atc>,
    val flights: Array<Flight>,
    val upcomingFlights: Array<Flight>
)

data class Flight(
    val callsign: String?, @JsonProperty("ac_type") val acTpe: String?, val flightplan: Flightplan?
)

data class Flightplan(val altnt: String?, val dep: String?, val dest: String?)

data class Atc(val telephony: String?, val fir: String?, val type: String?, val vhfFreq: String?)