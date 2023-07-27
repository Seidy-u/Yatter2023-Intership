package com.dmm.bootcamp.yatter2023.ui.post

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.Surface
import com.dmm.bootcamp.yatter2023.ui.theme.Yatter2023Theme
import org.koin.androidx.viewmodel.ext.android.viewModel

class PostActivity: AppCompatActivity() {
    companion object {
        fun newIntent(context: Context): Intent = Intent(
            context,
            PostActivity::class.java,
        )
    }

    private val viewModel: PostViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Yatter2023Theme {
                Surface {
                    PostPage(viewModel = viewModel)
                }
            }
        }

        viewModel.onCreate()

        viewModel.goBack.observe(this) {
            finish()
        }
    }
}