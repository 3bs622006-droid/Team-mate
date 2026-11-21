package com.example.teammate.ui.projects

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.teammate.ui.theme.*

@Composable
fun ProjectDetailsScreen(
    onBackClick: () -> Unit = {},
    onEditClick: () -> Unit = {}
) {
    val sampleTasks = listOf(
        ProjectTaskUiItem(
            title = "Market research summary",
            subtitle = "Done",
            isCompleted = true,
            showStatusBadge = true,
            assigneeInitials = "AM",
            assigneeBgColor = DarkGreen
        ),
        ProjectTaskUiItem(
            title = "Work on slides to present",
            subtitle = "In progress",
            isCompleted = false,
            showStatusBadge = false,
            assigneeInitials = "SH",
            assigneeBgColor = PrimaryGreen
        ),
        ProjectTaskUiItem(
            title = "Brainstorm ideas",
            subtitle = null,
            isCompleted = false,
            showStatusBadge = false,
            assigneeInitials = "OK",
            assigneeBgColor = StatusWarningText
        )
    )

    val sampleResources = listOf(
        ProjectResourceUiItem(
            title = "Course Brief & Guidelines",
            typeLabel = "PDF"
        ),
        ProjectResourceUiItem(
            title = "Shared Presentation Slides",
            typeLabel = "Google Slides"
        )
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundGray),
        contentPadding = PaddingValues(
            start = 20.dp,
            end = 20.dp,
            top = 14.dp,
            bottom = 24.dp
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            ProjectDetailsTopBar(
                onBackClick = onBackClick,
                onEditClick = onEditClick
            )
        }

        item {
            ProjectHeaderBanner(
                title = "Marketing",
                code = "MN406L",
                statusText = "On Track",
                deadlineDate = "25 April 2026",
                remainingText = "in 1 month"
            )
        }

        item {
            ProjectProgressCard(
                percentage = 33,
                progress = 0.33f,
                summaryText = "1 of 5 tasks completed • updates automatically"
            )
        }

        item {
            ProjectTasksCard(
                tasks = sampleTasks
            )
        }

        item {
            ProjectResourcesCard(
                resources = sampleResources
            )
        }
    }
}