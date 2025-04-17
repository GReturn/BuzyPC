package io.buzypc.app.ui.navigation

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.DecelerateInterpolator
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.buzypc.app.R
import io.buzypc.app.ui.navigation.viewmodel.ListsInformationViewModel
import io.buzypc.app.ui.navigation.viewmodel.StyleViewModel
import io.buzypc.app.ui.widget.NewBuildCircleRevealView
import kotlin.math.hypot


class BottomNavigationActivity : AppCompatActivity() {
    private val styleViewModel: StyleViewModel by viewModels()
    private val listInformationViewModel: ListsInformationViewModel by viewModels()
    private var backPressedTime = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_navigation)
        handleOnBackPress()

        // We'll let the BottomNavigation Activity handle the theme change instead of the
        // Settings Fragment
        styleViewModel.themeState.observe(this) { isDarkMode ->
            if (isDarkMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        // TODO get the count content from shared pref; have to set up first tho
        listInformationViewModel.setBuildCount(10)
        listInformationViewModel.setChecklistItemCount(20)
        listInformationViewModel.buildListCount.observe(this) { buildCount ->
            // TODO (save/change the sharedpref list counter)
        }
        listInformationViewModel.checkListCount.observe(this) { checkCount ->
            // TODO (save/change the sharedpref list counter)
        }

        setupBottomNavigation()
    }

    private fun setupBottomNavigation() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navController) as NavHostFragment
        val navController = navHostFragment.navController
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
                    showCircle(bottomNavigationView.menu.findItem(R.id.newBuildFragment))
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
            resetFragmentBackground(item.itemId)

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

    private fun handleOnBackPress() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (backPressedTime + 2000 > System.currentTimeMillis()) {
                    finish()
                } else {
                    Toast.makeText(this@BottomNavigationActivity, "Press back again to close the app", Toast.LENGTH_SHORT).show()
                    backPressedTime = System.currentTimeMillis()
                }
            }
        })
    }

    fun handleNavigationToOtherFragments(id: Int) {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.selectedItemId = id
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

    private fun resetFragmentBackground(fragmentId: Int) {
        if(fragmentId != R.id.newBuildFragment) {
            val typedValue = this.obtainStyledAttributes(intArrayOf(com.google.android.material.R.attr.background))
            window.decorView.setBackgroundColor(typedValue.getColor(0, R.color.bz_honeyYellow.toInt()))
            // it's deprecated but it's the easiest way I can set the gesture navigation bar colors
            window.navigationBarColor = typedValue.getColor(0, R.color.bz_honeyYellow.toInt())
            typedValue.recycle()
        }
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
                circleRevealView.visibility = View.GONE

                val fragmentContainer = findViewById<View>(R.id.navController)
                // to prevent showing the previous screen after animating, we hide the current
                // screen then show it after it has navigated ( after calling navigateToFragment() )
                fragmentContainer.translationY = window.decorView.height.toFloat()

                val typedArray = theme.obtainStyledAttributes(intArrayOf(com.google.android.material.R.attr.colorPrimary))
                window.decorView.setBackgroundColor(typedArray.getColor(0, R.color.bz_honeyYellow.toInt()))
                // it's deprecated but it's the easiest way I can set the gesture navigation bar colors
                window.navigationBarColor = typedArray.getColor(0, R.color.bz_honeyYellow.toInt())
                typedArray.recycle()

                //IMPORTANT: ONLY NAVIGATE TO ADD BUILD AFTER ANIMATION IS FINISHED!
                navigateToFragment(R.id.newBuildFragment)

                // show after navigation
                fragmentContainer.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
                    override fun onPreDraw(): Boolean {
                        fragmentContainer.viewTreeObserver.removeOnPreDrawListener(this)

                        fragmentContainer.animate()
                            .translationY(0f)
                            .setDuration(400)
                            .setInterpolator(DecelerateInterpolator())
                            .start()

                        return true
                    }
                })
        }
    }

}
