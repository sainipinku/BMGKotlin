package com.bookmygame.ui.user.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bookmygame.R
import com.bookmygame.databinding.ListItemGroundGroundOwnerBinding
import com.bookmygame.databinding.ListItemGroundUserBinding
import com.bookmygame.network.model.GroundOwnerGroundListResponse
import com.bookmygame.network.model.UserGroundListResponse
import com.bookmygame.ui.user.model.Ground
import java.util.*

class UserGroundsAdapter(

        private var groundsList: List<UserGroundListResponse.Ground>,
        private val onUpdateDetailsClick: (UserGroundListResponse.Ground) -> Unit,
        ) :
    RecyclerView.Adapter<GroundViewHolder>() {

    private var groundFilterList = listOf<UserGroundListResponse.Ground>()

    init {
        groundFilterList = groundsList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroundViewHolder {
        return GroundViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_ground_user, parent, false),
                { position ->
                    onUpdateDetailsClick.invoke(groundFilterList[position])
                }

        )
    }

    override fun onBindViewHolder(holder: GroundViewHolder, position: Int) {
        holder.bind(groundFilterList[position])
    }

    override fun getItemCount(): Int = groundFilterList.size

    /*override fun getFilter(): Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val charSearch = constraint.toString()
            groundFilterList = if (charSearch.isEmpty()) {
                groundsList
            } else {
                val resultList = ArrayList<Ground>()
                for (row in groundsList) {
                    if (row.name.toLowerCase(Locale.ROOT)
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
            groundFilterList = results?.values as ArrayList<Ground>
            notifyDataSetChanged()
        }
    }*/
}

/*
class GroundViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ListItemGroundUserBinding.bind(view)

    init {
        binding.root.setOnClickListener {
            Log.d("Basava", "Ground clicked")

        }
    }

    fun bind(ground: Ground?) {
        binding.apply {
            title.text = ground?.name
            groundAddress.text = ground?.city
        }
    }
}*/
class GroundViewHolder(
        view: View,
        private val onUpdateDetailsClick: (Int) -> Unit,
) :
        RecyclerView.ViewHolder(view) {

    private val binding = ListItemGroundUserBinding.bind(view)

    init {
        binding.apply {
            root.setOnClickListener {
                Log.d("Basava", "Ground clicked")
                onUpdateDetailsClick.invoke(adapterPosition)
            }
        }
    }

    fun bind(ground: UserGroundListResponse.Ground) {
        binding.apply {
            title.text = ground.groundName
            groundAddress.text = ground.groundAddress
        }
    }
}