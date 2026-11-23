package com.example.teammate.model

data class UserProfile(
    val id: String,
    val name: String,
    val academicId: String,
    val email: String,
    val role: String
)

data class TeamMember(
    val id: String,
    val name: String,
    val role: String,
    val initials: String,
    val email: String
)

data class TaskItem(
    val id: String,
    val projectId: String,
    val title: String,
    val assigneeName: String,
    val dueDate: String,
    val isCompleted: Boolean = false
)

data class Project(
    val id: String,
    val title: String,
    val code: String,
    val description: String,
    val deadline: String,
    val memberIds: List<String>
)