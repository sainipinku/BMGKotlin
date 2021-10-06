package com.bookmygame.ui.groundOwner.bottomsheet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import com.bookmygame.R
import com.bookmygame.databinding.FragmentAddTeamBinding
import com.bookmygame.network.model.TeamListResponse
import com.bookmygame.ui.groundOwner.Slot
import com.bookmygame.ui.groundOwner.adapter.TeamsAdapter

class AddTeamDialog : DialogFragment() {

    private var _binding: FragmentAddTeamBinding? = null
    private val binding get() = _binding!!

    private var teamId: String? = null

    private var listener: AddTeamListener? = null

    companion object {

        const val TAG = "SimpleDialog"

        private const val KEY_TITLE = "KEY_TITLE"
        private const val KEY_SUBTITLE = "KEY_SUBTITLE"

        private lateinit var teams: List<TeamListResponse.Team>

        fun newInstance(
            title: String,
            subTitle: String,
            teams: List<TeamListResponse.Team>
        ): AddTeamDialog {
            val args = Bundle()
            args.putString(KEY_TITLE, title)
            args.putString(KEY_SUBTITLE, subTitle)
            val fragment = AddTeamDialog()
            fragment.arguments = args
            this.teams = teams
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddTeamBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
//            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun setUpViews() {
        binding.apply {

            close.setOnClickListener {
                dismiss()
            }

            val teamsAdapter = TeamsAdapter(requireContext(), R.layout.list_item_team, teams)
            autocompleteTeamName.threshold = 1
            autocompleteTeamName.setAdapter(teamsAdapter)

            autocompleteTeamName.doOnTextChanged { text, start, before, count ->
                Log.d("Basava", "doOnTextChanged: text -> $text count -> $count")
                add.isEnabled = count != 0
            }

            autocompleteTeamName.setOnItemClickListener { parent, view, position, id ->
                val team = parent.getItemAtPosition(position)

                if (team is TeamListResponse.Team) {
                    Log.d("Basava", "item selected: id -> ${team.teamId} name -> ${team.teamName}")
                    teamId = team.teamId
                    autocompleteTeamName.setText(team.teamName)
                }
            }

            add.setOnClickListener {
                Log.d(
                    "Basava",
                    "add clicked: teamId -> $teamId"
                )
                Log.d(
                    "Basava",
                    "add clicked: name -> ${autocompleteTeamName.text.toString().trim()}"
                )
                val teamName = autocompleteTeamName.text.toString().trim()
                if (teamId != null || teamName.isEmpty().not()) {
                    listener?.onTeamAdded(Slot.Team(teamId, teamName))
                    dismiss()
                } else {
                    showToast("Please enter team name")
                }
            }
        }
    }

    fun setOnAddTeamListener(listener: AddTeamListener) {
        this.listener = listener
    }

    interface AddTeamListener {
        fun onTeamAdded(team: Slot.Team)
    }

    private fun showToast(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }
}