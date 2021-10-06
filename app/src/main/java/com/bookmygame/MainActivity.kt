package com.bookmygame

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import com.bookmygame.util.SPConstants
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), DrawerLocker,
    NavigationView.OnNavigationItemSelectedListener, FragmentCallbacks {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var toolbar: Toolbar
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var progressLayout: FrameLayout
    private lateinit var navigationView: NavigationView
    private lateinit var navController: NavController
    private lateinit var bottomNavigationView: BottomNavigationView

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar = findViewById(R.id.toolbar)

        setSupportActionBar(toolbar)
        toolbar.title = "test"

        sharedPreferences = this.getSharedPreferences(
            SPConstants.FILE_NAME,
            Context.MODE_PRIVATE
        )

        progressLayout = findViewById(R.id.progress_layout)

        Log.d("Basava", "MainActivity onCreate")

//        val fab: FloatingActionButton = findViewById(R.id.fab)
//        fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show()
//        }
        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)
        navController = findNavController(R.id.nav_host_fragment)

        bottomNavigationView = findViewById(R.id.bottom_navigation_view)
        bottomNavigationView.itemIconTintList = null
        NavigationUI.setupWithNavController(bottomNavigationView, navController)

//        bottomNavigationView.setOnItemSelectedListener { menuItem ->
//            Log.d("Basava", "onOptionsItemSelected: title -> ${menuItem.title}")
//            when (menuItem.itemId) {
//                R.id.home -> {
//
//                }
//                R.id.bookings -> {
//
//                }
//                R.id.profile -> {
//                    navController.navigate(R.id.navigate_to_profile_screen)
//                }
//            }
//            return@setOnItemSelectedListener true
//        }
//
//        bottomNavigationView.setOnItemReselectedListener { menuItem ->
//            Log.d("Basava", "setOnItemReselectedListener: title -> ${menuItem.title}")
//        }

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home_admin,
                R.id.nav_home_ground_owner,
                R.id.nav_home_user,
                R.id.nav_profile,
                R.id.nav_bookings,
                R.id.nav_user_bookings,
                R.id.nav_user_profile,
                R.id.nav_gallery,
                R.id.nav_slideshow
            ), drawerLayout
        )

        navigationView.setNavigationItemSelectedListener(this)

        setupActionBarWithNavController(navController, appBarConfiguration)
        navigationView.setupWithNavController(navController)

