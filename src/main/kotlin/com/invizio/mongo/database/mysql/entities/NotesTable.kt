package com.invizio.mongo.database.mysql.entities

import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.boolean
import org.ktorm.schema.int
import org.ktorm.schema.varchar

/**
 * Provide the actual table name as that in MySql database/schema
 */
object NotesTable : Table<NoteEntity>("notes") {

    //Define the MySql table columns here, along with their type
    // (as type defined in the MySql table)
    val id = int("id").primaryKey().bindTo { it.id }
    val title = varchar("title").bindTo { it.title }
    //Ktorm can auto map boolean values to tinyint(0) or tinyint(1)
    val done = boolean("done").bindTo { it.done }
}

interface NoteEntity: Entity<NoteEntity> {

    companion object : Entity.Factory<NoteEntity>()

    var id: Int
    var title: String?
    var done: Boolean?
}