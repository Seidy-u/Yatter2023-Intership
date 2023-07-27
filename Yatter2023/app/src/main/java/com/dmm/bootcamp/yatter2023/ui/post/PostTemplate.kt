package com.dmm.bootcamp.yatter2023.ui.post

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.dmm.bootcamp.yatter2023.ui.theme.Yatter2023Theme


@Composable
fun PostTemplate(
    postBindingModel: PostBindingModel,
    isLoading: Boolean,
    canPost: Boolean,
    onStatusTextChanged: (String) -> Unit,
    onClickPost: () -> Unit,
    onClickNavIcon: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "投稿")
                },
                navigationIcon = {
                    IconButton(onClick = onClickNavIcon) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "戻る"
                        )
                    }
                }
            )
        }
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                AsyncImage(
                    modifier = Modifier.size(64.dp),
                    model = postBindingModel.avatarUrl,
                    contentDescription = "アバター画像",
                    contentScale = ContentScale.Crop
                )

                Column(
                    horizontalAlignment = Alignment.End,
                ) {
                    TextField(
                        modifier = Modifier
                            .fillMaxWidth() // 横幅最大サイズ確保
                            .weight(1f), // 他のコンポーザブルのサイズを確保した上で最大サイズを取る
                        value = postBindingModel.statusText,
                        onValueChange = onStatusTextChanged,
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                        ), // TextFieldの枠を透明にするための設定
                        placeholder = {
                            Text(text = "今何してる？")
                        },
                    )
                    Button(
                        onClick = onClickPost,
                        modifier = Modifier.padding(16.dp),
                        enabled = canPost,
                    ) {
                        Text(text = "ツイート")
                    }
                }
            }
            if (isLoading) {
                CircularProgressIndicator()
            }
        }
    }
}

@Preview
@Composable
fun PostTemplatePreview() {
    Yatter2023Theme {
        Surface() {
            PostTemplate(
                postBindingModel = PostBindingModel(
                    avatarUrl = "",
                    statusText = "Hello world!",
                ),
                isLoading = false,
                canPost = true,
                onStatusTextChanged = {},
                onClickPost = {},
                onClickNavIcon = {},
            )
        }
    }
}
