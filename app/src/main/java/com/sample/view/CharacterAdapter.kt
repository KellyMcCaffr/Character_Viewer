package com.sample.view

import com.sample.R
import com.sample.mvm.CharacterItem
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import java.util.*

class CharacterAdapter internal constructor(
    data: List<CharacterItem>,
    callback: MainActivity.Callback,
    context: Context?
) : RecyclerView.Adapter<CharacterAdapter.ViewHolder>() {
    private val characterInfoList = data
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private val context = context
    private val callback = callback
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.full_character_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val character = characterInfoList[position]
        Log.e("34545","Here is character image url: " + character.imageUrl)
        holder.apply {
            /*ViewUtils.showCharacterImage(character.imageWidth, character.imageHeight,
                character.imageUrl, holder.characterImageView)*/
            //possibleCharacterImageWrapper.setVisibility(View.VISIBLE)
            nameTextView.apply {
                text = character.name
                visibility = View.VISIBLE
            }
            itemView.setOnClickListener {
                if (context != null) {
                    callback.onCharacterItemClicked(character, context)
                }
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

    inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        val characterImageView: ImageView = itemView.findViewById(R.id.characterImageView)
        val possibleCharacterImageWrapper: RelativeLayout = itemView.findViewById(R.id.possibleCharacterImageWrapper)
        val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
        val outerCardView: CardView = itemView.findViewById(R.id.outerCardView)
        override fun onClick(view: View) {}
    }
}