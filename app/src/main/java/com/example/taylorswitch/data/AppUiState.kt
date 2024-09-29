package com.example.taylorswitch.data

import androidx.compose.ui.graphics.vector.ImageVector

data class TabBarItem(
    val title: String,
    val path: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val badgeAmount: Int? = null
)

data class TopBarItem(
    val canNavigateBack: Boolean,
     val title: String,

)

data class MiniFabItems(
    val icon: ImageVector,
    val title: String,
    val route: String
)

data class AppUiState(
    val uid: String = "",
    val username: String = ""
)