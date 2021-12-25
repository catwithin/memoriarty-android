package com.gamesofni.memoriarty

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.gamesofni.memoriarty.databinding.ActivityMainBinding
import timber.log.Timber


//const val KEY_TIMER_SECONDS = "timer_seconds_key"


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
//    private lateinit var timer: Timer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        drawerLayout = binding.drawerLayout

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.overviewFragment) as NavHostFragment
        val navController = navHostFragment.navController
        NavigationUI.setupActionBarWithNavController(this,navController, drawerLayout)

        NavigationUI.setupWithNavController(binding.navView, navController)

//
//        timer = Timer(this.lifecycle)
//
//        if (savedInstanceState != null) {
//            timer.secondsCount = savedInstanceState.getInt(KEY_TIMER_SECONDS, 0)
//        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.overviewFragment) as NavHostFragment
        val navController = navHostFragment.navController
        return NavigationUI.navigateUp(navController, drawerLayout)
    }

//    override fun onStop() {
//        super.onStop()
//        Timber.i("On stop")
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        Timber.i("on destroy")
//    }
//
//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//
//        Timber.i("onSaveInstanceState Called")
//        outState.putInt(KEY_TIMER_SECONDS, timer.secondsCount)
//    }
}
