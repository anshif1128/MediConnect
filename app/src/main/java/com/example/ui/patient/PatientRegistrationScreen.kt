package com.example.ui.patient

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.ui.navigation.NavigationState
import com.example.ui.theme.*

@Composable
fun PatientRegistrationScreen(
    repository: HealthcareRepository,
    navState: NavigationState,
    modifier: Modifier = Modifier
) {
    val patient by repository.patient.collectAsState()
    var currentStep by remember { mutableIntStateOf(1) } // Steps 1 to 5

    var name by remember { mutableStateOf(patient.name) }
    var age by remember { mutableStateOf(patient.age.toString()) }
    var gender by remember { mutableStateOf(patient.gender) }
    var phone by remember { mutableStateOf(patient.phone) }
    var preferredLanguage by remember { mutableStateOf(patient.preferredLanguage) }
    var bloodGroup by remember { mutableStateOf(patient.bloodGroup) }

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
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navState.backToMain() }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = GovNavy)
                }
                Column {
                    Text(
                        text = "Patient Digital Registration",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = GovNavy
                    )
                    Text(
                        text = "Step $currentStep of 5: ${getStepTitle(currentStep)}",
                        fontSize = 12.sp,
                        color = MedicalTeal,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        // Progress Indicator Bar
        item {
            LinearProgressIndicator(
                progress = { currentStep / 5f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp),
                color = MedicalTeal,
                trackColor = Color(0xFFE2E8F0)
            )
        }

        // Step Content
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
                    when (currentStep) {
                        1 -> {
                            Text("STEP 1 — Basic Details", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = GovNavy)

                            OutlinedTextField(
                                value = name,
                                onValueChange = { name = it },
                                label = { Text("Full Name") },
                                modifier = Modifier.fillMaxWidth()
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                OutlinedTextField(
                                    value = age,
                                    onValueChange = { age = it },
                                    label = { Text("Age (Yrs)") },
                                    modifier = Modifier.weight(1f)
                                )
                                OutlinedTextField(
                                    value = gender,
                                    onValueChange = { gender = it },
                                    label = { Text("Gender") },
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            OutlinedTextField(
                                value = phone,
                                onValueChange = { phone = it },
                                label = { Text("Mobile Number") },
                                modifier = Modifier.fillMaxWidth()
                            )

                            OutlinedTextField(
                                value = bloodGroup,
                                onValueChange = { bloodGroup = it },
                                label = { Text("Blood Group") },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        2 -> {
                            Text("STEP 2 — Health History", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = GovNavy)
                            Text("Pre-existing conditions & Allergies", fontSize = 12.sp, color = TextSecondary)

                            Surface(
                                color = Color(0xFFF8FAFC),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Text("• Essential Hypertension (Diagnosed 2024)", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                                    Text("• Borderline Hyperlipidemia (Lipid profile elevated)", fontSize = 12.sp)
                                    Text("• Allergy: Penicillin (Mild Urticarial Rash)", fontSize = 12.sp, color = EmergencyRed, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        3 -> {
                            Text("STEP 3 — Medical Reports", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = GovNavy)
                            Text("3 Digitized Health Records Found on Government Gateway", fontSize = 12.sp, color = TextSecondary)

                            Surface(
                                color = MedicalTealLight,
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Text("✓ Biochemistry Lab Panel (GGH Coimbatore)", fontSize = 12.sp, color = GovNavy)
                                    Text("✓ OPD Cardiology Prescription Slip", fontSize = 12.sp, color = GovNavy)
                                    Text("✓ 2D Echocardiography Study", fontSize = 12.sp, color = GovNavy)
                                }
                            }
                        }

                        4 -> {
                            Text("STEP 4 — Review Profile", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = GovNavy)

                            Surface(
                                color = Color(0xFFF1F5F9),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Text("Name: $name", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                    Text("Age/Gender: $age Yrs / $gender", fontSize = 12.sp)
                                    Text("Phone: $phone", fontSize = 12.sp)
                                    Text("Blood Group: $bloodGroup", fontSize = 12.sp)
                                    Text("Demo Health ID: ${patient.healthId}", fontSize = 12.sp, color = MedicalTeal, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        5 -> {
                            Text("STEP 5 — Consent & Share", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = GovNavy)
                            Surface(
                                color = VerifiedGreenLight,
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "✓ By proceeding, you consent to store your medical record securely and share it with authorized doctors across participating government hospitals upon verification.",
                                    fontSize = 12.sp,
                                    color = VerifiedGreen,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(10.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Buttons: BACK, NEXT, SAVE & CONTINUE LATER
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (currentStep > 1) {
                            OutlinedButton(
                                onClick = { currentStep-- },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("BACK")
                            }
                        }

                        Button(
                            onClick = {
                                if (currentStep < 5) {
                                    currentStep++
                                } else {
                                    repository.updatePatient(
                                        name = name,
                                        age = age.toIntOrNull() ?: 45,
                                        gender = gender,
                                        phone = phone,
                                        lang = preferredLanguage
                                    )
                                    navState.backToMain()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MedicalTeal),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(if (currentStep == 5) "FINISH" else "NEXT")
                        }
                    }

                    OutlinedButton(
                        onClick = { navState.backToMain() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("SAVE & CONTINUE LATER", fontSize = 11.sp)
                    }
                }
            }
        }
    }
}

private fun getStepTitle(step: Int): String = when (step) {
    1 -> "Basic Details"
    2 -> "Health History"
    3 -> "Medical Reports"
    4 -> "Review Profile"
    5 -> "Consent & Share"
    else -> ""
}
