package com.invizio.mongo.authorization

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*

class JwtConfig(app: Application) {

    companion object {
        //claims
        private const val CLAIM_USER_ID = "userId"
        private const val CLAIM_USER_NAME = "userName"
    }

    //jwt config constants
    private val secret = app.environment.config.property("jwt.secret").getString()
    private val issuer = app.environment.config.property("jwt.issuer").getString()
    private val appRealm = app.environment.config.property("jwt.realm").getString()

    private val algorithm = Algorithm.HMAC512(secret)
    private val verifier: JWTVerifier = JWT.require(algorithm).withIssuer(issuer).build()

    /**
     * Generate a token for authorized user
     */
    fun generateToken(user: JwtUser): String = JWT.create()
        .withSubject("Authorization")
        .withIssuer(issuer)
        .withClaim(CLAIM_USER_ID, user.userId)
        .withClaim(CLAIM_USER_NAME, user.userName)
        .sign(algorithm)

    /**
     * Configure the Jwt Ktor Authentication Feature
     */
    fun configureAuthenticationFeature(config: JWTAuthenticationProvider.Configuration) =
        with(config) {
            verifier(verifier)
            realm = appRealm
            validate {
                val userId = it.payload.getClaim(CLAIM_USER_ID).asInt()
                val userName = it.payload.getClaim(CLAIM_USER_NAME).asString()

                if(userId != null && !userName.isNullOrBlank())
                    JwtUser(userId, userName)
                else null
            }
        }

    data class JwtUser(val userId: Int, val userName: String) : Principal
}