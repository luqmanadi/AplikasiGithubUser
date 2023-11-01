package com.dicoding.aplikasigithubuser.ui.adapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dicoding.aplikasigithubuser.ui.DetailFolFragment

class SectionsPagerAdapter(activity: AppCompatActivity): FragmentStateAdapter(activity) {
    var username: String = ""
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = DetailFolFragment()
        fragment.arguments = Bundle().apply {
            putInt(DetailFolFragment.ARG_POSITION, position + 1)
            putString(DetailFolFragment.ARG_USERNAME, username)
        }
        return fragment
    }
}