package com.dmm.bootcamp.yatter2023.ui.timeline

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.dmm.bootcamp.yatter2023.ui.theme.Yatter2023Theme
import com.dmm.bootcamp.yatter2023.ui.timeline.bindingmodel.StatusBindingModel

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PublicTimelineTemplate(
    statusList: List<StatusBindingModel>,
    isLoading: Boolean,
    isRefreshing: Boolean,
    onClickPost: () -> Unit,
    onRefresh: () -> Unit,
    ) {

    val pullRefreshState = rememberPullRefreshState(isRefreshing, onRefresh)

    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    val openDialog = remember { mutableStateOf(true) }

    val bottomNavItems = listOf(
        BottomNavItem(
            name = "Timeline",
//            route = "home",
            icon = Icons.Rounded.Home,
        ),
        BottomNavItem(
            name = "Post",
//            route = "add",
            icon = Icons.Rounded.AddCircle,
        ),
        BottomNavItem(
            name = "Chat",
//            route = "chat",
            icon = Icons.Rounded.AddCircle,
        ),
        BottomNavItem(
            name = "Logout",
//            route = "logout",
            icon = Icons.Rounded.ArrowBack,
        ),
    )


    Scaffold(
//        floatingActionButton = {
//            FloatingActionButton(onClick = onClickPost) {
//                Icon(
//                    imageVector = Icons.Default.Add,
//                    contentDescription = "post"
//                )
//            }
//        },
        scaffoldState = scaffoldState,

        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Timeline")
                },
                backgroundColor = Color.Blue,
                contentColor = Color.White,
                navigationIcon = {
                    IconButton(onClick = {
                        coroutineScope.launch { scaffoldState.drawerState.open() }
                    }) {
                        Icon(Icons.Filled.Menu, contentDescription = "Open drawer")
                    }
                },
            )
        },
        drawerContent = {
            bottomNavItems.forEach { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                        .clickable {
                            if (item.name == "Post") {
                                onClickPost()
                            }
                            if (item.name == "Chat") {
                                //TODO : chat機能を追加する
                            }

                            if (item.name == "Logout") {
                                //TODO : logout機能を追加する
                            }
                            coroutineScope.launch {
                                scaffoldState.drawerState.close()
                            }
                        },
                    elevation = 0.dp,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start,
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = "${item.name} Icon"
                        )

                        Text(
                            modifier = Modifier
                                .padding(start = 24.dp),
                            text = item.name,
                        )
                    }
                }
            }
        },
        drawerGesturesEnabled = true,

    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .pullRefresh(pullRefreshState),
            contentAlignment = Alignment.Center,
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(8.dp),
            )
            {
                items(statusList) { item ->
                    StatusRow(statusBindingModel = item)
                }
            }
            PullRefreshIndicator(
                isRefreshing,
                pullRefreshState,
                Modifier.align(Alignment.TopCenter)
            )
            if (isLoading) {
                CircularProgressIndicator()
            }
        }
    }
}

@Preview
@Composable
private fun PublicTimelineTemplatePreview() {
    Yatter2023Theme {
        Surface {
            PublicTimelineTemplate(
                statusList = listOf(
                    StatusBindingModel(
                        id = "id",
                        displayName = "display name",
                        username = "username",
                        avatar = null,
                        content = "preview content",
                        attachmentMediaList = listOf()
                    )
                ),
                isLoading = true,
                isRefreshing = false,
                onClickPost = {},
                onRefresh = {},
            )
        }
    }
}