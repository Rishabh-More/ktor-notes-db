package com.invizio.mongo.plugins

import com.invizio.mongo.authorization.JwtConfig
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import org.koin.ktor.ext.inject

fun Application.configureAuthentication(){
    //Get our JWT Config Instance from Dependency Injection
    val jwtConfig: JwtConfig by inject()

    install(Authentication) {
        jwt {
            //Allows us to fetch a JwtUser object using claims or returns null
            jwtConfig.configureAuthenticationFeature(this)
        }
    }
}