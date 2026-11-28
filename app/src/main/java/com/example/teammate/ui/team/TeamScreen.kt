package com.example.teammate.ui.team

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.teammate.model.TeamMember
import com.example.teammate.state.TeamMateState
import com.example.teammate.ui.theme.CardWhite
import com.example.teammate.ui.theme.DarkGreen
import com.example.teammate.ui.theme.PrimaryGreen
import com.example.teammate.ui.theme.SoftGreenAccent
import com.example.teammate.ui.theme.StatusErrorText
import com.example.teammate.ui.theme.StatusInfoText
import com.example.teammate.ui.theme.StatusSuccessBg
import com.example.teammate.ui.theme.StatusSuccessText
import com.example.teammate.ui.theme.StatusWarningBg
import com.example.teammate.ui.theme.StatusWarningText
import com.example.teammate.ui.theme.SurfaceVariantLight
import com.example.teammate.ui.theme.TextOnPrimary
import com.example.teammate.ui.theme.TextPrimary
import com.example.teammate.ui.theme.TextSecondary

data class TeamMemberUi(
    val id: String,
    val name: String,
    val role: String,
    val completedTasks: Int = 0,
    val remainingTasks: Int = 0
) {
    val totalTasks: Int get() = completedTasks + remainingTasks
    val progressPercent: Int
        get() = if (totalTasks == 0) 0 else ((completedTasks * 100f) / totalTasks).toInt()
}

private data class MemberColorTheme(
    val accent: Color,
    val softBg: Color,
    val badgeText: Color
)

private val MemberPalette = listOf(
    MemberColorTheme(
        accent = PrimaryGreen,
        softBg = PrimaryGreen,
        badgeText = TextOnPrimary
    ),
    MemberColorTheme(
        accent = DarkGreen,
        softBg = DarkGreen,
        badgeText = TextOnPrimary
    ),
    MemberColorTheme(
        accent = StatusWarningText,
        softBg = StatusWarningText,
        badgeText = TextOnPrimary
    ),
    MemberColorTheme(
        accent = SoftGreenAccent,
        softBg = SoftGreenAccent,
        badgeText = TextOnPrimary
    ),
    MemberColorTheme(
        accent = StatusErrorText,
        softBg = StatusErrorText,
        badgeText = TextOnPrimary
    ),
    MemberColorTheme(
        accent = StatusInfoText,
        softBg = StatusInfoText,
        badgeText = TextOnPrimary
    )
)

private fun memberColorAt(index: Int): MemberColorTheme =
    MemberPalette[index % MemberPalette.size]

@Composable
fun TeamScreen(
    modifier: Modifier = Modifier
) {
    LaunchedEffect(Unit) {
        TeamMateState.startFirestoreSync()
    }

    val allTasks = TeamMateState.tasks
    val members = TeamMateState.teamMembers.map { member ->
        val memberTasks = allTasks.filter {
            it.assigneeName.equals(member.name, ignoreCase = true)
        }
        val completed = memberTasks.count { it.isCompleted }
        val remaining = memberTasks.count { !it.isCompleted }

        TeamMemberUi(
            id = member.id,
            name = member.name,
            role = member.role,
            completedTasks = completed,
            remainingTasks = remaining
        )
    }

    var showAddDialog by remember { mutableStateOf(false) }
    var nameInput by remember { mutableStateOf("") }
    var roleInput by remember { mutableStateOf("") }
    val fairSharePercent = if (members.isEmpty()) 0 else (100f / members.size).toInt()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = SurfaceVariantLight,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SurfaceVariantLight)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Team-Members",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                IconButton(onClick = { showAddDialog = true }) {
                    Icon(
                        imageVector = Icons.Rounded.Add,
                        contentDescription = "Add member",
                        tint = PrimaryGreen
                    )
                }
            }
        }
    ) { innerPadding ->
        if (members.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No team members yet. Tap + to add one.",
                    fontSize = 14.sp,
                    color = TextSecondary
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    ContributionSplitCard(members = members)
                }
                itemsIndexed(members, key = { _, member -> member.id }) { index, member ->
                    TeamMemberCard(
                        member = member,
                        fairSharePercent = fairSharePercent,
                        colorTheme = memberColorAt(index)
                    )
                }
            }
        }
    }

    if (showAddDialog) {
        AlertDialog(
            containerColor = CardWhite,
            onDismissRequest = {
                showAddDialog = false
                nameInput = ""
                roleInput = ""
            },
            title = {
                Text(
                    text = "Add Team Member",
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = nameInput,
                        onValueChange = { nameInput = it },
                        label = { Text("Name") },
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = roleInput,
                        onValueChange = { roleInput = it },
                        label = { Text("Role") },
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val cleanName = nameInput.trim()
                        val cleanRole = roleInput.trim()
                        if (cleanName.isNotEmpty() && cleanRole.isNotEmpty()) {
                            val initials = cleanName
                                .split(" ")
                                .filter { it.isNotBlank() }
                                .take(2)
                                .joinToString("") { it.first().uppercase() }
                            val newId = "u${TeamMateState.teamMembers.size + 1}"
                            val emailSlug = cleanName.lowercase().replace(" ", "")

                            TeamMateState.teamMembers.add(
                                TeamMember(
                                    id = newId,
                                    name = cleanName,
                                    role = cleanRole,
                                    initials = initials,
                                    email = "$emailSlug@university.edu.eg"
                                )
                            )
                            nameInput = ""
                            roleInput = ""
                            showAddDialog = false
                        }
                    }
                ) {
                    Text("Add", color = DarkGreen, fontWeight = FontWeight.SemiBold)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showAddDialog = false
                        nameInput = ""
                        roleInput = ""
                    }
                ) {
                    Text("Cancel", color = TextSecondary)
                }
            }
        )
    }
}

