package com.example.teammate.ui.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.compose.*
import com.example.teammate.ui.auth.LoginScreen
import com.example.teammate.ui.calendar.CalendarScreen
import com.example.teammate.ui.dashboard.DashboardScreen
import com.example.teammate.ui.onboarding.OnboardingScreen
import com.example.teammate.ui.profile.ProfileScreen
import com.example.teammate.ui.projects.ProjectsScreen
import com.example.teammate.ui.team.TeamScreen
import com.example.teammate.ui.theme.PrimaryGreen

sealed class Screen(val route: String, val title: String = "", val icon: ImageVector? = null) {
    object Onboarding : Screen("onboarding")
    object Login : Screen("login")
    object Dashboard : Screen("dashboard", "Dashboard", Icons.Default.Home)
    object Projects : Screen("projects", "Projects", Icons.Default.Folder)
    object Calendar : Screen("calendar", "Calendar", Icons.Default.CalendarToday)
    object Team : Screen("team", "Team", Icons.Default.Groups)
    object Profile : Screen("profile", "Profile", Icons.Default.Person)
}

// Cinematic 850ms duration for Hero Shared Element animations
const val HeroTransitionDuration = 850

@OptIn(ExperimentalSharedTransitionApi::class)
val HeroBoundsTransform = BoundsTransform { _, _ ->
    tween(
        durationMillis = HeroTransitionDuration,
        easing = FastOutSlowInEasing
    )
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val bottomNavItems = listOf(
        Screen.Dashboard,
        Screen.Projects,
        Screen.Calendar,
        Screen.Team,
        Screen.Profile
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val shouldShowBottomBar = bottomNavItems.any { it.route == currentRoute }

    Scaffold(
        bottomBar = {
            if (shouldShowBottomBar) {
                NavigationBar(containerColor = Color.White) {
                    bottomNavItems.forEach { screen ->
                        NavigationBarItem(
                            icon = {
                                screen.icon?.let {
                                    Icon(it, contentDescription = screen.title)
                                }
                            },
                            label = { Text(screen.title) },
                            selected = currentRoute == screen.route,
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = PrimaryGreen,
                                selectedTextColor = PrimaryGreen,
                                indicatorColor = PrimaryGreen.copy(alpha = 0.12f)
                            ),
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(Screen.Dashboard.route) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        SharedTransitionLayout {
            NavHost(
                navController = navController,
                startDestination = Screen.Onboarding.route,
                modifier = Modifier.padding(if (shouldShowBottomBar) innerPadding else androidx.compose.foundation.layout.PaddingValues()),
                enterTransition = {
                    fadeIn(
                        animationSpec = tween(
                            durationMillis = HeroTransitionDuration,
                            easing = FastOutSlowInEasing
                        )
                    ) + slideInHorizontally(
                        initialOffsetX = { 200 },
                        animationSpec = tween(
                            durationMillis = HeroTransitionDuration,
                            easing = FastOutSlowInEasing
                        )
                    )
                },
                exitTransition = {
                    fadeOut(
                        animationSpec = tween(
                            durationMillis = HeroTransitionDuration,
                            easing = FastOutSlowInEasing
                        )
                    ) + slideOutHorizontally(
                        targetOffsetX = { -200 },
                        animationSpec = tween(
                            durationMillis = HeroTransitionDuration,
                            easing = FastOutSlowInEasing
                        )
                    )
                },
                popEnterTransition = {
                    fadeIn(
                        animationSpec = tween(
                            durationMillis = HeroTransitionDuration,
                            easing = FastOutSlowInEasing
                        )
                    ) + slideInHorizontally(
                        initialOffsetX = { -200 },
                        animationSpec = tween(
                            durationMillis = HeroTransitionDuration,
                            easing = FastOutSlowInEasing
                        )
                    )
                },
                popExitTransition = {
                    fadeOut(
                        animationSpec = tween(
                            durationMillis = HeroTransitionDuration,
                            easing = FastOutSlowInEasing
                        )
                    ) + slideOutHorizontally(
                        targetOffsetX = { 200 },
                        animationSpec = tween(
                            durationMillis = HeroTransitionDuration,
                            easing = FastOutSlowInEasing
                        )
                    )
                }
            ) {
                composable(Screen.Onboarding.route) {
                    OnboardingScreen(
                        onGetStarted = {
                            navController.navigate(Screen.Login.route)
                        },
                        animatedVisibilityScope = this@composable,
                        sharedTransitionScope = this@SharedTransitionLayout
                    )
                }
                composable(Screen.Login.route) {
                    LoginScreen(
                        onNavigateBack = {
                            navController.popBackStack()
                        },
                        onLoginSuccess = {
                            navController.navigate(Screen.Dashboard.route) {
                                popUpTo(Screen.Onboarding.route) { inclusive = true }
                            }
                        },
                        animatedVisibilityScope = this@composable,
                        sharedTransitionScope = this@SharedTransitionLayout
                    )
                }
                composable(Screen.Dashboard.route) { DashboardScreen() }
                composable(Screen.Projects.route) { ProjectsScreen() }
                composable(Screen.Calendar.route) { CalendarScreen() }
                composable(Screen.Team.route) { TeamScreen() }
                composable(Screen.Profile.route) { ProfileScreen() }
            }
        }
    }
}
