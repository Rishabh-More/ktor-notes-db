package com.invizio.mongo.repository

import com.invizio.mongo.common.Constants
import com.invizio.mongo.model.InsertOrUpdate
import com.invizio.mongo.model.Note

class InMemoryNotesRepositoryImpl : NotesRepository {

    override fun insertOrUpdate(
        id: Long?, body: InsertOrUpdate?
    ): Note {
        val existingNote = Constants.tempNotes.firstOrNull { it.id == id }
        //If existingNote object is not null, then we update existing object
        return if(existingNote != null) {
            //Update existing note's value except id
            existingNote.title = body?.title
            existingNote.done = body?.isDone
            //finally, return the existing note
            existingNote
        } else {
            //otherwise, we create a new object because there was no existing note found
            val noteToInsertOrUpdate = Note(
                id = (Constants.tempNotes.size + 1).toLong(),
                title = body?.title,
                done = body?.isDone,
            )
            //Add new note to List
            Constants.tempNotes.add(noteToInsertOrUpdate)
            //finally, we return the new note
            noteToInsertOrUpdate
        }
    }

    override fun getAllNotes(): List<Note> {
        //return sorted list with ascending order of id's
        return Constants.tempNotes.sortedBy { it.id }
    }

    override fun getNote(id: Long): Note? {
        return Constants.tempNotes.firstOrNull { it.id == id }
    }

    override fun removeNote(id: Long): Boolean {
        //Returns a boolean if any value was removed from the list
        return Constants.tempNotes.removeIf { it.id == id }
    }
}