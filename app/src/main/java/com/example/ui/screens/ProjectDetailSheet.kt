package com.example.ui.screens

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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ProjectEntity
import com.example.ui.BraveXViewModel
import com.example.ui.theme.*

@Composable
fun ProjectDetailSheet(
    project: ProjectEntity,
    viewModel: BraveXViewModel,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val activeFile by viewModel.activeFileSelected.collectAsState()
    val scrollState = rememberScrollState()

    // Determine simulated folder structures based on tech stacks
    val files = when (project.language) {
        "TypeScript" -> listOf("page.tsx", "package.json", "prisma.schema")
        "Go" -> listOf("main.go", "config.yaml", "service.proto")
        "Dart" -> listOf("main.dart", "pubspec.yaml", "bloc.dart")
        "Rust" -> listOf("lib.rs", "Cargo.toml", "canvas.js")
        else -> listOf("MainActivity.kt", "ProjectDao.kt", "build.gradle")
    }

    // High fidelity mock code structures to read in console
    val activeCodeContent = when (activeFile) {
        "page.tsx" -> """
            import { prisma } from "@/lib/db";
            import { Stripe } from "@/lib/billing";
            
            export default async function Dashboard() {
              const users = await prisma.user.findMany();
              return (
                <div className="bg-slate-950 text-emerald-400 p-8">
                  <h1>BraveX Web Console Starter</h1>
                  <span className="text-xs">Database Count: {users.length}</span>
                </div>
              );
            }
        """.trimIndent()
        "package.json" -> """
            {
              "name": "nextjs-saas-starter",
              "version": "1.1.0",
              "private": true,
              "dependencies": {
                "next": "^15.0.1",
                "react": "19.0.0",
                "prisma": "^6.1.0",
                "stripe": "^15.3.0"
              }
            }
        """.trimIndent()
        "prisma.schema" -> """
            datasource db {
              provider = "postgresql"
              url      = env("DATABASE_URL")
            }
            
            model User {
              id        String   @id @default(uuid())
              email     String   @unique
              createdAt DateTime @default(now())
              isActive  Boolean  @default(true)
            }
        """.trimIndent()
        "main.go" -> """
            package main
            
            import (
                "log"
                "net"
                "google.golang.org/grpc"
            )
            
            func main() {
                lis, err := net.Listen("tcp", ":9090")
                if err != nil {
                    log.Fatalf("Core Socket Error: %v", err)
                }
                server := grpc.NewServer()
                log.Println("gRPC Matrix booted successfully on :9090")
                server.Serve(lis)
            }
        """.trimIndent()
        "config.yaml" -> """
            server:
              port: 9090
              environment: production
            database:
              host: postgres-cluster.db
              connection_timeout: 5s
              pool_limit: 50
        """.trimIndent()
        "service.proto" -> """
            syntax = "proto3";
            package bravex;
            
            service TemplateService {
              rpc QueryTemplate (ConfigRequest) returns (ConfigResponse);
            }
            
            message ConfigRequest {
              string template_id = 1;
            }
            
            message ConfigResponse {
              string blob_hash = 1;
              int64 file_size = 2;
            }
        """.trimIndent()
        "main.dart" -> """
            import 'package:flutter/material.dart';
            import 'package:google_generative_ai/google_generative_ai.dart';
            
            void main() {
              runApp(const MaterialApp(
                home: ChatCompanionShell(),
              ));
            }
        """.trimIndent()
        "pubspec.yaml" -> """
            name: ai_chat_companion
            description: Fully reactive Flutter chatbot integration.
            version: 3.1.0
            dependencies:
              flutter:
                sdk: flutter
              google_generative_ai: ^0.4.0
              flutter_bloc: ^8.1.0
        """.trimIndent()
        "bloc.dart" -> """
            import 'package:flutter_bloc/flutter_bloc.dart';
            
            class CompanionBloc extends Bloc<ChatEvent, ChatState> {
              CompanionBloc() : super(InitialState()) {
                on<MessageReceived>((event, emit) async {
                  emit(AwaitingAIResponse());
                  // Call Gemini system layers
                });
              }
            }
        """.trimIndent()
        "lib.rs" -> """
            use wasm_bindgen::prelude::*;
            
            #[wasm_bindgen]
            pub fn compute_particle_matrix(width: f64, height: f64) -> f64 {
                let system_vectors = width * height;
                println!("Rust Core computing math: {}", system_vectors);
                system_vectors
            }
        """.trimIndent()
        "Cargo.toml" -> """
            [package]
            name = "wasm-canvas-engine"
            version = "2.3.0"
            authors = ["CyberCore"]
            edition = "2021"
            
            [lib]
            crate-type = ["cdylib", "rlib"]
            
            [dependencies]
            wasm-bindgen = "0.2.8"
        """.trimIndent()
        "canvas.js" -> """
            import init, { compute_particle_matrix } from './wasm_canvas_engine.js';
            
            async function bootSystem() {
              await init();
              const vectorSize = compute_particle_matrix(1024, 768);
              console.log("Interactive Canvas vector size: " + vectorSize);
            }
        """.trimIndent()
        "ProjectDao.kt" -> """
            package com.example.data
            
            import androidx.room.*
            import kotlinx.coroutines.flow.Flow
            
            @Dao
            interface ProjectDao {
                @Query("SELECT * FROM projects")
                fun getAllProjects(): Flow<List<ProjectEntity>>
            }
        """.trimIndent()
        "build.gradle" -> """
            plugins {
                alias(libs.plugins.android.application)
                alias(libs.plugins.kotlin.compose)
            }
            
            android {
                namespace = "com.bravex.boilerplate"
                compileSdk = 36
            }
        """.trimIndent()
        else -> """
            package com.example
            
            import android.os.Bundle
            import androidx.activity.ComponentActivity
            
            class MainActivity : ComponentActivity() {
                override fun onCreate(savedInstanceState: Bundle?) {
                    super.onCreate(savedInstanceState)
                    // High performance loading...
                }
            }
        """.trimIndent()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(16.dp)
            .testTag("project_detail_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(bottom = 32.dp)
        ) {
            // Dismiss and Bookmark Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .background(DarkSurface, RoundedCornerShape(50))
                        .testTag("back_to_list")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close details",
                        tint = Color.White
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { viewModel.toggleSaveProject(project) },
                        modifier = Modifier
                            .background(DarkSurface, RoundedCornerShape(50))
                            .testTag("detail_toggle_save")
                    ) {
                        Icon(
                            imageVector = if (project.isSaved) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Save Project",
                            tint = if (project.isSaved) PrimaryTeal else Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Categories list banner
            Text(
                text = project.category.uppercase(),
                color = AccentPurple,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Main Title
            Text(
                text = project.title,
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Author and Date stats
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "AUTHOR: ",
                    color = TextMuted,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace
                )
                Text(
                    text = project.creator,
                    color = PrimaryTeal,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "DENSE LAYER STACK",
                    color = SecondaryEmerald,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier
                        .background(Color(0xFF0F2618), RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Complete Technical Overview description
            Text(
                text = "TECHNICAL SPECIFICATION DOCUMENT",
                color = TextSecondary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                letterSpacing = 1.5.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = project.longDescription,
                color = TextSecondary,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Grid metrics boxes
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(DarkSurface, RoundedCornerShape(8.dp))
                        .border(1.dp, SlateBorder, RoundedCornerShape(8.dp))
                        .padding(12.dp)
                ) {
                    Column {
                        Text("POPULARITY", color = TextMuted, fontSize = 9.sp, fontFamily = FontFamily.Monospace)
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Star, "stars", tint = Color(0xFFFFC107), modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("${project.stars} Units", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(DarkSurface, RoundedCornerShape(8.dp))
                        .border(1.dp, SlateBorder, RoundedCornerShape(8.dp))
                        .padding(12.dp)
                ) {
                    Column {
                        Text("MAPPED VOLUME", color = TextMuted, fontSize = 9.sp, fontFamily = FontFamily.Monospace)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(project.codeSize, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                    }
                }

                Box(
                    modifier = Modifier
                        .weight(1.2f)
                        .background(DarkSurface, RoundedCornerShape(8.dp))
                        .border(1.dp, SlateBorder, RoundedCornerShape(8.dp))
                        .padding(12.dp)
                ) {
                    Column {
                        Text("COMPILATION ENGINE", color = TextMuted, fontSize = 9.sp, fontFamily = FontFamily.Monospace)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(project.language.uppercase(), color = SecondaryEmerald, fontSize = 13.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // SYNTAX HIGHLIGHTED MOCK DIRECTORY BROWSER
            Text(
                text = "INTERACTIVE CODE CONFIGURATION MATRIX",
                color = TextSecondary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                letterSpacing = 1.5.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // File selection tabs
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(files) { file ->
                    val isSelected = activeFile == file
                    Box(
                        modifier = Modifier
                            .background(
                                color = if (isSelected) Color(0xFF142435) else Color.Transparent,
                                shape = RoundedCornerShape(6.dp)
                            )
                            .border(
                                width = 1.dp,
                                color = if (isSelected) PrimaryTeal else SlateBorder,
                                shape = RoundedCornerShape(6.dp)
                            )
                            .clickable { viewModel.selectActiveFile(file) }
                            .testTag("file_tab_$file")
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = file,
                            color = if (isSelected) PrimaryTeal else TextSecondary,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Monospace black terminal reader console
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF030712))
                    .border(1.dp, SlateBorder, RoundedCornerShape(8.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Box(modifier = Modifier.size(8.dp).clip(RoundedCornerShape(50)).background(Color(0xFFFF5F56)))
                            Box(modifier = Modifier.size(8.dp).clip(RoundedCornerShape(50)).background(Color(0xFFFFBD2E)))
                            Box(modifier = Modifier.size(8.dp).clip(RoundedCornerShape(50)).background(Color(0xFF27C93F)))
                        }
                        Text(
                            text = "OFFLINE COMPILATION READY",
                            color = SecondaryEmerald.copy(alpha = 0.7f),
                            fontSize = 8.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = activeCodeContent,
                        color = Color(0xFFA5F3FC), // light cyan color code output
                        fontSize = 12.sp,
                        lineHeight = 16.sp,
                        fontFamily = FontFamily.Monospace,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Action: Download Boilerplate
            Button(
                onClick = { /* Simulated local download launcher */ },
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryTeal),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("install_template_button")
            ) {
                Text(
                    text = "DOWNLOAD ZIP SOURCE PACKAGE",
                    color = DarkBackground,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.0.sp
                )
            }
        }
    }
}
