package dk.itu.moapd.scootersharing

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)

class ValidatorTest {

    @Test
    //pass scenario
    fun whenInputIsValid(){
        val result = LoginValidation.validateLoginInput(
            "JohnDoe@gmail.com",
            "P@ssw0rd"
        )
        assertThat(result).isEqualTo(true)
    }
    @Test
    //fail scenario: when user enters a invalid email sequence
    fun whenInputEmailIsNotValid(){
        val result = LoginValidation.isValidEmail(
            "JohnDoekail.com"
        )
        println(result)
        assertThat(result).isEqualTo(false)
    }
}