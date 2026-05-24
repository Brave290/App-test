package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ProjectEntity
import com.example.ui.BraveXViewModel
import com.example.ui.components.ProjectCard
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConsoleScreen(
    viewModel: BraveXViewModel,
    userProjects: List<ProjectEntity>,
    onSelectProject: (ProjectEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    val currentUser by viewModel.currentUser.collectAsState()
    val isLoginLoading by viewModel.isLoginLoading.collectAsState()
    val fingerprintEnabled by viewModel.isFingerprintEnabledInSettings.collectAsState()

    // Interactive switcher
    var isSignUpTab by remember { mutableStateOf(false) }
    var emailInput by remember { mutableStateOf("") }
    var usernameInput by remember { mutableStateOf("") }
    var passwordInput by remember { mutableStateOf("") }
    var showsResetScreen by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        if (currentUser == null) {
            // AUTHENTICATION PROTOCOL LAYER
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.95f)
                        .background(DarkSurface, RoundedCornerShape(16.dp))
                        .border(1.dp, SlateBorder, RoundedCornerShape(16.dp))
                    .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (showsResetScreen) {
                        // Reset Password Layout
                        Text(
                            text = "RESET ACCESS KEY KEYS 🗝",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Enter your email for a high-entropy validation token.",
                            color = TextMuted,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(20.dp))

                        OutlinedTextField(
                            value = emailInput,
                            onValueChange = { emailInput = it },
                            placeholder = { Text("email@developer.com", color = TextMuted) },
                            leadingIcon = { Icon(Icons.Default.Email, "Email", tint = PrimaryTeal) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PrimaryTeal,
                                unfocusedBorderColor = SlateBorder,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            shape = RoundedCornerShape(8.dp),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = { showsResetScreen = false },
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryTeal),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("SEND RESET ENVELOPE", color = DarkBackground, fontWeight = FontWeight.Bold)
                        }
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Back to Sign In",
                            color = PrimaryTeal,
                            fontSize = 12.sp,
                            fontFamily = FontFamily.Monospace,
                            modifier = Modifier
                                .clickable { showsResetScreen = false }
                                .padding(6.dp)
                        )
                    } else {
                        // Sign In & Sign Up Logic
                        Text(
                            text = if (isSignUpTab) "PROVISION DEVELOPER KEY" else "AUTHENICATE ACCESS SHELL",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        if (isLoginLoading) {
                            CircularProgressIndicator(color = PrimaryTeal, modifier = Modifier.size(24.dp))
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "DECRYPTING RSA PAYLOAD...",
                                color = PrimaryTeal,
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        } else {
                            if (isSignUpTab) {
                                OutlinedTextField(
                                    value = usernameInput,
                                    onValueChange = { usernameInput = it },
                                    label = { Text("Developer Signature Name") },
                                    leadingIcon = { Icon(Icons.Default.Person, "User", tint = AccentPurple) },
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = AccentPurple,
                                        unfocusedBorderColor = SlateBorder,
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White
                                    ),
                                    shape = RoundedCornerShape(8.dp),
                                    singleLine = true,
                                    modifier = Modifier.fillMaxWidth().testTag("auth_name_field")
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                            }

                            OutlinedTextField(
                                value = emailInput,
                                onValueChange = { emailInput = it },
                                label = { Text("Secure Email Address") },
                                leadingIcon = { Icon(Icons.Default.Email, "Email", tint = PrimaryTeal) },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = PrimaryTeal,
                                    unfocusedBorderColor = SlateBorder,
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White
                                ),
                                shape = RoundedCornerShape(8.dp),
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth().testTag("auth_email_field")
                            )
                            Spacer(modifier = Modifier.height(10.dp))

                            OutlinedTextField(
                                value = passwordInput,
                                onValueChange = { passwordInput = it },
                                label = { Text("Passcode Matrix") },
                                leadingIcon = { Icon(Icons.Default.Lock, "Pass", tint = SecondaryEmerald) },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = SecondaryEmerald,
                                    unfocusedBorderColor = SlateBorder,
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White
                                ),
                                shape = RoundedCornerShape(8.dp),
                                visualTransformation = PasswordVisualTransformation(),
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Button(
                                onClick = {
                                    viewModel.mockSignIn(
                                        email = emailInput.ifEmpty { "developer@bravex-labs.org" },
                                        name = usernameInput.ifEmpty { "HexGopher" }
                                    )
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isSignUpTab) AccentPurple else PrimaryTeal
                                ),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("submit_auth_button")
                            ) {
                                Text(
                                    text = if (isSignUpTab) "REGISTER PROTOCOL KEY" else "CONNECT DESKTOP COMPILER",
                                    color = DarkBackground,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.SansSerif
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Sub links
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = if (isSignUpTab) "Have account? Sign In" else "Sign Up Account",
                                    color = PrimaryTeal,
                                    fontSize = 11.sp,
                                    fontFamily = FontFamily.Monospace,
                                    modifier = Modifier
                                        .clickable { isSignUpTab = !isSignUpTab }
                                        .padding(4.dp)
                                )

                                Text(
                                    text = "Forgot password?",
                                    color = TextSecondary,
                                    fontSize = 11.sp,
                                    fontFamily = FontFamily.Monospace,
                                    modifier = Modifier
                                        .clickable { showsResetScreen = true }
                                        .padding(4.dp)
                                )
                            }
                        }
                    }
                }
            }
        } else {
            // LOGGED IN USER INTERFACE CONSOLE
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding = PaddingValues(bottom = 90.dp)
            ) {
                item {
                    // Profile Overview Card
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(DarkSurface, GlowCardBackground)
                                )
                            )
                            .border(1.dp, SlateBorder, RoundedCornerShape(16.dp))
                            .padding(20.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Avatar circle with digital symbol
                                Box(
                                    modifier = Modifier
                                        .size(54.dp)
                                        .clip(CircleShape)
                                        .background(PrimaryTeal.copy(alpha = 0.2f))
                                        .border(2.dp, PrimaryTeal, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = currentUser!!.username.take(2).uppercase(),
                                        color = PrimaryTeal,
                                        fontWeight = FontWeight.Black,
                                        fontSize = 18.sp,
                                        fontFamily = FontFamily.Monospace
                                    )
                                }

                                Spacer(modifier = Modifier.width(14.dp))

                                Column {
                                    Text(
                                        text = currentUser!!.username.uppercase(),
                                        color = Color.White,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Black,
                                        fontFamily = FontFamily.Monospace
                                    )
                                    Text(
                                        text = currentUser!!.email,
                                        color = TextSecondary,
                                        fontSize = 12.sp,
                                        fontFamily = FontFamily.Monospace
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Text(
                                text = currentUser!!.bio,
                                color = TextSecondary,
                                fontSize = 12.sp,
                                lineHeight = 16.sp
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            // Stats row
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text("CUSTOM REPOS", color = TextMuted, fontSize = 9.sp, fontFamily = FontFamily.Monospace)
                                    Text("${userProjects.size}", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                                }
                                Column {
                                    Text("RATING STARS", color = TextMuted, fontSize = 9.sp, fontFamily = FontFamily.Monospace)
                                    Text("★ ${currentUser!!.starsReceived}", color = Color(0xFFFFC107), fontSize = 20.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                                }
                                Column {
                                    Text("COMPANY ORG", color = TextMuted, fontSize = 9.sp, fontFamily = FontFamily.Monospace)
                                    Text(currentUser!!.company, color = SecondaryEmerald, fontSize = 14.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, modifier = Modifier.padding(top = 4.dp))
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // Settings toggle
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFF0F202D), RoundedCornerShape(8.dp))
                                    .clickable { viewModel.toggleFingerprintSettings() }
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "Authorize with biometric fingerprint",
                                        color = Color.White,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "Requires scanning verification pass to publish zip templates",
                                        color = TextSecondary,
                                        fontSize = 10.sp
                                    )
                                }
                                Switch(
                                    checked = fingerprintEnabled,
                                    onCheckedChange = { viewModel.toggleFingerprintSettings() },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = PrimaryTeal,
                                        checkedTrackColor = Color(0xFF1F4D5C)
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Sign Out Button
                            Text(
                                text = "DISCONNECT TERMINAL KEY ↩",
                                color = ErrorRed,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                modifier = Modifier
                                    .clickable { viewModel.mockSignOut() }
                                    .padding(vertical = 4.dp)
                            )
                        }
                    }
                }

                item {
                    // Custom Uploads Inventory header
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "YOUR ACTIVE TEMPLATES REPOS 🛠",
                        color = TextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 1.5.sp
                    )
                }

                if (userProjects.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(140.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Your inventory is currently empty.\nPublish custom code packages in the Upload tab.",
                                color = TextMuted,
                                fontSize = 12.sp,
                                fontFamily = FontFamily.Monospace,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                } else {
                    items(userProjects, key = { it.id }) { project ->
                        ProjectCard(
                            project = project,
                            onTab = { onSelectProject(project) },
                            onToggleSave = { viewModel.toggleSaveProject(project) },
                            onDelete = { viewModel.deleteProject(project) }
                        )
                    }
                }
            }
        }
    }
}
