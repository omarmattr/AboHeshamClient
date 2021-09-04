package com.ps.omarmattr.abohesham.client.other

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.net.ConnectivityManager
import android.os.Build
import androidx.core.view.isVisible
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.ps.omarmattr.abohesham.client.ui.MainActivity
import com.ps.omarmattr.aboheshamclient.databinding.ToolbarLayoutBinding
import java.util.*


fun Activity.setToolbarView(
    view: ToolbarLayoutBinding,
    title: String,
    main: Boolean,
    hasMenu:Boolean = false,
    menuIcon:Int = 0,
    onComplete: (type:Int) -> Unit
): ToolbarLayoutBinding {

    view.tvTitle.text = title

    when (hasMenu) {
        true -> {
            view.apply {
                btnCategory.isVisible = true
                btnCategory.setOnClickListener {
                    onComplete(TWO)
                }

            }
        }
        else->{
            view.btnCategory.isVisible = false
        }
    }

    when (main) {
        true -> {
            view.btnBack.isVisible = false
        }
        else->{
            view.btnBack.apply {
                isVisible = true
                setOnClickListener {
                    onComplete(ONE)
                }
            }
        }
    }
    return view
}





fun Activity.setLanguage(lan: String) {
    val res = resources
    val dr = res.displayMetrics
    val cr = res.configuration
    cr.setLocale(Locale(lan))
    res.updateConfiguration(cr, dr)
}

fun getDefaultLang(onComplete: (String) -> Unit){
    val locale: Locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Resources.getSystem().configuration.locales.get(0)
    } else {
        Resources.getSystem().configuration.locale
    }
    val lang  = locale.displayLanguage
    if (lang == "English"){
        onComplete("en")
    }else{
        onComplete("ar")
    }
}

fun Activity.getPermission(
    permissions: ArrayList<String>,
    onSuccess: () -> Unit,
    onDenied: () -> Unit
) {
    Dexter.withContext(this)
        .withPermissions(
            permissions
        )
        .withListener(object : MultiplePermissionsListener {
            override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                report?.let {
                    when {
                        report.areAllPermissionsGranted() -> {
                            onSuccess()
                        }
                        else -> {
                            onDenied()
                        }
                    }
                }
            }

            override fun onPermissionRationaleShouldBeShown(
                p0: MutableList<com.karumi.dexter.listener.PermissionRequest>?,
                p1: PermissionToken?
            ) {
                p1?.continuePermissionRequest()
            }

        })
        .withErrorListener {}
        .check()
}

fun Activity.restartActivity(){
    finish()
    startActivity(
        Intent(
            this,
            MainActivity::class.java
        )
    )
    overridePendingTransition(
        android.R.anim.fade_in,
        android.R.anim.fade_out
    )
}

fun Activity.checkConnection(onComplete: (status:Boolean)->Unit){
    val manager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkActive =manager.activeNetworkInfo

    when {
        networkActive != null -> {
            if (networkActive.type == ConnectivityManager.TYPE_WIFI || networkActive.type == ConnectivityManager.TYPE_MOBILE){
                onComplete(true)
            }
        }
        else -> {
            onComplete(false)
        }
    }

}



