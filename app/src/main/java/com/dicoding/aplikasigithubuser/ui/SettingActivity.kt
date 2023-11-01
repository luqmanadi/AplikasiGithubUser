package com.dicoding.aplikasigithubuser.ui

import android.os.Bundle
import android.widget.CompoundButton
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.dicoding.aplikasigithubuser.databinding.ActivitySettingBinding
import com.dicoding.aplikasigithubuser.ui.viewmodel.SettingViewModel
import com.dicoding.aplikasigithubuser.ui.viewmodel.ViewModelFactory
import com.google.android.material.appbar.MaterialToolbar

class SettingActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySettingBinding

    private val settingViewModel by viewModels<SettingViewModel>{
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar: MaterialToolbar = binding.topAppBarSetting
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }


        settingViewModel.getThemeSetting().observe(this){isDarkModeActive: Boolean ->
            if (isDarkModeActive){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.switchTheme.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.switchTheme.isChecked = false
            }
        }

        binding.switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            settingViewModel.saveThemeSetting(isChecked)
        }

    }
}