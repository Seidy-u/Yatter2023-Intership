package com.dmm.bootcamp.yatter2023

import android.os.Bundle
import android.view.View
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import com.dmm.bootcamp.yatter2023.ui.login.LoginActivity
import com.dmm.bootcamp.yatter2023.ui.theme.Yatter2023Theme
import com.dmm.bootcamp.yatter2023.ui.timeline.PublicTimelineActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Yatter2023Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background,
                ) {
                }
            }
        }

        viewModel.onCreate()

        viewModel.navigateToPublicTimeline.observe(this) {
            startActivity(PublicTimelineActivity.newIntent(this))
            finish()
        }

        viewModel.navigateToLogin.observe(this) {
            startActivity(LoginActivity.newIntent(this))
            finish()
        }

        val content: View = findViewById(android.R.id.content)
        content.viewTreeObserver.addOnPreDrawListener { // Check if the initial data is ready.
            false
        }
    }
}

