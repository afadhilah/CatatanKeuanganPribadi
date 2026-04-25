package com.example.catatankeuanganpribadi.presentation.splash

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.catatankeuanganpribadi.R

val BrandPrimary = Color(0xFF3D5CFF)
val BgGradientLight = Color(0xFFF0F3FF)
val TextMain = Color(0xFF1A1C1E)
val TextSub = Color(0xFF64748B)

@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit
) {
    var startAnimation by remember { mutableStateOf(false) }

    val breathingAnimation = rememberInfiniteTransition(label = "splash-breath")
    val logoScale by breathingAnimation.animateFloat(
        initialValue = 0.98f,
        targetValue = 1.02f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "logo-scale"
    )

    LaunchedEffect(Unit) {
        startAnimation = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color.White, BgGradientLight)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = 60.dp, y = (-60).dp)
                .size(250.dp)
                .background(BrandPrimary.copy(alpha = 0.1f), CircleShape)
                .blur(70.dp)
        )

        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(x = (-40).dp, y = 40.dp)
                .size(200.dp)
                .background(Color(0xFF00CFE8).copy(alpha = 0.08f), CircleShape)
                .blur(60.dp)
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 32.dp)
        ) {
            AnimatedVisibility(
                visible = startAnimation,
                enter = fadeIn(tween(1200)) + scaleIn(tween(1000, easing = EaseOutBack))
            ) {
                Box(
                    modifier = Modifier
                        .size(180.dp)
                        .scale(logoScale),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.artharum_icon_clean),
                        contentDescription = "Artharum Logo",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                    )
                }
            }

            Spacer(Modifier.height(28.dp))

            AnimatedVisibility(
                visible = startAnimation,
                enter = fadeIn(tween(800, 400)) + slideInVertically(tween(800, 400)) { 40 }
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "artharum",
                        color = TextMain,
                        fontSize = 42.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp,
                        fontFamily = FontFamily.Serif,
                        textAlign = TextAlign.Center
                    )

                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = "Your Personal Finance Partner",
                        color = TextSub,
                        style = MaterialTheme.typography.bodyMedium,
                        letterSpacing = 0.5.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(Modifier.height(64.dp))

            AnimatedVisibility(
                visible = startAnimation,
                enter = fadeIn(tween(800, 1000)) + slideInVertically(tween(800, 1000)) { 20 }
            ) {
                Button(
                    onClick = onSplashFinished,
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(56.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BrandPrimary
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                ) {
                    Text(
                        "Mulai Kelola",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }
        }

        AnimatedVisibility(
            visible = startAnimation,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp),
            enter = fadeIn(tween(1000, 1600))
        ) {
            Text(
                text = "© 2026 Artharum Team",
                style = MaterialTheme.typography.labelMedium,
                color = TextSub.copy(alpha = 0.6f)
            )
        }
    }
}