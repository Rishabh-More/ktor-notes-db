package com.invizio.mongo.di

import com.invizio.mongo.repository.AuthRepository
import com.invizio.mongo.repository.MySqlNotesRepositoryImpl
import com.invizio.mongo.repository.MySqlUserRepositoryImpl
import com.invizio.mongo.repository.NotesRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<NotesRepository> { MySqlNotesRepositoryImpl(get()) }
    //single<NotesRepository> { InMemoryNotesRepositoryImpl() }
    single<AuthRepository> { MySqlUserRepositoryImpl(get()) }
}