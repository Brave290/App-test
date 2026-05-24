package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.PrimaryTeal
import com.example.ui.theme.SecondaryEmerald
import com.example.ui.theme.SlateBorder
import com.example.ui.theme.TextMuted

@Composable
fun FingerprintScanner(
    isScanning: Boolean,
    progress: Float,
    statusMessage: String,
    onTriggerScan: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "FingerprintScanLines")
    
    // Laser line scanning sweep animation
    val laserYOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "LaserY"
    )

    // Breathing pulse for glowing rings
    val glowPulse by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "GlowPulse"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(DarkSurface, RoundedCornerShape(16.dp))
            .border(1.dp, SlateBorder, RoundedCornerShape(16.dp))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "DEVELOPER AUTHSIG REQUIRED",
            color = Color.White,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp,
            fontFamily = FontFamily.Monospace
        )
        
        Spacer(modifier = Modifier.height(6.dp))
        
        Text(
            text = "Hold down fingerprint sensor to sign deployment ZIP package",
            color = TextMuted,
            fontSize = 10.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier.padding(horizontal = 8.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Large glowing biometric circle touch pad
        Box(
            modifier = Modifier
                .size(140.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = if (isScanning) {
                            listOf(PrimaryTeal.copy(alpha = 0.25f * glowPulse), Color.Transparent)
                        } else {
                            listOf(Color(0xFF141E2D), Color.Transparent)
                        },
                        radius = 210f
                    )
                )
                .border(
                    width = 2.dp,
                    color = if (isScanning) PrimaryTeal.copy(alpha = glowPulse) else SlateBorder,
                    shape = CircleShape
                )
                .clickable(!isScanning) { onTriggerScan() }
                .testTag("fingerprint_sensor_pad"),
            contentAlignment = Alignment.Center
        ) {
            // Draw a detailed visual biometric layout
            Canvas(modifier = Modifier.size(90.dp)) {
                val center = Offset(size.width / 2f, size.height / 2f)
                val canvasWidth = size.width
                val canvasHeight = size.height

                // Draw 4 curved concentric biometric arches
                val strokeColor = if (isScanning) PrimaryTeal else TextMuted
                val strokeWidth = 3f

                for (i in 1..4) {
                    val radius = (canvasWidth / 2f) * (i / 4f)
                    drawArc(
                        color = strokeColor.copy(alpha = if (isScanning) 1.0f - (i * 0.15f) else 0.4f),
                        startAngle = 180f,
                        sweepAngle = 180f,
                        useCenter = false,
                        topLeft = Offset(center.x - radius, center.y - radius),
                        size = Size(radius * 2, radius * 2),
                        style = Stroke(width = strokeWidth + i, cap = StrokeCap.Round)
                    )
                    
                    drawArc(
                        color = strokeColor.copy(alpha = if (isScanning) 1.0f - (i * 0.15f) else 0.4f),
                        startAngle = 00f,
                        sweepAngle = 140f,
                        useCenter = false,
                        topLeft = Offset(center.x - radius, center.y - radius + 5),
                        size = Size(radius * 2, radius * 2),
                        style = Stroke(width = strokeWidth + i, cap = StrokeCap.Round)
                    )
                }

                // Scan line laser brush (cyan beam sweeper)
                if (isScanning) {
                    val laserY = canvasHeight * laserYOffset
                    drawLine(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color.Transparent,
                                SecondaryEmerald,
                                PrimaryTeal,
                                SecondaryEmerald,
                                Color.Transparent
                            )
                        ),
                        start = Offset(0f, laserY),
                        end = Offset(canvasWidth, laserY),
                        strokeWidth = 5f
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Progress bar for scanning progress
        if (isScanning) {
            Box(
                modifier = Modifier
                    .width(180.dp)
                    .height(4.dp)
                    .background(Color(0xFF1F2937), RoundedCornerShape(2.dp))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(progress)
                        .background(
                            Brush.horizontalGradient(
                                listOf(PrimaryTeal, SecondaryEmerald)
                            ),
                            RoundedCornerShape(2.dp)
                        )
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
        }

        // Status messages and scanning stats
        Text(
            text = statusMessage.uppercase(),
            color = if (statusMessage.contains("VERIFIED")) SecondaryEmerald else PrimaryTeal,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            letterSpacing = 1.sp
        )
    }
}
