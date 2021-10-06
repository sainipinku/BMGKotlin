package com.bookmygame.ui.groundOwner.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bookmygame.R
import com.bookmygame.databinding.FragmentAddTeamBinding
import com.bookmygame.network.model.TeamListResponse
import com.bookmygame.ui.groundOwner.Slot
import com.bookmygame.ui.groundOwner.adapter.TeamsAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddTeamBottomSheet(private val teams: List<TeamListResponse.Team>) :
    BottomSheetDialogFragment() {

    private var _binding: FragmentAddTeamBinding? = null
    private val binding get() = _binding!!

    private var listener: AddTeamListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddTeamBinding.inflate(inflater, container, false)

        setUpViews()

        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun setUpViews() {
        binding.apply {
            val teamsAdapter = TeamsAdapter(requireContext(), R.layout.list_item_team, teams)
            autocompleteTeamName.threshold = 1
            autocompleteTeamName.setAdapter(teamsAdapter)

//            Handler().postDelayed({
//                val movies = arrayOf(
//                    "Avengers: Endgame",
//                    "Captain Marvel",
//                    "Shazam!",
//                    "Spider-Man: Far From Home",
//                    "Dark Phoenix",
//                    "Hellboy",
//                    "Glass",
//                    "Reign of the Superman",
//                    "Brightburn"
//                )
//                val adapter =
//                    ArrayAdapter(requireContext(), android.R.layout.select_dialog_item, movies)
//
//                //actv is the AutoCompleteTextView from your layout file
//                autocompleteTeamName.threshold =
//                    1 //start searching for values after typing first character
//                autocompleteTeamName.setAdapter(adapter)
//                adapter.setNotifyOnChange(true)
//                adapter.notifyDataSetChanged()
//                autocompleteTeamName.showDropDown()
//            },1000)

            add.setOnClickListener {
//                val teamName = edittextTeamName.text.toString().trim()
//                if (teamName.isEmpty().not()) {
//                    listener?.onTeamAdded(Slot.Team(teamName))
//                    dismiss()
//                } else {
//                    showToast("Please enter team name")
//                }
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