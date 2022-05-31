package com.invizio.mongo.repository

import com.invizio.mongo.database.mysql.dao.NotesDao
import com.invizio.mongo.database.mysql.dao.toNote
import com.invizio.mongo.database.mysql.dao.toNotes
import com.invizio.mongo.model.InsertOrUpdate
import com.invizio.mongo.model.Note

class MySqlNotesRepositoryImpl(val dao: NotesDao) : NotesRepository {

    override fun insertOrUpdate(id: Long?, body: InsertOrUpdate?): Note {
        return dao.insertOrUpdate(id, body).toNote()
    }

    override fun getAllNotes(): List<Note> {
        return dao.getNotes().toNotes()
    }

    override fun getNote(id: Long): Note? {
        return dao.getNote(id)?.toNote()
    }

    override fun removeNote(id: Long): Boolean {
        return dao.removeNote(id)
    }
}