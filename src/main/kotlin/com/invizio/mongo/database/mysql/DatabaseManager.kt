package com.invizio.mongo.database.mysql

import org.ktorm.database.Database
import org.ktorm.logging.ConsoleLogger
import org.ktorm.logging.LogLevel

class DatabaseManager {

    //configuration
    private val hostname = "127.0.0.1"
    private val port = "3306"
    private val dbName = "ktor-notes"
    private val username = "ktor_user"
    private val password = "ktor-notes-123"

    //database instance
    val ktormDb: Database

    init {
        val jdbcUrl = "jdbc:mysql://$hostname:$port/$dbName"
        val jdbcDriver = "com.mysql.cj.jdbc.Driver"
        //Connect to Db & get the instance
        ktormDb = Database.connect(
            url = jdbcUrl,
            driver = jdbcDriver,
            user = username,
            password = password,
            logger = ConsoleLogger(threshold = LogLevel.DEBUG)
        )
    }
}