package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.BraveXViewModel
import com.example.ui.components.FingerprintScanner
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadScreen(
    viewModel: BraveXViewModel,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    // Form states
    var title by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Android Templates") }
    var selectedLanguage by remember { mutableStateOf("Kotlin") }
    var techStack by remember { mutableStateOf("") }
    var shortDesc by remember { mutableStateOf("") }
    var longDesc by remember { mutableStateOf("") }
    var codeSize by remember { mutableStateOf("1.5 MB") }
    var creatorName by remember { mutableStateOf("DeveloperHex") }

    val isScanning by viewModel.isScanningFingerprint.collectAsState()
    val scanProgress by viewModel.scanProgress.collectAsState()
    val scanMessage by viewModel.scanStatusMessage.collectAsState()
    val uploadSuccess by viewModel.uploadSuccessEvent.collectAsState()

    // Mock zip state
    var selectedZipName by remember { mutableStateOf<String?>(null) }

    val categories = listOf("Android Templates", "Web Templates", "Cross-Platform", "Backend & DevOps")
    val languages = listOf("Kotlin", "TypeScript", "Dart", "Go", "Rust")

    LaunchedEffect(uploadSuccess) {
        if (uploadSuccess) {
            // Reset form fields on success
            title = ""
            techStack = ""
            shortDesc = ""
            longDesc = ""
            selectedZipName = null
            // Switched to Explore screen or keep on upload screen with custom notification
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 90.dp)
        ) {
            // Header
            Text(
                text = "COMPILE & UPLOAD PROJECT 🚀",
                color = TextSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                letterSpacing = 1.5.sp
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (uploadSuccess) {
                // Success banner
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0F2C1F)),
                    border = BorderStroke(1.dp, SecondaryEmerald),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Success",
                            tint = SecondaryEmerald,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "BUNDLE COMPILED SUCCESSFULLY!",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Black,
                                fontFamily = FontFamily.Monospace
                            )
                            Text(
                                text = "Your template package is signed and available in the local registry catalog.",
                                color = TextSecondary,
                                fontSize = 11.sp,
                                fontFamily = FontFamily.SansSerif
                            )
                        }
                    }
                }
                
                Button(
                    onClick = { viewModel.clearUploadSuccessEvent() },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryTeal),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Add Another Template", color = DarkBackground, fontWeight = FontWeight.Bold)
                }
            } else {
                // Upload Form
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    // Title Field
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Project Title") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryTeal,
                            unfocusedBorderColor = SlateBorder,
                            focusedLabelColor = PrimaryTeal,
                            unfocusedLabelColor = TextMuted,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("upload_title_field"),
                        singleLine = true
                    )

                    // Category row selection
                    Text(
                        text = "Category",
                        color = TextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(categories) { category ->
                            val isSelected = selectedCategory == category
                            Box(
                                modifier = Modifier
                                    .background(
                                        color = if (isSelected) AccentPurple else Color.Transparent,
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .border(1.dp, if (isSelected) AccentPurple else SlateBorder, RoundedCornerShape(8.dp))
                                    .clickable { selectedCategory = category }
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = category,
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    // Language Selector
                    Text(
                        text = "Core Language",
                        color = TextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(languages) { lang ->
                            val isSelected = selectedLanguage == lang
                            Box(
                                modifier = Modifier
                                    .background(
                                        color = if (isSelected) Color(0xFF0F2537) else Color.Transparent,
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .border(1.dp, if (isSelected) PrimaryTeal else SlateBorder, RoundedCornerShape(8.dp))
                                    .clickable { selectedLanguage = lang }
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = lang,
                                    color = if (isSelected) PrimaryTeal else TextSecondary,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    // Tech Stack Comma separated
                    OutlinedTextField(
                        value = techStack,
                        onValueChange = { techStack = it },
                        label = { Text("Technologies Stack (comma separated)") },
                        placeholder = { Text("e.g. React, Tailwind, Prisma, PostgreSQL") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryTeal,
                            unfocusedBorderColor = SlateBorder,
                            focusedLabelColor = PrimaryTeal,
                            unfocusedLabelColor = TextMuted,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    // Short description
                    OutlinedTextField(
                        value = shortDesc,
                        onValueChange = { shortDesc = it },
                        label = { Text("Short Pitch Summary") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryTeal,
                            unfocusedBorderColor = SlateBorder,
                            focusedLabelColor = PrimaryTeal,
                            unfocusedLabelColor = TextMuted,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 2
                    )

                    // Bundle metadata Size & Author
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedTextField(
                            value = codeSize,
                            onValueChange = { codeSize = it },
                            label = { Text("Package Size") },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PrimaryTeal,
                                unfocusedBorderColor = SlateBorder,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = creatorName,
                            onValueChange = { creatorName = it },
                            label = { Text("Author Alias") },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PrimaryTeal,
                                unfocusedBorderColor = SlateBorder,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            modifier = Modifier.weight(1.2f),
                            singleLine = true
                        )
                    }

                    // Long description Text area
                    OutlinedTextField(
                        value = longDesc,
                        onValueChange = { longDesc = it },
                        label = { Text("Detailed Project Documentation") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryTeal,
                            unfocusedBorderColor = SlateBorder,
                            focusedLabelColor = PrimaryTeal,
                            unfocusedLabelColor = TextMuted,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        maxLines = 5
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // Zip file dropper box
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(GlowCardBackground)
                            .border(1.dp, PrimaryTeal.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                            .clickable {
                                selectedZipName = "${title.lowercase().replace(" ", "_")}_release.zip"
                            }
                            .padding(20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "📁",
                                fontSize = 32.sp
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = selectedZipName ?: "Tap to select project release ZIP file",
                                color = if (selectedZipName == null) Color.White else SecondaryEmerald,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = if (selectedZipName == null) "COMPILER WILL CRUNCH THE ZIP" else "READY FOR COMPILE & SIGNATURE Verification",
                                color = TextMuted,
                                fontSize = 10.sp,
                                fontFamily = FontFamily.Monospace,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Authenticate and deployment action
                    if (isScanning || selectedZipName != null) {
                        FingerprintScanner(
                            isScanning = isScanning,
                            progress = scanProgress,
                            statusMessage = scanMessage,
                            onTriggerScan = {
                                viewModel.startFingerprintScanning {
                                    viewModel.uploadCustomProject(
                                        name = title,
                                        category = selectedCategory,
                                        techStack = techStack,
                                        description = shortDesc,
                                        longDescription = longDesc,
                                        size = codeSize,
                                        lang = selectedLanguage,
                                        creator = creatorName
                                    )
                                }
                            }
                        )
                    } else {
                        Button(
                            onClick = {
                                if (title.isEmpty()) {
                                    // simple trigger toast validation is handled by enabling checks
                                } else {
                                    selectedZipName = "${title.lowercase().replace(" ", "_")}_release.zip"
                                }
                            },
                            enabled = title.isNotEmpty() && shortDesc.isNotEmpty(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = PrimaryTeal,
                                disabledContainerColor = SlateBorder
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("validate_build_button")
                        ) {
                            Text(
                                text = "VALIDATE SOURCE SYSTEM",
                                color = if (title.isNotEmpty() && shortDesc.isNotEmpty()) DarkBackground else TextMuted,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 1.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
