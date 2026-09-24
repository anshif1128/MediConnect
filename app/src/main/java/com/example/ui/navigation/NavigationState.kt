package com.example.ui.navigation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.data.model.UserRole

enum class PatientTab {
    HOME,
    RECORDS,
    UPLOAD,
    TIMELINE,
    PROFILE
}

enum class ActiveScreen {
    MAIN_VIEW,
    REGISTRATION_WIZARD,
    AI_CASE_TAKING,
    CONSENT_MANAGER,
    AYUSH_ASSESSMENT,
    ABDM_SANDBOX,
    SYSTEM_ARCHITECTURE
}

class NavigationState {
    var currentRole by mutableStateOf(UserRole.PATIENT)
    var patientTab by mutableStateOf(PatientTab.HOME)
    var activeScreen by mutableStateOf(ActiveScreen.MAIN_VIEW)

    // Senior citizen accessible mode
    var isSeniorMode by mutableStateOf(true)

    // Dialog state for role switcher
    var showRoleDialog by mutableStateOf(false)

    // Doctor context
    var selectedDoctorHospital by mutableStateOf("Government Medical College Hospital - Madurai")
    var selectedDoctorName by mutableStateOf("Dr. Priya Sharma, MD, DM")
    var doctorSearchQuery by mutableStateOf("MHID-2026-00125")
    var doctorPatientFound by mutableStateOf(true)
    var doctorSelectedTab by mutableStateOf(0) // 0: Overview, 1: History, 2: Reports, 3: Rx, 4: Timeline, 5: Consent

    // Language context
    var isTamil by mutableStateOf(false)

    // Audio guidance playback simulation state
    var isAudioPlaying by mutableStateOf(false)
    var audioMessage by mutableStateOf<String?>(null)

    fun navigateTo(screen: ActiveScreen) {
        activeScreen = screen
    }

    fun backToMain() {
        activeScreen = ActiveScreen.MAIN_VIEW
    }

    fun switchRole(role: UserRole) {
        currentRole = role
        activeScreen = ActiveScreen.MAIN_VIEW
        showRoleDialog = false
    }

    fun playAudio(text: String) {
        audioMessage = text
        isAudioPlaying = true
    }

    fun stopAudio() {
        isAudioPlaying = false
        audioMessage = null
    }
}
