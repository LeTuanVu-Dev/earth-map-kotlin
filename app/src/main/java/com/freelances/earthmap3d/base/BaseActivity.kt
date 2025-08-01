package com.freelances.earthmap3d.base

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Pair
import android.view.LayoutInflater
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.freelances.earthmap3d.R
import com.freelances.earthmap3d.extensions.PreferenceHelper
import com.freelances.earthmap3d.extensions.utils.Language
import com.freelances.earthmap3d.extensions.utils.hideSystemBar
import com.freelances.earthmap3d.extensions.utils.setFullScreen
import org.koin.android.ext.android.inject

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {
    protected val preferenceHelper: PreferenceHelper by inject()
    protected lateinit var binding: VB
        private set

    protected abstract fun inflateBinding(layoutInflater: LayoutInflater): VB
    abstract fun updateUI(savedInstanceState: Bundle?)
    open fun isDisplayCutout(): Boolean = true

    open fun loadAd() {}

    private val activityResultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        handleActivityResult(result)
    }
    open fun handleActivityResult(result: ActivityResult) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = inflateBinding(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val insetTypes =
                if (!isDisplayCutout()) WindowInsetsCompat.Type.displayCutout() or WindowInsetsCompat.Type.systemBars()
                else WindowInsetsCompat.Type.systemBars()
            val systemBars = insets.getInsets(insetTypes)
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        window.setFullScreen()
        window.hideSystemBar()
        updateUI(savedInstanceState)
        loadAd()
    }

    override fun attachBaseContext(newBase: Context?) {
        if (this.javaClass.simpleName == "Language1Activity" || this.javaClass.simpleName == "Language2Activity") {
            super.attachBaseContext(newBase)
        } else {
            newBase?.let {
                super.attachBaseContext(
                    Language.changeLanguage(newBase, preferenceHelper.languageSelected)
                )
            } ?: run { super.attachBaseContext(newBase) }
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        window.hideSystemBar()
    }

    open fun addFragment(
        fragment: Fragment,
        viewId: Int = android.R.id.content,
        addToBackStack: Boolean = true
    ) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(
            R.anim.slide_in_right,
            R.anim.slide_out_left,
        )
        transaction.add(viewId, fragment, fragment.javaClass.simpleName)
        if (addToBackStack) {
            transaction.addToBackStack(fragment.javaClass.simpleName)
        }
        // check add crash
        // transaction.commitNow()
        if (!supportFragmentManager.isStateSaved) {
            transaction.commit()
        }

    }

    fun setViewInsets(view: View, isDisplayCutout: Boolean = false) {
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val insetTypes =
                if (!isDisplayCutout) WindowInsetsCompat.Type.displayCutout() or WindowInsetsCompat.Type.systemBars()
                else WindowInsetsCompat.Type.systemBars()
            val systemBars = insets.getInsets(insetTypes)
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun navigateTo(
        clazz: Class<out AppCompatActivity>,
        bundle: Bundle? = null,
        navigationAnimation: Pair<Int, Int>? = Pair(R.anim.slide_in_right, R.anim.slide_out_left),
        isFinish: Boolean = false,
    ) {
        val intent = Intent(this, clazz)
        bundle?.let { intent.putExtras(it) }


        startActivity(
            intent,
            ActivityOptions.makeCustomAnimation(
                this,
                navigationAnimation?.first ?: 0,
                navigationAnimation?.second ?: 0
            ).toBundle()
        )

        if (isFinish) finish()
    }

    private fun navigateTo(
        clazz: Class<out AppCompatActivity>,
        bundle: Bundle? = null,
        navigationAnimation: Pair<Int, Int>? = Pair(R.anim.slide_in_right, R.anim.slide_out_left),
        intentFlag: NavigationFlag?
    ) {
        val intent = Intent(this, clazz)

        intentFlag?.let {
            when (it) {
                NavigationFlag.ClearTop -> intent.flags =
                    Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK

                NavigationFlag.ClearTask -> intent.flags =
                    Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK

                NavigationFlag.NewTask -> intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
        }

        bundle?.let { intent.putExtras(it) }


        startActivity(
            intent,
            ActivityOptions.makeCustomAnimation(
                this,
                navigationAnimation?.first ?: 0,
                navigationAnimation?.second ?: 0
            ).toBundle()
        )

    }

    fun navigateThenClearTask(
        clazz: Class<out AppCompatActivity>,
        bundle: Bundle? = null,
        navigationAnimation: Pair<Int, Int>? = Pair(R.anim.slide_in_right, R.anim.slide_out_left),
    ) {
        navigateTo(
            clazz,
            bundle,
            navigationAnimation,
            intentFlag = NavigationFlag.ClearTask
        )
    }

    fun navigateThenClearTop(
        clazz: Class<out AppCompatActivity>,
        bundle: Bundle? = null,
        navigationAnimation: Pair<Int, Int>? = Pair(R.anim.slide_in_right, R.anim.slide_out_left),

        ) {
        navigateTo(
            clazz,
            bundle,
            navigationAnimation,
            intentFlag = NavigationFlag.ClearTop
        )
    }

    fun navigateWithNewTask(
        clazz: Class<out AppCompatActivity>,
        bundle: Bundle? = null,
        navigationAnimation: Pair<Int, Int>? = Pair(R.anim.slide_in_right, R.anim.slide_out_left),
    ) {
        navigateTo(
            clazz,
            bundle,
            navigationAnimation,
            intentFlag = NavigationFlag.NewTask
        )
    }

    fun navigateBack(
        navigationAnimation: Pair<Int, Int> = Pair(R.anim.slide_in_right, R.anim.slide_out_left),
    ) {
        finish()

        overridePendingTransition(
            navigationAnimation.first,
            navigationAnimation.second
        )
    }

    fun navigateThenClearTask(
        clazz: Class<out BaseActivity<*>>,
        bundle: Bundle? = null,
        onCallback: (Intent) -> Unit = {}
    ) {
        val intent = Intent(this, clazz).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        bundle?.let { intent.putExtras(it) }
        onCallback.invoke(intent)
        startActivity(intent)
    }

    fun navigateForResult(
        clazz: Class<out AppCompatActivity>,
        bundle: Bundle? = null,
    ) {
        val intent = Intent(this, clazz)
        bundle?.let {
            intent.putExtras(bundle)
        }
        activityResultLauncher.launch(intent)
    }

    fun backFragment() {
        supportFragmentManager.popBackStack()
    }
}
