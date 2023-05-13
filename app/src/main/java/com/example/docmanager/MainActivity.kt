package com.example.docmanager

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.app.ActivityManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
import com.example.docmanager.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


    //Permissions setup
    private var REQUEST_PERMISSIONS = 12
    private val PERMISSIONS = arrayOf(READ_EXTERNAL_STORAGE)
    private var PERMISSIONS_COUNT = 1

    private fun arePermissionsGranted(): Boolean {

        var p: Int = 0
        while (p < PERMISSIONS_COUNT) {
            if (checkSelfPermission(PERMISSIONS[p]) != PackageManager.PERMISSION_GRANTED){
                return false
            }
            p+=1
        }
        return true
    }

    override fun onResume() {
        super.onResume()
        if (!arePermissionsGranted() && Build.VERSION.SDK_INT < 33) {
            requestPermissions(PERMISSIONS, REQUEST_PERMISSIONS)
            //Toast.makeText(this, "Get permission try", Toast.LENGTH_SHORT).show();
            return
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode != REQUEST_PERMISSIONS) return

        // If request is cancelled, the result arrays are empty.
        if (grantResults.isNotEmpty() && arePermissionsGranted()) {
            // permission was granted, yay! Do the
            // contacts-related task you need to do.
            //Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
        } else {

            val am:ActivityManager = applicationContext.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            am.clearApplicationUserData()
            recreate()

            //Toast.makeText(this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
        }
        return
    }
}