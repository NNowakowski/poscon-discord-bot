package de.nnowakowski.poscondiscordbot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PosconDiscordBotApplication

@Suppress("SpreadOperator")
fun main(args: Array<String>) {
    runApplication<PosconDiscordBotApplication>(*args)
}
