 package io.buzypc.app.ui.navigation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.buzypc.app.R
import io.buzypc.app.ui.utils.loadCurrentUserDetails

 class LandingPageFragment : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userDetails = loadCurrentUserDetails(requireContext())  // Use requireContext() instead of 'this'
        val txtHelloUser = view.findViewById<TextView>(R.id.hello_user)
        txtHelloUser.text = getString(R.string.hello_user, userDetails.getUsername())
    }

     // - LayoutInflater converts xml file into a View object that the fragment displays
     // - 'container' is the parent view where this fragment’s UI will be placed, passing false as the third parameter because the system
     //    will handle attaching the fragment to the container automatically.
     // - savedInstanceState contains any previously saved state, restores them upon recreating (updating) the fragment.
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_landing_page, container, false)
    }
}