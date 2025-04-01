package com.example.campus_item_sharing.friendmodel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.campus_item_sharing.R

class FriendAdapter(
    private var friendList: List<FriendItem>,
    private val itemClickListener: ((FriendItem) -> Unit)? = null
) : RecyclerView.Adapter<FriendAdapter.FriendViewHolder>() {

    inner class FriendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val avatar: ImageView = itemView.findViewById(R.id.friend_avatar)
        val account: TextView = itemView.findViewById(R.id.friend_account)
        val lastMessage: TextView = itemView.findViewById(R.id.last_message)

        fun bind(friendItem: FriendItem) {
            // 设置好友账户名与最后一条消息
            account.text = friendItem.account
            lastMessage.text = friendItem.lastMessage

            // 这里直接使用布局中的默认头像
            avatar.setImageResource(R.drawable.ic_avatar_wechat)

            // 点击事件
            itemView.setOnClickListener {
                itemClickListener?.invoke(friendItem)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_friend, parent, false)
        return FriendViewHolder(view)
    }

    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        holder.bind(friendList[position])
    }

    override fun getItemCount(): Int = friendList.size

    // 更新数据的方法
    fun updateData(newList: List<FriendItem>) {
        friendList = newList
        notifyDataSetChanged()
    }
}