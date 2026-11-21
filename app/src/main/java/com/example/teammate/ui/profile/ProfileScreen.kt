package com.example.teammate.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.Groups
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Green = Color(0xFF1DB954)
private val GreenLight = Color(0xFFE3F6EA)
private val BgScreen = Color(0xFFF7F8F7)
private val TextPrimary = Color(0xFF1A1A1A)
private val TextSecondary = Color(0xFF7A7A7A)
private val Red = Color(0xFFE53935)

data class ProfileGroup(
    val name: String,
    val membersCount: Int = 0
)

data class ProfileData(
    val initials: String = "",
    val name: String = "",
    val major: String = "",
    val term: String = "",
    val studentId: String = "",
    val projectsCount: Int = 0,
    val tasksDone: Int = 0,
    val onTimePercent: Int = 0,
    val groups: List<ProfileGroup> = emptyList(),
    val deadlineReminders: Boolean = false,
    val taskUpdates: Boolean = false,
    val weeklyReport: Boolean = false
)

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    profileData: ProfileData = ProfileData(),
    onSignOut: () -> Unit = {}
) {
    var deadlineReminders by remember(profileData.deadlineReminders) {
        mutableStateOf(profileData.deadlineReminders)
    }
    var taskUpdates by remember(profileData.taskUpdates) {
        mutableStateOf(profileData.taskUpdates)
    }
    var weeklyReport by remember(profileData.weeklyReport) {
        mutableStateOf(profileData.weeklyReport)
    }

    val subtitleParts = buildList {
        if (profileData.major.isNotBlank()) add(profileData.major)
        if (profileData.term.isNotBlank()) add(profileData.term)
        if (profileData.studentId.isNotBlank()) add("ID ${profileData.studentId}")
    }
    val subtitleText = subtitleParts.joinToString(" · ")

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BgScreen)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .size(88.dp)
                .clip(CircleShape)
                .background(Green),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = profileData.initials,
                color = Color.White,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = profileData.name,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )

        Text(
            text = subtitleText,
            fontSize = 12.sp,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(18.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            StatCard(
                value = "${profileData.projectsCount}",
                label = "Projects",
                modifier = Modifier.weight(1f)
            )
            StatCard(
                value = "${profileData.tasksDone}",
                label = "Tasks done",
                modifier = Modifier.weight(1f)
            )
            StatCard(
                value = "${profileData.onTimePercent}%",
                label = "On time",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        AppCard {
            Text(
                text = "Joined groups",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = TextPrimary
            )
            profileData.groups.forEach { group ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(GreenLight),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Groups,
                            contentDescription = null,
                            tint = Green,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = group.name,
                        modifier = Modifier.weight(1f),
                        fontSize = 14.sp,
                        color = TextPrimary
                    )
                    Text(
                        text = "${group.membersCount} members",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        AppCard {
            Text(
                text = "Notifications",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = TextPrimary
            )
            SwitchRow(
                text = "Upcoming deadline reminders",
                checked = deadlineReminders,
                onChange = { deadlineReminders = it }
            )
            SwitchRow(
                text = "Task updates & assignments",
                checked = taskUpdates,
                onChange = { taskUpdates = it }
            )
            SwitchRow(
                text = "Weekly progress report",
                checked = weeklyReport,
                onChange = { weeklyReport = it }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))
        AppCard {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Account settings",
                    modifier = Modifier.weight(1f),
                    fontSize = 14.sp,
                    color = TextPrimary
                )
                Icon(
                    imageVector = Icons.Rounded.ChevronRight,
                    contentDescription = null,
                    tint = Color(0xFF9E9E9E)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        AppCard(onClick = onSignOut) {
            Text(
                text = "Sign out",
                fontSize = 14.sp,
                color = Red
            )
        }
    }
}

@Composable
private fun AppCard(
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val shape = RoundedCornerShape(18.dp)
    val colors = CardDefaults.cardColors(containerColor = Color.White)
    val elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)

    if (onClick != null) {
        Card(
            onClick = onClick,
            modifier = Modifier.fillMaxWidth(),
            shape = shape,
            colors = colors,
            elevation = elevation
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                content = content
            )
        }
    } else {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = shape,
            colors = colors,
            elevation = elevation
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                content = content
            )
        }
    }
}

@Composable
private fun StatCard(
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Green
            )
            Text(
                text = label,
                fontSize = 11.sp,
                color = TextSecondary
            )
        }
    }
}

@Composable
private fun SwitchRow(
    text: String,
    checked: Boolean,
    onChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            modifier = Modifier.weight(1f),
            fontSize = 14.sp,
            color = TextPrimary
        )
        Switch(
            checked = checked,
            onCheckedChange = onChange,
            colors = SwitchDefaults.colors(
                checkedTrackColor = Green,
                checkedThumbColor = Color.White,
                uncheckedTrackColor = Color(0xFFD5D5D5),
                uncheckedThumbColor = Color.White,
                uncheckedBorderColor = Color.Transparent
            )
        )
    }
}