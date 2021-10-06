package com.bookmygame.ui.groundOwner.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bookmygame.R
import com.bookmygame.databinding.ListItemGroundGroundOwnerBinding
import com.bookmygame.network.model.GroundOwnerGroundListResponse
import java.util.*

class GroundOwnerGroundsAdapter(
    private var groundsList: List<GroundOwnerGroundListResponse.Ground>,
    private val onUpdateDetailsClick: (GroundOwnerGroundListResponse.Ground) -> Unit,
    private val onAddSlotClick: (GroundOwnerGroundListResponse.Ground) -> Unit
) :
    RecyclerView.Adapter<GroundViewHolder>(), Filterable {

    private var groundFilterList = listOf<GroundOwnerGroundListResponse.Ground>()

    init {
        groundFilterList = groundsList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroundViewHolder {
        return GroundViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_ground_ground_owner, parent, false),
            { position ->
                onUpdateDetailsClick.invoke(groundFilterList[position])
            },
            { position ->
                onAddSlotClick.invoke(groundFilterList[position])
            }
        )
    }

    override fun onBindViewHolder(holder: GroundViewHolder, position: Int) {
        holder.bind(groundFilterList[position])
    }

    override fun getItemCount(): Int = groundFilterList.size

    override fun getFilter(): Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val charSearch = constraint.toString()
            groundFilterList = if (charSearch.isEmpty()) {
                groundsList
            } else {
                val resultList = ArrayList<GroundOwnerGroundListResponse.Ground>()
                for (row in groundsList) {
                    if (row.groundName.toLowerCase(Locale.ROOT)
                            .contains(charSearch.toLowerCase(Locale.ROOT))
                    ) {
                        resultList.add(row)
                    }
                }
                resultList
            }
            val filterResults = FilterResults()
            filterResults.values = groundFilterList
            return filterResults
        }

        @Suppress("UNCHECKED_CAST")
        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            groundFilterList = results?.values as ArrayList<GroundOwnerGroundListResponse.Ground>
            notifyDataSetChanged()
        }
    }
}

class GroundViewHolder(
    view: View,
    private val onUpdateDetailsClick: (Int) -> Unit,
    private val onAddSlotClick: (Int) -> Unit
) :
    RecyclerView.ViewHolder(view) {

    private val binding = ListItemGroundGroundOwnerBinding.bind(view)

    init {
        binding.apply {
            root.setOnClickListener {
                Log.d("Basava", "Ground clicked")
                onUpdateDetailsClick.invoke(adapterPosition)
            }
            updateDetails.setOnClickListener {
                Log.d("Basava", "Update Details clicked")
                onUpdateDetailsClick.invoke(adapterPosition)
            }
            addSlots.setOnClickListener {
                Log.d("Basava", "Add Slots clicked")
                onAddSlotClick.invoke(adapterPosition)
            }
        }
    }

    fun bind(ground: GroundOwnerGroundListResponse.Ground) {
        binding.apply {
            title.text = ground.groundName
            groundAddress.text = ground.groundAddress
        }
    }
}