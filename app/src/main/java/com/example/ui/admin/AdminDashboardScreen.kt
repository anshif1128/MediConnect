package com.example.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.repository.HealthcareRepository
import com.example.ui.components.AudioListenButton
import com.example.ui.navigation.NavigationState
import com.example.ui.theme.*

@Composable
fun AdminDashboardScreen(
    repository: HealthcareRepository,
    navState: NavigationState,
    modifier: Modifier = Modifier
) {
    val hospitals by repository.hospitals.collectAsState()
    val accessLogs by repository.accessLogs.collectAsState()
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
                        text = if (isTamil) "நிர்வாகக் கட்டுப்பாட்டு மையம்" else "Hospital & Network Administration",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = GovNavy
                    )
                    Text(
                        text = "Federated Health Information Exchange (HIE-CM)",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                }

                AudioListenButton(
                    textToSpeak = "Hospital administration and federated health gateway dashboard. Real-time audit logs and participating hospital registry.",
                    navState = navState
                )
            }
        }

        // Prototype Disclaimer Banner
        item {
            Card(
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF3C7)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = Color(0xFF92400E),
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "PROTOTYPE / DEMO METRICS — Indicative throughput simulation for SIH evaluation.",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF92400E)
                    )
                }
            }
        }

        // 6 Metric Counters as specified in prompt:
        // 1. Total Registered Patients: 14,820
        // 2. Total Digital Records: 38,940
        // 3. Documents Processed: 29,410
        // 4. Participating Hospitals: 42
        // 5. Records Accessed Across Hospitals: 91,200
        // 6. Average History Retrieval Time: 1.8s
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    AdminMetricCard(
                        title = "Registered Patients",
                        value = "14,820",
                        sub = "+340 this week",
                        icon = Icons.Default.People,
                        modifier = Modifier.weight(1f)
                    )
                    AdminMetricCard(
                        title = "Digital Records",
                        value = "38,940",
                        sub = "FHIR Encounters",
                        icon = Icons.Default.FolderShared,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    AdminMetricCard(
                        title = "Docs Processed",
                        value = "29,410",
                        sub = "OCR & Tokenized",
                        icon = Icons.Default.DocumentScanner,
                        modifier = Modifier.weight(1f)
                    )
                    AdminMetricCard(
                        title = "Network Hospitals",
                        value = "42",
                        sub = "Govt Medical Centers",
                        icon = Icons.Default.LocalHospital,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    AdminMetricCard(
                        title = "Cross-Hospital Access",
                        value = "91,200",
                        sub = "Federated Queries",
                        icon = Icons.Default.SyncAlt,
                        modifier = Modifier.weight(1f)
                    )
                    AdminMetricCard(
                        title = "Retrieval Time",
                        value = "1.8s",
                        sub = "Sub-2s Query Latency",
                        icon = Icons.Default.Speed,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Participating Government Hospitals Directory
        item {
            Text(
                text = "Participating Government Health Facilities:",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = GovNavy
            )
        }

        items(hospitals) { hospital ->
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = hospital.name,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = GovNavy,
                            modifier = Modifier.weight(1f)
                        )
                        Surface(
                            color = VerifiedGreenLight,
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = "ACTIVE NODE",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = VerifiedGreen,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Text(
                        text = "📍 ${hospital.city}, ${hospital.state} • Nodal Officer: ${hospital.nodalOfficer}",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                    Text(
                        text = "Departments: ${hospital.departments.joinToString(", ")}",
                        fontSize = 11.sp,
                        color = TextMuted
                    )
                    Text(
                        text = "Registered Doctors: ${hospital.activeDoctorsCount} | Facility ID: ${hospital.id}",
                        fontSize = 10.sp,
                        color = MedicalTeal,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        // Live Real-Time Audit Log
        item {
            Text(
                text = "Live Audit Trail (Cross-Hospital Record Access):",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = GovNavy
            )
        }

        items(accessLogs) { log ->
            Card(
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    verticalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "MHID: ${log.patientId}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = GovNavy
                        )
                        Text(
                            text = log.timestamp,
                            fontSize = 10.sp,
                            color = TextMuted
                        )
                    }
                    Text(
                        text = "${log.doctorName} (${log.department}) at ${log.hospitalName}",
                        fontSize = 11.sp,
                        color = TextPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Reason: ${log.purpose} • Result: ${log.details}",
                        fontSize = 10.sp,
                        color = TextSecondary
                    )
                }
            }
        }
    }
}

@Composable
private fun AdminMetricCard(
    title: String,
    value: String,
    sub: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    fontSize = 11.sp,
                    color = TextSecondary,
                    fontWeight = FontWeight.SemiBold
                )
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MedicalTeal,
                    modifier = Modifier.size(16.dp)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = value,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = GovNavy
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = sub,
                fontSize = 10.sp,
                color = VerifiedGreen,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
