package com.example.teammate.ui.projects

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.teammate.ui.theme.*

@Composable
fun ProjectsScreen() {
    var selectedProjectId by remember { mutableStateOf<String?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    if (selectedProjectId != null) {
        ProjectDetailsScreen(
            onBackClick = { selectedProjectId = null }
        )
        return
    }

    val sampleProjects = listOf(
        ProjectUiItem(
            id = "1",
            title = "Marketing",
            code = "MN406L",
            statusText = "On Track",
            statusTextColor = StatusSuccessText,
            statusBgColor = StatusSuccessBg,
            progress = 0.33f,
            percentage = 33,
            deadlineText = "25 April 2026",
            memberInitials = listOf("AM", "SH", "OK")
        ),
        ProjectUiItem(
            id = "2",
            title = "Statistics",
            code = "ST210",
            statusText = "In Progress",
            statusTextColor = StatusWarningText,
            statusBgColor = StatusWarningBg,
            progress = 0.65f,
            percentage = 65,
            deadlineText = "10 May 2026",
            memberInitials = listOf("AM", "HA")
        ),
        ProjectUiItem(
            id = "3",
            title = "Business",
            code = "B6301",
            statusText = "Needs Review",
            statusTextColor = StatusErrorText,
            statusBgColor = StatusErrorBg,
            progress = 0.85f,
            percentage = 85,
            deadlineText = "02 May 2026",
            memberInitials = listOf("SH", "OK", "HA", "AM")
        )
    )

    val filteredProjects = sampleProjects.filter {
        it.title.contains(searchQuery, ignoreCase = true) ||
                it.code.contains(searchQuery, ignoreCase = true)
    }

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
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            ProjectsHeader(onAddProjectClick = {})
        }

        item {
            ProjectsSearchBar(
                query = searchQuery,
                onQueryChange = { searchQuery = it }
            )
        }

        items(filteredProjects) { project ->
            ProjectListCard(
                project = project,
                onClick = { selectedProjectId = project.id }
            )
        }
    }
}