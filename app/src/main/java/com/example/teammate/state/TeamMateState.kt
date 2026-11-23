package com.example.teammate.state

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import com.example.teammate.model.Project
import com.example.teammate.model.TaskItem
import com.example.teammate.model.TeamMember
import com.example.teammate.model.UserProfile

object TeamMateState {

    val currentUser = mutableStateOf(
        UserProfile(
            id = "u1",
            name = "Abdelrahman Mohamed",
            academicId = "20230145",
            email = "abdelrahman@university.edu.eg",
            role = "Team Leader"
        )
    )

    val teamMembers = mutableStateListOf(
        TeamMember("u1", "Abdelrahman Mohamed", "Team Leader", "AM", "abdelrahman@university.edu.eg"),
        TeamMember("u2", "Asmaa Attia", "Mobile Developer", "AA", "asmaa@university.edu.eg"),
        TeamMember("u3", "Norhan Khaled", "Android Developer", "NK", "nourhan@university.edu.eg"),
        TeamMember("u4", "Ahmed Reda", "UI & UX Designer", "AR", "ahmed@university.edu.eg"),
        TeamMember("u5", "Kholoud Rashed", "UI & UX Designer", "KR", "kholoud@university.edu.eg"),
        TeamMember("u6", "Asmaa Elappasy", "Backend", "AE", "asmaaelappasy@university.edu.eg")
    )

    val projects = mutableStateListOf(
        Project(
            id = "p1",
            title = "Marketing",
            code = "MN406L",
            description = "Campaign planning and social media strategy for final semester presentation.",
            deadline = "25 April 2026",
            memberIds = listOf("u1", "u2", "u4")
        ),
        Project(
            id = "p2",
            title = "Statistics",
            code = "ST210",
            description = "Data analysis and probability distribution models using R and Python.",
            deadline = "10 May 2026",
            memberIds = listOf("u1", "u3", "u6")
        ),
        Project(
            id = "p3",
            title = "Business",
            code = "B6301",
            description = "Feasibility study and financial projection report for startup pitch.",
            deadline = "02 May 2026",
            memberIds = listOf("u1", "u2", "u3", "u5", "u6")
        )
    )

    val tasks = mutableStateListOf(
        TaskItem("t1", "p1", "Design campaign slides", "Ahmed Reda", "20 April 2026", isCompleted = true),
        TaskItem("t2", "p1", "Prepare mobile prototype", "Asmaa Attia", "22 April 2026", isCompleted = false),
        TaskItem("t3", "p1", "Review final script", "Abdelrahman Mohamed", "24 April 2026", isCompleted = false),
        TaskItem("t4", "p2", "Clean dataset & API", "Asmaa Elappasy", "05 May 2026", isCompleted = true),
        TaskItem("t5", "p3", "Build Android module", "Norhan Khaled", "28 April 2026", isCompleted = true),
        TaskItem("t6", "p3", "Finalize UI wireframes", "Kholoud Rashed", "30 April 2026", isCompleted = false)
    )

    fun toggleTaskCompletion(taskId: String) {
        val index = tasks.indexOfFirst { it.id == taskId }
        if (index != -1) {
            val current = tasks[index]
            tasks[index] = current.copy(isCompleted = !current.isCompleted)
        }
    }

    fun addProject(title: String, code: String, description: String, deadline: String) {
        val newProject = Project(
            id = "p${projects.size + 1}",
            title = title,
            code = code,
            description = description,
            deadline = deadline,
            memberIds = listOf(currentUser.value.id)
        )
        projects.add(0, newProject)
    }

    fun addTask(projectId: String, title: String, assigneeName: String, dueDate: String) {
        val newTask = TaskItem(
            id = "t${tasks.size + 1}",
            projectId = projectId,
            title = title,
            assigneeName = assigneeName,
            dueDate = dueDate,
            isCompleted = false
        )
        tasks.add(newTask)
    }

    fun getProjectProgress(projectId: String): Float {
        val projectTasks = tasks.filter { it.projectId == projectId }
        if (projectTasks.isEmpty()) return 0f
        val completedCount = projectTasks.count { it.isCompleted }
        return completedCount.toFloat() / projectTasks.size.toFloat()
    }
}