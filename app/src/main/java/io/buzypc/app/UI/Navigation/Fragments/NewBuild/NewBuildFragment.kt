package io.buzypc.app.UI.Navigation.Fragments.NewBuild

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import io.buzypc.app.R
import io.buzypc.app.Data.AppSession.BuzyUserAppSession

class NewBuildFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_new_build, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val etBudgetInput = view.findViewById<EditText>(R.id.et_budgetInput)
        val etBuildName = view.findViewById<EditText>(R.id.et_buildName)

        val btnBuild = view.findViewById<Button>(R.id.btn_build)

        etBuildName.addTextChangedListener( object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if(s?.length == 10) Toast.makeText(this@NewBuildFragment.context, "Only 10 characters allowed for the build name.", Toast.LENGTH_LONG).show()
            }
        })

        btnBuild.setOnClickListener {
            if(etBuildName.text.isNullOrEmpty()) etBuildName.setText("New Build")

            if(etBudgetInput.text.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "Input your budget.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if(etBudgetInput.text.toString().toInt() < 20_000 || etBudgetInput.text.toString().toInt() > 999_999) {
                Toast.makeText(requireContext(), "You can only place a budget between PHP 20,000 and PHP 999,999.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            requireContext()
            (context?.applicationContext as BuzyUserAppSession).buildName = etBuildName.text.toString()
            (context?.applicationContext as BuzyUserAppSession).buildBudget = etBudgetInput.text.toString().toDouble()
            val intent = Intent(requireActivity(), NewBuildSummaryActivity::class.java)
            startActivity(intent)
        }
    }
}