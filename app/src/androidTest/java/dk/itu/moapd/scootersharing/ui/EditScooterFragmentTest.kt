package dk.itu.moapd.scootersharing.ui
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import androidx.fragment.app.testing.FragmentScenario
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.*
import dk.itu.moapd.scootersharing.R


@RunWith(AndroidJUnit4::class)
class EditScooterFragmentTest {

    private lateinit var scenario  : FragmentScenario<EditScooterFragment>

    @Before
    fun setup(){
        scenario = launchFragmentInContainer(themeResId = R.style.Theme_ScooterSharing)
        scenario.moveToState(Lifecycle.State.STARTED)
    }

    @Test

    fun testCheckInput(){
        val name = "Test"
        val price = 50
        val status ="Locked"


        onView(withId(R.id.name_text)).perform(
            clearText()).perform(typeText(name))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.price)).perform(
            clearText()).perform(typeText(price.toString()))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.status)).perform(
            clearText()).perform(typeText(status))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.update_button)).perform(click());
        assertThat(onView(withId(R.id.resultTxt)).check(matches(withText("Scooter Updated!"))))


    }

}