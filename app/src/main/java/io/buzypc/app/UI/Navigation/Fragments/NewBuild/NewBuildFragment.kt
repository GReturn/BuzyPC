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
import io.buzypc.app.UI.Navigation.Fragments.Shared.BuildSummary.Activities.NewBuildSummaryActivity
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import io.buzypc.app.UI.Widget.LoadingScreenActivity


class NewBuildFragment : Fragment() {
    private var budgetCheckJob: Job? = null  // To track/cancel previous checks
    private val scope = MainScope()         // Coroutine scope tied to the UI lifecycle

    override fun onDestroyView() {
        scope.cancel()  // Clean up coroutines to avoid leaks
        super.onDestroyView()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_new_build, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val etBudgetInput = view.findViewById<EditText>(R.id.et_budgetInput)
        val etBuildName = view.findViewById<EditText>(R.id.et_buildName)

        val btnBuild = view.findViewById<Button>(R.id.btn_build)

        etBuildName.addTextChangedListener( object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if(s?.length == 20)
                    Toast.makeText(this@NewBuildFragment.context, "Only 20 characters allowed for the build name.", Toast.LENGTH_LONG).show()
            }
        })
        etBudgetInput.addTextChangedListener( object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                budgetCheckJob?.cancel()
                budgetCheckJob = scope.launch {
                    delay(500)
                    checkBudget(s)
                }
            }

            private fun checkBudget(s: Editable?) {
                s?.let {
                    try {
                        // Remove commas or other non-digit characters if present
                        val cleanString = it.toString().replace("[^\\d]".toRegex(), "")
                        if (cleanString.isNotEmpty()) {
                            val budget = cleanString.toLong()
                            if (budget > 999999) {
                                Toast.makeText(
                                    this@NewBuildFragment.context,
                                    "You can only place a budget between PHP 20,000 and PHP 999,999.",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    } catch (e: NumberFormatException) {
                        // Handle the case where the number is too large or invalid
                        Toast.makeText(
                            this@NewBuildFragment.context,
                            "Invalid or too large! Please enter a valid budget amount.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        })

        btnBuild.setOnClickListener {
            if(etBuildName.text.isNullOrEmpty()) etBuildName.setText("New Build")

            if(etBudgetInput.text.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "Input your budget.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if(etBudgetInput.text.toString().toLong() < 20_000 || etBudgetInput.text.toString().toLong() > 999_999) {
                Toast.makeText(requireContext(), "You can only place a budget between PHP 20,000 and PHP 999,999.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            requireContext()
            (context?.applicationContext as BuzyUserAppSession).buildName = etBuildName.text.toString()
            (context?.applicationContext as BuzyUserAppSession).buildBudget = etBudgetInput.text.toString().toDouble()
            val intent = Intent(requireActivity(), LoadingScreenActivity::class.java)
            startActivity(intent)
        }
    }
}