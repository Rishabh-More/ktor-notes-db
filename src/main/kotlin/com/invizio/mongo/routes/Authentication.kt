package com.invizio.mongo.routes

import com.invizio.mongo.authorization.JwtConfig
import com.invizio.mongo.model.Login
import com.invizio.mongo.model.PackagedResponse
import com.invizio.mongo.model.User
import com.invizio.mongo.repository.AuthRepository
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject

fun Application.authorizationRoutes() {
    routing {
        //Initialize our Authentication Repository
        val repository: AuthRepository by inject()

        //route to create a new user
        post("/signup") {
            //Get a valid request body with username, email & password
            val requestBody: Login = call.withValidRequestBody {
                call.respond(
                    HttpStatusCode.BadRequest,
                    PackagedResponse<String>(
                        status = HttpStatusCode.BadRequest.value,
                        message = "Invalid Request Body! check your request body again"
                    )
                )
                return@post
            }!!
            val hasGenerated = repository.createUser(requestBody)
            if(hasGenerated.first) {
                //User was created successfully, send success response
                call.respond(
                    HttpStatusCode.OK,
                    PackagedResponse<String>(
                        status = HttpStatusCode.OK.value,
                        message = hasGenerated.second.toString()
                    )
                )
            } else {
                //Failed to create a user, respond with 401 UnAuthorized
                call.respond(
                    HttpStatusCode.Unauthorized,
                    PackagedResponse<String>(
                        status = HttpStatusCode.Unauthorized.value,
                        message = hasGenerated.second.toString()
                    )
                )
            }
        }

        //route to log in with existing user
        post("/login") {
            //Get a valid request body with username & password
            val requestBody: Login = call.withValidRequestBody {
                call.respond(
                    HttpStatusCode.BadRequest,
                    PackagedResponse<String>(
                        status = HttpStatusCode.BadRequest.value,
                        message = "Invalid Request Body! check your request body again"
                    )
                )
                return@post
            }!!
            val hasValidated = repository.validateUser(requestBody)
            //If validation result has returned true, then we send the JWT token in response
            if(hasValidated.first){
                call.respond(
                    HttpStatusCode.OK,
                    PackagedResponse(
                        status = HttpStatusCode.OK.value,
                        message = "Login Successful!",
                        data = hasValidated.second //send the JWT token to the user
                    )
                )
            } else {
                call.respond(
                    HttpStatusCode.Unauthorized,
                    PackagedResponse<String>(
                        status = HttpStatusCode.Unauthorized.value,
                        message = hasValidated.second //return the message for which validation failed
                    )
                )
            }
        }

        //TODO Forgot Password API

        //Rest of the user operations require Authorization
        authenticate {
            //Route to get details of currently logged-in user
            get("/me"){
                //Get the current user, so that we have user id to fetch details for our user
                val currentUser = call.authentication.principal as JwtConfig.JwtUser

                //Attempt to get the user details for currently logged-in user
                val details = repository.getUser(currentUser.userId)
                //If user details is not null meaning user details were successfully fetched
                if(details != null) {
                    call.respond(
                        HttpStatusCode.OK,
                        PackagedResponse(
                            status = HttpStatusCode.OK.value,
                            message = "Found details for the current user",
                            data = details
                        )
                    )
                } else {
                    //otherwise, user was not found, & so details are null
                    call.respond(
                        HttpStatusCode.NotFound,
                        PackagedResponse<User>(
                            status = HttpStatusCode.NotFound.value,
                            message = "Could not find the user! Are you an alien?"
                        )
                    )
                }
            }
        }
    }
}