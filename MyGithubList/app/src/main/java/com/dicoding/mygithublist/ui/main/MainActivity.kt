package com.dicoding.mygithublist.ui

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.mygithublist.R
import com.dicoding.mygithublist.databinding.ActivityMainBinding
import com.dicoding.mygithublist.helper.SettingPreferences
import com.dicoding.mygithublist.ui.adapter.UserAdapter
import com.dicoding.mygithublist.ui.main.DetailUserActivity
import com.dicoding.mygithublist.ui.main.FavoriteListActivity
import com.dicoding.mygithublist.ui.main.SettingActivity
import com.dicoding.mygithublist.ui.viewmodel.SettingViewModel
import com.dicoding.mygithublist.helper.SettingViewModelFactory
import com.dicoding.mygithublist.helper.dataStore


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val settingViewModel: SettingViewModel by viewModels {
        SettingViewModelFactory(SettingPreferences.getInstance(application.dataStore))
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        settingViewModel.getThemeSettings().observe(this) { isDarkModeActive ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
        val layoutManager = LinearLayoutManager(this)
        binding.homeRecyclerView.layoutManager = layoutManager

        val mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)

        val adapter = UserAdapter { user ->
            val intent = DetailUserActivity.newIntents(this@MainActivity, user.login)
            startActivity(intent)
        }


        binding.homeRecyclerView.adapter = adapter

        mainViewModel.listUser.observe(this) { githubUser ->
            adapter.submitList(githubUser)
        }

        mainViewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }

        mainViewModel.notFound.observe(this) { notFound ->
            if (notFound) {
                Toast.makeText(this@MainActivity, "User Not found", Toast.LENGTH_SHORT).show()
            }
        }

        binding.searchView.editText.setOnEditorActionListener { textView, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val query = textView.text.toString()
                mainViewModel.search(query)
                binding.searchView.hide()
                true
            } else {
                false
            }
        }

        binding.searchView.editText.setOnEditorActionListener { textView, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val query = textView.text.toString()
                mainViewModel.search(query)
                binding.searchView.hide()
                true
            } else {
                false
            }
        }

        binding.searchView.editText.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                val query = binding.searchView.editText.text.toString()
                mainViewModel.search(query)
                binding.searchView.hide()
            }
            false
        }
        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchBar.apply {
                inflateMenu(R.menu.item_menu)
                setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.menu_favorite -> {
                            startActivity(
                                Intent(
                                    this@MainActivity,
                                    FavoriteListActivity::class.java
                                )
                            )
                            true
                        }

                        R.id.menu_settings -> {
                            startActivity(
                                Intent(
                                    this@MainActivity,
                                    SettingActivity::class.java
                                )
                            )
                            true
                        }

                        else -> false
                    }
                }
            }
        }

    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
