package com.invizio.mongo

import com.invizio.mongo.plugins.*
import com.invizio.mongo.routes.authorizationRoutes
import com.invizio.mongo.routes.noteRoutes
import io.ktor.application.*
import io.ktor.server.netty.*

fun main(args: Array<String>) = EngineMain.main(args)

fun Application.main() {
    configureRouting()
    configureMonitoring()
    configureSerialization()
    configureDependencyInjection()
    configureAuthentication()
    authorizationRoutes()
    noteRoutes()
}