@Composable
private fun ContributionSplitCard(
    members: List<TeamMemberUi>,
    modifier: Modifier = Modifier
) {
    val memberCount = members.size
    val fairSharePercent = if (memberCount == 0) 0 else (100f / memberCount).toInt()
    val onTargetCount = if (memberCount == 0) 0 else members.count { it.progressPercent >= fairSharePercent }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, SurfaceVariantLight, RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Text(
                text = "Contribution split",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(CircleShape)
                    .background(SurfaceVariantLight)
            ) {
                if (memberCount > 0) {
                    val totalWeight = members.sumOf { it.progressPercent.coerceAtLeast(1) }.toFloat()

                    members.forEachIndexed { index, member ->
                        val weightValue = member.progressPercent.coerceAtLeast(1).toFloat() / totalWeight
                        Box(
                            modifier = Modifier
                                .weight(weightValue)
                                .fillMaxHeight()
                                .background(memberColorAt(index).accent)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Fair share = $fairSharePercent% each · $onTargetCount member${if (onTargetCount == 1) "" else "s"} to target",
                fontSize = 12.sp,
                color = TextSecondary
            )
        }
    }
}

@Composable
private fun TeamMemberCard(
    member: TeamMemberUi,
    fairSharePercent: Int,
    colorTheme: MemberColorTheme,
    modifier: Modifier = Modifier
) {
    val initials = member.name
        .split(" ")
        .filter { it.isNotBlank() }
        .take(2)
        .joinToString("") { it.first().uppercase() }
    val progressFraction = (member.progressPercent / 100f).coerceIn(0f, 1f)
    val isOnTrack = member.progressPercent >= fairSharePercent
    val statusLabel = if (isOnTrack) "On track" else "Behind"
    val statusBgColor = if (isOnTrack) StatusSuccessBg else StatusWarningBg
    val statusTextColor = if (isOnTrack) StatusSuccessText else StatusWarningText

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(colorTheme.softBg),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = initials,
                        fontWeight = FontWeight.Bold,
                        color = colorTheme.badgeText
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = member.name,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                    Text(
                        text = member.role,
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${member.completedTasks} done · ${member.remainingTasks} remaining",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }

                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = "${member.progressPercent}%",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(statusBgColor)
                            .padding(horizontal = 10.dp, vertical = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = statusLabel,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = statusTextColor,
                            maxLines = 1
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(CircleShape)
                    .background(SurfaceVariantLight)
            ) {
                if (progressFraction > 0f) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(progressFraction)
                            .fillMaxHeight()
                            .background(colorTheme.accent)
                    )
                }
            }
        }
    }
}