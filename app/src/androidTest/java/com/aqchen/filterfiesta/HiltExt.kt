package com.aqchen.filterfiesta

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.annotation.StyleRes
import androidx.core.util.Preconditions
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider

// This functions will take a fragment type and attaches it the to the HiltTestActivity
// https://github.com/android/architecture-samples/blob/2291fc6d2e17a37be584a89b80ee73c207c804c3/app/src/androidTest/java/com/example/android/architecture/blueprints/todoapp/HiltExt.kt#L37
// https://www.youtube.com/watch?v=k4zG93ogWFY
// https://github.com/android/architecture-samples/issues/832
inline fun <reified T : Fragment> launchFragmentInHiltContainer(
    fragmentArgs: Bundle? = null,
    @StyleRes themeResId: Int = R.style.Theme_FilterFiesta,
    fragmentFactory: FragmentFactory? = null,
    crossinline action: T.() -> Unit = {}
) {
    // create intent to launch HiltTestActivity
    val startActivityIntent = Intent.makeMainActivity(
        ComponentName(
            ApplicationProvider.getApplicationContext(),
            HiltTestActivity::class.java
        )
    ).putExtra(HiltTestActivity.THEME_EXTRAS_BUNDLE_KEY, themeResId)

    Log.d("HiltExt", "$startActivityIntent, $themeResId")
    Log.d("HiltExt", "${themeResId == androidx.fragment.testing.R.style.FragmentScenarioEmptyFragmentActivityTheme}")

    // launch HiltTestActivity using startActivityIntent and get reference to activity
    ActivityScenario.launch<HiltTestActivity>(startActivityIntent).onActivity { activity ->
        // set fragmentFactory if not null
        fragmentFactory?.let {
            activity.supportFragmentManager.fragmentFactory = it
        }
        // instantiate fragment we want to test
        val fragment: Fragment = activity.supportFragmentManager.fragmentFactory.instantiate(
            Preconditions.checkNotNull(T::class.java.classLoader),
            T::class.java.name
        )
        fragment.arguments = fragmentArgs
        // launch fragment using a transaction
        activity.supportFragmentManager
            .beginTransaction()
            .add(android.R.id.content, fragment, "")
            .commitNow()
        // call lambda function with reference to the fragment
        (fragment as T).action()
    }
}
