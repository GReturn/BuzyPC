package io.buzypc.app.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.buzypc.app.R

class AboutDevelopersFragment : Fragment() {

    // - LayoutInflater converts xml file into a View object that the fragment displays
    // - 'container' is the parent view where this fragment’s UI will be placed, passing false as the third parameter because the system
    //    will handle attaching the fragment to the container automatically.
    // - savedInstanceState contains any previously saved state, restores them upon recreating (updating) the fragment.
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about_developers, container, false)
    }
}