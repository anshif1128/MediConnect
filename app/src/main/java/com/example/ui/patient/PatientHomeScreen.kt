package com.example.ui.patient

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.repository.HealthcareRepository
import com.example.ui.components.AudioListenButton
import com.example.ui.components.DemoHealthIdCard
import com.example.ui.navigation.ActiveScreen
import com.example.ui.navigation.NavigationState
import com.example.ui.navigation.PatientTab
import com.example.ui.theme.*

@Composable
fun PatientHomeScreen(
    repository: HealthcareRepository,
    navState: NavigationState,
    modifier: Modifier = Modifier
) {
    val patient by repository.patient.collectAsState()
    val isTamil = navState.isTamil

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .padding(horizontal = 18.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp),
        contentPadding = PaddingValues(top = 18.dp, bottom = 100.dp)
    ) {
        // Warm, Large Friendly Greeting
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (isTamil) "வணக்கம், ${patient.name}! 🙏" else "Namaste, ${patient.name}! 🙏",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = GovNavy
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (isTamil) "அரசு மருத்துவமனைகளில் உங்கள் மருத்துவ ஆவணங்கள் அனைத்தும் பாதுகாப்பாக உள்ளன."
                            else "All your medical files are safely linked across government hospitals.",
                            fontSize = 13.sp,
                            color = TextSecondary,
                            lineHeight = 18.sp
                        )
                    }

                    AudioListenButton(
                        textToSpeak = if (isTamil) "வணக்கம் அருண் குமார். உங்கள் அனைத்து அரசு மருத்துவ ஆவணங்களும் இங்கே கிடைக்கின்றன."
                        else "Hello Arun Kumar. Your government health records and prescriptions are securely saved here.",
                        navState = navState
                    )
                }
            }
        }

        // Demo Health ID Card (Large, High Contrast)
        item {
            DemoHealthIdCard(
                patient = patient,
                isTamil = isTamil,
                onAudioClick = {
                    navState.playAudio(
                        if (isTamil) "உங்கள் அடையாள எண் ${patient.healthId}. பெயர்: ${patient.name}. வயது: ${patient.age}."
                        else "Your Health ID is ${patient.healthId}. Patient name is ${patient.name}, age 45 years, Blood group O positive."
                    )
                }
            )
        }

        // Section Title: Simple Patient Services
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = if (isTamil) "முக்கிய சேவைகள் (எளிய முறை)" else "What would you like to do?",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = GovNavy
                )
                Text(
                    text = if (isTamil) "தொட்டுப் பார்க்கவும்" else "Tap any card",
                    fontSize = 12.sp,
                    color = TextMuted
                )
            }
        }

        // 4 LARGE, EASY-TO-UNDERSTAND ACTION TILES (Spacious, Uncluttered, Senior-Friendly)

        // TILE 1: Tell Doctor What Hurts (AI Voice & Case Taking)
        item {
            SeniorServiceCard(
                title = if (isTamil) "1. எங்கு வலிக்கிறது என்று சொல்லுங்கள்" else "1. Tell Us What Hurts (Voice & Touch)",
                description = if (isTamil) "குரல் அல்லது தொடுதிரை மூலம் உங்கள் பிரச்சனையை சொல்லலாம். மருத்துவர் பரிசோதிக்க உதவும்."
                else "Answer simple questions with your voice. Prepares an accurate summary for your doctor.",
                icon = Icons.Default.Hearing,
                badgeText = if (isTamil) "குரல் உதவி" else "SPEAK TO AI",
                themeColor = MedicalTeal,
                bgColor = Color(0xFFE6FFFA),
                onClick = { navState.navigateTo(ActiveScreen.AI_CASE_TAKING) }
            )
        }

        // TILE 2: My Medical Papers & Tests (Records / OCR)
        item {
            SeniorServiceCard(
                title = if (isTamil) "2. என் மருத்துவ ஆவணங்கள் & அறிக்கைகள்" else "2. My Medical Papers & Test Reports",
                description = if (isTamil) "ரத்த பரிசோதனை, ஈசிஜி மற்றும் மருந்து சீட்டுகள். பழைய ஆவணங்களை கேமரா மூலம் சேர்க்கலாம்."
                else "View past blood tests, ECG reports, and prescriptions. Scan paper reports with your camera.",
                icon = Icons.Default.Description,
                badgeText = if (isTamil) "3 ஆவணங்கள்" else "3 SAVED REPORTS",
                themeColor = Color(0xFF1D4ED8),
                bgColor = Color(0xFFEFF6FF),
                onClick = { navState.patientTab = PatientTab.RECORDS }
            )
        }

        // TILE 3: My Hospital Visits Timeline (Hospital A to Hospital B)
        item {
            SeniorServiceCard(
                title = if (isTamil) "3. என் மருத்துவமனை வருகைகள்" else "3. My Hospital Visits (Coimbatore ➔ Madurai)",
                description = if (isTamil) "கோயம்புத்தூர் மற்றும் மதுரை அரசு மருத்துவமனைகளில் நீங்கள் பெற்ற சிகிச்சைகள்."
                else "See your complete treatment history across Coimbatore and Madurai government hospitals.",
                icon = Icons.Default.History,
                badgeText = if (isTamil) "2024 - 2026" else "TIMELINE",
                themeColor = Color(0xFFC2410C),
                bgColor = Color(0xFFFFF7ED),
                onClick = { navState.patientTab = PatientTab.TIMELINE }
            )
        }

        // TILE 4: Who Can See My Records (Consent Manager)
        item {
            SeniorServiceCard(
                title = if (isTamil) "4. என் ஆவணங்களை யார் பார்க்கலாம்?" else "4. Who Can See My Records? (Privacy)",
                description = if (isTamil) "அரசு மருத்துவர்கள் உங்கள் ஆவணங்களை பார்க்க நீங்கள் அனுமதி கொடுக்கலாம் அல்லது ரத்து செய்யலாம்."
                else "You control which doctors can see your medical history. Simple Yes / No permission toggles.",
                icon = Icons.Default.Shield,
                badgeText = if (isTamil) "பாதுகாப்பானது" else "PRIVATE & SAFE",
                themeColor = VerifiedGreen,
                bgColor = Color(0xFFF0FDF4),
                onClick = { navState.navigateTo(ActiveScreen.CONSENT_MANAGER) }
            )
        }

        // Senior-Friendly Emergency Alert Card
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = EmergencyRedLight),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(EmergencyRed.copy(alpha = 0.5f))),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { navState.navigateTo(ActiveScreen.AI_CASE_TAKING) }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(EmergencyRed),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Emergency,
                            contentDescription = "Emergency Alert",
                            tint = Color.White,
                            modifier = Modifier.size(30.dp)
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (isTamil) "அவசர மருத்துவ உதவி தேவையா?" else "Need Urgent Medical Help?",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = EmergencyRed
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = if (isTamil) "கடுமையான நெஞ்சு வலி, மூச்சுத் திணறல் இருந்தால் உடனே தட்டவும்."
                            else "Severe chest pain or difficulty breathing? Tap here to alert triage hospital staff immediately.",
                            fontSize = 13.sp,
                            color = TextPrimary,
                            lineHeight = 17.sp
                        )
                    }

                    Icon(
                        imageVector = Icons.Default.ArrowForwardIos,
                        contentDescription = null,
                        tint = EmergencyRed,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        // Additional Specialized Tools (Ayush, Registration, System Specs)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "Additional Clinical Services:",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextSecondary
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = { navState.navigateTo(ActiveScreen.AYUSH_ASSESSMENT) },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                    ) {
                        Icon(Icons.Default.Spa, contentDescription = null, tint = AyushGreen, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("AYUSH / AIIA", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = AyushGreen)
                    }

                    OutlinedButton(
                        onClick = { navState.navigateTo(ActiveScreen.ABDM_SANDBOX) },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                    ) {
                        Icon(Icons.Default.Api, contentDescription = null, tint = MedicalTeal, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("ABDM & FHIR", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MedicalTeal)
                    }
                }
            }
        }
    }
}

@Composable
private fun SeniorServiceCard(
    title: String,
    description: String,
    icon: ImageVector,
    badgeText: String,
    themeColor: Color,
    bgColor: Color,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Large Friendly Icon Box
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(bgColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = themeColor,
                    modifier = Modifier.size(32.dp)
                )
            }

            // Text content
            Column(modifier = Modifier.weight(1f)) {
                Surface(
                    color = bgColor,
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = badgeText,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = themeColor,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = GovNavy,
                    lineHeight = 20.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    fontSize = 13.sp,
                    color = TextSecondary,
                    lineHeight = 17.sp
                )
            }

            Icon(
                imageVector = Icons.Default.ArrowForwardIos,
                contentDescription = "Open",
                tint = Color(0xFF94A3B8),
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
