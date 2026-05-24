package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.BraveXViewModel
import com.example.ui.BraveXViewModelFactory
import com.example.ui.components.LogoHeader
import com.example.ui.screens.ConsoleScreen
import com.example.ui.screens.ExploreScreen
import com.example.ui.screens.ProjectDetailSheet
import com.example.ui.screens.SavedScreen
import com.example.ui.screens.UploadScreen
import com.example.ui.theme.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val factory = BraveXViewModelFactory(application)
                val viewModel: BraveXViewModel = viewModel(factory = factory)
                MainAppShell(viewModel)
            }
        }
    }
}

@Composable
fun MainAppShell(viewModel: BraveXViewModel) {
    val currentTab by viewModel.currentTab.collectAsState()
    val filteredProjects by viewModel.filteredProjects.collectAsState()
    val savedProjects by viewModel.savedProjects.collectAsState()
    val userProjects by viewModel.userUploadedProjects.collectAsState()
    val selectedProject by viewModel.selectedProject.collectAsState()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground),
        topBar = {
            LogoHeader(
                modifier = Modifier
                    .statusBarsPadding()
                    .fillMaxWidth()
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = DarkSurface,
                tonalElevation = 8.dp,
                modifier = Modifier
                    .navigationBarsPadding()
                    .testTag("bottom_nav_bar")
            ) {
                // Explore Tab
                NavigationBarItem(
                    selected = currentTab == BraveXViewModel.ScreenTab.EXPLORE,
                    onClick = { viewModel.switchTab(BraveXViewModel.ScreenTab.EXPLORE) },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Explore") },
                    label = { 
                        Text(
                            "EXPLORE", 
                            fontSize = 10.sp, 
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        ) 
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = DarkBackground,
                        selectedTextColor = PrimaryTeal,
                        indicatorColor = PrimaryTeal,
                        unselectedIconColor = TextSecondary,
                        unselectedTextColor = TextSecondary
                    ),
                    modifier = Modifier.testTag("tab_explore")
                )

                // Saved Tab
                NavigationBarItem(
                    selected = currentTab == BraveXViewModel.ScreenTab.SAVED,
                    onClick = { viewModel.switchTab(BraveXViewModel.ScreenTab.SAVED) },
                    icon = { Icon(Icons.Default.Favorite, contentDescription = "Saved") },
                    label = { 
                        Text(
                            "SAVED", 
                            fontSize = 10.sp, 
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        ) 
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = DarkBackground,
                        selectedTextColor = PrimaryTeal,
                        indicatorColor = PrimaryTeal,
                        unselectedIconColor = TextSecondary,
                        unselectedTextColor = TextSecondary
                    ),
                    modifier = Modifier.testTag("tab_saved")
                )

                // Upload Tab
                NavigationBarItem(
                    selected = currentTab == BraveXViewModel.ScreenTab.UPLOAD,
                    onClick = { viewModel.switchTab(BraveXViewModel.ScreenTab.UPLOAD) },
                    icon = { Icon(Icons.Default.Add, contentDescription = "Upload") },
                    label = { 
                        Text(
                            "UPLOAD", 
                            fontSize = 10.sp, 
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        ) 
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = DarkBackground,
                        selectedTextColor = PrimaryTeal,
                        indicatorColor = PrimaryTeal,
                        unselectedIconColor = TextSecondary,
                        unselectedTextColor = TextSecondary
                    ),
                    modifier = Modifier.testTag("tab_upload")
                )

                // Console Tab
                NavigationBarItem(
                    selected = currentTab == BraveXViewModel.ScreenTab.CONSOLE,
                    onClick = { viewModel.switchTab(BraveXViewModel.ScreenTab.CONSOLE) },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Console") },
                    label = { 
                        Text(
                            "CONSOLE", 
                            fontSize = 10.sp, 
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        ) 
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = DarkBackground,
                        selectedTextColor = PrimaryTeal,
                        indicatorColor = PrimaryTeal,
                        unselectedIconColor = TextSecondary,
                        unselectedTextColor = TextSecondary
                    ),
                    modifier = Modifier.testTag("tab_console")
                )
            }
        },
        contentWindowInsets = WindowInsets.safeDrawing
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Main content based on active Screen Tab
            when (currentTab) {
                BraveXViewModel.ScreenTab.EXPLORE -> {
                    ExploreScreen(
                        viewModel = viewModel,
                        projects = filteredProjects,
                        onSelectProject = { viewModel.selectProject(it) }
                    )
                }
                BraveXViewModel.ScreenTab.SAVED -> {
                    SavedScreen(
                        viewModel = viewModel,
                        savedProjects = savedProjects,
                        onSelectProject = { viewModel.selectProject(it) }
                    )
                }
                BraveXViewModel.ScreenTab.UPLOAD -> {
                    UploadScreen(
                        viewModel = viewModel
                    )
                }
                BraveXViewModel.ScreenTab.CONSOLE -> {
                    ConsoleScreen(
                        viewModel = viewModel,
                        userProjects = userProjects,
                        onSelectProject = { viewModel.selectProject(it) }
                    )
                }
            }

            // High Fidelity Overlay Slide-up Detail sheet
            AnimatedVisibility(
                visible = selectedProject != null,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it })
            ) {
                if (selectedProject != null) {
                    ProjectDetailSheet(
                        project = selectedProject!!,
                        viewModel = viewModel,
                        onDismiss = { viewModel.selectProject(null) }
                    )
                }
            }
        }
    }
}
