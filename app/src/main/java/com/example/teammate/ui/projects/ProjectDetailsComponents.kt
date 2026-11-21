package com.example.teammate.ui.projects

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBackIos
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.teammate.ui.theme.*

data class ProjectTaskUiItem(
    val title: String,
    val subtitle: String? = null,
    val isCompleted: Boolean,
    val showStatusBadge: Boolean = false,
    val assigneeInitials: String,
    val assigneeBgColor: Color
)

data class ProjectResourceUiItem(
    val title: String,
    val typeLabel: String
)

@Composable
fun ProjectDetailsTopBar(
    onBackClick: () -> Unit,
    onEditClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .clickable { onBackClick() }
                .padding(vertical = 4.dp, horizontal = 2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowBackIos,
                contentDescription = "Back",
                tint = PrimaryGreen,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(2.dp))
            Text(
                text = "Back",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                color = PrimaryGreen
            )
        }

        Text(
            text = "Edit",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
            color = PrimaryGreen,
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .clickable { onEditClick() }
                .padding(vertical = 4.dp, horizontal = 6.dp)
        )
    }
}

@Composable
fun ProjectHeaderBanner(
    title: String,
    code: String,
    statusText: String,
    deadlineDate: String,
    remainingText: String
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = LightGreenBg,
        border = BorderStroke(1.dp, PrimaryGreen.copy(alpha = 0.25f))
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineMedium,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = code,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = PrimaryGreen
                    )
                }

                Surface(
                    shape = CircleShape,
                    color = CardWhite,
                    border = BorderStroke(1.dp, BorderLight)
                ) {
                    Text(
                        text = statusText,
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = PrimaryGreen,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.Schedule,
                        contentDescription = null,
                        tint = PrimaryGreen,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "Deadline",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary
                        )
                        Text(
                            text = deadlineDate,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = TextPrimary
                        )
                    }
                }

                Text(
                    text = remainingText,
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = PrimaryGreen
                )
            }
        }
    }
}

@Composable
fun ProjectProgressCard(
    percentage: Int,
    progress: Float,
    summaryText: String
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = CardWhite,
        border = BorderStroke(1.dp, BorderLight)
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Progress",
                    style = MaterialTheme.typography.titleLarge,
                    color = TextPrimary
                )
                Text(
                    text = "$percentage%",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryGreen
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(CircleShape),
                color = PrimaryGreen,
                trackColor = SurfaceVariantLight
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = summaryText,
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )
        }
    }
}

@Composable
fun ProjectTasksCard(
    tasks: List<ProjectTaskUiItem>,
    onAddTaskClick: () -> Unit = {}
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = CardWhite,
        border = BorderStroke(1.dp, BorderLight)
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Tasks",
                    style = MaterialTheme.typography.titleLarge,
                    color = TextPrimary
                )

                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(PrimaryGreen)
                        .clickable { onAddTaskClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Add,
                        contentDescription = "Add Task",
                        tint = TextOnPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            tasks.forEachIndexed { index, task ->
                ProjectTaskRow(task = task)
                if (index < tasks.lastIndex) {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 12.dp),
                        thickness = 1.dp,
                        color = DividerGray
                    )
                }
            }
        }
    }
}

@Composable
private fun ProjectTaskRow(
    task: ProjectTaskUiItem
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(22.dp)
                .clip(CircleShape)
                .background(if (task.isCompleted) LightGreenBg else Color.Transparent)
                .border(
                    width = 1.5.dp,
                    color = if (task.isCompleted) PrimaryGreen else TextHint,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            if (task.isCompleted) {
                Icon(
                    imageVector = Icons.Rounded.Check,
                    contentDescription = null,
                    tint = PrimaryGreen,
                    modifier = Modifier.size(14.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = task.title,
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary
            )

            if (task.subtitle != null) {
                Spacer(modifier = Modifier.height(4.dp))
                if (task.showStatusBadge) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = StatusSuccessBg
                    ) {
                        Text(
                            text = task.subtitle,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                            color = StatusSuccessText,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                } else {
                    Text(
                        text = task.subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(task.assigneeBgColor),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = task.assigneeInitials,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = TextOnPrimary
            )
        }
    }
}

@Composable
fun ProjectResourcesCard(
    resources: List<ProjectResourceUiItem>,
    onAddResourceClick: () -> Unit = {}
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = CardWhite,
        border = BorderStroke(1.dp, BorderLight)
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Links & Resources",
                    style = MaterialTheme.typography.titleLarge,
                    color = TextPrimary
                )

                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(PrimaryGreen)
                        .clickable { onAddResourceClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Add,
                        contentDescription = "Add Resource",
                        tint = TextOnPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            resources.forEachIndexed { index, resource ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(LightGreenBg),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (resource.typeLabel == "PDF") Icons.Outlined.Description else Icons.Outlined.Link,
                            contentDescription = null,
                            tint = PrimaryGreen,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = resource.title,
                            style = MaterialTheme.typography.titleMedium,
                            color = TextPrimary
                        )
                        Text(
                            text = resource.typeLabel,
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary
                        )
                    }
                }

                if (index < resources.lastIndex) {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 10.dp),
                        thickness = 1.dp,
                        color = DividerGray
                    )
                }
            }
        }
    }
}