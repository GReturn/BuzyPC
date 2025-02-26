package io.buzypc.app.ui;

import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import io.buzypc.app.R;
import io.buzypc.app.databinding.ActivityBottomNavigationBinding;
import io.buzypc.app.ui.fragments.AboutDevelopersFragment;
import io.buzypc.app.ui.fragments.BuildListFragment;
import io.buzypc.app.ui.fragments.LandingPageFragment;
import io.buzypc.app.ui.fragments.NewBuildFragment;
import io.buzypc.app.ui.fragments.SettingsFragment;

public class BottomNavigation extends AppCompatActivity {

    private ActivityBottomNavigationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBottomNavigationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Restore or set default fragment
        if (savedInstanceState == null) {
            replaceFragment(new LandingPageFragment(), false);
        }

        // FAB launches a secondary screen, so we add it to the back stack
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> replaceFragment(new NewBuildFragment(), true));

        // Bottom navigation items are considered top-level destinations
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.landing_page) {
                replaceFragment(new LandingPageFragment(), false);
            } else if (itemId == R.id.about_devs) {
                replaceFragment(new AboutDevelopersFragment(), false);
            } else if (itemId == R.id.build_list) {
                replaceFragment(new BuildListFragment(), false);
            } else if (itemId == R.id.profile_view) {
                replaceFragment(new SettingsFragment(), false);
            }
            return true;
        });
    }

    private void replaceFragment(Fragment fragment, boolean addToBackStack) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.frame_layout);

        // Prevent reloading the same fragment
        if (currentFragment != null && currentFragment.getClass().equals(fragment.getClass())) {
            return;
        }

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);

        if (addToBackStack) {
            transaction.addToBackStack(null);
        }

        transaction.commit();
        recreate();
    }
}
