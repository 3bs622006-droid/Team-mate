package com.example.teammate.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FolderOpen
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.teammate.state.TeamMateState
import com.example.teammate.ui.theme.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DashboardScreen(
    onSeeAllDeadlinesClick: () -> Unit = {}
) {
    LaunchedEffect(Unit) {
        TeamMateState.startFirestoreSync()
    }

    val currentUser = TeamMateState.currentUser.value
    val firstName = currentUser.name.substringBefore(" ")

    val projects = TeamMateState.projects
    val allTasks = TeamMateState.tasks
    val completedTasksCount = allTasks.count { it.isCompleted }
    val totalTasksCount = allTasks.size
    val completionProgress = if (totalTasksCount > 0) {
        completedTasksCount.toFloat() / totalTasksCount.toFloat()
    } else {
        0f
    }
    val completionPercentage = (completionProgress * 100).toInt()

    val pendingTasks = allTasks.filter { !it.isCompleted }

    val dynamicDeadlines = pendingTasks.mapIndexed { index, task ->
        val project = projects.find { it.id == task.projectId }
        val subtitle = if (project != null) {
            "${project.title} ${project.code}"
        } else {
            "Team Task • ${task.assigneeName}"
        }

        val (indicatorColor, badgeTextColor, badgeBgColor) = when (index % 3) {
            0 -> Triple(StatusWarningText, StatusWarningText, StatusWarningBg)
            1 -> Triple(PrimaryGreen, StatusSuccessText, StatusSuccessBg)
            else -> Triple(StatusInfoText, StatusInfoText, StatusInfoBg)
        }

        DeadlineUiItem(
            title = task.title,
            subtitle = subtitle,
            badgeText = task.dueDate,
            indicatorColor = indicatorColor,
            badgeTextColor = badgeTextColor,
            badgeBgColor = badgeBgColor
        )
    }

    val todayText = SimpleDateFormat("EEEE, dd MMM", Locale.ENGLISH).format(Date())

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundGray),
        contentPadding = PaddingValues(
            start = 20.dp,
            end = 20.dp,
            top = 16.dp,
            bottom = 24.dp
        ),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        item {
            DashboardHeader(
                dateText = todayText,
                userName = firstName
            )
        }

        item {
            OverallCompletionCard(
                percentage = completionPercentage,
                progress = completionProgress,
                summaryText = "$completedTasksCount of $totalTasksCount tasks completed across all projects"
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                DashboardStatCard(
                    icon = Icons.Outlined.FolderOpen,
                    iconTint = PrimaryGreen,
                    iconBgColor = LightGreenBg,
                    count = projects.size.toString(),
                    label = "Active projects",
                    modifier = Modifier.weight(1f)
                )
                DashboardStatCard(
                    icon = Icons.Outlined.Schedule,
                    iconTint = StatusWarningText,
                    iconBgColor = StatusWarningBg,
                    count = pendingTasks.size.toString(),
                    label = "Pending tasks",
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Upcoming deadlines",
                    style = MaterialTheme.typography.titleLarge,
                    color = TextPrimary
                )
                Text(
                    text = "See all",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = PrimaryGreen,
                    modifier = Modifier.clickable { onSeeAllDeadlinesClick() }
                )
            }
        }

        items(dynamicDeadlines) { item ->
            DeadlineItemCard(item = item)
        }
    }
}