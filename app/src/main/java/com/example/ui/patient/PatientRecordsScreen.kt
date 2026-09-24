package com.example.ui.patient

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
import com.example.data.model.DocumentCategory
import com.example.data.model.MedicalDocument
import com.example.data.repository.HealthcareRepository
import com.example.ui.components.AudioListenButton
import com.example.ui.navigation.NavigationState
import com.example.ui.navigation.PatientTab
import com.example.ui.theme.*

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PatientRecordsScreen(
    repository: HealthcareRepository,
    navState: NavigationState,
    modifier: Modifier = Modifier
) {
    val documents by repository.documents.collectAsState()
    val isTamil = navState.isTamil
    var selectedCategoryFilter by remember { mutableStateOf<DocumentCategory?>(null) }

    val filteredDocs = remember(documents, selectedCategoryFilter) {
        if (selectedCategoryFilter == null) documents
        else documents.filter { it.category == selectedCategoryFilter }
    }

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
                        text = if (isTamil) "என் மருத்துவ ஆவணங்கள்" else "My Medical Records",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = GovNavy
                    )
                    Text(
                        text = "${documents.size} Digitized Records Across Government Hospitals",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                }

                AudioListenButton(
                    textToSpeak = if (isTamil) "உங்கள் அனைத்து மருத்துவ ஆவணங்களும் இங்கே பட்டியலிடப்பட்டுள்ளன."
                    else "All your digitized medical records, prescriptions, and laboratory reports from government hospitals.",
                    navState = navState
                )
            }
        }

        // Filter chips
        item {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                FilterChip(
                    selected = selectedCategoryFilter == null,
                    onClick = { selectedCategoryFilter = null },
                    label = { Text("All Records (${documents.size})", fontSize = 11.sp) }
                )
                DocumentCategory.values().forEach { cat ->
                    val count = documents.count { it.category == cat }
                    if (count > 0) {
                        FilterChip(
                            selected = selectedCategoryFilter == cat,
                            onClick = { selectedCategoryFilter = cat },
                            label = { Text("${cat.label} ($count)", fontSize = 11.sp) }
                        )
                    }
                }
            }
        }

        // Upload shortcut button
        item {
            OutlinedButton(
                onClick = { navState.patientTab = PatientTab.UPLOAD },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (isTamil) "+ புதிய ஆவணத்தை பதிவேற்றுக" else "+ Upload / Scan New Report",
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Document Cards
        items(filteredDocs) { doc ->
            MedicalDocumentCard(doc = doc)
        }
    }
}

@Composable
private fun MedicalDocumentCard(doc: MedicalDocument) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = MedicalTealLight,
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = doc.category.label,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = MedicalTeal,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
                Text(doc.date, fontSize = 11.sp, color = TextMuted)
            }

            Text(
                text = doc.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Text(
                text = "Facility: ${doc.hospitalName}",
                fontSize = 11.sp,
                color = TextSecondary
            )

            // Extracted values preview
            if (doc.extractedValues.isNotEmpty()) {
                Surface(
                    color = Color(0xFFF8FAFC),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "AI-Extracted Diagnostic Values:",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = GovNavy
                        )
                        doc.extractedValues.forEach { (k, v) ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(k, fontSize = 11.sp, color = TextPrimary)
                                Text(v, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MedicalTeal)
                            }
                        }
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "OCR Confidence: ${(doc.ocrConfidence * 100).toInt()}%",
                    fontSize = 10.sp,
                    color = VerifiedGreen,
                    fontWeight = FontWeight.SemiBold
                )

                Surface(
                    color = Color(0xFFEFF6FF),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = "Encrypted in Transit",
                        fontSize = 9.sp,
                        color = Color(0xFF2563EB),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
        }
    }
}
