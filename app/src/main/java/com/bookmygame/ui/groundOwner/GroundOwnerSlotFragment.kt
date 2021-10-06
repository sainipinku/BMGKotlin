package com.bookmygame.ui.groundOwner

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.bookmygame.MainActivity
import com.bookmygame.databinding.FragmentSlotBinding
import com.bookmygame.network.model.GroundOwnerGroundListResponse
import com.bookmygame.ui.groundOwner.adapter.GroundOwnerGroundSlotsAdapter
import com.bookmygame.ui.groundOwner.bottomsheet.AddSlotBottomSheet
import com.bookmygame.ui.groundOwner.bottomsheet.AddTeamDialog
import com.bookmygame.util.DateTimeUtil
import org.joda.time.DateTime

class GroundOwnerSlotFragment : Fragment(), AddSlotBottomSheet.AddSlotListener,
    AddTeamDialog.AddTeamListener {

    private var _binding: FragmentSlotBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: GroundOwnerSlotViewModel
    private val sharedViewModel: GroundOwnerGroundSharedViewModel by activityViewModels()
    private var position: Int? = null
    private var slot: Slot? = null
    private var teamType: String? = null
    private lateinit var selectedDate: String
    private lateinit var ground: GroundOwnerGroundListResponse.Ground

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSlotBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(GroundOwnerSlotViewModel::class.java)

        ground = sharedViewModel.getSelectedGround()!!

        setListeners()
        setUpView()

        viewModel.loadSlots(ground.slots)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).hideBottomNavigationBar()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun setUpView() {
        binding.apply {
            recyclerView.adapter =
                GroundOwnerGroundSlotsAdapter(
                    groundName = ground.groundName,
                    slotsList = mutableListOf(),
                    onEditClickListener = { position: Int, slot: Slot ->
                        showAddSlotBottomSheet(position, slot, AddSlotBottomSheet.Action.Update)
                    },
                    onDeleteClickListener = { position: Int, slot: Slot ->
                        deleteSlot(position, slot)
                    },
                    onAddTeam1ClickListener = { position: Int, slot: Slot ->
                        showAddTeamBottomSheet("team1", position, slot)
                    },
                    onAddTeam2ClickListener = { position: Int, slot: Slot ->
                        showAddTeamBottomSheet("team2", position, slot)
                    })

            datePicker
                .setOffset(0)
                .setListener { dateSelected ->
                    Log.d(
                        "Basava",
                        "Date Selected -> $dateSelected"
                    )
                    Log.d(
                        "Basava",
                        "Date Selected: year -> ${dateSelected.year}"
                    )
                    Log.d(
                        "Basava",
                        "Date Selected: monthOfYear -> ${dateSelected.monthOfYear}"
                    )
                    Log.d(
                        "Basava",
                        "Date Selected: dayOfMonth -> ${dateSelected.dayOfMonth}"
                    )
                    val formattedDate = DateTimeUtil.format(dateSelected.toString())
                    Log.d(
                        "Basava",
                        "Date Selected: formattedDate -> $formattedDate"
                    )
                    selectedDate = formattedDate
                }
                .init()
            datePicker.setDate(DateTime.now())

            addSlot.setOnClickListener {
                showAddSlotBottomSheet(action = AddSlotBottomSheet.Action.Add)
            }
        }
    }

    private fun setListeners() {
        viewModel.slots.observe(viewLifecycleOwner, { slots ->
            binding.recyclerView.adapter = GroundOwnerGroundSlotsAdapter(
                groundName = ground.groundName,
                slotsList = slots as ArrayList<Slot>,
                onEditClickListener = { position: Int, slot: Slot ->
                    showAddSlotBottomSheet(position, slot, AddSlotBottomSheet.Action.Update)
                },
                onDeleteClickListener = { position: Int, slot: Slot ->
                    deleteSlot(position, slot)
                },
                onAddTeam1ClickListener = { position: Int, slot: Slot ->
                    showAddTeamBottomSheet("team1", position, slot)
                },
                onAddTeam2ClickListener = { position: Int, slot: Slot ->
                    showAddTeamBottomSheet("team2", position, slot)
                }
            )
        })

        viewModel.addSlotResponse.observe(viewLifecycleOwner, { event ->
            Log.d("Basava", "GroundOwnerSlotFragment addSlotResponse.observe")
            event.getContentIfNotHandled()?.let { response ->
                if (response.error.not()) {
                    Log.d("Basava", "data ${response.message}")
                    // update UI
                    showToast(response.message)
                } else {
                    showToast(response.message)
                }
                (activity as? MainActivity)?.hideProgressBar()
            }
        })

        viewModel.updateSlotResponse.observe(viewLifecycleOwner, { event ->
            Log.d("Basava", "GroundOwnerSlotFragment updateSlotResponse.observe")
            event.getContentIfNotHandled()?.let { response ->
                (activity as? MainActivity)?.hideProgressBar()
                if (response.error.not()) {
                    Log.d("Basava", "data ${response.message}")
                    // update UI
                    showToast(response.message)
                } else {
                    showToast(response.message)
                }
            }
        })

        viewModel.removeSlotResponse.observe(viewLifecycleOwner, { event ->
            Log.d("Basava", "GroundOwnerSlotFragment removeSlotResponse.observe")
            event.getContentIfNotHandled()?.let { response ->
                (activity as? MainActivity)?.hideProgressBar()
                if (response.error.not()) {
                    Log.d("Basava", "data ${response.message}")
                    // update UI
                    showToast(response.message)
                } else {
                    showToast(response.message)
                }
            }
        })

        viewModel.teamListResponse.observe(viewLifecycleOwner, { event ->
            Log.d("Basava", "GroundOwnerSlotFragment teamListResponse.observe")
            event.getContentIfNotHandled()?.let { response ->
                (activity as? MainActivity)?.hideProgressBar()
                if (response.error.not()) {
                    Log.d("Basava", "data: ${response.data}")
                    // update UI
//                    val bottomSheet = AddTeamBottomSheet(response.data)
//                    bottomSheet.setOnAddTeamListener(this)
//                    bottomSheet.show(childFragmentManager, bottomSheet.tag)

                    val dialog = AddTeamDialog.newInstance("", "", response.data)
                    dialog.setOnAddTeamListener(this)
                    dialog.show(childFragmentManager, dialog.tag)
                } else {
                    showToast(response.message)
                }
            }
        })

        viewModel.bookSlotResponse.observe(viewLifecycleOwner, { event ->
            Log.d("Basava", "GroundOwnerSlotFragment bookSlotResponse.observe")
            event.getContentIfNotHandled()?.let { response ->
                (activity as? MainActivity)?.hideProgressBar()
                if (response.error.not()) {
                    Log.d("Basava", "data ${response.message}")
                    // update UI
                    showToast(response.message)
                } else {
                    showToast(response.message)
                }
            }
        })

        viewModel.errorMessage.observe(viewLifecycleOwner, { event ->
            event.getContentIfNotHandled()?.let { errorMessage ->
                showToast(errorMessage)
                (activity as? MainActivity)?.hideProgressBar()
            }
        })
    }

    private fun showAddSlotBottomSheet(
        position: Int = -1,
        slot: Slot? = null,
        action: AddSlotBottomSheet.Action
    ) {
        this.position = position
        this.slot = slot

        val bottomSheet = AddSlotBottomSheet(action, selectedDate, slot)
        bottomSheet.setOnAddSlotListener(this@GroundOwnerSlotFragment)
        bottomSheet.show(childFragmentManager, bottomSheet.tag)
    }

    private fun showAddTeamBottomSheet(teamType: String, position: Int, slot: Slot) {
        this.teamType = teamType
        this.position = position
        this.slot = slot

        (activity as MainActivity).showProgressBar()
        viewModel.getTeams()

//        val bottomSheet = AddTeamBottomSheet()
//        bottomSheet.setOnAddTeamListener(this)
//        bottomSheet.show(childFragmentManager, bottomSheet.tag)
    }

    override fun onSlotAdded(slot: Slot) {
        Log.d("Basava", "onSlotAdded: slot -> $slot")
        (activity as MainActivity).showProgressBar()
        (binding.recyclerView.adapter as GroundOwnerGroundSlotsAdapter).addSlot(slot)
        viewModel.addSlot(userId = ground.userId, groundId = ground.groundId, slot = slot)
    }

    override fun onSlotUpdated(slot: Slot) {
        Log.d("Basava", "onSlotUpdated: slot -> $slot")
        (activity as MainActivity).showProgressBar()
        (binding.recyclerView.adapter as GroundOwnerGroundSlotsAdapter).updateSlot(position!!, slot)
        viewModel.updateSlot(slot)
    }

    private fun deleteSlot(position: Int, slot: Slot) {
        (activity as MainActivity).showProgressBar()
        (binding.recyclerView.adapter as GroundOwnerGroundSlotsAdapter).deleteSlot(position, slot)
        viewModel.removeSlot(ground.userId, slot.id)
    }

    override fun onTeamAdded(team: Slot.Team) {
        Log.d("Basava", "onTeamAdded: position -> $position teamName -> $team")
        when (teamType) {
            "team1" -> slot!!.team1 = team
            "team2" -> slot!!.team2 = team
            else -> error("No team")
        }

        (activity as MainActivity).showProgressBar()
        (binding.recyclerView.adapter as GroundOwnerGroundSlotsAdapter).updateSlot(
            position!!,
            slot!!
        )
        viewModel.bookSlot(ground.userId, ground.groundId, slot!!.id, team)

        teamType = null
        position = -1
        slot = null
    }

    private fun showToast(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }
}