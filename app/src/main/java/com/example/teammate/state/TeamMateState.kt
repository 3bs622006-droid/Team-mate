package com.example.teammate.state

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import com.example.teammate.model.Project
import com.example.teammate.model.TaskItem
import com.example.teammate.model.TeamMember
import com.example.teammate.model.UserProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

object TeamMateState {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private var isListening = false

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

    val projects = mutableStateListOf<Project>()
    val tasks = mutableStateListOf<TaskItem>()

    fun startFirestoreSync() {
        if (isListening) return
        isListening = true

        // 1. الاستماع اللحظي للمشاريع من Firestore
        db.collection("projects").addSnapshotListener { snapshot, error ->
            if (error != null || snapshot == null) return@addSnapshotListener

            if (snapshot.isEmpty) {
                seedInitialData()
            } else {
                projects.clear()
                for (doc in snapshot.documents) {
                    val project = Project(
                        id = doc.id,
                        title = doc.getString("title") ?: "",
                        code = doc.getString("code") ?: "",
                        description = doc.getString("description") ?: "",
                        deadline = doc.getString("deadline") ?: "",
                        memberIds = (doc.get("memberIds") as? List<*>)?.mapNotNull { it as? String } ?: emptyList()
                    )
                    projects.add(project)
                }
            }
        }

        // 2. الاستماع اللحظي للتاسكات من Firestore
        db.collection("tasks").addSnapshotListener { snapshot, error ->
            if (error != null || snapshot == null) return@addSnapshotListener

            tasks.clear()
            for (doc in snapshot.documents) {
                val task = TaskItem(
                    id = doc.id,
                    projectId = doc.getString("projectId") ?: "",
                    title = doc.getString("title") ?: "",
                    assigneeName = doc.getString("assigneeName") ?: "",
                    dueDate = doc.getString("dueDate") ?: "",
                    isCompleted = doc.getBoolean("isCompleted") ?: false
                )
                tasks.add(task)
            }
        }

        // 3. تحديث بيانات اليوزر الحالي لو مسجل دخول في Firebase Auth
        auth.currentUser?.let { firebaseUser ->
            db.collection("users").document(firebaseUser.uid).get()
                .addOnSuccessListener { doc ->
                    if (doc.exists()) {
                        currentUser.value = UserProfile(
                            id = firebaseUser.uid,
                            name = doc.getString("name") ?: currentUser.value.name,
                            academicId = doc.getString("academicId") ?: currentUser.value.academicId,
                            email = firebaseUser.email ?: currentUser.value.email,
                            role = doc.getString("role") ?: "Member"
                        )
                    }
                }
        }
    }

    private fun seedInitialData() {
        val initialProjects = listOf(
            mapOf(
                "title" to "Marketing",
                "code" to "MN406L",
                "description" to "Campaign planning and social media strategy for final semester presentation.",
                "deadline" to "25 April 2026",
                "memberIds" to listOf("u1", "u2", "u4")
            ),
            mapOf(
                "title" to "Statistics",
                "code" to "ST210",
                "description" to "Data analysis and probability distribution models using R and Python.",
                "deadline" to "10 May 2026",
                "memberIds" to listOf("u1", "u3", "u6")
            ),
            mapOf(
                "title" to "Business",
                "code" to "B6301",
                "description" to "Feasibility study and financial projection report for startup pitch.",
                "deadline" to "02 May 2026",
                "memberIds" to listOf("u1", "u2", "u3", "u5", "u6")
            )
        )

        initialProjects.forEach { proj ->
            db.collection("projects").add(proj)
        }

        val initialTasks = listOf(
            mapOf("projectId" to "p1", "title" to "Design campaign slides", "assigneeName" to "Ahmed Reda", "dueDate" to "20 April 2026", "isCompleted" to true),
            mapOf("projectId" to "p1", "title" to "Prepare mobile prototype", "assigneeName" to "Asmaa Attia", "dueDate" to "22 April 2026", "isCompleted" to false),
            mapOf("projectId" to "p1", "title" to "Review final script", "assigneeName" to "Abdelrahman Mohamed", "dueDate" to "24 April 2026", "isCompleted" to false),
            mapOf("projectId" to "p2", "title" to "Clean dataset & API", "assigneeName" to "Asmaa Elappasy", "dueDate" to "05 May 2026", "isCompleted" to true),
            mapOf("projectId" to "p3", "title" to "Build Android module", "assigneeName" to "Norhan Khaled", "dueDate" to "28 April 2026", "isCompleted" to true)
        )

        initialTasks.forEach { task ->
            db.collection("tasks").add(task)
        }
    }

    fun toggleTaskCompletion(taskId: String) {
        val current = tasks.find { it.id == taskId } ?: return
        db.collection("tasks").document(taskId)
            .update("isCompleted", !current.isCompleted)
    }

    fun addProject(title: String, code: String, description: String, deadline: String) {
        val data = mapOf(
            "title" to title,
            "code" to code,
            "description" to description,
            "deadline" to deadline,
            "memberIds" to listOf(currentUser.value.id)
        )
        db.collection("projects").add(data)
    }

    fun addTask(projectId: String, title: String, assigneeName: String, dueDate: String) {
        val data = mapOf(
            "projectId" to projectId,
            "title" to title,
            "assigneeName" to assigneeName,
            "dueDate" to dueDate,
            "isCompleted" to false
        )
        db.collection("tasks").add(data)
    }

    fun getProjectProgress(projectId: String): Float {
        val projectTasks = tasks.filter { it.projectId == projectId }
        if (projectTasks.isEmpty()) return 0f
        val completedCount = projectTasks.count { it.isCompleted }
        return completedCount.toFloat() / projectTasks.size.toFloat()
    }
}