package com.dicoding.mygithublist.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.mygithublist.databinding.FragmentFollowBinding
import com.dicoding.mygithublist.ui.adapter.UserAdapter
import com.dicoding.mygithublist.ui.main.DetailUserActivity

class FollowFragment : Fragment() {

    private lateinit var binding: FragmentFollowBinding
    companion object {
        const val POSITION = "position"
        const val USERNAME = "username"
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val followViewModel = ViewModelProvider(requireActivity()).get(FollowViewModel::class.java)
        val adapter = UserAdapter { user ->
            val intent = DetailUserActivity.newIntents(requireContext(), user.login)
            startActivity(intent)
        }

        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvFollow.layoutManager = layoutManager
        binding.rvFollow.adapter = adapter

        val position = requireArguments().getInt(POSITION)
        val username = requireArguments().getString(USERNAME) ?: ""
        if (position == 1) {
            followViewModel.listFollowers.observe(viewLifecycleOwner) { followers ->
                adapter.submitList(followers)
            }
            followViewModel.getFollowers(username)
        } else {
            followViewModel.listFollowing.observe(viewLifecycleOwner) { following ->
                adapter.submitList(following)
            }
            followViewModel.getFollowing(username)
        }

        followViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.followProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

}
