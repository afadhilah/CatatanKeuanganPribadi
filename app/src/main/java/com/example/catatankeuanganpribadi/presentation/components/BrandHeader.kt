package com.example.catatankeuanganpribadi.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.catatankeuanganpribadi.R

@Composable
fun BrandHeader(
    modifier: Modifier = Modifier,
    version: String = "v1.0"
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFF002B5B)) // Dark blue background from image
            .padding(horizontal = 20.dp, vertical = 18.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Using the provided artharum_icon.png
            Image(
                painter = painterResource(id = R.drawable.artharum_icon),
                contentDescription = "Artharum Logo",
                modifier = Modifier.size(32.dp),
                contentScale = ContentScale.Fit
            )
            
            Spacer(Modifier.width(12.dp))
            
            Text(
                text = "artharum",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Light,
                letterSpacing = 4.sp,
                fontFamily = FontFamily.Serif
            )
        }
        
        // Version Label
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.12f))
                .padding(horizontal = 12.dp, vertical = 4.dp)
        ) {
            Text(
                text = version,
                color = Color.White.copy(alpha = 0.6f),
                style = MaterialTheme.typography.labelSmall,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
