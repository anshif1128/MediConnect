package com.example.ui.patient

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
import com.example.data.repository.HealthcareRepository
import com.example.ui.components.AudioListenButton
import com.example.ui.navigation.NavigationState
import com.example.ui.theme.*

@Composable
fun AyushAssessmentScreen(
    repository: HealthcareRepository,
    navState: NavigationState,
    modifier: Modifier = Modifier
) {
    val ayushData by repository.ayushAssessment.collectAsState()
    var isSaved by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundWhite)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 90.dp)
    ) {
        // Back Header
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
                            text = "AYUSH Clinical Assessment",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = AyushGreen
                        )
                        Text(
                            text = "Ministry of Ayush • All India Institute of Ayurveda",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                }

                AudioListenButton(
                    textToSpeak = "AYUSH Clinical Assessment. Evaluates Prakriti, Agni, Koshtha and Dashavidha Pariksha according to Ayurvedic clinical principles.",
                    navState = navState
                )
            }
        }

        // Institutional Badge Banner
        item {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = AyushGreenLight),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(AyushGreen),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Spa,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Column {
                        Text(
                            text = "National Ayush Morbidity & Standardized Terminology",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1B5E20)
                        )
                        Text(
                            text = "Integrating Traditional Wisdom with Modern EHR Infrastructure",
                            fontSize = 10.sp,
                            color = TextSecondary
                        )
                    }
                }
            }
        }

        // Fundamental Ayurvedic Diagnostic Parameters
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
                    Text(
                        text = "1. Fundamental Ayurvedic Assessment:",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = AyushGreen
                    )

                    AyushParameterRow("Prakriti (Constitution):", ayushData.prakriti)
                    AyushParameterRow("Vikriti (Current Imbalance):", ayushData.vikriti)
                    AyushParameterRow("Agni (Digestive Fire):", ayushData.agni)
                    AyushParameterRow("Koshtha (Bowel Habit):", ayushData.koshtha)
                    AyushParameterRow("Ahara (Diet Pattern):", ayushData.ahara)
                    AyushParameterRow("Vihara (Lifestyle / Sleep):", ayushData.vihara)
                    AyushParameterRow("Nidana (Etiology):", ayushData.nidana)
                    AyushParameterRow("Samprapti (Pathogenesis):", ayushData.samprapti)
                }
            }
        }

        // Dashavidha Pariksha (10-fold Examination)
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
                        text = "2. Dashavidha Pariksha (दशविध परीक्षा):",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = AyushGreen
                    )
                    Text(
                        text = "Comprehensive 10-Fold Clinical Evaluation",
                        fontSize = 11.sp,
                        color = TextMuted
                    )

                    HorizontalDivider(color = BorderSubtle)

                    ayushData.dashavidhaPariksha.forEach { (pariksha, finding) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = pariksha,
                                fontSize = 12.sp,
                                color = TextPrimary,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = finding,
                                fontSize = 12.sp,
                                color = AyushGreen,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // Prescribed Ayurvedic Formulations
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
                        text = "3. Prescribed Ayurvedic Formulations (Aushadhi):",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = AyushGreen
                    )

                    ayushData.ayurvedicFormulations.forEach { formulation ->
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = AyushGreenLight,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "🌿 $formulation",
                                fontSize = 12.sp,
                                color = Color(0xFF1B5E20),
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            repository.saveAyushAssessment(ayushData)
                            isSaved = true
                            navState.playAudio("Ayush assessment saved to patient unified timeline.")
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = AyushGreen),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                    ) {
                        Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(if (isSaved) "✓ Saved to Unified Health Timeline" else "Save AYUSH Record", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun AyushParameterRow(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 3.dp)) {
        Text(text = label, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextSecondary)
        Text(text = value, fontSize = 12.sp, color = TextPrimary, lineHeight = 16.sp)
        HorizontalDivider(color = Color(0xFFF1F5F9), modifier = Modifier.padding(top = 4.dp))
    }
}
