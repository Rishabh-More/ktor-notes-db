package com.invizio.mongo.authorization

import org.mindrot.jbcrypt.BCrypt

object BCryptHasher {

    /**
     * Validates a plain un-encrypted password, with the
     * encrypted hash saved in the database
     */
    fun validatePassword(passwordToValidate: String?, encryptedHash: String) : Boolean {
        return BCrypt.checkpw(passwordToValidate, encryptedHash)
    }

    /**
     * Generates a Password Hash using the Bcrypt algorithm for the supplied plain text password
     */
    fun hashPassword(password: String) : String =
        BCrypt.hashpw(password, BCrypt.gensalt())
}