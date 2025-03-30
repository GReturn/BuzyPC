package io.buzypc.app.ui.navigation.fragments

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
import io.buzypc.app.data.appsession.BuzyUserAppSession
import io.buzypc.app.ui.BuildSummary

class NewBuildFragment : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val etBudgetInput = view.findViewById<EditText>(R.id.et_budgetInput)
        val etBuildName = view.findViewById<EditText>(R.id.et_buildName)

        val btnBuild = view.findViewById<Button>(R.id.btn_build)

        btnBuild.setOnClickListener {
            if(etBudgetInput.text.isNullOrEmpty()){
                Toast.makeText(requireContext(), "Input your budget", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if(etBuildName.text.isNullOrEmpty()) etBuildName.setText("New Build")
            requireContext()
            (context?.applicationContext as BuzyUserAppSession).buildName = etBuildName.text.toString()
            (context?.applicationContext as BuzyUserAppSession).buildBudget = etBudgetInput.text.toString()
            val intent = Intent(requireActivity(), BuildSummary::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_new_build, container, false)
    }
}