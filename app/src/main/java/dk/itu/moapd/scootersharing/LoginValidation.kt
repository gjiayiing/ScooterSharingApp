package dk.itu.moapd.scootersharing

import java.util.regex.Pattern

object LoginValidation {
    fun validateLoginInput(
        email: String,
        password:String
    ):Boolean{
        return true
    }
    val EMAIL_ADDRESS_PATTERN: Pattern = Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )
    fun isValidEmail(email: String): Boolean{
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }
}