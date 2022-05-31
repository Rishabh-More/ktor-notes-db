package com.invizio.mongo.routes

import com.invizio.mongo.model.Note
import com.invizio.mongo.model.PackagedResponse
import com.invizio.mongo.repository.NotesRepository
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject

fun Application.noteRoutes() {
    routing {
        //Initialize our repository
        //val repository = InMemoryNotesRepositoryImpl()
        val repository: NotesRepository by inject() //Koin will provide our repository

        get("/") {
            call.respondText("Welcome to the Notes App")
        }

        authenticate {
            //Api to get list of notes
            get("/notes") {
                call.respond(
                    HttpStatusCode.OK,
                    PackagedResponse(
                        status = HttpStatusCode.OK.value,
                        message = "List of Notes fetched successfully",
                        data = repository.getAllNotes()
                    )
                )
            }

            //Api to get a Single Note with Id
            get("/notes/{id}") {
                //Get the parameter from request parameter
                val id = call.parameters["id"]?.toLongOrNull()
                if(id == null) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        PackagedResponse<Note>(
                            status = HttpStatusCode.BadRequest.value,
                            message = "Id must be a valid Integer"
                        )
                    )
                } else {
                    val note = repository.getNote(id) //it will be either note or null (not found)
                    if(note != null) { //If a note was found, return the note
                        call.respond(
                            HttpStatusCode.OK,
                            PackagedResponse(
                                status = HttpStatusCode.OK.value,
                                message = "Note fetched successfully",
                                data = note
                            )
                        )
                    } else {
                        call.respond(
                            HttpStatusCode.NotFound,
                            PackagedResponse<Note>(
                                status = HttpStatusCode.NotFound.value,
                                message = "Note does not exist"
                            )
                        )
                    }
                }
            }

            //Api to Add a list of notes
            post("/notes") {
                val note = repository.insertOrUpdate(
                    null, //we don't have an id parameter here
                    //Get the Json Request body from the post request
                    call.withValidRequestBody {
                        call.respond(
                            HttpStatusCode.BadRequest,
                            PackagedResponse<Note>(
                                status = HttpStatusCode.BadRequest.value,
                                message = "Invalid Request Body! check your request body again!"
                            )
                        )
                        return@post
                    }
                )
                //Return the inserted object to the response
                call.respond(
                    HttpStatusCode.OK,
                    PackagedResponse(
                        status = HttpStatusCode.OK.value,
                        message = "Note created Successfully",
                        data = note
                    )
                )
            }

            //Api to Update an existing Note with Id
            put("/notes/{id}") {
                val noteId = call.parameters["id"]?.toLongOrNull()
                if(noteId == null) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        PackagedResponse<Note>(
                            status = HttpStatusCode.BadRequest.value,
                            message = "Id must be a valid Integer"
                        )
                    )
                } else {
                    val updatedNote = repository.insertOrUpdate(
                        noteId, //Send the id which ID needs to be updated
                        //Get the Json Request body from the put request
                        call.withValidRequestBody {
                            call.respond(
                                HttpStatusCode.BadRequest,
                                PackagedResponse<Note>(
                                    status = HttpStatusCode.BadRequest.value,
                                    message = "Invalid Request Body! check your request body again!"
                                )
                            )
                            return@put
                        }
                    )
                    //Return the updated object to the response
                    call.respond(
                        HttpStatusCode.OK,
                        PackagedResponse(
                            status = HttpStatusCode.OK.value,
                            message = "Note updated successfully!",
                            data = updatedNote
                        )
                    )
                }
            }

            //Api to Delete an existing Note with Id
            delete("/notes/{id}") {
                //Get the id to delete a note
                val noteId = call.parameters["id"]?.toLongOrNull()
                if(noteId == null) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        PackagedResponse<Note>(
                            status = HttpStatusCode.BadRequest.value,
                            message = "Id must be a valid Integer"
                        )
                    )
                } else {
                    val hasDeleted = repository.removeNote(noteId)
                    if(hasDeleted) {
                        call.respond(
                            HttpStatusCode.OK,
                            PackagedResponse<Note>(
                                status = HttpStatusCode.OK.value,
                                message = "Note removed successfully"
                            )
                        )
                    } else {
                        call.respond(
                            HttpStatusCode.NotFound,
                            PackagedResponse<Note>(
                                status = HttpStatusCode.NotFound.value,
                                message = "Failed to delete, Note does not exist"
                            )
                        )
                    }
                }
            }
        }
    }
}

suspend inline fun <reified T : Any> ApplicationCall.withValidRequestBody(
    onBadRequest: () -> Unit = {}
) : T? {
    //Try to parse the json request
    return try {
        this.receive()
    } catch (e: Exception) {
        //If a serialization exception occurred, let the use handle the exception
        onBadRequest()
        null
    }
}