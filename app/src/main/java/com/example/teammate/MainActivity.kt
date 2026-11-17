package com.example.teammate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.teammate.ui.navigation.AppNavigation
import com.example.teammate.ui.theme.TeamMateTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TeamMateTheme {
                AppNavigation()
            }
        }
    }
}