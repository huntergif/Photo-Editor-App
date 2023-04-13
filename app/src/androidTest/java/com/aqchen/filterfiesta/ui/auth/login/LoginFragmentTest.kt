package com.aqchen.filterfiesta.ui.auth.login

import android.os.Build
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.filters.MediumTest
import androidx.test.filters.SdkSuppress
import com.aqchen.filterfiesta.launchFragmentInHiltContainer
import com.aqchen.filterfiesta.R
import com.aqchen.filterfiesta.util.clickClickableSpan
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.After
import org.junit.AfterClass
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.Thread.sleep

@MediumTest
@HiltAndroidTest
class LoginFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    private lateinit var navController: NavController

    @Before
    fun setup() {
        hiltRule.inject()
        navController = mockk()
        // launch login fragment in HiltTestActivity
        launchFragmentInHiltContainer<LoginFragment> {
            Navigation.setViewNavController(requireView(), navController)
        }
    }

    @After
    fun clearAll() {
        clearAllMocks()
    }

    companion object {
        @JvmStatic
        @AfterClass
        fun cleanup() {
            unmockkAll()
        }
    }

    @Test
    fun whenClickSignUpActionText_navigateToRegisterFragment() {
        // setup
        // need to mock method or else calls will not be tracked - also must provide answer (Unit in this case)
        every {
            navController.navigate(R.id.action_loginFragment_to_registerFragment)
        } returns Unit

        // run
        onView(withId(R.id.login_signup_text)).perform(clickClickableSpan(R.string.login_signup_action_text))

        // assert
        verify(exactly = 1) {
            navController.navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    @Test
    fun whenClickResetPasswordActionText_navigateToResetPasswordFragment() {
        // setup
        // need to mock method or else calls will not be tracked - also must provide answer (Unit in this case)
        every {
            navController.navigate(R.id.action_loginFragment_to_passwordResetFragment)
        } returns Unit

        // run
        onView(withId(R.id.forgot_password_text)).perform(clickClickableSpan(R.string.login_password_action_text))

        // assert
        verify(exactly = 1) {
            navController.navigate(R.id.action_loginFragment_to_passwordResetFragment)
        }
    }

    @Test
    fun whenInputEmptyEmailAndPasswordAndSubmit_showErrorAndDoNotNavigate() {
        // setup
        // nothing

        // run
        onView(withId(R.id.login_email_input)).perform(typeText(""))
        onView(withId(R.id.login_password_input)).perform(typeText(""))
        onView(withId(R.id.login_submit_button)).perform(click())

        // assert
        verify(exactly = 0) {
            navController.navigate(any<Int>())
        }
        onView(withText("The email can't be blank")).check(matches(isDisplayed()))
        onView(withText("The password can't be empty")).check(matches(isDisplayed()))
        onView(withId(R.id.login_submit_button)).check(matches(isEnabled()))
    }

    @Test
    fun whenInputInvalidEmailAndSubmit_showErrorAndDoNotNavigate() {
        // setup
        // nothing

        // run
        onView(withId(R.id.login_email_input)).perform(typeText("a"))
        onView(withId(R.id.login_password_input)).perform(typeText("1"))
        onView(withId(R.id.login_submit_button)).perform(click())

        // assert
        verify(exactly = 0) {
            navController.navigate(any<Int>())
        }
        onView(withText("Invalid email format")).check(matches(isDisplayed()))
        onView(withId(R.id.login_submit_button)).check(matches(isEnabled()))
    }

    // Due to Espresso bug, this test won't work on api levels 28-30
    // Only run this test on api levels >= 31 (S)
    // https://github.com/android/android-test/issues/1642
    @SdkSuppress(minSdkVersion = Build.VERSION_CODES.S)
    @Test
    fun whenInputInvalidCredentialsAndSubmit_doNotNavigate() {
        // setup
        // nothing

        // run
        onView(withId(R.id.login_email_input)).perform(typeText("test@invalid.com"))
        onView(withId(R.id.login_password_input)).perform(typeText("1"))
        onView(withId(R.id.login_submit_button)).perform(click())
        // should refactor in the future to reduce flakiness
        sleep(100)
        // assert
        verify(exactly = 0) {
            navController.navigate(any<Int>())
        }
        onView(withId(R.id.login_submit_button)).check(matches(isEnabled()))
    }
}
