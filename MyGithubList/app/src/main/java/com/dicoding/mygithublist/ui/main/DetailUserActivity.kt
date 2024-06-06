package com.dicoding.mygithublist.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.dicoding.mygithublist.R
import com.dicoding.mygithublist.data.repository.FavoriteAddUpdateViewModel
import com.dicoding.mygithublist.data.response.DetailResponse
import com.dicoding.mygithublist.database.Favorite
import com.dicoding.mygithublist.databinding.ActivityDetailUserBinding

import com.dicoding.mygithublist.ui.DetailViewModel
import com.dicoding.mygithublist.ui.adapter.SectionsPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class DetailUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailUserBinding
    private lateinit var detailViewModel: DetailViewModel
    private lateinit var favoriteAddUpdateViewModel: FavoriteAddUpdateViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra("username") ?: ""

        detailViewModel = ViewModelProvider(this).get(DetailViewModel::class.java)
        detailViewModel.getDetail(username)
        detailViewModel.detail.observe(this) { detail ->
            disDetail(detail)
        }

        detailViewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }

        favoriteAddUpdateViewModel = ViewModelProvider(this).get(FavoriteAddUpdateViewModel::class.java)

        val usernameFav = intent.getStringExtra("username")
        if (usernameFav != null) {
            detailViewModel.getDetail(usernameFav)
            checkIsUserFavorite(usernameFav)
        }


        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.username = username
        binding.viewPager.adapter = sectionsPagerAdapter

        binding.tabs.apply {
            TabLayoutMediator(this, binding.viewPager) { tab, position ->
                tab.text = resources.getString(TAB_TITLES[position])
            }.attach()
            supportActionBar?.elevation = 0f

            binding.fab.setOnClickListener {
                saveToFavorite()
            }
        }
    }

    private fun disDetail(detailUser: DetailResponse) {
        binding.apply {
            tvDetailName.text = detailUser.name ?: ""
            tvUsername.text = detailUser.login ?: ""
            tvUserDetailFollowers.text = detailUser.followers.toString()
            tvUserDetailFollowing.text = detailUser.following.toString()

            Glide.with(this@DetailUserActivity)
                .load(detailUser.avatarUrl)
                .transition(DrawableTransitionOptions.withCrossFade())
                .placeholder(R.drawable.baseline_person_24)
                .into(detailProfile)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.detailProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        private const val EXTRA_USERNAME = "extra_username"

        fun newIntents(context: Context, username: String): Intent {
            val intent = Intent(context, DetailUserActivity::class.java)
            intent.putExtra(EXTRA_USERNAME, username)

            return intent
        }
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }
    private fun checkIsUserFavorite(username: String) {
        favoriteAddUpdateViewModel.checkIsUserFavorite(username).observe(this) { isFavorite ->
            if (isFavorite) {
                binding.fab.setImageResource(R.drawable.favorite_fill)
                binding.fab.setOnClickListener {
                    deleteFromFavorite()
                }
            } else {
                binding.fab.setImageResource(R.drawable.favorite_border)
                binding.fab.setOnClickListener {
                    saveToFavorite()
                }
            }
        }
    }

    private fun saveToFavorite() {
        val username = intent.getStringExtra("username")
        val avatarUrl = intent.getStringExtra("avatarUrl")

        if (username != null && avatarUrl != null) {
            val favorite = Favorite(username = username, avatarUrl = avatarUrl)
            favoriteAddUpdateViewModel.insert(favorite)

            Toast.makeText(this, "Berhasil menambahkan user ke favorite", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "$username", Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteFromFavorite() {
        val username = intent.getStringExtra("username")
        val avatarUrl = intent.getStringExtra("avatarUrl")

        if (username != null && avatarUrl != null) {
            val favoriteUser = Favorite(username = username, avatarUrl = avatarUrl)
            favoriteAddUpdateViewModel.delete(favoriteUser)

            Toast.makeText(this, "Berhasil menghapus user dari favorite", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, " $username", Toast.LENGTH_SHORT).show()

        }
    }

}