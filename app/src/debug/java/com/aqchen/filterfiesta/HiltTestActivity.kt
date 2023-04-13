package com.aqchen.filterfiesta

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HiltTestActivity : AppCompatActivity() {
    companion object {
        const val THEME_EXTRAS_BUNDLE_KEY = "androidx.fragment.app.testing.FragmentScenario.EmptyFragmentActivity.THEME_EXTRAS_BUNDLE_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        // https://github.com/android/architecture-samples/issues/832
        setTheme(intent.getIntExtra(THEME_EXTRAS_BUNDLE_KEY, androidx.fragment.testing.R.style.FragmentScenarioEmptyFragmentActivityTheme))
        super.onCreate(savedInstanceState)
    }
}
