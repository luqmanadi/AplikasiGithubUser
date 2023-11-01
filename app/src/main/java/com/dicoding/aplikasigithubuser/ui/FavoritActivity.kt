package com.dicoding.aplikasigithubuser.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.aplikasigithubuser.R
import com.dicoding.aplikasigithubuser.data.local.entity.FavoriteUserEntity
import com.dicoding.aplikasigithubuser.databinding.ActivityFavoritBinding
import com.dicoding.aplikasigithubuser.ui.adapter.FavoriteUserAdapter
import com.dicoding.aplikasigithubuser.ui.viewmodel.FavoriteViewModel
import com.dicoding.aplikasigithubuser.ui.viewmodel.ViewModelFactory
import com.google.android.material.appbar.MaterialToolbar

class FavoritActivity : AppCompatActivity() {

    private var binding: ActivityFavoritBinding? = null

    private val favoriteViewModel by viewModels<FavoriteViewModel> {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoritBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val layoutManager = LinearLayoutManager(this)
        binding?.rvFavoritUser?.layoutManager =layoutManager

        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding?.rvFavoritUser?.addItemDecoration(itemDecoration)

        val toolbar: MaterialToolbar? = binding?.topAppBar
        toolbar?.setNavigationOnClickListener {
            onBackPressed()
        }
        toolbar?.setOnMenuItemClickListener{ item ->
            when(item.itemId) {
                R.id.menuSetting2 -> {
                    val intent = Intent(this, SettingActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

        favoriteViewModel.getUserFavorite().observe(this){
            showLoading(true)
            if (it.isNotEmpty()){
                showLoading(false)
                setListUser(it)
            } else{
                showLoading(false)
                showListNull(true)
            }
        }

    }

    private fun setListUser(listUser: List<FavoriteUserEntity>) {
        val adapter = FavoriteUserAdapter()
        adapter.submitList(listUser)
        binding?.rvFavoritUser?.adapter = adapter
    }

    private fun showLoading(state: Boolean) { binding?.progressBar?.visibility = if (state) View.VISIBLE else View.GONE }

    private fun showListNull(state: Boolean) { binding?.tvListKosong?.visibility = if (state) View.VISIBLE else View.GONE }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}