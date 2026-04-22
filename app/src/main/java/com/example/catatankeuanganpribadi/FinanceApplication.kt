package com.example.catatankeuanganpribadi

import android.app.Application
import com.example.catatankeuanganpribadi.di.AppContainer

class FinanceApplication : Application() {
    val container by lazy { AppContainer(this) }
}
