package com.example.teammate.ui.auth

import androidx.compose.runtime.Composable

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
