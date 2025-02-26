package io.buzypc.app.ui;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;


import io.buzypc.app.R;

public abstract class BaseActivity extends AppCompatActivity {

    protected io.buzypc.app.databinding.ActivityBottomNavigationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = io.buzypc.app.databinding.ActivityBottomNavigationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        binding.bottomNavigationView.setBackground(null);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            // Check if we're not already on the selected activity
            if (itemId == R.id.landing_page) {
                navigateTo(LandingPageActivity.class);
                return true;
            } else if (itemId == R.id.about_devs) {
                navigateTo(AboutDevelopersActivity.class);
                return true;
            } else if (itemId == R.id.build_list) {
                navigateTo(BuildsListActivity.class);
                return true;
            } else if (itemId == R.id.profile_view) {
                navigateTo(SettingsActivity.class);
                return true;
            }
            return false;
        });
    }

    private void navigateTo(Class<?> targetActivity) {
        Intent intent = new Intent(this, targetActivity);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }
}
