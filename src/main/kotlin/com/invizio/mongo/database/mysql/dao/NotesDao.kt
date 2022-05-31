package com.invizio.mongo.database.mysql.dao

import com.invizio.mongo.database.mysql.DatabaseManager
import com.invizio.mongo.database.mysql.entities.NoteEntity
import com.invizio.mongo.database.mysql.entities.NotesTable
import com.invizio.mongo.model.InsertOrUpdate
import com.invizio.mongo.model.Note
import org.ktorm.dsl.delete
import org.ktorm.dsl.eq
import org.ktorm.dsl.insertAndGenerateKey
import org.ktorm.dsl.update
import org.ktorm.entity.firstOrNull
import org.ktorm.entity.sequenceOf
import org.ktorm.entity.toList

class NotesDao(val db: DatabaseManager) {

    fun insertOrUpdate(id: Long?, body: InsertOrUpdate?) : NoteEntity {
        val existingNote = db.ktormDb.sequenceOf(NotesTable).firstOrNull { it.id eq (id?.toInt() ?: 0) }
        //If existingNote object is not null, then we update existing object
        return if(existingNote != null) {
            db.ktormDb.update(NotesTable) {
                set(NotesTable.title, body?.title)
                set(NotesTable.done, body?.isDone)
                //Add a conditional where query clause, to only update the fields if it matches the id
                where { it.id eq id!!.toInt() }
            }
            //Return a NoteEntity object using the Companion Object Factory method from Ktorm
            NoteEntity {
                this.id = id!!.toInt()
                this.title = body?.title
                this.done = body?.isDone
            }
        } else {
            //otherwise, insert a new object in db & auto-generate it's primary key
            val generatedId = db.ktormDb.insertAndGenerateKey(NotesTable) {
                set(NotesTable.title, body?.title)
                set(NotesTable.done, body?.isDone)
            } as Int
            //Return a NoteEntity object using the Companion Object Factory method from Ktorm
            NoteEntity {
                this.id = generatedId
                this.title = body?.title
                this.done = body?.isDone
            }
        }
    }

    fun getNotes() : List<NoteEntity> {
        //Return a list of Note Entities from Notes Table in MySql Database
        return db.ktormDb.sequenceOf(NotesTable).toList()
    }

    fun getNote(id: Long) : NoteEntity? {
        return db.ktormDb.sequenceOf(NotesTable)
            //Here the "eq" method is from the Ktorm ORM and works as equals/ ==
            .firstOrNull { it.id eq id.toInt() }
    }

    fun removeNote(id: Long): Boolean {
        val deletedRows = db.ktormDb.delete(NotesTable) {
            //Specify condition where delete entity row only if it's id matches
            it.id eq id.toInt()
        }
        //If No. of rows deleted will be greater than 0, means at least 1 row indeed deleted
        return deletedRows > 0
    }
}

/**
 * Mapper function to convert Entity Object to Model
 */
fun NoteEntity.toNote() : Note {
    return Note(
        id = id.toLong(),
        title = title,
        done = done
    )
}

/**
 * Mapper function to convert list of Entities to list of Models
 */
fun List<NoteEntity>.toNotes() : List<Note> {
    return map { it.toNote() }
}