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


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_player, viewGroup, false)
    )


    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val playerData = data?.get(position)?.split(Constants.FIRESTORE_VALUE_SEPARATOR)
        val playerName = playerData?.get(1) ?: context.getString(R.string.not_available)
        val playerId = playerData?.get(0) ?: ""
        val isAdmin = playerId == adminId
        return viewHolder.bind(context, playerName, isAdmin, listener)
    }


    override fun getItemCount(): Int = data?.size ?: 0


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private var rootView = view

        fun bind(context: Context, playerName: String, isAdmin: Boolean, listener: IPlayersListener?) {
            rootView.playerName.text = playerName

            val icPlayerDrawable = if(isAdmin) {
                ContextCompat.getDrawable(context, R.drawable.ic_baseline_star_24)
            } else {
                ContextCompat.getDrawable(context, R.drawable.ic_baseline_account_circle_24)
            }

            rootView.icPlayer.setImageDrawable(icPlayerDrawable)

            rootView.setOnClickListener { listener?.onPlayerSelected(playerName) }
        }
    }
}