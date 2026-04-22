package com.example.catatankeuanganpribadi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.catatankeuanganpribadi.presentation.FinanceApp
import com.example.catatankeuanganpribadi.ui.theme.CatatanKeuanganPribadiTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CatatanKeuanganPribadiTheme {
                FinanceApp()
            }
        }
    }
}