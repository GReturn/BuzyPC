package io.buzypc.app.ui.fragments

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import io.buzypc.app.R
import io.buzypc.app.data.user.BuzyUser
import io.buzypc.app.data.user.BuzyUserSettings
import io.buzypc.app.ui.AboutDevelopersActivity
import io.buzypc.app.ui.LandingPageActivity
import io.buzypc.app.ui.LogoutPromptActivity
import io.buzypc.app.ui.ProfileViewActivity

class SettingsFragment : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userDetails = BuzyUser(requireContext())
        val btnBackNavigation = view.findViewById<ImageView>(R.id.image_backNavigation)
        val txtUsername = view.findViewById<TextView>(R.id.textView_usernameDisplay)

        val imageProfilePicture = view.findViewById<ImageView>(R.id.image_profile_picture)
        val imageBitmap = userDetails.getImageFromInternalStorage()
        if(imageBitmap != null) imageProfilePicture.setImageBitmap(imageBitmap)
        else imageProfilePicture.setImageResource(R.drawable.profilepic)

        val btnEditAccount = view.findViewById<Button>(R.id.btn_edit_account)
        val btnAboutDevelopers = view.findViewById<Button>(R.id.btn_about_developers)

        val radioBtnLightMode = view.findViewById<RadioButton>(R.id.rb_lightMode)
        val radioBtnDarkMode = view.findViewById<RadioButton>(R.id.rb_darkMode)
        val cardLightMode = view.findViewById<com.google.android.material.card.MaterialCardView>(R.id.card_lightMode)
        val cardDarkMode = view.findViewById<com.google.android.material.card.MaterialCardView>(R.id.card_darkMode)
        cardLightMode.strokeWidth = 0
        cardDarkMode.strokeWidth = 0

        val userSettings = BuzyUserSettings(requireContext())
        if (userSettings.getTheme() == "light" || userSettings.getTheme() == null) {
            radioBtnLightMode.isChecked = true
            cardLightMode.strokeWidth = 3
            cardDarkMode.strokeWidth = 0
        } else {
            radioBtnDarkMode.isChecked = true
            cardLightMode.strokeWidth = 0
            cardDarkMode.strokeWidth = 3
        }

        val buttonLogout = view.findViewById<Button>(R.id.btn_logout)

        txtUsername.text = userDetails.getUsername()

        // Navigation Click Listeners
        btnBackNavigation.setOnClickListener {
            val intent = Intent(requireActivity(), LandingPageActivity::class.java)
            startActivity(intent)
        }

        btnEditAccount.setOnClickListener {
            val intent = Intent(requireActivity(), ProfileViewActivity::class.java)
            startActivity(intent)
        }

        btnAboutDevelopers.setOnClickListener {
            val intent = Intent(requireActivity(), AboutDevelopersActivity::class.java)
            startActivity(intent)
        }

        // Theme Selection
        radioBtnLightMode.setOnClickListener {
            if (radioBtnLightMode.isChecked) {
                radioBtnDarkMode.isChecked = false
                userSettings.setTheme("light")

                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

                cardLightMode.strokeWidth = 3
                cardDarkMode.strokeWidth = 0
            }
        }

        radioBtnDarkMode.setOnClickListener {
            if (radioBtnDarkMode.isChecked) {
                radioBtnLightMode.isChecked = false
                userSettings.setTheme("dark")

                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

                cardLightMode.strokeWidth = 0
                cardDarkMode.strokeWidth = 3
            }
        }

        // Logout Button
        buttonLogout.setOnClickListener {
            val intent = Intent(requireActivity(), LogoutPromptActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }
}