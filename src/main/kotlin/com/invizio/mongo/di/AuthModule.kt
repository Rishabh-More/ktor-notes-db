package com.invizio.mongo.di

import com.invizio.mongo.authorization.JwtConfig
import org.koin.dsl.module

val authModule = module {
    //Return a single instance of our JWT Configuration
    single { JwtConfig(get()) }
}