package com.zidan.zidanapp.Activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.zidan.zidanapp.R
import com.zidan.zidanapp.ViewModel.LoginViewModel
import com.zidan.zidanapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var toolbar: Toolbar
    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }
        initViews()
    }

    private fun initViews() {
        // Bottom navigation
        with (binding) {
            val navHostFragment = supportFragmentManager.findFragmentById(mainFragmentContainerView.id) as NavHostFragment
            mainBottomNavigationView.setupWithNavController(navHostFragment.navController)
        }
        // Toolbar
        toolbar = binding.mainToolbar
        setSupportActionBar(toolbar)
    }

    // Options toolbar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_toolbar, menu)
        return true
        //return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.main_toolbar_profile -> Toast.makeText(this, "Ini adalah profil", Toast.LENGTH_SHORT).show()
            R.id.main_toolbar_settings -> Toast.makeText(this, "Ini adalah settings", Toast.LENGTH_SHORT).show()
            R.id.main_toolbar_logout -> {
                lifecycleScope.launch {
                    viewModel.setLoginStatus(false)
                    val intent = Intent(this@MainActivity, AuthActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }
}