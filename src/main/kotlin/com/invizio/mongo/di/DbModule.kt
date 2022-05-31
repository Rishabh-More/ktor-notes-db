package com.invizio.mongo.di

import com.invizio.mongo.database.mysql.DatabaseManager
import com.invizio.mongo.database.mysql.dao.NotesDao
import com.invizio.mongo.database.mysql.dao.UsersDao
import org.koin.dsl.module

val dbModule = module {
    single { DatabaseManager() } //Single Instance of MySql Database Connection
    factory { UsersDao(get(), get()) }
    factory { NotesDao(get()) }
}