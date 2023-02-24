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
import android.view.animation.AnticipateInterpolator
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.aqchen.filterfiesta.R
import com.aqchen.filterfiesta.databinding.ActivityMainBinding
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
            fadeOut.interpolator = LinearInterpolator()
            fadeOut.duration = 200L // 200 ms

            // Call SplashScreenView.remove at the end of the animation.
            fadeOut.doOnEnd { splashScreenView.remove() }
            // Run the animation.
            fadeOut.start()
        }

        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        Log.i("MainActivity", "MainActivity onCreate")

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onStart() {
        super.onStart()
        Log.i("MainActivity", "MainActivity onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.i("MainActivity", "MainActivity onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.i("MainActivity", "MainActivity onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.i("MainActivity", "MainActivity onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("MainActivity", "MainActivity onDestroy")
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

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