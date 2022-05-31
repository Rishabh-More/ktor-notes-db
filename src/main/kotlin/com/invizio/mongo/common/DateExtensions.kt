package com.invizio.mongo.common

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

const val FORMAT_12_HOUR_TIME_YEAR = "hh:mm:ss a dd-MM-yyyy"
const val FORMAT_24_HOUR_TIME_YEAR = "HH:mm:ss dd-MM-yyyy"
const val FORMAT_24_HOUR_YEAR_TIME = "yyyy-MM-dd HH:mm:ss"

fun getSystemTimeInstant(format: String = FORMAT_12_HOUR_TIME_YEAR) : Instant {
    //val formatter = DateTimeFormatter.ofPattern(format)
    return LocalDateTime.now()
        .atZone(ZoneId.of("Asia/Kolkata"))
        .toInstant() //An Instant is always in UTC by default
}