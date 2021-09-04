package com.ps.omarmattr.abohesham.client.ui

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.firebase.messaging.FirebaseMessaging
import com.ps.omarmattr.aboheshamclient.R
import com.ps.omarmattr.abohesham.client.model.Product
import com.ps.omarmattr.abohesham.client.other.PRODUCT_TYPE
import com.ps.omarmattr.abohesham.client.other.setLanguage
import com.ps.omarmattr.aboheshamclient.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


    private lateinit var mBinding: ActivityMainBinding
    private lateinit var navHostFragment: Fragment

    private var product: Product? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        window.decorView.layoutDirection = View.LAYOUT_DIRECTION_RTL

        setContentView(mBinding.root)
        val extras = intent.extras

        FirebaseMessaging.getInstance().subscribeToTopic("foo-bar")

        setLanguage("ar")

        extras?.let {
            product = it.getParcelable(PRODUCT_TYPE)
        }
//
//        val fragment = SplashFragment()
//        val bundle = Bundle().apply {
//            putParcelable(PRODUCT_TYPE, product)
//        }
//        fragment.arguments = bundle
//        supportFragmentManager.beginTransaction()
//            .replace(R.id.container, fragment).commit()

        navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentNavHostHome)!!

        val navController = navHostFragment.findNavController()

        NavigationUI.setupWithNavController(
            mBinding.bottomNavigation,
            navController
        )
        var id = 0
        mBinding.bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigationHome -> {
                    if (item.itemId != id)
                        navController.navigate(R.id.navigationHome, null, getNavOptions())

                }
                R.id.navigationTicket -> {
                    if (item.itemId != id)
                        navController.navigate(R.id.navigationTicket, null, getNavOptions())
                }
                R.id.navigationFavourite -> {
                    if (item.itemId != id)
                        navController.navigate(R.id.navigationFavourite, null, getNavOptions())
                }

            }
            id = item.itemId
            true
        }


        navHostFragment.findNavController()
            .addOnDestinationChangedListener { _: NavController?, destination: NavDestination, _: Bundle? ->
                when (destination.id) {
                    R.id.splashFragment -> {
                        window.apply {
                            addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
                        }

                    }
                    R.id.navigationHome, R.id.navigationFavourite, R.id.navigationTicket -> {
                        window.apply {
                            clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
                        }
                        mBinding.bottomNavigation.visibility = View.VISIBLE
                    }
                    else -> {
                        window.apply {
                            clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
                        }
                        mBinding.bottomNavigation.visibility = View.GONE
                    }
                }
            }
    }

    private fun getNavOptions(): NavOptions {
        return NavOptions.Builder()
            .setEnterAnim(R.anim.slide_in_right)
            .setExitAnim(R.anim.slide_out_left)
            .setPopEnterAnim(R.anim.slide_in_left)
            .setPopExitAnim(R.anim.slide_out_right)
            .build()
    }

}
