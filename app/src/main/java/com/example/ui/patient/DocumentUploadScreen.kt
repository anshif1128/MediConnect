package com.example.ui.patient

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ai.DefaultAiHealthcareService
import com.example.data.model.DocumentCategory
import com.example.data.model.MedicalDocument
import com.example.data.repository.HealthcareRepository
import com.example.ui.components.AudioListenButton
import com.example.ui.navigation.NavigationState
import com.example.ui.navigation.PatientTab
import com.example.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun DocumentUploadScreen(
    repository: HealthcareRepository,
    navState: NavigationState,
    modifier: Modifier = Modifier
) {
    val aiService = remember { DefaultAiHealthcareService() }
    val scope = rememberCoroutineScope()
    val isTamil = navState.isTamil

    var isProcessing by remember { mutableStateOf(false) }
    var uploadedSuccessfully by remember { mutableStateOf(false) }
    var extractedDoc by remember { mutableStateOf<MedicalDocument?>(null) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .padding(horizontal = 18.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 18.dp, bottom = 100.dp)
    ) {
        // Simple Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = if (isTamil) "காகித ஆவணங்களை ஸ்கேன் செய்க" else "Add Medical Report",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = GovNavy
                    )
                    Text(
                        text = if (isTamil) "புகைப்படம் எடுத்து எளிதாக சேர்க்கலாம்" else "Take a photo or pick a document",
                        fontSize = 13.sp,
                        color = TextSecondary
                    )
                }

                AudioListenButton(
                    textToSpeak = if (isTamil) "உங்கள் மருத்துவ காகிதங்களை புகைப்படம் எடுத்து பதிவேற்றலாம். ரத்த பரிசோதனை அளவுகளை தானாகவே படித்துவிடும்."
                    else "Take a clear picture of your prescription or blood test report. The system will automatically read and organize the values for your doctor.",
                    navState = navState
                )
            }
        }

        // 2 Big Senior Action Buttons
        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                // Large Photo Button
                Button(
                    onClick = {
                        scope.launch {
                            isProcessing = true
                            uploadedSuccessfully = false
                            val doc = aiService.processOcrDocument(
                                "Sample Lab Report - Blood Glucose & Lipid Panel (Coimbatore)",
                                DocumentCategory.LAB_REPORT
                            )
                            extractedDoc = doc
                            isProcessing = false
                            uploadedSuccessfully = true
                            navState.playAudio("Report scanned successfully! Blood sugar and hemoglobin values extracted.")
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MedicalTeal),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.PhotoCamera,
                        contentDescription = null,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = if (isTamil) "1. புகைப்படம் எடுக்கவும் (Camera)" else "1. Take Photo of Report (Camera)",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }

                // Pick from Phone Button
                OutlinedButton(
                    onClick = {
                        scope.launch {
                            isProcessing = true
                            uploadedSuccessfully = false
                            val doc = aiService.processOcrDocument(
                                "ECG & Cardiology Evaluation Summary (Coimbatore GGH)",
                                DocumentCategory.IMAGING_REPORT
                            )
                            extractedDoc = doc
                            isProcessing = false
                            uploadedSuccessfully = true
                        }
                    },
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.FolderOpen,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isTamil) "2. கோப்பினை தேர்ந்தெடுக்கவும் (File)" else "2. Pick File from Phone (PDF / Image)",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Processing Spinner
        if (isProcessing) {
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        CircularProgressIndicator(color = MedicalTeal, strokeWidth = 3.dp)
                        Column {
                            Text(
                                text = if (isTamil) "ஆவணத்தை படித்துக்கொண்டிருக்கிறது..." else "Reading medical document...",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = GovNavy
                            )
                            Text(
                                text = "Extracting lab values and doctor handwriting...",
                                fontSize = 12.sp,
                                color = TextSecondary
                            )
                        }
                    }
                }
            }
        }

        // Extracted Document Review Card (Senior Accessible)
        extractedDoc?.let { doc ->
            item {
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
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "✓ Read Successfully!",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = VerifiedGreen
                            )
                            Surface(
                                color = Color(0xFFEFF6FF),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = doc.category.label,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1D4ED8),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Text(
                            text = doc.title,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = GovNavy
                        )

                        Text(
                            text = "Hospital: ${doc.hospitalName}",
                            fontSize = 13.sp,
                            color = TextSecondary
                        )

                        // Extracted Key Values
                        Surface(
                            color = Color(0xFFF8FAFC),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = "Extracted Diagnostic Values:",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = GovNavy
                                )

                                doc.extractedValues.forEach { (k, v) ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(k, fontSize = 13.sp, color = TextPrimary)
                                        Text(v, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = MedicalTeal)
                                    }
                                }
                            }
                        }

                        // Big Confirm & Save Button
                        Button(
                            onClick = {
                                repository.addDocument(doc)
                                navState.patientTab = PatientTab.RECORDS
                                navState.playAudio("Document saved into your permanent health record.")
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = VerifiedGreen),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                        ) {
                            Icon(Icons.Default.Save, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isTamil) "மருத்துவ கோப்பில் சேமிக்கவும்" else "Save to My Records",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }
                }
            }
        }
    }
}
