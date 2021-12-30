package com.example.chatapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapp.R
import com.example.chatapp.model.ChatModel
import kotlinx.android.synthetic.main.left_bubble.view.*

class RecyclerAdapter(private var mutableChatList: MutableList<ChatModel>, var userKey: String) :
    RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    private var leftCode: Int = 12312;
    private var rightCode: Int = 97893;

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.ViewHolder {
        return if (viewType == leftCode) {
            ViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.left_bubble, parent, false)
            )
        } else {
            ViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.right_bubble, parent, false)
            )

        }
    }

    override fun onBindViewHolder(holder: RecyclerAdapter.ViewHolder, position: Int) {
        holder.messageBox.text = mutableChatList[position].message
        holder.name.text = mutableChatList[position].name
        holder.time.text = mutableChatList[position].time
    }

    override fun getItemCount(): Int = mutableChatList.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var messageBox = itemView.message!!
        var name = itemView.name!!
        var time = itemView.time!!
    }

    override fun getItemViewType(position: Int): Int {
        return if (mutableChatList[position].userId == userKey) rightCode else leftCode
    }


}