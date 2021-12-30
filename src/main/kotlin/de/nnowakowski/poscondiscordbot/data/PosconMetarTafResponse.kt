package de.nnowakowski.poscondiscordbot.data

data class PosconMetarTafResponse(
    val icao: String?,
    val metar: String?,
    val taf: String?
)
