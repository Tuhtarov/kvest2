package com.example.kvest2.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.kvest2.R
import com.example.kvest2.data.db.QuestDatabase
import com.example.kvest2.data.model.AppUserSingleton
import com.example.kvest2.data.repository.UserRepository
import com.example.kvest2.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userDao = QuestDatabase.getDatabase(applicationContext).userDao()
        val userRepository = UserRepository(userDao)
        val currentUser = AppUserSingleton.getUser()!!

        val header = binding.navView.getHeaderView(0)

        header.findViewById<TextView>(R.id.navHeaderTitle).text = currentUser.name

        header.findViewById<TextView>(R.id.exitLink).setOnClickListener {
            GlobalScope.launch(Dispatchers.Default) {
                userRepository.signOutUser(currentUser)
                AppUserSingleton.clearUser()

                // переходим на страницу авторизации приложения
                val intent = Intent(applicationContext, LoginActivity::class.java)
                startActivity(intent)
            }
        }

        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_events, R.id.nav_map
            ), drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}