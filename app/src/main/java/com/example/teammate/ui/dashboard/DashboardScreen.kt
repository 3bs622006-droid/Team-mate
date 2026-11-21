
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.teammate.ui.theme.*

@Composable
fun DashboardScreen(
    onSeeAllDeadlinesClick: () -> Unit = {}
) {
    val sampleDeadlines = listOf(
        DeadlineUiItem(
            title = "Work on slides",
            subtitle = "Marketing MN406L",
            badgeText = "Tomorrow",
            indicatorColor = StatusWarningText,
            badgeTextColor = StatusWarningText,
            badgeBgColor = StatusWarningBg
        ),
        DeadlineUiItem(
            title = "Survey analysis",
            subtitle = "Statistics ST210",
            badgeText = "2 days left",
            indicatorColor = PrimaryGreen,
            badgeTextColor = StatusSuccessText,
            badgeBgColor = StatusSuccessBg
        ),
        DeadlineUiItem(
            title = "Final report draft",
            subtitle = "Business B6301",
            badgeText = "Overdue",
            indicatorColor = StatusErrorText,
            badgeTextColor = StatusErrorText,
            badgeBgColor = StatusErrorBg
        ),
        DeadlineUiItem(
            title = "UI Wireframes review",
            subtitle = "Software Engineering",
            badgeText = "In 4 days",
            indicatorColor = StatusInfoText,
            badgeTextColor = StatusInfoText,
            badgeBgColor = StatusInfoBg
        )
    )

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
                dateText = "Thursday, 23 Oct",
                userName = "Abdelrahman"
            )
        }

        item {
            OverallCompletionCard(
                percentage = 68,
                progress = 0.68f,
                summaryText = "17 of 25 tasks completed across all projects"
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
                    count = "3",
                    label = "Active projects",
                    modifier = Modifier.weight(1f)
                )
                DashboardStatCard(
                    icon = Icons.Outlined.Schedule,
                    iconTint = StatusWarningText,
                    iconBgColor = StatusWarningBg,
                    count = "5",
                    label = "Due this week",
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

        items(sampleDeadlines) { item ->
            DeadlineItemCard(item = item)
        }
    }
}