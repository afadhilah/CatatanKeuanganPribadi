package com.example.catatankeuanganpribadi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.catatankeuanganpribadi.presentation.FinanceApp
import com.example.catatankeuanganpribadi.ui.theme.ArtharumTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ArtharumTheme {
                FinanceApp()
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun FinanceAppPreview() {
    ArtharumTheme {
        FinanceApp()
    }
}