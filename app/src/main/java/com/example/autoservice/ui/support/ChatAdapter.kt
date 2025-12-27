package com.example.autoservice.ui.support

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.autoservice.R
import com.example.autoservice.data.model.ChatMessage

class ChatAdapter(private val messages: List<ChatMessage>) : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvSenderName: TextView = itemView.findViewById(R.id.tvSenderName)
        val tvMessage: TextView = itemView.findViewById(R.id.tvMessage)
        val tvTime: TextView = itemView.findViewById(R.id.tvTime)
        val llMessageContainer: LinearLayout = itemView.findViewById(R.id.llMessageContainer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_chat_message, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val message = messages[position]
        
        holder.tvSenderName.text = message.senderName
        holder.tvMessage.text = message.message
        holder.tvTime.text = message.time
        
        // Выравнивание сообщений
        val layoutParams = holder.llMessageContainer.layoutParams as RecyclerView.LayoutParams
        if (message.isFromUser) {
            layoutParams.marginStart = 80
            layoutParams.marginEnd = 0
            holder.tvMessage.setBackgroundResource(R.drawable.bg_card)
        } else {
            layoutParams.marginStart = 0
            layoutParams.marginEnd = 80
            holder.tvMessage.setBackgroundResource(R.drawable.bg_card)
        }
        
        holder.llMessageContainer.layoutParams = layoutParams
    }

    override fun getItemCount() = messages.size
}
