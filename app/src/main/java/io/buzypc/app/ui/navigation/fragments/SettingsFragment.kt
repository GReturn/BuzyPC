package io.buzypc.app.ui.navigation.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import io.buzypc.app.R
import io.buzypc.app.data.user.BuzyUserSettings
import io.buzypc.app.ui.AboutDevelopersActivity
import io.buzypc.app.ui.LogoutPromptActivity
import io.buzypc.app.ui.ProfileViewActivity
import io.buzypc.app.ui.navigation.viewmodel.StyleViewModel
import io.buzypc.app.ui.utils.loadCurrentUserDetails

class SettingsFragment : Fragment() {
    private val viewModel: StyleViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadUserData()

        // Theme and Navigation Setup
        val btnEditAccount = view.findViewById<Button>(R.id.btn_edit_account)
        val btnAboutDevelopers = view.findViewById<Button>(R.id.btn_about_developers)
        val radioBtnLightMode = view.findViewById<RadioButton>(R.id.rb_lightMode)
        val radioBtnDarkMode = view.findViewById<RadioButton>(R.id.rb_darkMode)
        val cardLightMode = view.findViewById<com.google.android.material.card.MaterialCardView>(R.id.card_lightMode)
        val cardDarkMode = view.findViewById<com.google.android.material.card.MaterialCardView>(R.id.card_darkMode)
        cardLightMode.strokeWidth = 0
        cardDarkMode.strokeWidth = 0

        val userSettings = BuzyUserSettings(requireContext())

        // Check current theme and update radio buttons + highlights
        if (userSettings.getTheme() == "dark") {
            radioBtnDarkMode.isChecked = true
            cardDarkMode.strokeWidth = 3
        } else {
            // Default to light if theme is null or "light"
            radioBtnLightMode.isChecked = true
            cardLightMode.strokeWidth = 3
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
                cardLightMode.strokeWidth = 3
                cardDarkMode.strokeWidth = 0

                userSettings.setTheme("light")
                viewModel.setDarkMode(false)
            }
        }

        radioBtnDarkMode.setOnClickListener {
            if (radioBtnDarkMode.isChecked) {
                radioBtnLightMode.isChecked = false
                cardLightMode.strokeWidth = 0
                cardDarkMode.strokeWidth = 3

                userSettings.setTheme("dark")
                viewModel.setDarkMode(true)
            }
        }

        // Logout Button
        val buttonLogout = view.findViewById<Button>(R.id.btn_logout)
        buttonLogout.setOnClickListener {
            // Clear the last logged-in user
            userSettings.setLastUser(null)
            // Store whichever theme the user had selected at logout
            userSettings.setTheme(if (radioBtnDarkMode.isChecked) "dark" else "light")

            val intent = Intent(requireActivity(), LogoutPromptActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        loadUserData()
    }

    private fun loadUserData() {
        val currentView = view ?: return

        val userDetails = loadCurrentUserDetails(requireContext())

        val txtUsername = currentView.findViewById<TextView>(R.id.textView_usernameDisplay)
        txtUsername.text = userDetails.getUsername() ?: "Guest"

        val imageProfilePicture = currentView.findViewById<ImageView>(R.id.image_profile_picture)
        val imageBitmap = userDetails.getImageFromInternalStorage()
        if (imageBitmap != null)
            imageProfilePicture.setImageBitmap(imageBitmap)
        else
            imageProfilePicture.setImageResource(R.drawable.profilepic)
    }
}
