package com.invizio.mongo.repository

import com.invizio.mongo.model.InsertOrUpdate
import com.invizio.mongo.model.Note

interface NotesRepository {

    fun insertOrUpdate(id: Long?, body: InsertOrUpdate?) : Note

    fun getAllNotes() : List<Note>

    fun  getNote(id: Long) : Note?

    fun removeNote(id: Long) : Boolean
}