package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserRole
import com.example.data.repository.HealthcareRepository
import com.example.ui.admin.AdminDashboardScreen
import com.example.ui.components.AudioGuidanceNotification
import com.example.ui.components.GovernmentHeader
import com.example.ui.doctor.DoctorDashboardScreen
import com.example.ui.navigation.ActiveScreen
import com.example.ui.navigation.NavigationState
import com.example.ui.navigation.PatientTab
import com.example.ui.patient.*
import com.example.ui.screens.AbdmSandboxScreen
import com.example.ui.screens.SystemArchitectureScreen
import com.example.ui.theme.GovNavy
import com.example.ui.theme.MedicalTeal
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MediConnectApp()
            }
        }
    }
}

@Composable
fun MediConnectApp(
    repository: HealthcareRepository = remember { HealthcareRepository.instance },
    navState: NavigationState = remember { NavigationState() }
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .testTag("mediconnect_scaffold"),
        topBar = {
            Column {
                GovernmentHeader(navState = navState)
                AudioGuidanceNotification(
                    message = navState.audioMessage,
                    isPlaying = navState.isAudioPlaying,
                    onDismiss = { navState.stopAudio() }
                )
            }
        },
        bottomBar = {
            // Show bottom navigation bar only when on patient main view
            if (navState.currentRole == UserRole.PATIENT && navState.activeScreen == ActiveScreen.MAIN_VIEW) {
                NavigationBar(
                    containerColor = Color.White,
                    contentColor = GovNavy,
                    tonalElevation = 8.dp
                ) {
                    NavigationBarItem(
                        selected = navState.patientTab == PatientTab.HOME,
                        onClick = { navState.patientTab = PatientTab.HOME },
                        icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                        label = { Text(if (navState.isTamil) "முகப்பு" else "Home", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MedicalTeal,
                            selectedTextColor = MedicalTeal,
                            indicatorColor = Color(0xFFE0F2FE)
                        ),
                        modifier = Modifier.testTag("nav_home")
                    )

                    NavigationBarItem(
                        selected = navState.patientTab == PatientTab.RECORDS,
                        onClick = { navState.patientTab = PatientTab.RECORDS },
                        icon = { Icon(Icons.Default.FolderShared, contentDescription = "Records") },
                        label = { Text(if (navState.isTamil) "ஆவணங்கள்" else "Records", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MedicalTeal,
                            selectedTextColor = MedicalTeal,
                            indicatorColor = Color(0xFFE0F2FE)
                        ),
                        modifier = Modifier.testTag("nav_records")
                    )

                    NavigationBarItem(
                        selected = navState.patientTab == PatientTab.UPLOAD,
                        onClick = { navState.patientTab = PatientTab.UPLOAD },
                        icon = { Icon(Icons.Default.CloudUpload, contentDescription = "Upload") },
                        label = { Text(if (navState.isTamil) "பதிவேற்றம்" else "Upload", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MedicalTeal,
                            selectedTextColor = MedicalTeal,
                            indicatorColor = Color(0xFFE0F2FE)
                        ),
                        modifier = Modifier.testTag("nav_upload")
                    )

                    NavigationBarItem(
                        selected = navState.patientTab == PatientTab.TIMELINE,
                        onClick = { navState.patientTab = PatientTab.TIMELINE },
                        icon = { Icon(Icons.Default.Timeline, contentDescription = "Timeline") },
                        label = { Text(if (navState.isTamil) "காலவரிசை" else "Timeline", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MedicalTeal,
                            selectedTextColor = MedicalTeal,
                            indicatorColor = Color(0xFFE0F2FE)
                        ),
                        modifier = Modifier.testTag("nav_timeline")
                    )

                    NavigationBarItem(
                        selected = navState.patientTab == PatientTab.PROFILE,
                        onClick = { navState.patientTab = PatientTab.PROFILE },
                        icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                        label = { Text(if (navState.isTamil) "சுயவிவரம்" else "Profile", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MedicalTeal,
                            selectedTextColor = MedicalTeal,
                            indicatorColor = Color(0xFFE0F2FE)
                        ),
                        modifier = Modifier.testTag("nav_profile")
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                // Secondary / Sub-flows
                navState.activeScreen == ActiveScreen.REGISTRATION_WIZARD -> {
                    PatientRegistrationScreen(repository = repository, navState = navState)
                }
                navState.activeScreen == ActiveScreen.AI_CASE_TAKING -> {
                    AiCaseTakingScreen(repository = repository, navState = navState)
                }
                navState.activeScreen == ActiveScreen.CONSENT_MANAGER -> {
                    ConsentManagerScreen(repository = repository, navState = navState)
                }
                navState.activeScreen == ActiveScreen.AYUSH_ASSESSMENT -> {
                    AyushAssessmentScreen(repository = repository, navState = navState)
                }
                navState.activeScreen == ActiveScreen.ABDM_SANDBOX -> {
                    AbdmSandboxScreen(navState = navState)
                }
                navState.activeScreen == ActiveScreen.SYSTEM_ARCHITECTURE -> {
                    SystemArchitectureScreen(navState = navState)
                }

                // Primary views based on User Role
                navState.currentRole == UserRole.PATIENT -> {
                    when (navState.patientTab) {
                        PatientTab.HOME -> PatientHomeScreen(repository = repository, navState = navState)
                        PatientTab.RECORDS -> PatientRecordsScreen(repository = repository, navState = navState)
                        PatientTab.UPLOAD -> DocumentUploadScreen(repository = repository, navState = navState)
                        PatientTab.TIMELINE -> TimelineScreen(repository = repository, navState = navState)
                        PatientTab.PROFILE -> PatientProfileScreen(repository = repository, navState = navState)
                    }
                }

                navState.currentRole == UserRole.DOCTOR -> {
                    DoctorDashboardScreen(repository = repository, navState = navState)
                }

                navState.currentRole == UserRole.ADMIN -> {
                    AdminDashboardScreen(repository = repository, navState = navState)
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(text = "MediConnect: $name", modifier = modifier)
}
