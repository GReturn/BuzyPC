package io.buzypc.app.UI.Navigation.Fragments.Settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import io.buzypc.app.R
import io.buzypc.app.UI.Authentication.LogoutPromptActivity
import io.buzypc.app.UI.Navigation.ViewModels.StyleViewModel
import io.buzypc.app.UI.Utils.loadCurrentUserDetails
import io.buzypc.app.UI.Utils.saveBuildList
import io.buzypc.app.UI.Widget.DialogView.CustomActionDialogView
import io.buzypc.app.UI.Widget.DialogView.DialogType

class SettingsFragment : Fragment() {
    private val styleViewModel: StyleViewModel by activityViewModels()

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
        val btnPrivacyPolicy = view.findViewById<Button>(R.id.btn_privacy_policy)
        val btnFeedback = view.findViewById<Button>(R.id.btn_feedback)

        val radioBtnLightMode = view.findViewById<RadioButton>(R.id.rb_lightMode)
        val radioBtnDarkMode = view.findViewById<RadioButton>(R.id.rb_darkMode)
        val cardLightMode = view.findViewById<com.google.android.material.card.MaterialCardView>(R.id.card_lightMode)
        val cardDarkMode = view.findViewById<com.google.android.material.card.MaterialCardView>(R.id.card_darkMode)
        cardLightMode.strokeWidth = 0
        cardDarkMode.strokeWidth = 0

        val userManager = loadCurrentUserDetails(requireContext())

        // Check current theme and update radio buttons + highlights
        if (userManager.getTheme() == "dark") {
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
        btnPrivacyPolicy.setOnClickListener {
            val intent = Intent(requireActivity(), PrivacyPolicyActivity::class.java)
            startActivity(intent)
        }
        btnFeedback.setOnClickListener {
            CustomActionDialogView(requireActivity(), DialogType.CONFIRMATION)
                .setTitle("Exiting BuzyPC")
                .setDescription("By proceeding, you will be redirected to your web browser to answer an online form. Confirm?")
                .setOnCancelClickListener { }
                .setOnConfirmClickListener {
                    val googleFormUrl = "https://forms.gle/9ZrjSQnHW6vcPvTv9"
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(googleFormUrl))
                    startActivity(intent)
                }
                .show()
        }

        // Theme Selection
        radioBtnLightMode.setOnClickListener {
            if (radioBtnLightMode.isChecked) {
                radioBtnDarkMode.isChecked = false
                cardLightMode.strokeWidth = 3
                cardDarkMode.strokeWidth = 0

                userManager.setTheme("light")
                styleViewModel.setDarkMode(false)
            }
        }

        radioBtnDarkMode.setOnClickListener {
            if (radioBtnDarkMode.isChecked) {
                radioBtnLightMode.isChecked = false
                cardLightMode.strokeWidth = 0
                cardDarkMode.strokeWidth = 3

                userManager.setTheme("dark")
                styleViewModel.setDarkMode(true)
            }
        }

        // Logout Button
        val buttonLogout = view.findViewById<Button>(R.id.btn_logout)
        buttonLogout.setOnClickListener {

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
