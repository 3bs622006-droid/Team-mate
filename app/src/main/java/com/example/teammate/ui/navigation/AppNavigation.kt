package com.example.teammate.ui.navigation
import com.example.teammate.ui.team.TeamScreen
import com.example.teammate.ui.calendar.CalendarScreen
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
import com.example.teammate.ui.components.PlaceholderScreen
import com.example.teammate.ui.dashboard.DashboardScreen
import com.example.teammate.ui.projects.ProjectDetailsScreen
import com.example.teammate.ui.profile.ProfileScreen
import com.example.teammate.ui.projects.ProjectsScreen
import com.example.teammate.ui.theme.PrimaryGreen

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Dashboard : Screen("dashboard", "Dashboard", Icons.Default.Home)
    object Projects : Screen("projects", "Projects", Icons.Default.Folder)
    object Calendar : Screen("calendar", "Calendar", Icons.Default.CalendarToday)
    object Team : Screen("team", "Team", Icons.Default.Groups)
    object Profile : Screen("profile", "Profile", Icons.Default.Person)
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val items = listOf(Screen.Dashboard, Screen.Projects, Screen.Calendar, Screen.Team, Screen.Profile)
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                items.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.title) },
                        label = { Text(screen.title) },
                        selected = currentRoute == screen.route,
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = PrimaryGreen,
                            selectedTextColor = PrimaryGreen,
                            indicatorColor = PrimaryGreen.copy(alpha = 0.12f)
                        ),
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Dashboard.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Dashboard.route) { DashboardScreen()}
            composable(Screen.Projects.route) { ProjectsScreen() }
            composable(Screen.Calendar.route) { CalendarScreen() }
            composable(Screen.Team.route) { TeamScreen() }
            composable(Screen.Profile.route) { ProfileScreen () }
        }
    }
}