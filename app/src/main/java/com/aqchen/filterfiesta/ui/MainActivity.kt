package com.aqchen.filterfiesta.ui

import android.animation.ObjectAnimator
import android.animation.TimeInterpolator
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.LinearInterpolator
import android.view.animation.PathInterpolator
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.fragment.NavHostFragment
import com.aqchen.filterfiesta.R
import com.aqchen.filterfiesta.databinding.ActivityMainBinding
import com.google.android.material.motion.MotionUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        // Handle the splash screen transition.
        // Note: when launching the app from Android Studio (rather from the device), there is a bug
        // where the app icon will not show up: https://issuetracker.google.com/issues/205021357
        // Note: Must be *before* super.onCreate()
        val splashScreen = installSplashScreen()
        splashScreen.setOnExitAnimationListener { splashScreenView ->
            // Add fade out animation for the splash screen when exiting
            val fadeOut = ObjectAnimator.ofFloat(
                splashScreenView.view,
                View.ALPHA,
                1f,
                0f
            )
            // Resolve Material 3 emphasized accelerate easing, with linear fallback
            fadeOut.interpolator = MotionUtils.resolveThemeInterpolator(
                applicationContext,
                com.google.android.material.R.attr.motionEasingEmphasizedAccelerateInterpolator,
                LinearInterpolator()
            )
            // Resolve Material 3 duration short 4, with fallback to 200 ms
            fadeOut.duration = MotionUtils.resolveThemeDuration(
                applicationContext,
                com.google.android.material.R.attr.motionDurationShort4, // 200 ms
                200
            ).toLong()

            // Call SplashScreenView.remove at the end of the animation.
            fadeOut.doOnEnd { splashScreenView.remove() }
            // Run the animation.
            fadeOut.start()
        }

        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        // https://developer.android.com/guide/navigation/navigation-getting-started#navigate
        // https://stackoverflow.com/questions/58703451/fragmentcontainerview-as-navhostfragment
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
        val navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.menu_main, menu)
//        return true
//    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}