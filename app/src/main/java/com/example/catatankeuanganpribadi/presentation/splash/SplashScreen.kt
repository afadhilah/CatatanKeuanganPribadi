package com.example.catatankeuanganpribadi.presentation.splash

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.catatankeuanganpribadi.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit
) {
    var showButton by remember { mutableStateOf(false) }
    
    val infiniteTransition = rememberInfiniteTransition(label = "splash")
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.98f,
        targetValue = 1.02f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    LaunchedEffect(Unit) {
        delay(1500) // Delay sedikit sebelum tombol muncul
        showButton = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background), // Sesuaikan dengan warna background UI (DeepNavy)
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.artharum_icon),
                contentDescription = "Artharum Logo",
                modifier = Modifier
                    .size(140.dp)
                    .scale(scale)
            )
            
            Spacer(Modifier.height(24.dp))
            
            Text(
                text = "artharum",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 36.sp,
                fontWeight = FontWeight.Light,
                letterSpacing = 8.sp,
                fontFamily = FontFamily.Serif
            )
            
            Spacer(Modifier.height(8.dp))
            
            Text(
                text = "Personal Finance Manager",
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                style = MaterialTheme.typography.bodySmall,
                letterSpacing = 2.sp
            )
            
            Spacer(Modifier.height(64.dp))
            
            AnimatedVisibility(
                visible = showButton,
                enter = fadeIn() + expandVertically()
            ) {
                Button(
                    onClick = onSplashFinished,
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .height(54.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(
                        "Mulai",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
