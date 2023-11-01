package com.dicoding.aplikasigithubuser.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.aplikasigithubuser.R
import com.dicoding.aplikasigithubuser.data.local.Result
import com.dicoding.aplikasigithubuser.data.remote.response.ItemsItem
import com.dicoding.aplikasigithubuser.databinding.ActivityMainBinding
import com.dicoding.aplikasigithubuser.ui.adapter.UserAdapter
import com.dicoding.aplikasigithubuser.ui.viewmodel.MainViewModel
import com.dicoding.aplikasigithubuser.ui.viewmodel.ViewModelFactory

class MainActivity : AppCompatActivity() {

    private  var binding: ActivityMainBinding? = null
    private val mainViewModel by viewModels<MainViewModel>{
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        supportActionBar?.hide()

        val layoutManager = LinearLayoutManager(this)
        binding?.rvUser?.layoutManager =layoutManager

        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding?.rvUser?.addItemDecoration(itemDecoration)

        mainViewModel.getThemeSetting().observe(this){isDarkModeActive: Boolean ->
            if (isDarkModeActive){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        mainViewModel.findUser().observe(this){listUser ->
            listUser?.let {
                when (it) {
                    is Result.Loading -> {
                        showLoading(true)
                    }
                    is Result.Success -> {
                        showLoading(false)
                        val newData = it.data
                        setListUser(newData)
                    }
                    is Result.Error -> {
                        showLoading(false)
                        Toast.makeText(this@MainActivity, "Terjadi kesalahan " + it.error, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        val currentTheme = resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK

        with(binding){
            this!!.searchBar.inflateMenu(R.menu.menu)
            searchBar.setOnMenuItemClickListener { menuItem ->
                when(menuItem.itemId){
                    R.id.menuFavorit -> {
                        val intent = Intent(this@MainActivity, FavoritActivity::class.java)
                        startActivity(intent)
                        true
                    }
                    R.id.menuSetting -> {
                        val intent = Intent(this@MainActivity,SettingActivity::class.java)
                        startActivity(intent)
                        true
                    }
                    else -> false
                }
            }
            val menuFavoritItem = searchBar.menu.findItem(R.id.menuFavorit)

            if (currentTheme == android.content.res.Configuration.UI_MODE_NIGHT_YES){
                menuFavoritItem.setIcon(R.drawable.baseline_favorite_white_24)
            }else{
                menuFavoritItem.setIcon(R.drawable.baseline_favorite_24)
            }

            this.searchView.setupWithSearchBar(this.searchBar)
            searchView
                .editText
                .setOnEditorActionListener { textView, _, _ ->
                    searchBar.text = textView.text
                    mainViewModel.searchUser(textView.text.toString()).observe(this@MainActivity){ listUser ->
                        when(listUser){
                            is Result.Loading -> {
                                showLoading(true)
                            }
                            is Result.Success -> {
                                showLoading(false)
                                val newData = listUser.data
                                if (newData.isNotEmpty()){
                                    showDataNull(false)
                                    setListUser(newData)
                                } else{
                                    setListUser(emptyList())
                                    showDataNull(true)
                                }
                            }
                            is Result.Error -> {
                                showLoading(false)
                                Toast.makeText(this@MainActivity,"Terjadi kesalahan " + listUser.error, Toast.LENGTH_SHORT ).show()
                            }
                        }
                    }
                    Toast.makeText(this@MainActivity, "mencari user " + textView.text, Toast.LENGTH_SHORT).show()
                    searchView.hide()
                    false
            }
        }
    }

    private fun setListUser(listUser: List<ItemsItem>) {
        val adapter = UserAdapter()
        adapter.submitList(listUser)
        binding?.rvUser?.adapter = adapter
    }

    private fun showLoading(state: Boolean) { binding?.progressBar?.visibility = if (state) View.VISIBLE else View.GONE }

    private fun showDataNull(state: Boolean) { binding?.tvListKosong?.visibility = if (state) View.VISIBLE else View.GONE }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}