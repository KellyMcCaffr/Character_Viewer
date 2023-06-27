package com.sample.view

import com.sample.R
import com.sample.mvm.CharacterItem
import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class CharacterAdapter internal constructor(
    data: List<CharacterItem>,
    private val callback: MainActivity.Callback,
    private val isTablet: Boolean,
    private val context: Context?
) : RecyclerView.Adapter<CharacterAdapter.ViewHolder>() {
    var characterInfoListRaw = data
    private var characterInfoListFiltered = data
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var selectedItemPosition = -1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.full_character_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val character = characterInfoListFiltered[position]
        holder.apply {
            handleTabletDetailViewsOnBind(position, character, this, false)
            nameTextView.apply {
                text = character.name
                visibility = View.VISIBLE
            }
            val positionalCharacterImageView = characterImageView
            itemView.setOnClickListener {
                if (context != null) {
                    val isShowing = positionalCharacterImageView != null &&
                        positionalCharacterImageView.visibility == View.VISIBLE
                    if (isShowing && isTablet) {
                        selectedItemPosition = -1
                        hideTabletDetailViews(holder)
                    } else {
                        selectedItemPosition = position
                        handleTabletDetailViewsOnBind(position, character, holder, true)
                    }
                }
            }
            outerCardView.setOnTouchListener(
                View.OnTouchListener {
                    view, motionEvent ->
                    when (motionEvent.action) {
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

    fun setFilteredCharacterList(
        newList: List<CharacterItem>
    ) {
        characterInfoListFiltered = newList
        notifyDataSetChanged()
    }

    private fun handleTabletDetailViewsOnBind(
        position: Int,
        character: CharacterItem,
        holder: ViewHolder,
        isClick: Boolean
    ) {
        if (position != selectedItemPosition) {
            hideTabletDetailViews(holder)
        } else if (context != null) {
            if (isTablet) {
                showTabletDetailViews(holder, character)
            } else if(isClick) {
                callback.onCharacterItemClickedNonTablet(character, context)
            }
        }
    }

    private fun hideTabletDetailViews(
        holder: ViewHolder
    ) {
        holder.apply {
            descriptionTextView.visibility = View.GONE
            characterImageView.visibility = View.GONE
            possibleCharacterImageWrapper.visibility = View.GONE
        }
    }

    private fun showTabletDetailViews(
        holder: ViewHolder,
        characterItem: CharacterItem
    ) {
        holder.apply {
            descriptionTextView.apply {
                visibility = View.VISIBLE
                text = characterItem.description
            }
            characterImageView.visibility = View.VISIBLE
            possibleCharacterImageWrapper.visibility = View.VISIBLE
            ViewUtils.displayCharacterImageFromUrlAndAdjustBounds(
                characterItem.imageWidth,
                characterItem.imageHeight,
                characterItem.imageUrl,
                characterImageView,
                context
            )
        }
    }

    override fun getItemCount(): Int {
        return characterInfoListFiltered.size
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