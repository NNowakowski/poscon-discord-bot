# poscon-discord-bot

### Description

This repository contains a [Discord](https://discord.com) chatbot for easily accessing various API functionalities of
the [POSCON](https://poscon.net/) flight simulation network. As the API is steadily evolving the bot will continue to
receive more and more commands.

#### Connected Endpoints

* [Online Data](https://cdn.poscon.com/docs/services/index.html#operation/get-online-data)
* [Metar](https://cdn.poscon.com/docs/services/index.html#operation/get-metar-ICAO)

### Languages & Frameworks

* [Kotlin](https://kotlinlang.org/)
* [Gradle](https://gradle.org/)
* [Spring](https://spring.io/)
* [MongoDB](https://www.mongodb.com/)
* [Kord](https://github.com/kordlib/kord/)

### Additional Notes

MongoDB aswell as some Spring functionality like Security or the embedded Netty are currently unused. If you don't want
to launch a MongoDB you can safely remove the dependencies; otherwise you can spin up a simple container with the
included docker-compose.yml. 
