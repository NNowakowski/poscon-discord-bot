package de.nnowakowski.poscondiscordbot.data

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.ZonedDateTime

data class PosconOnlineResponse(
    val lastUpdated: ZonedDateTime?,
    val atc: Set<Atc>,
    val upcomingAtc: Set<Atc>,
    val flights: Set<Flight>,
    val upcomingFlights: Set<Flightplan>
)

data class Flight(
    val callsign: String?,
    @JsonProperty("ac_type") val acType: String?,
    val flightplan: Flightplan?
)

data class Flightplan(
    val callsign: String?,
    @JsonProperty("ac_type") val acType: String?,
    val dep: String?,
    val dest: String?,
    val std: ZonedDateTime?
)

data class Atc(
    val telephony: String?,
    val fir: String?,
    val type: String?,
    val vhfFreq: String?,
    val start: ZonedDateTime?
)
