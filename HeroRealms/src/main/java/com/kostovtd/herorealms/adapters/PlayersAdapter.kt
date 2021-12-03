package com.kostovtd.herorealms.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.kostovtd.boardy.util.Constants
import com.kostovtd.herorealms.R
import kotlinx.android.synthetic.main.item_player.view.*
import java.util.*

class PlayersAdapter(private val context: Context) : RecyclerView.Adapter<PlayersAdapter.ViewHolder>() {

    var listener: IPlayersListener? = null
    var data: ArrayList<String>? = null
    var adminId: String? = null
    var currentPlayerId: String? = null


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_player, viewGroup, false)
    )


    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        return viewHolder.bind(context, data?.get(position), adminId, currentPlayerId, listener)
    }


    override fun getItemCount(): Int = data?.size ?: 0


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private var rootView = view

        fun bind(context: Context, itemData: String?, adminId: String?, currentPlayerId: String?, listener: IPlayersListener?) {
            val playerItemData = itemData?.split(Constants.FIRESTORE_VALUE_SEPARATOR)
            val playerItemName = playerItemData?.get(1) ?: context.getString(R.string.not_available)
            val playerItemId = playerItemData?.get(0) ?: ""
            val isItemAdmin = playerItemId == adminId
            val isCurrentUserAdmin = adminId == currentPlayerId

            rootView.playerName.text = playerItemName

            val icPlayerDrawable = if(isItemAdmin) {
                ContextCompat.getDrawable(context, R.drawable.ic_baseline_star_24)
            } else {
                ContextCompat.getDrawable(context, R.drawable.ic_baseline_account_circle_24)
            }

            if(isCurrentUserAdmin && !isItemAdmin) {
                rootView.removePlayer.visibility = View.VISIBLE
            } else {
                rootView.removePlayer.visibility = View.GONE
            }

            rootView.icPlayer.setImageDrawable(icPlayerDrawable)

            rootView.removePlayer.setOnClickListener { listener?.onRemovePlayerSelected(itemData ?: "") }
        }
    }
}