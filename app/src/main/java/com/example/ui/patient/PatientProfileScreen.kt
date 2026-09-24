package com.example.ui.patient

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.repository.HealthcareRepository
import com.example.ui.components.AudioListenButton
import com.example.ui.components.DemoHealthIdCard
import com.example.ui.navigation.ActiveScreen
import com.example.ui.navigation.NavigationState
import com.example.ui.theme.*

@Composable
fun PatientProfileScreen(
    repository: HealthcareRepository,
    navState: NavigationState,
    modifier: Modifier = Modifier
) {
    val patient by repository.patient.collectAsState()
    val isTamil = navState.isTamil

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundWhite)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 90.dp)
    ) {
        // Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = if (isTamil) "நோயாளி சுயவிவரம்" else "Patient Digital Profile",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = GovNavy
                    )
                    Text(
                        text = "ABDM Linked Citizen Record",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                }

                AudioListenButton(
                    textToSpeak = if (isTamil) "நோயாளி சுயவிவரம் மற்றும் அவசர தொடர்பு எண்கள்."
                    else "Patient digital profile, emergency contacts, and hackathon presentation guide.",
                    navState = navState
                )
            }
        }

        // Health ID Card
        item {
            DemoHealthIdCard(
                patient = patient,
                isTamil = isTamil,
                onAudioClick = {
                    navState.playAudio("Patient name: ${patient.name}, Health ID: ${patient.healthId}")
                }
            )
        }

        // Profile Details Card
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text("Demographic Information", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = GovNavy)

                    ProfileRow("Full Name", patient.name)
                    ProfileRow("Age & Gender", "${patient.age} Years • ${patient.gender}")
                    ProfileRow("Phone Number", patient.phone)
                    ProfileRow("Blood Group", patient.bloodGroup)
                    ProfileRow("ABHA Address", patient.abhaAddress)
                    ProfileRow("Emergency Contact", "${patient.emergencyContactName} (${patient.emergencyContactPhone})")
                }
            }
        }

        // Allergies & Chronic Conditions
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text("Medical Vulnerabilities & Alerts", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = GovNavy)

                    Surface(
                        color = EmergencyRedLight,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text("⚠️ Documented Drug Allergies:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = EmergencyRed)
                            patient.allergies.forEach { allergy ->
                                Text("• $allergy", fontSize = 12.sp, color = TextPrimary)
                            }
                        }
                    }

                    Surface(
                        color = Color(0xFFF8FAFC),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text("Chronic Health Conditions:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = GovNavy)
                            patient.chronicConditions.forEach { cond ->
                                Text("• $cond", fontSize = 12.sp, color = TextPrimary)
                            }
                        }
                    }
                }
            }
        }

        // SIH 26047 5-Minute Pitch Presentation Guide (Super valuable for Smart India Hackathon presentation)
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MedicalTealLight),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(MedicalTeal)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.EmojiEvents,
                            contentDescription = null,
                            tint = MedicalTeal,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "SIH 26047: 5-Minute Judge Demo Guide",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = GovNavy
                        )
                    }

                    Text(
                        text = "Follow this exact sequence when presenting to the Ministry of Ayush / SIH jury:",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )

                    PitchStepItem("1", "Patient Home", "Show Arun Kumar's MHID card with active consent and multilingual switch.")
                    PitchStepItem("2", "AI Case-Taking", "Demonstrate voice input & SOCRATES questions for chest pain, triggering the emergency red-flag alert.")
                    PitchStepItem("3", "OCR Digitization", "Simulate paper lab report OCR tokenizing Hb and fasting blood glucose.")
                    PitchStepItem("4", "Ayush Module", "Show AIIA Prakriti, Agni, Koshtha and Dashavidha Pariksha integration.")
                    PitchStepItem("5", "Doctor Hospital B", "Switch to Dr. Priya Sharma at Madurai. Search MHID to instantly retrieve Coimbatore records!")
                    PitchStepItem("6", "AI Structured Draft", "Show physician draft with Confirm/Edit/Reject buttons, avoiding autonomous diagnosis.")
                    PitchStepItem("7", "Add Consultation", "Prescribe new meds from Madurai; watch patient timeline update instantaneously.")
                }
            }
        }

        // Edit Profile Action
        item {
            Button(
                onClick = { navState.navigateTo(ActiveScreen.REGISTRATION_WIZARD) },
                colors = ButtonDefaults.buttonColors(containerColor = MedicalTeal),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp)
            ) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = null)
                Spacer(modifier = Modifier.width(6.dp))
                Text("Edit Profile & Demographics", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun ProfileRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontSize = 12.sp, color = TextSecondary)
        Text(value, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = GovNavy)
    }
    HorizontalDivider(color = Color(0xFFF1F5F9))
}

@Composable
private fun PitchStepItem(num: String, stepTitle: String, stepDesc: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("$num.", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MedicalTeal)
        Column {
            Text(stepTitle, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = GovNavy)
            Text(stepDesc, fontSize = 11.sp, color = TextSecondary, lineHeight = 15.sp)
        }
    }
}
