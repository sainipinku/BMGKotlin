package com.bookmygame.ui.groundOwner.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import androidx.annotation.LayoutRes
import com.bookmygame.databinding.ListItemTeamBinding
import com.bookmygame.network.model.TeamListResponse
import java.util.*

class TeamsAdapter(
    private val c: Context,
    @LayoutRes private val layoutResource: Int,
    private val teams: List<TeamListResponse.Team>
) : ArrayAdapter<TeamListResponse.Team>(c, layoutResource, teams) {

    var filteredTeams: List<TeamListResponse.Team> = listOf()

    override fun getCount(): Int = filteredTeams.size

    override fun getItem(position: Int): TeamListResponse.Team = filteredTeams[position]

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = ListItemTeamBinding.inflate(LayoutInflater.from(c), parent, false)
        binding.name.text = filteredTeams[position].teamName
        Log.d("Basava", "getView: name -> ${filteredTeams[position].teamName}")
        return binding.root
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun publishResults(charSequence: CharSequence?, filterResults: FilterResults) {
                @Suppress("UNCHECKED_CAST")
                filteredTeams = filterResults.values as List<TeamListResponse.Team>
                Log.d("Basava", "publishResults: filteredTeams -> $filteredTeams")
                notifyDataSetChanged()
            }

            override fun performFiltering(charSequence: CharSequence?): FilterResults {
                val queryString = charSequence?.toString()?.toLowerCase(Locale.ROOT)
                Log.d("Basava", "performFiltering: queryString -> $queryString")
                val filterResults = FilterResults()
                filterResults.values = if (queryString == null || queryString.isEmpty())
                    teams
                else
                    teams.filter {
                        it.teamName.toLowerCase(Locale.ROOT).contains(queryString)
                    }
                return filterResults
            }
        }
    }
}