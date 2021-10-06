package com.bookmygame.ui.groundOwner.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bookmygame.R
import com.bookmygame.databinding.ListItemSlotGroundOwnerBinding
import com.bookmygame.ui.groundOwner.Slot

class GroundOwnerGroundSlotsAdapter(
    private val groundName: String,
    private var slotsList: MutableList<Slot>,
    private val onEditClickListener: (Int, Slot) -> Unit,
    private val onDeleteClickListener: (Int, Slot) -> Unit,
    private val onAddTeam1ClickListener: (Int, Slot) -> Unit,
    private val onAddTeam2ClickListener: (Int, Slot) -> Unit
) :
    RecyclerView.Adapter<SlotViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SlotViewHolder {
        return SlotViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_slot_ground_owner, parent, false),
            onEditClickListener,
            onDeleteClickListener,
            onAddTeam1ClickListener,
            onAddTeam2ClickListener
        )
    }

    override fun onBindViewHolder(holder: SlotViewHolder, position: Int) {
        holder.bind(groundName, slotsList[position])
    }

    override fun getItemCount(): Int = slotsList.size

    fun addSlot(slot: Slot) {
        slotsList.add(slot)
        notifyDataSetChanged()
    }

    fun updateSlot(position: Int, slot: Slot) {
        Log.d("Basava", "updateSlot: slot -> $slot")
        slotsList[position] = slot
        notifyItemChanged(position)
    }

    fun deleteSlot(position: Int, slot: Slot) {
        slotsList.remove(slot)
        notifyItemRemoved(position)
    }
}

class SlotViewHolder(
    view: View,
    private val onEditClickListener: (Int, Slot) -> Unit,
    private val onDeleteClickListener: (Int, Slot) -> Unit,
    private val onAddTeam1ClickListener: (Int, Slot) -> Unit,
    private val onAddTeam2ClickListener: (Int, Slot) -> Unit
) : RecyclerView.ViewHolder(view) {

    private val binding = ListItemSlotGroundOwnerBinding.bind(view)

    init {
        binding.apply {
            root.setOnClickListener {
                Log.d("Basava", "Slot clicked")
            }
        }
    }

    fun bind(groundName: String, slot: Slot) {
        Log.d("Basava", "bind position -> $adapterPosition slot -> $slot")
        binding.apply {
            this.groundName.text = groundName
            overs.text = slot.noOfOvers
            time.text = slot.startTime

            menu.setOnClickListener {
                val popupMenu = PopupMenu(menu.context, menu)
                popupMenu.inflate(R.menu.menu_slot_item)
                popupMenu.setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.edit -> {
                            Log.d("Basava", "Edit")
                            onEditClickListener.invoke(adapterPosition, slot)
                        }
                        R.id.delete -> {
                            Log.d("Basava", "Delete")
                            onDeleteClickListener.invoke(adapterPosition, slot)
                        }
                    }
                    return@setOnMenuItemClickListener false
                }
                popupMenu.show()
            }

            // If teams name is not available, show Add Team option
            slot.team1?.let {
                team1Name.isVisible = true
                team1Add.isVisible = false
                team1Name.text = it.name
            } ?: run {
                team1Name.isVisible = false
                team1Add.isVisible = true

                team1Add.setOnClickListener {
                    Log.d("Basava", "Add Team1 clicked")
                    onAddTeam1ClickListener.invoke(adapterPosition, slot)
                }
            }

            slot.team2?.let {
                team2Name.isVisible = true
                team2Add.isVisible = false
                team2Name.text = it.name
            } ?: run {
                team2Name.isVisible = false
                team2Add.isVisible = true

                team2Add.setOnClickListener {
                    Log.d("Basava", "Add Team2 clicked")
                    onAddTeam2ClickListener.invoke(adapterPosition, slot)
                }
            }
        }
    }

    enum class ClickType {
        Edit,
        Delete,
        TeamOne,
        TeamTwo
    }
}