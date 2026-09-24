package com.example.ui.patient

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.TimelineEvent
import com.example.data.repository.HealthcareRepository
import com.example.ui.components.AudioListenButton
import com.example.ui.navigation.NavigationState
import com.example.ui.theme.*

@Composable
fun TimelineScreen(
    repository: HealthcareRepository,
    navState: NavigationState,
    modifier: Modifier = Modifier
) {
    val events by repository.timeline.collectAsState()
    val isTamil = navState.isTamil

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .padding(horizontal = 18.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 18.dp, bottom = 100.dp)
    ) {
        // Simple Senior-Friendly Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = if (isTamil) "என் மருத்துவ வரலாறு" else "My Hospital Visits",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = GovNavy
                    )
                    Text(
                        text = if (isTamil) "கோயம்புத்தூர் & மதுரை அரசு மருத்துவமனைகள்" else "Coimbatore & Madurai Govt Hospitals",
                        fontSize = 13.sp,
                        color = TextSecondary
                    )
                }

                AudioListenButton(
                    textToSpeak = if (isTamil) "உங்கள் மருத்துவமனை வருகைகள் இங்கே காட்டப்பட்டுள்ளன. கோயம்புத்தூர் மருத்துவமனையில் செய்யப்பட்ட சிகிச்சைகள் மதுரை மருத்துவமனை மருத்துவருக்கும் தெரியும்."
                    else "Your hospital visits are shown here. Treatments from Coimbatore Hospital are automatically visible to your doctor in Madurai.",
                    navState = navState
                )
            }
        }

        // Visual "How it helps you" Card
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE0F2FE)),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(MedicalTeal)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(MedicalTeal),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Sync, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp))
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (isTamil) "பழைய காகிதங்கள் தொலைந்துவிட்டதா? கவலை வேண்டாம்!" else "Never lose your medical history again!",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = GovNavy
                        )
                        Text(
                            text = if (isTamil) "நீங்கள் எந்த அரசு மருத்துவமனைக்குச் சென்றாலும், மருத்துவர் முந்தைய பரிசோதனைகளை உடனே பார்க்க முடியும்."
                            else "Whichever government hospital you visit, the doctor sees your past tests and prescriptions instantly.",
                            fontSize = 12.sp,
                            color = TextSecondary,
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }

        // Timeline Cards (One for each major visit)
        items(events) { event ->
            SeniorTimelineCard(event = event, isTamil = isTamil)
        }
    }
}

@Composable
private fun SeniorTimelineCard(
    event: TimelineEvent,
    isTamil: Boolean
) {
    val isMadurai = event.hospitalName.contains("Madurai")
    val hospitalBadgeColor = if (isMadurai) Color(0xFFC2410C) else Color(0xFF1D4ED8)
    val hospitalBgColor = if (isMadurai) Color(0xFFFFF7ED) else Color(0xFFEFF6FF)

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Header Row: Hospital Location & Date
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = hospitalBgColor,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Place,
                            contentDescription = null,
                            tint = hospitalBadgeColor,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isMadurai) "Hospital B (Madurai)" else "Hospital A (Coimbatore)",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = hospitalBadgeColor
                        )
                    }
                }

                Text(
                    text = event.date,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }

            // Title (Large)
            Text(
                text = event.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold,
                color = GovNavy
            )

            // Hospital & Doctor
            Text(
                text = "${event.hospitalName} • ${event.doctorName}",
                fontSize = 12.sp,
                color = TextSecondary
            )

            // Description
            Text(
                text = event.description,
                fontSize = 13.sp,
                color = TextPrimary,
                lineHeight = 18.sp
            )

            // Lab results if present
            if (event.labValues.isNotEmpty()) {
                Surface(
                    color = Color(0xFFF1F5F9),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("🧪 Test Results / ஆய்வக முடிவுகள்:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = GovNavy)
                        event.labValues.forEach { (test, result) ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(test, fontSize = 12.sp, color = TextPrimary)
                                Text(result, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MedicalTeal)
                            }
                        }
                    }
                }
            }

            // Prescriptions if present
            if (event.prescriptionSummary.isNotEmpty()) {
                Surface(
                    color = Color(0xFFF0FDF4),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("💊 Prescribed Medicines / மருந்துகள்:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = VerifiedGreen)
                        event.prescriptionSummary.forEach { med ->
                            Text("• $med", fontSize = 12.sp, color = TextPrimary)
                        }
                    }
                }
            }
        }
    }
}
