package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.AudioListenButton
import com.example.ui.navigation.NavigationState
import com.example.ui.theme.*

@Composable
fun SystemArchitectureScreen(
    navState: NavigationState,
    modifier: Modifier = Modifier
) {
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
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(onClick = { navState.backToMain() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = GovNavy)
                    }
                    Column {
                        Text(
                            text = "System Architecture & Flow",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = GovNavy
                        )
                        Text(
                            text = "SIH Problem Statement ID 26047 Specification",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                }

                AudioListenButton(
                    textToSpeak = "System architecture diagram illustrating the seven federated tiers from patient kiosk to doctor workstation.",
                    navState = navState
                )
            }
        }

        // Architecture Pipeline Overview
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
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "End-to-End Federated Health Architecture:",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = GovNavy
                    )

                    ArchStepCard("1", "Patient Interface", "Mobile App / Physical Kiosk Intake", "Touch & voice enabled multilingual registration, SOCRATES symptom recording, and document upload.", Icons.Default.Smartphone)
                    ArchStepCard("2", "Central Consent Gateway", "Granular ABDM Consent Manager", "Validates explicit digital consent tokens before initiating any cross-hospital data transaction.", Icons.Default.Security)
                    ArchStepCard("3", "AI Case-Taking Engine", "SOCRATES Protocol Structuring", "Extracts onset, character, site, radiation, severity, and checks emergency red-flag triggers.", Icons.Default.SmartToy)
                    ArchStepCard("4", "Document OCR Service", "Diagnostic Parameter Extraction", "Digitizes paper prescriptions and lab reports; tokenizes Hb, glucose, ECG, echo values.", Icons.Default.DocumentScanner)
                    ArchStepCard("5", "Clinical Summary Generator", "Physician Draft Formatting", "Synthesizes multi-hospital longitudinal timeline into structured HPI, past history, and allergies.", Icons.Default.Summarize)
                    ArchStepCard("6", "Federated Health Network", "HIE-CM Decentralized Routing", "Encrypted point-to-point FHIR transport between Hospital A (Coimbatore) and Hospital B (Madurai).", Icons.Default.Hub)
                    ArchStepCard("7", "Doctor Clinical Dashboard", "Physician Verification & Rx Entry", "Allows doctor to confirm/edit AI drafts, review history across hospitals, and issue prescriptions.", Icons.Default.LocalHospital)
                }
            }
        }

        // Technical Specifications
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
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Technical Implementation Specifications:",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = GovNavy
                    )

                    TechSpecItem("Client Architecture", "100% Kotlin Jetpack Compose (Modern Material 3)")
                    TechSpecItem("Data Interoperability", "FHIR R4 (Fast Healthcare Interoperability Resources)")
                    TechSpecItem("National Health Standard", "ABDM Electronic Health Record (EHR) & ABHA Protocol")
                    TechSpecItem("Traditional Medicine", "Ministry of Ayush / AIIA Standardized Clinical Terminology")
                    TechSpecItem("Privacy Compliance", "Zero Plaintext Leakage • Explicit Patient Opt-In • Immutable Audit Logs")
                    TechSpecItem("Data Locality", "Federated Hospital Repositories (Hospital A & B Distributed Storage)")
                }
            }
        }
    }
}

@Composable
private fun ArchStepCard(
    stepNum: String,
    title: String,
    subTitle: String,
    desc: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Surface(
        color = Color(0xFFF8FAFC),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(MedicalTeal),
                contentAlignment = Alignment.Center
            ) {
                Text(stepNum, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = GovNavy)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(subTitle, fontSize = 10.sp, color = MedicalTeal, fontWeight = FontWeight.SemiBold)
                }
                Text(desc, fontSize = 11.sp, color = TextSecondary, lineHeight = 15.sp)
            }
        }
    }
}

@Composable
private fun TechSpecItem(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontSize = 11.sp, color = TextSecondary, fontWeight = FontWeight.SemiBold)
        Text(value, fontSize = 11.sp, color = GovNavy, fontWeight = FontWeight.Bold)
    }
    HorizontalDivider(color = Color(0xFFF1F5F9))
}
