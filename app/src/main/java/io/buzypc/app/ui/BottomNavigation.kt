package io.buzypc.app.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import io.buzypc.app.R
import io.buzypc.app.databinding.ActivityBottomNavigationBinding
import io.buzypc.app.ui.fragments.TEMPORARY_AboutDevelopersFragment
import io.buzypc.app.ui.fragments.BuildListFragment
import io.buzypc.app.ui.fragments.LandingPageFragment
import io.buzypc.app.ui.fragments.NewBuildFragment
import io.buzypc.app.ui.fragments.SettingsFragment

class BottomNavigation : AppCompatActivity() {
    private var binding: ActivityBottomNavigationBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBottomNavigationBinding.inflate(layoutInflater)
        setContentView(binding!!.getRoot())

        // Restore or set default fragment
        if (savedInstanceState == null) {
            replaceFragment(LandingPageFragment(), false, null)
        }

        // Bottom navigation items are considered top-level destinations
        binding!!.bottomNavigationView.setOnItemSelectedListener { item ->
            val itemId = item.itemId
            if (itemId == R.id.landing_page) {
                replaceFragment(LandingPageFragment(), false, null)
            } else if (itemId == R.id.about_devs) {
                replaceFragment(TEMPORARY_AboutDevelopersFragment(), false, null)
            } else if (itemId == R.id.build_list) {
                replaceFragment(BuildListFragment(), false, null)
            } else if (itemId == R.id.add_build) {
                replaceFragment(NewBuildFragment(), false, null)
            } else if (itemId == R.id.profile_view) {
                val args = Bundle()
                args.putBoolean("refresh", true) // Signal to refresh data
                replaceFragment(SettingsFragment(), false, args)
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment, addToBackStack: Boolean, args: Bundle?) {
        val fragmentManager = supportFragmentManager
        val currentFragment = fragmentManager.findFragmentById(R.id.frame_layout)

        // Prevent reloading the same fragment
        if (currentFragment != null && currentFragment.javaClass == fragment.javaClass) {
            return
        }
        if (args != null) {
            fragment.arguments = args
        }
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.frame_layout, fragment)
        if (addToBackStack) {
            transaction.addToBackStack(null)
        }
        transaction.commit()
    }
}
