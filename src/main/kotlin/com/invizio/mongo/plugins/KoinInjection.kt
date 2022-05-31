package com.invizio.mongo.plugins

import com.invizio.mongo.di.authModule
import com.invizio.mongo.di.dbModule
import com.invizio.mongo.di.repositoryModule
import io.ktor.application.*
import org.koin.dsl.module
import org.koin.ktor.ext.Koin

fun Application.configureDependencyInjection() {
    //Create an app module which will provide Singleton Instance for Application
    val appModule = module {
        single { this@configureDependencyInjection }
    }

    //Install Koin Module for our server
    install(Koin) {
        modules(
            listOf(
                appModule,
                authModule,
                dbModule,
                repositoryModule
            )
        ) //define Koin Modules here
    }
}