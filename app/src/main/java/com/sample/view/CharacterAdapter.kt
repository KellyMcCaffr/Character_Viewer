package com.sample.view

import com.sample.R
import com.sample.mvm.CharacterItem
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class CharacterAdapter internal constructor(context: Context?, data: List<CharacterItem>) : RecyclerView.Adapter<CharacterAdapter.ViewHolder>() {
    private val characterInfoList: List<CharacterItem> = data
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.full_character_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val character = characterInfoList[position]
        holder.apply {
            nameTextView.text = character.name
            itemView.setOnClickListener {
                onItemClicked(position)
            }
            outerCardView.setOnTouchListener(
                View.OnTouchListener(){
                    view, motionEvent ->
                    when (motionEvent.action){
                        MotionEvent.ACTION_DOWN -> {
                            view.alpha = 0.6f
                        }
                        MotionEvent.ACTION_UP -> {
                            view.alpha = 1f
                            view.performClick()
                        }
                        MotionEvent.ACTION_CANCEL -> {
                            view.alpha = 1f
                        }
                    }
                    return@OnTouchListener true
                }
            )
        }
    }

    override fun getItemCount(): Int {
        return characterInfoList.size
    }

    private fun onItemClicked(
        position: Int
    ) {

    }

    inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        var outerCardView: CardView = itemView.findViewById(R.id.outerCardView)
        override fun onClick(view: View) {}
    }
}