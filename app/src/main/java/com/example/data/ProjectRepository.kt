package com.example.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers

class ProjectRepository(private val projectDao: ProjectDao) {

    val allProjects: Flow<List<ProjectEntity>> = projectDao.getAllProjects()
    val savedProjects: Flow<List<ProjectEntity>> = projectDao.getSavedProjects()
    val userUploadedProjects: Flow<List<ProjectEntity>> = projectDao.getUserUploadedProjects()

    suspend fun getProjectById(id: Int): ProjectEntity? = withContext(Dispatchers.IO) {
        projectDao.getProjectById(id)
    }

    suspend fun insertProject(project: ProjectEntity): Long = withContext(Dispatchers.IO) {
        projectDao.insertProject(project)
    }

    suspend fun updateProject(project: ProjectEntity) = withContext(Dispatchers.IO) {
        projectDao.updateProject(project)
    }

    suspend fun deleteProjectById(id: Int) = withContext(Dispatchers.IO) {
        projectDao.deleteProjectById(id)
    }

    suspend fun checkAndSeedDatabase() = withContext(Dispatchers.IO) {
        val currentList = projectDao.getAllProjects().firstOrNull()
        if (currentList.isNullOrEmpty()) {
            val initialList = listOf(
                ProjectEntity(
                    title = "Compose Clean Boilerplate",
                    category = "Android Templates",
                    techStack = "Kotlin, Compose, Room, MVVM, KSP, Coroutines, Flow",
                    description = "A modern, highly polished Android starter template with Jetpack Compose, Material 3, Clean Architecture, MVVM and local Room persistence.",
                    longDescription = "Kickstart your next Android project with standard best practices already built-in. This boilerplate features fully custom Material 3 theming, clean screen navigation, robust local database caching, background workers, and dynamic edge-to-edge support. Includes responsive layout utilities for foldables and tablets.",
                    stars = 142,
                    downloads = 549,
                    isSaved = false,
                    codeSize = "1.4 MB",
                    language = "Kotlin",
                    creator = "BraveHex",
                    isUserUploaded = false
                ),
                ProjectEntity(
                    title = "Next.js 15 SaaS Core",
                    category = "Web Templates",
                    techStack = "React, NextJS 15, Tailwind CSS, TypeScript, Prisma, Stripe",
                    description = "A complete software-as-a-service starter kit featuring secure user authentication, database integration, Stripe checkouts, and clean dashboard layouts.",
                    longDescription = "This production-ready Next.js 15 starter helps you launch your SaaS product in hours. Built with a responsive dark-mode cockpit interface, full authentication flow with multi-factor support, Prisma database connection adapters, ready-to-use mail templates, and custom webhook handlers for transactional Stripe billing.",
                    stars = 289,
                    downloads = 1205,
                    isSaved = true, // Start with one saved to show the feature off!
                    codeSize = "4.8 MB",
                    language = "TypeScript",
                    creator = "NextTitan",
                    isUserUploaded = false
                ),
                ProjectEntity(
                    title = "Flutter AI Chat Companion",
                    category = "Cross-Platform",
                    techStack = "Dart, Flutter, Gemini SDK, Bloc, MarkDown, Multiplatform",
                    description = "A cross-platform app template integrating Gemini API for seamless AI text generation, conversation history, and rich markdown rendering.",
                    longDescription = "Build interactive chatbot experiences on iOS, Android, and Web with this highly polished Flutter template. It integrates direct, optimized REST calls to Gemini models, featuring fully fluid slide-in chat list cells, beautiful markdown rendering, speech-to-text input filters, offline chat logs, and local device-key storage configurations.",
                    stars = 95,
                    downloads = 312,
                    isSaved = false,
                    codeSize = "3.1 MB",
                    language = "Dart",
                    creator = "BraveHex",
                    isUserUploaded = false
                ),
                ProjectEntity(
                    title = "Go Microservices gRPC Kit",
                    category = "Backend & DevOps",
                    techStack = "Go, gRPC, Protobuf, Docker, PostgreSQL, Prometheus",
                    description = "High-performance Go backend environment demonstrating structured gRPC microservices, environment configurations, and metrics instrumentation.",
                    longDescription = "A powerful blueprint for distributed microservices. Features automated protocol buffer compilation, integrated structured JSON logging, secure database migration scripts, health check routes, and pre-configured Prometheus gauges. Includes a step-by-step CI/CD GitHub Actions workflow.",
                    stars = 175,
                    downloads = 638,
                    isSaved = false,
                    codeSize = "1.1 MB",
                    language = "Go",
                    creator = "GopherPro",
                    isUserUploaded = false
                ),
                ProjectEntity(
                    title = "Rust WebAssembly Canvas Engine",
                    category = "Graphics",
                    techStack = "Rust, Wasm, WebGL, Webpack, High Performance Canvas",
                    description = "Supercharge your browser projects with a high-performance interactive physics engine compiled from Rust web assemblies.",
                    longDescription = "This template bundles high-performance Rust calculations with modern Webpack-brokered WebAssembly bindings. Includes a responsive 2D canvas particle physics playground, custom event handlers bridging JS and Rust, automated memory layouts, and extremely fast vector calculus models optimized for chrome rendering cycles.",
                    stars = 310,
                    downloads = 890,
                    isSaved = false,
                    codeSize = "2.3 MB",
                    language = "Rust",
                    creator = "CyberCore",
                    isUserUploaded = false
                )
            )
            projectDao.insertProjects(initialList)
        }
    }
}
