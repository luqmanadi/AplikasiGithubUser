package com.dicoding.aplikasigithubuser.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.aplikasigithubuser.R
import com.dicoding.aplikasigithubuser.data.local.Result
import com.dicoding.aplikasigithubuser.data.remote.response.ItemsItem
import com.dicoding.aplikasigithubuser.databinding.FragmentDetailFolBinding
import com.dicoding.aplikasigithubuser.ui.adapter.UserAdapter
import com.dicoding.aplikasigithubuser.ui.viewmodel.DetailUserViewModel
import com.dicoding.aplikasigithubuser.ui.viewmodel.ViewModelFactory


class DetailFolFragment : Fragment() {

    private  var binding: FragmentDetailFolBinding? = null
    private var position: Int = 0
    private var username: String = ""

    private val detailUserViewModel by viewModels<DetailUserViewModel> { ViewModelFactory.getInstance(requireActivity()) }

    companion object{
        const val ARG_POSITION = "position_number"
        const val ARG_USERNAME = "username"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDetailFolBinding.inflate(inflater,container,false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            position = it.getInt(ARG_POSITION)
            username = it.getString(ARG_USERNAME).toString()
        }

        val layoutManager = LinearLayoutManager(requireActivity())
        binding?.rvFoll?.layoutManager = layoutManager

        val itemDecoration = DividerItemDecoration(requireActivity(), layoutManager.orientation)
        binding?.rvFoll?.addItemDecoration(itemDecoration)


        if (position == 1){
            detailUserViewModel.listFollower(username).observe(viewLifecycleOwner){listFollower ->
                when(listFollower){
                    is Result.Loading -> {
                        showLoading(true)
                    }
                    is Result.Success -> {
                        showLoading(false)
                        val newData = listFollower.data
                        if (newData.isEmpty()){
                            binding?.testUsername?.text = this@DetailFolFragment.resources.getString(R.string.have_no_followers)
                        }else{
                            setListFollowerFollowing(newData)
                        }
                    }
                    is Result.Error -> {
                        showLoading(false)
                        Toast.makeText(context, "Terjadi kesalahan " + listFollower.error, Toast.LENGTH_SHORT).show()
                    }
                }

            }
        } else{
            detailUserViewModel.listFollowing(username).observe(viewLifecycleOwner){listFollowing ->
                when(listFollowing){
                    is Result.Loading -> {
                        showLoading(true)
                    }
                    is Result.Success -> {
                        showLoading(false)
                        val newData = listFollowing.data
                        if (newData.isEmpty()){
                            binding?.testUsername?.text = this@DetailFolFragment.resources.getString(R.string.have_no_following)
                        }else{
                            setListFollowerFollowing(newData)
                        }
                    }
                    is Result.Error -> {
                        showLoading(false)
                        Toast.makeText(context, "Terjadi kesalahan " + listFollowing.error, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }


    private fun setListFollowerFollowing(listFolwerwing: List<ItemsItem>){
        val adapter = UserAdapter()
        adapter.submitList(listFolwerwing)
       binding?.rvFoll?.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean){binding?.progressBar?.visibility = if (isLoading) View.VISIBLE else View.GONE}

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}