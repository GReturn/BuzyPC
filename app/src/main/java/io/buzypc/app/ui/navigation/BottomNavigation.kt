package io.buzypc.app.ui.navigation

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.buzypc.app.R
import io.buzypc.app.ui.widget.NewBuildCircleRevealView
import kotlin.math.hypot


class BottomNavigation : AppCompatActivity() {
    private val viewModel: StyleViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_navigation)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        // We'll let the BottomNavigation Activity handle the theme change instead of the
        // Settings Fragment
        viewModel.themeState.observe(this) { isDarkMode ->
            if (isDarkMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }


        val navController = findNavController(R.id.navController)
        bottomNavigationView.setupWithNavController(navController)

        bottomNavigationView.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.landingPageFragment -> {
                    Log.d("BottomNavigation", "LANDING Page Selected")
                    navigateToFragment(R.id.landingPageFragment)
                }
                R.id.buildListFragment -> {
                    Log.d("BottomNavigation", "BUILD LIST Page Selected")
                    navigateToFragment(R.id.buildListFragment)
                }
                R.id.newBuildFragment -> {
                    Log.d("BottomNavigation", "NEW BUILD Page Selected")
                    // we won't normally just navigate; check the function on how the
                    // navigation is handled
                    showCircle(item)
                }
                R.id.TEMPORARY_AboutDevelopersFragment2 -> {
                    Log.d("BottomNavigation", "TEMP Page Selected")
                    navigateToFragment(R.id.TEMPORARY_AboutDevelopersFragment2)
                }
                R.id.settingsFragment -> {
                    Log.d("BottomNavigation", "SETTINGS Page Selected")
                    navigateToFragment(R.id.settingsFragment)
                }
            }
            true
        }

        bottomNavigationView.setOnItemReselectedListener {
            when(it.itemId) {
                R.id.newBuildFragment -> {
                    return@setOnItemReselectedListener
                }
            }
        }
    }

    private fun navigateToFragment(fragmentId: Int) {
        val navController = findNavController(R.id.navController)
        navController.navigate(
            fragmentId,
            null,
            // for smoother transitions between fragments
            NavOptions.Builder()
                .setExitAnim(androidx.navigation.ui.R.anim.nav_default_exit_anim)
                .setEnterAnim(androidx.navigation.ui.R.anim.nav_default_enter_anim)
                .setPopExitAnim(androidx.navigation.ui.R.anim.nav_default_pop_enter_anim)
                .setPopEnterAnim(androidx.navigation.ui.R.anim.nav_default_pop_exit_anim)
                .build()
        )
    }

    private fun showCircle(item: MenuItem) {
        val menuItemView = findViewById<View>(item.itemId)
        val circleRevealView = findViewById<NewBuildCircleRevealView>(R.id.circleRevealView)
        val location = IntArray(2)
        menuItemView.getLocationOnScreen(location)

        val centerX = location[0] + menuItemView.width / 2f
        val centerY = location[1] + menuItemView.height / 2f

        val maxRadius = hypot(
            resources.displayMetrics.widthPixels.toDouble(),
            resources.displayMetrics.heightPixels.toDouble()
        ).toFloat()

        circleRevealView.visibility = View.VISIBLE

        circleRevealView.startAnimation(centerX, centerY, maxRadius) {
            circleRevealView.postDelayed({
                circleRevealView.visibility = View.GONE

                // to prevent showing the previous screen after animating, we hide the current
                // screen then show it after it has navigated
                window.decorView.alpha = 0f

                //IMPORTANT: ONLY NAVIGATE TO ADD BUILD AFTER ANIMATION IS FINISHED!
                navigateToFragment(R.id.newBuildFragment)

                // show after navigation
                window.decorView.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
                    override fun onPreDraw(): Boolean {
                        window.decorView.viewTreeObserver.removeOnPreDrawListener(this)

                        window.decorView.postDelayed({
                            window.decorView.animate()
                                .alpha(1f)
                                .setDuration(300)
                                .start()
                        }, 200)

                        return true
                    }
                })

            }, 50)
        }
    }

}
