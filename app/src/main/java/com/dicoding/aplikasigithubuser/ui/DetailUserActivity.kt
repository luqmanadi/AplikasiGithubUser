package com.dicoding.aplikasigithubuser.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.dicoding.aplikasigithubuser.R
import com.dicoding.aplikasigithubuser.data.local.Result
import com.dicoding.aplikasigithubuser.data.local.entity.FavoriteUserEntity
import com.dicoding.aplikasigithubuser.data.remote.response.DetailUserResponse
import com.dicoding.aplikasigithubuser.databinding.ActivityDetailUserBinding
import com.dicoding.aplikasigithubuser.ui.adapter.SectionsPagerAdapter
import com.dicoding.aplikasigithubuser.ui.viewmodel.DetailUserViewModel
import com.dicoding.aplikasigithubuser.ui.viewmodel.ViewModelFactory
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailUserActivity : AppCompatActivity() {

    companion object{
        const val EXTRA_USERNAME = "extra_username"
        private val TAB_TITLES = intArrayOf(
            R.string.followers_tabs,
            R.string.following_tabs
        )
    }
    private val detailViewModel by viewModels<DetailUserViewModel>{
        ViewModelFactory.getInstance(application)
    }

    private val dataEntity = FavoriteUserEntity()

    private var binding: ActivityDetailUserBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val username = intent.getStringExtra(EXTRA_USERNAME)

        val currentTheme = resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK

        val toolbar: MaterialToolbar? = binding?.topAppBarDetailUser

        val menuFavoritItem = toolbar?.menu?.findItem(R.id.menuFavorit)

        toolbar?.title = username
        toolbar?.setNavigationOnClickListener {
            onBackPressed()
        }

        if (currentTheme == android.content.res.Configuration.UI_MODE_NIGHT_YES){
            menuFavoritItem?.setIcon(R.drawable.baseline_favorite_white_24)
        }else{
            menuFavoritItem?.setIcon(R.drawable.baseline_favorite_24)
        }

        toolbar?.setOnMenuItemClickListener{ item ->
            when(item.itemId) {
                R.id.menuFavorit -> {
                    val intent = Intent(this@DetailUserActivity, FavoritActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.menuSetting -> {
                    val intent = Intent(this@DetailUserActivity, SettingActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }

        }

        detailViewModel.detailUser(username.toString()).observe(this){detailUser ->
            if (detailUser != null){
                when(detailUser){
                    is Result.Loading -> {
                        showLoading(true)
                    }
                    is Result.Success -> {
                        showLoading(false)
                        val dataNew = detailUser.data
                        setDetailUser(dataNew)
                        tabLayout(dataNew)
                    }
                    is Result.Error -> {
                        showLoading(false)
                        Toast.makeText(this@DetailUserActivity, "Terjadi kesalahan : " + detailUser.error, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        detailViewModel.getIsFavorite(username.toString()).observe(this){isExist ->
            if (isExist){
                binding?.fabFavorite?.setImageResource(R.drawable.baseline_favorite_24)
                binding?.fabFavorite?.setOnClickListener {
                    detailViewModel.deleteFavoriteUser(dataEntity)
                    Toast.makeText(this, "User berhasil dihapus dari daftar favorit", Toast.LENGTH_SHORT).show()
                }
            } else{
                binding?.fabFavorite?.setImageResource(R.drawable.baseline_favorite_border_24)
                binding?.fabFavorite?.setOnClickListener {
                    detailViewModel.insertUserToFavorite(dataEntity)
                    Toast.makeText(this, "User berhasil ditambahkan di daftar favorite", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun tabLayout(detailUser: DetailUserResponse){
        val username = detailUser.login
        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        val viewPager: ViewPager2? = binding?.viewPager2
        viewPager?.adapter = sectionsPagerAdapter
        sectionsPagerAdapter.username = username
        val tabs: TabLayout? = binding?.tabs
        TabLayoutMediator(tabs!!, viewPager!!){ tab, position->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }


    private fun setDetailUser(detailUser: DetailUserResponse){

        dataEntity.username = detailUser.login
        dataEntity.avatarUrl = detailUser.avatarUrl
        binding?.tvNameUser?.text = detailUser.name
        binding?.tvUsernameUser?.text = detailUser.login
        val followersText = this@DetailUserActivity.resources.getString(R.string.followers, detailUser.followers)
        binding?.tvFollowers?.text = followersText

        val followingText = this@DetailUserActivity.resources.getString(R.string.following,detailUser.following)
        binding?.tvFollowing?.text = followingText

        Glide.with(this@DetailUserActivity)
            .load(detailUser.avatarUrl)
            .into(binding!!.imgDetailUser)
    }

    private fun showLoading(isLoading: Boolean) {binding?.progressBar?.visibility = if (isLoading) View.VISIBLE else View.GONE}

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}