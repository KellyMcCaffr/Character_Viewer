package com.sample.view

import com.sample.R
import com.sample.mvm.CharacterItem
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CharacterAdapter internal constructor(context: Context?, data: List<CharacterItem>) : RecyclerView.Adapter<CharacterAdapter.ViewHolder>() {
    private val characterInfoList: List<CharacterItem> = data
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.character_item, parent, false)
        Log.e("435435","Create view holder")
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        /*val (name, region, code, capital) = countryInfoList[position]
        val nameAndRegionText = "| $name, $region"
        val capitalText = "| $capital"
        val codeText = "$code |"
        holder.nameAndRegionTextView.text = nameAndRegionText
        holder.codeTextView.text = codeText
        holder.capitalTextView.text = capitalText
        val isLastPosition = position == countryInfoList.size - 1
        if (isLastPosition) {
            holder.possibleFinalTextView.visibility = View.VISIBLE
        } else {
            holder.possibleFinalTextView.visibility = View.GONE
        }*/
        val character = characterInfoList[position]
        holder.nameTextView.text = character.name
        Log.e("435435","Loaded name to view: " + character.name)
    }

    override fun getItemCount(): Int {
        return characterInfoList.size
    }

    inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        /*var nameAndRegionTextView: TextView = itemView.findViewById(R.id.nameAndRegionTextView)
        var codeTextView: TextView = itemView.findViewById(R.id.codeTextView)
        var capitalTextView: TextView = itemView.findViewById(R.id.capitalTextView)
        var possibleFinalTextView: TextView = itemView.findViewById(R.id.possibleLastRow)*/
        var nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        override fun onClick(view: View) {}
    }
}