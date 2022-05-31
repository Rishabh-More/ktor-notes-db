package com.invizio.mongo.common

import com.invizio.mongo.model.Note

object Constants {

    /**
     * A Regex Pattern to validate an email string
     */
    const val EMAIL_PATTERN = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
        "\\@" +
        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
        "(" +
        "\\." +
        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
        ")+"

    val tempNotes = mutableListOf(
        Note(1, "Do some amazing work", true),
        Note(2, "We are the mf suicide squad!", false),
        Note(3, "Tell me. Do you bleed red or blue?", false)
    )
}