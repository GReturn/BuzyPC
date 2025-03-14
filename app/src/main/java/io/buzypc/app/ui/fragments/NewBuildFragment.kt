package io.buzypc.app.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import io.buzypc.app.R
import io.buzypc.app.ui.BuildSummary
import io.buzypc.app.ui.ProfileViewActivity

class NewBuildFragment : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val etBudgetInput = view.findViewById<EditText>(R.id.edittext_budgetInput)
        val btnBuild = view.findViewById<Button>(R.id.build_button)
        btnBuild.setOnClickListener(){
            if(etBudgetInput.text.isNullOrEmpty()){
                Toast.makeText(requireContext(), "Input your budget", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            val intent = Intent(requireActivity(), BuildSummary::class.java)
            startActivity(intent)
        }
    }

    // - LayoutInflater converts xml file into a View object that the fragment displays
    // - 'container' is the parent view where this fragment’s UI will be placed, passing false as the third parameter because the system
    //    will handle attaching the fragment to the container automatically.
    // - savedInstanceState contains any previously saved state, restores them upon recreating (updating) the fragment.
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_build, container, false)
    }
}