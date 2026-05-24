package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.ProjectEntity
import com.example.data.ProjectRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BraveXViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ProjectRepository
    
    init {
        val database = AppDatabase.getDatabase(application)
        repository = ProjectRepository(database.projectDao())
        
        // Seed the database with beautiful initial developer templates
        viewModelScope.launch {
            repository.checkAndSeedDatabase()
        }
    }

    // Screens / Tabs helper
    enum class ScreenTab {
        EXPLORE, SAVED, UPLOAD, CONSOLE
    }

    private val _currentTab = MutableStateFlow(ScreenTab.EXPLORE)
    val currentTab: StateFlow<ScreenTab> = _currentTab.asStateFlow()

    fun switchTab(tab: ScreenTab) {
        _currentTab.value = tab
    }

    // Authentication & Profile state
    data class UserProfile(
        val email: String,
        val username: String,
        val bio: String,
        val company: String = "BraveX Labs",
        val starsReceived: Int = 124,
        val isFingerprintUploadEnabled: Boolean = true
    )

    private val _currentUser = MutableStateFlow<UserProfile?>(null) // starts as guest
    val currentUser: StateFlow<UserProfile?> = _currentUser.asStateFlow()

    private val _isLoginLoading = MutableStateFlow(false)
    val isLoginLoading: StateFlow<Boolean> = _isLoginLoading.asStateFlow()

    fun mockSignIn(email: String, name: String) {
        viewModelScope.launch {
            _isLoginLoading.value = true
            delay(1200) // Realistic delay for web authentication simulation
            _currentUser.value = UserProfile(
                email = email,
                username = name.ifEmpty { "developer_bravex" },
                bio = "Full-Stack Software Engineer building futuristic mobile & web platforms."
            )
            _isLoginLoading.value = false
        }
    }

    fun mockSignOut() {
        _currentUser.value = null
    }

    // Search and Category states
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun selectCategory(category: String) {
        _selectedCategory.value = category
    }

    // Combine Room flow lists with UI filters
    val filteredProjects: StateFlow<List<ProjectEntity>> = repository.allProjects
        .combine(_searchQuery) { list, query ->
            if (query.isBlank()) list else {
                list.filter {
                    it.title.contains(query, ignoreCase = true) ||
                    it.techStack.contains(query, ignoreCase = true) ||
                    it.description.contains(query, ignoreCase = true)
                }
            }
        }
        .combine(_selectedCategory) { list, category ->
            if (category == "All") list else {
                list.filter { it.category.equals(category, ignoreCase = true) }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val savedProjects: StateFlow<List<ProjectEntity>> = repository.savedProjects
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val userUploadedProjects: StateFlow<List<ProjectEntity>> = repository.userUploadedProjects
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Save / Favorite Action Handler
    fun toggleSaveProject(project: ProjectEntity) {
        viewModelScope.launch {
            val updated = project.copy(isSaved = !project.isSaved)
            repository.updateProject(updated)
        }
    }

    // Detailed Project sheet selection
    private val _selectedProject = MutableStateFlow<ProjectEntity?>(null)
    val selectedProject: StateFlow<ProjectEntity?> = _selectedProject.asStateFlow()

    // Simulated Code files browser state for detailed view
    private val _activeFileSelected = MutableStateFlow("main.kt")
    val activeFileSelected: StateFlow<String> = _activeFileSelected.asStateFlow()

    fun selectProject(project: ProjectEntity?) {
        _selectedProject.value = project
        // Set default file view based on tech stacks
        _activeFileSelected.value = if (project != null && project.language == "TypeScript") "page.tsx" else if (project != null && project.language == "Go") "main.go" else "MainActivity.kt"
    }

    fun selectActiveFile(fileName: String) {
        _activeFileSelected.value = fileName
    }

    // Upload simulation variables and custom fingerprint scanning state
    private val _isScanningFingerprint = MutableStateFlow(false)
    val isScanningFingerprint: StateFlow<Boolean> = _isScanningFingerprint.asStateFlow()

    private val _scanProgress = MutableStateFlow(0f)
    val scanProgress: StateFlow<Float> = _scanProgress.asStateFlow()

    private val _scanStatusMessage = MutableStateFlow("Ready to verify signature...")
    val scanStatusMessage: StateFlow<String> = _scanStatusMessage.asStateFlow()

    private val _uploadSuccessEvent = MutableStateFlow(false)
    val uploadSuccessEvent: StateFlow<Boolean> = _uploadSuccessEvent.asStateFlow()

    fun clearUploadSuccessEvent() {
        _uploadSuccessEvent.value = false
    }

    fun startFingerprintScanning(
        onScanComplete: () -> Unit
    ) {
        viewModelScope.launch {
            _isScanningFingerprint.value = true
            _scanProgress.value = 0f
            _scanStatusMessage.value = "Scanning credential..."
            
            // Increment progress with cool realistic fingerprint messages
            for (progress in 1..10) {
                delay(180)
                _scanProgress.value = progress / 10f
                when (progress) {
                    3 -> _scanStatusMessage.value = "Awaiting reader placement..."
                    6 -> _scanStatusMessage.value = "Scanning package certificate..."
                    8 -> _scanStatusMessage.value = "Optimizing signing values..."
                    10 -> {
                        _scanStatusMessage.value = "VERIFIED IN 0.18s✓"
                        delay(400)
                    }
                }
            }
            
            _isScanningFingerprint.value = false
            onScanComplete()
        }
    }

    fun uploadCustomProject(
        name: String,
        category: String,
        techStack: String,
        description: String,
        longDescription: String,
        size: String,
        lang: String,
        creator: String
    ) {
        viewModelScope.launch {
            val newProject = ProjectEntity(
                title = name.ifEmpty { "Unnamed Template" },
                category = category,
                techStack = techStack.ifEmpty { lang },
                description = description.ifEmpty { "A custom compiled development asset." },
                longDescription = longDescription.ifEmpty { "A high quality user-created workspace starter with complete build modules." },
                stars = 0,
                downloads = 0,
                isSaved = false,
                codeSize = if (size.isEmpty()) "1.2 MB" else size,
                language = lang.ifEmpty { "Kotlin" },
                creator = creator.ifEmpty { "Guest Developer" },
                isUserUploaded = true
            )
            repository.insertProject(newProject)
            _uploadSuccessEvent.value = true
        }
    }

    fun deleteProject(project: ProjectEntity) {
        viewModelScope.launch {
            repository.deleteProjectById(project.id)
            if (_selectedProject.value?.id == project.id) {
                _selectedProject.value = null
            }
        }
    }

    // Toggle fingerprint verification feature inside settings/upload
    private val _isFingerprintEnabledInSettings = MutableStateFlow(true)
    val isFingerprintEnabledInSettings: StateFlow<Boolean> = _isFingerprintEnabledInSettings.asStateFlow()

    fun toggleFingerprintSettings() {
        _isFingerprintEnabledInSettings.value = !_isFingerprintEnabledInSettings.value
    }
}

class BraveXViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BraveXViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BraveXViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
