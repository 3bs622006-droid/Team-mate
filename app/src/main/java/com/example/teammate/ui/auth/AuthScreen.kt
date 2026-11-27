package com.example.teammate.ui.auth

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.runtime.Composable

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun AuthScreen(
    onNavigateBack: () -> Unit = {},
    onLoginSuccess: () -> Unit = {}
) {
    LoginScreen(
        onNavigateBack = onNavigateBack,
        onLoginSuccess = onLoginSuccess
    )
}
