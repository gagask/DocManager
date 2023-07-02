package com.example.docmanager

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_AUDIO
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.Manifest.permission.READ_MEDIA_VIDEO
import android.app.ActivityManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.annotation.RequiresApi
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
    private var REQUEST_PERMISSIONS = 32123

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private val PERMISSIONS = arrayOf(READ_MEDIA_AUDIO, READ_MEDIA_VIDEO, READ_MEDIA_IMAGES)
    private val PERMISSIONS_DEPRECATED = arrayOf(READ_EXTERNAL_STORAGE)


    private fun requestNotGrantedPermissions(){
        val permissions = when {
            Build.VERSION.SDK_INT < 33 -> PERMISSIONS_DEPRECATED
            else -> PERMISSIONS
        }.filter {
                checkSelfPermission(it) != PackageManager.PERMISSION_GRANTED
            }
        if (permissions.isNotEmpty())
            requestPermissions(permissions.toTypedArray(), REQUEST_PERMISSIONS)
    }

    private fun arePermissionsGranted(): Boolean {
        when {
            Build.VERSION.SDK_INT < 33 -> PERMISSIONS_DEPRECATED
            else -> PERMISSIONS
        }.forEach {
            if (checkSelfPermission(it) != PackageManager.PERMISSION_GRANTED)
                return false
        }
        return true
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onResume() {
        super.onResume()
        requestNotGrantedPermissions()
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

            val am: ActivityManager =
                applicationContext.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            am.clearApplicationUserData()
            recreate()

            //Toast.makeText(this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
        }
        return
    }
}