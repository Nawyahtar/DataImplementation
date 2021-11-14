package com.example.dataimplementation

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration

import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.preference.PreferenceManager
import com.example.dataimplementation.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {
    private lateinit var dataBinding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        drawerLayout = dataBinding.layoutDrawer

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.myNavHostFragment) as NavHostFragment
        val navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        setupWithNavController(dataBinding.navView, navController)

        navController.addOnDestinationChangedListener { nc: NavController, nd: NavDestination, args: Bundle? ->
            if (nd.id == nc.graph.startDestination) {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            } else {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }

        }

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val email = sharedPreferences.getString("email", "")
        if (email != null) {
            setUserEmail(email)
        }

        dataBinding.navView.setNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = true
            when (menuItem.itemId) {
                R.id.loginFragment -> {
                    FirebaseAuth.getInstance().signOut()
                    sharedPreferences.edit().remove("email").apply()
                    setUserEmail("")
                    findNavController(R.id.myNavHostFragment).navigate(R.id.action_titleFragment_to_loginFragment)
                    drawerLayout.close()
                }
            }
            true
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }



    fun setUserEmail(email: String) {
        dataBinding.navView.menu.findItem(R.id.email).setTitle(email)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.myNavHostFragment)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }


}