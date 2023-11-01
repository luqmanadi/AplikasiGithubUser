package com.dicoding.aplikasigithubuser.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.aplikasigithubuser.data.remote.response.ItemsItem
import com.dicoding.aplikasigithubuser.databinding.ItemUserBinding
import com.dicoding.aplikasigithubuser.ui.DetailUserActivity

class UserAdapter: ListAdapter<ItemsItem, UserAdapter.MyViewHolder>(DIFF_CALLBACK) {

    class MyViewHolder(private val binding: ItemUserBinding): RecyclerView.ViewHolder(binding.root) {
        private fun ImageView.loadImage(url: String?) {
            Glide.with(this.context)
                .load(url)
                .into(this)
        }
        fun bind(user: ItemsItem){
            binding.imgUser.loadImage(user.avatarUrl)
            binding.tvUser.text = user.login
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)

        holder.itemView.setOnClickListener{
            Toast.makeText(holder.itemView.context, "Memilih ${user.login}" , Toast.LENGTH_SHORT).show()
            val intentDetailUser = Intent(holder.itemView.context, DetailUserActivity::class.java)
            intentDetailUser.putExtra(DetailUserActivity.EXTRA_USERNAME, user.login)
            holder.itemView.context.startActivity(intentDetailUser)

        }
    }

    companion object{
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ItemsItem>(){
            override fun areItemsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
                return oldItem == newItem
            }

        }
    }
}