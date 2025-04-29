 package io.buzypc.app.UI.Navigation.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import com.google.android.material.button.MaterialButton
import io.buzypc.app.Data.AppSession.BuzyUserAppSession
import io.buzypc.app.R
import io.buzypc.app.UI.Navigation.BottomNavigationActivity
import io.buzypc.app.UI.Navigation.ViewModels.ListsInformationViewModel
import io.buzypc.app.UI.Utils.loadCurrentUserDetails

 class LandingPageFragment : Fragment() {
     private val listsInformationViewModel : ListsInformationViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = requireActivity() as BottomNavigationActivity

        val userDetails = loadCurrentUserDetails(requireContext())  // Use requireContext() instead of 'this'
        val txtHelloUser = view.findViewById<TextView>(R.id.hello_user)

        val btnBuildNow = view.findViewById<MaterialButton>(R.id.button_landing_build_now_cta)
        val btnViewBuilds = view.findViewById<MaterialButton>(R.id.button_landing_view_builds)
        val btnGoToChecklist = view.findViewById<MaterialButton>(R.id.button_landing_go_to_checklist)

        val txtCurrentBuildList = view.findViewById<TextView>(R.id.textview_dynamic_build_list_counter)
        val txtCurrentChecklist = view.findViewById<TextView>(R.id.textview_dynamic_checklist_list_counter)
        val app = context?.applicationContext as BuzyUserAppSession


        txtHelloUser.text = getString(R.string.hello_user, userDetails.getUsername())

        listsInformationViewModel.setBuildCount(app.buildList.count{!it.isDeleted && !it.isArchived})
        listsInformationViewModel.buildListCount.observe(requireActivity()) { buildCount ->
            txtCurrentBuildList.text = getString(
                R.string.landingpage_build_count,
                buildCount.toString()
            )
        }
        listsInformationViewModel.checkListCount.observe(requireActivity()) { checkCount ->
            txtCurrentChecklist.text = getString(
                R.string.landingpage_checklist_count,
                checkCount.toString()
            )
        }

        val imageProfilePicture = view.findViewById<ImageView>(R.id.image_landingpage_profile_pic)
        val imageBitmap = userDetails.getImageFromInternalStorage()
        if (imageBitmap != null)
            imageProfilePicture.setImageBitmap(imageBitmap)
        else
            imageProfilePicture.setImageResource(R.drawable.profilepic)

        imageProfilePicture.setOnClickListener {
            activity.handleNavigationToOtherFragments(R.id.settingsFragment)
        }
        btnBuildNow.setOnClickListener {
            activity.handleNavigationToOtherFragments(R.id.newBuildFragment)
        }

        btnViewBuilds.setOnClickListener {
            activity.handleNavigationToOtherFragments(R.id.buildListFragment)
        }
        btnGoToChecklist.setOnClickListener {
            activity.handleNavigationToOtherFragments(R.id.checklistFragment)
        }
    }

     override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_landing_page, container, false)
    }
 }