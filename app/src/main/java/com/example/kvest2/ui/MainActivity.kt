package com.example.kvest2.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.kvest2.R
import com.example.kvest2.data.db.QuestDatabase
import com.example.kvest2.data.repository.UserRepository
import com.example.kvest2.databinding.ActivityMainBinding
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    val viewModel: MainActivityViewModel by viewModels {
        MainActivityViewModelFactory(context = binding.root.context)
    }

    private val REQUEST_PERMISSIONS = 10001

    override fun onCreate(savedInstanceState: Bundle?) {
        checkPermissions()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initHeader()

        val drawerLayout = binding.drawerLayout
        val navView = binding.navView

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment

        val navController = navHostFragment.navController

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_events, R.id.nav_map
            ), drawerLayout
        )

        setSupportActionBar(binding.appBarMain.toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }


    private fun initHeader() {
        val header = binding.navView.getHeaderView(0)
        val headerTitle = header.findViewById<TextView>(R.id.navHeaderTitle)
        val button = header.findViewById<TextView>(R.id.exitLink)

        val userDao = QuestDatabase.getDatabase(this).userDao()
        val userRepository = UserRepository(userDao)

        button.setOnClickListener {
            lifecycleScope.launch(Dispatchers.Default) {
                userRepository.signOutUser()
                val intent = Intent(applicationContext, LoginActivity::class.java)
                startActivity(intent)
            }
        }

        viewModel.loggedUser.observe(this) {
            if (it != null) {
                headerTitle.text = it.name
            }
        }
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

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        )
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                REQUEST_PERMISSIONS
            )
    }

    override fun onMapReady(googleMap: GoogleMap) {
        TODO("Not yet implemented")
        googleMap.addMarker(MarkerOptions()
            .position(LatLng(0.0,0.0))
            .title("Marker"))
    }

}

