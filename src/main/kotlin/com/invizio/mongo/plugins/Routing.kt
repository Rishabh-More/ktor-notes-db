package com.invizio.mongo.plugins

import com.invizio.mongo.model.PackagedResponse
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.response.*
import io.ktor.routing.*

fun Application.configureRouting() {

    routing {
        install(StatusPages) {
            //Default response to 401 Unauthorized unless handled by the route throwing 401
            status(HttpStatusCode.Unauthorized) {
                call.respond(
                    HttpStatusCode.Forbidden,
                    PackagedResponse<String>(
                        status = HttpStatusCode.Forbidden.value,
                        message = "You do not have the credentials to access this page!"
                    )
                )
            }
        }
        // Static plugin. Try to access `/static/index.html`
        static("/static") {
            resources("static")
        }
    }
}

class AuthenticationException : RuntimeException()
class AuthorizationException : RuntimeException()
