package com.dicoding.mygithublist.ui.adapter
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.dicoding.mygithublist.R
import com.dicoding.mygithublist.data.response.ItemsItem
import com.dicoding.mygithublist.databinding.ItemBinding
import com.dicoding.mygithublist.ui.main.DetailUserActivity


class UserAdapter(private val onItemClick: (ItemsItem) -> Unit) : ListAdapter<ItemsItem, UserAdapter.MyViewHolder>(
    DIFF_CALLBACK
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)

        holder.itemView.setOnClickListener {
            val intentDetail = Intent(holder.itemView.context, DetailUserActivity::class.java)
            intentDetail.putExtra("username", user.login)
            intentDetail.putExtra("avatarUrl", user.avatarUrl)
            holder.itemView.context.startActivity(intentDetail)
        }
    }

    class MyViewHolder(val binding: ItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: ItemsItem){
            binding.apply {
                tvUserName.text = user.login

                Glide.with(root)
                    .load(user.avatarUrl)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .placeholder(R.drawable.baseline_person_24)
                    .into(image)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ItemsItem>() {
            override fun areItemsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