//        Handler().postDelayed({
//            Log.d("Basava", "postDelayed")
//            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
//            toolbar.navigationIcon = null
//        }, 1000)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun setDrawerLocked(shouldLock: Boolean) {
        // TODO: 29/08/21 Make it proper with activity create callback
        if (shouldLock) {
           //drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
//            toolbar.navigationIcon = null
        } else {
            //drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        }
    }
    fun openCloseNavigationDrawer(view: View) {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            drawerLayout.openDrawer(GravityCompat.START)
        }
    }
    fun addAdminMenuItems() {
        // TODO: 18/09/21 Inflate menu like bottomNavigationView instead of manually adding
        val menu = navigationView.menu
        menu.clear()

        menu.add("Profile")
        menu.add("Logout")
        navigationView.bringToFront()
        navigationView.refreshDrawableState()
        navigationView.setNavigationItemSelectedListener { item ->
            Log.d("Basava", "onNavigationItemSelected1: title -> ${item.title}")
            when (item.title) {
                "Logout" -> logOut()
            }
            true
        }
    }

    fun addGroundOwnerMenuItems() {
        // TODO: 18/09/21 Inflate menu like bottomNavigationView instead of manually adding
        val menu = navigationView.menu
        menu.clear()

        menu.add("Profile")
       menu.add("Logout")
        navigationView.menu.clear()
        navigationView.inflateMenu(R.menu.ground_owner_drawer)
//        navigationView.bringToFront()
//        navigationView.refreshDrawableState()

        navigationView.setNavigationItemSelectedListener { item ->
            Log.d("Basava", "onNavigationItemSelected1: title -> ${item.title}")

            // This is for maintaining the behavior of the Navigation view
            NavigationUI.onNavDestinationSelected(item, navController)
            // This is for closing the drawer after acting on it
            drawerLayout.closeDrawer(GravityCompat.START)

            when (item.itemId) {
                // Logout
                R.id.nav_login -> {
                    navController.popBackStack(R.id.nav_login, false)
                    hideBottomNavigationBar()
                    logOut()
                }
            }
            Log.d("Basava", "backstack: ${navController.backStack.size}")
            true
        }

        showBottomNavigationBar()
        // Setup menu for Ground Owner. Fragment id in navigation graph is given as id for menu item
        // so navigation will happen automatically when bottom navigation bar item is clicked
        bottomNavigationView.menu.clear()
        bottomNavigationView.inflateMenu(R.menu.menu_bottom_navigation_ground_owner)
    }
    fun addUserMenuItems() {
        // TODO: 18/09/21 Inflate menu like bottomNavigationView instead of manually adding
        val menu = navigationView.menu
        menu.clear()

        menu.add("Profile")
        menu.add("Logout")
        navigationView.menu.clear()
        navigationView.inflateMenu(R.menu.ground_owner_drawer)
//        navigationView.bringToFront()
//      navigationView.refreshDrawableState()

        navigationView.setNavigationItemSelectedListener { item ->
            Log.d("Basava", "onNavigationItemSelected1: title -> ${item.title}")

            // This is for maintaining the behavior of the Navigation view
            NavigationUI.onNavDestinationSelected(item, navController)
            // This is for closing the drawer after acting on it
            drawerLayout.closeDrawer(GravityCompat.START)

            when (item.itemId) {
                // Logout
                R.id.nav_login -> {
                    navController.popBackStack(R.id.nav_login, false)
                    hideBottomNavigationBar()
                    logOut()
                }
            }
            Log.d("Basava", "backstack: ${navController.backStack.size}")
            true
        }

        showBottomNavigationBar()
        // Setup menu for Ground Owner. Fragment id in navigation graph is given as id for menu item
        // so navigation will happen automatically when bottom navigation bar item is clicked
        bottomNavigationView.menu.clear()
        bottomNavigationView.inflateMenu(R.menu.menu_bottom_navigation_user)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        Log.d("Basava", "onNavigationItemSelected: title -> ${item.title}")
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d("Basava", "onOptionsItemSelected: title -> ${item.title}")
        return item.onNavDestinationSelected(navController) ||
                super.onOptionsItemSelected(item)
    }

//    fun showBottomNavigationBar() {
//        bottomNavigationView.visibility = View.VISIBLE
//    }
//
//    fun hideBottomNavigationBar() {
//        bottomNavigationView.visibility = View.GONE
//    }

    fun saveValue(key: String, value: String) {
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getValue(key: String): String? = sharedPreferences.getString(key, null)

    private fun logOut() {
        val editor = sharedPreferences.edit()
        editor.remove(SPConstants.ROLE)
        editor.remove(SPConstants.USED_ID)
        editor.apply()

//        finish()
//        val navController = findNavController(R.id.nav_host_fragment)
//        navController.navigate(R.id.navigate_to_login_screen)
        drawerLayout.close()
    }

    override fun showProgressBar() {
        progressLayout.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        progressLayout.visibility = View.GONE
    }

    override fun showBottomNavigationBar() {
        bottomNavigationView.visibility = View.VISIBLE
    }

    override fun hideBottomNavigationBar() {
        bottomNavigationView.visibility = View.GONE
    }
}

interface DrawerLocker {
    fun setDrawerLocked(shouldLock: Boolean)
}

interface FragmentCallbacks {
    fun showProgressBar()
    fun hideProgressBar()
    fun showBottomNavigationBar()
    fun hideBottomNavigationBar()
}