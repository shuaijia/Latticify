package io.github.tonnyl.latticify.ui.auth

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.github.tonnyl.latticify.R

/**
 * Created by lizhaotailang on 19/09/2017.
 */
class AuthActivity : AppCompatActivity() {

    private lateinit var mAuthFragment: AuthFragment

    companion object {
        @JvmField
        val ACTION_FROM_MAIN = "ACTION_FROM_MAIN"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_container)

        mAuthFragment = fragmentManager.findFragmentByTag(AuthFragment::class.java.simpleName)
                as AuthFragment? ?: AuthFragment.newInstance()

        supportFragmentManager.beginTransaction()
                .add(R.id.container, mAuthFragment, AuthFragment::class.java.simpleName)
                .commit()

        AuthPresenter(mAuthFragment)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        mAuthFragment.handleAuthCallback(intent)
    }

}