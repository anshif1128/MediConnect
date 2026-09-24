package com.example.ui.doctor

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.data.model.ConsentStatus
import com.example.data.repository.HealthcareRepository
import com.example.ui.components.AudioListenButton
import com.example.ui.components.StatusBadge
import com.example.ui.navigation.ActiveScreen
import com.example.ui.navigation.NavigationState
import com.example.ui.patient.ConsentManagerScreen
import com.example.ui.patient.TimelineScreen
import com.example.ui.theme.*

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DoctorDashboardScreen(
    repository: HealthcareRepository,
    navState: NavigationState,
    modifier: Modifier = Modifier
) {
    val patient by repository.patient.collectAsState()
    val timeline by repository.timeline.collectAsState()
    val documents by repository.documents.collectAsState()
    val consents by repository.consents.collectAsState()
    val aiSummary by repository.aiClinicalSummary.collectAsState()

    var searchQuery by remember { mutableStateOf("MHID-2026-00125") }
    var searchExecuted by remember { mutableStateOf(true) }

    // Doctor tabs: 0: Overview, 1: History & AI Summary, 2: Reports, 3: Prescriptions, 4: Timeline, 5: Consent
    var selectedTab by remember { mutableIntStateOf(navState.doctorSelectedTab) }

    // Dialog for adding consultation
    var showAddConsultationDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundWhite)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 90.dp)
    ) {
        // Doctor Identification Bar
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = GovNavy),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(MedicalTeal),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.MedicalServices,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Column {
                                Text(
                                    text = navState.selectedDoctorName,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = navState.selectedDoctorHospital,
                                    fontSize = 11.sp,
                                    color = Color(0xFFCAD5E2)
                                )
                            }
                        }

                        Surface(
                            color = MedicalTeal,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "HOSPITAL B",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider(color = Color(0x33FFFFFF))
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Department: Cardiology OPD • Token: #08",
                            fontSize = 11.sp,
                            color = Color(0xFFA0AEC0)
                        )
                        Text(
                            text = "HIE-CM Federated Gateway: ONLINE",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF81E6D9)
                        )
                    }
                }
            }
        }

        // Search Patient Bar (Key SIH flow item)
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
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Search Patient by Health ID / ABHA Address:",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = GovNavy
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text("e.g. MHID-2026-00125") },
                            leadingIcon = {
                                Icon(Icons.Default.Search, contentDescription = null, tint = MedicalTeal)
                            },
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )

                        Button(
                            onClick = {
                                searchExecuted = true
                                navState.playAudio("Patient record retrieved across participating government hospitals.")
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MedicalTeal),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.height(52.dp)
                        ) {
                            Text("Search", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Section 12: Cross-Hospital Record Access Banner
        if (searchExecuted) {
            item {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Color(0xFF86EFAC))),
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
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = VerifiedGreen,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Patient Record Found (Cross-Hospital History)",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = VerifiedGreen
                                )
                            }

                            Surface(
                                color = VerifiedGreen,
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = "Access Granted",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }

                        HorizontalDivider(color = Color(0xFFBBF7D0))

                        // Cross Hospital Breakdown details
                        Text(
                            text = "Originating Hospital A: Government General Hospital - Coimbatore",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = GovNavy
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            HospitalStatBadge("Departments", "Cardiology")
                            HospitalStatBadge("Consultations", "3 Records")
                            HospitalStatBadge("Lab Reports", "${documents.size} Files")
                            HospitalStatBadge("Rx History", "4 Items")
                        }

                        Text(
                            text = "✓ Patient medical history automatically migrated from Coimbatore to Madurai with valid patient consent.",
                            fontSize = 11.sp,
                            color = Color(0xFF166534),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // Quick Patient Demographics Card
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
                            .padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = patient.name,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Text(
                                    text = "${patient.age} Yrs • ${patient.gender} • Blood Group: ${patient.bloodGroup}",
                                    fontSize = 12.sp,
                                    color = TextSecondary
                                )
                            }

                            Button(
                                onClick = { showAddConsultationDialog = true },
                                colors = ButtonDefaults.buttonColors(containerColor = MedicalTeal),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("+ Add Consultation", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        // Allergy Warning Chip
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(EmergencyRedLight, RoundedCornerShape(6.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(Icons.Default.Warning, contentDescription = null, tint = EmergencyRed, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "CRITICAL ALLERGY: ${patient.allergies.joinToString(", ")}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = EmergencyRed
                            )
                        }
                    }
                }
            }

            // Section 13: Doctor Tabs
            // OVERVIEW | HISTORY & AI SUMMARY | REPORTS | PRESCRIPTIONS | TIMELINE | CONSENT
            item {
                ScrollableTabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = SurfaceWhite,
                    contentColor = MedicalTeal,
                    edgePadding = 0.dp
                ) {
                    listOf("Overview", "AI Summary", "Reports & Labs", "Prescriptions", "Timeline", "Consent").forEachIndexed { index, tabTitle ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = { Text(tabTitle, fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                        )
                    }
                }
            }

            // Tab Contents
            when (selectedTab) {
                0 -> {
                    // Overview Tab
                    item {
                        DoctorOverviewSection(patient = patient, repository = repository)
                    }
                }

                1 -> {
                    // History & AI Structured Clinical Summary (Section 10)
                    item {
                        AiClinicalSummarySection(
                            summary = aiSummary,
                            doctorName = navState.selectedDoctorName,
                            repository = repository,
                            navState = navState
                        )
                    }
                }

                2 -> {
                    // Reports & Lab Values
                    item {
                        DoctorReportsSection(documents = documents)
                    }
                }

                3 -> {
                    // Prescriptions
                    item {
                        DoctorPrescriptionsSection()
                    }
                }

                4 -> {
                    // Timeline
                    item {
                        TimelineScreen(repository = repository, navState = navState)
                    }
                }

                5 -> {
                    // Consent
                    item {
                        ConsentManagerScreen(repository = repository, navState = navState)
                    }
                }
            }
        }
    }

    // Add Consultation Dialog (Section 14)
    if (showAddConsultationDialog) {
        AddConsultationDialog(
            hospitalName = navState.selectedDoctorHospital,
            doctorName = navState.selectedDoctorName,
            onDismiss = { showAddConsultationDialog = false },
            onSave = { diag, notes, rxs, advice ->
                repository.addHospitalBConsultation(
                    hospitalName = navState.selectedDoctorHospital,
                    doctorName = navState.selectedDoctorName,
                    department = "Cardiology OPD",
                    diagnosis = diag,
                    notes = notes,
                    prescriptions = rxs,
                    advice = advice
                )
                showAddConsultationDialog = false
                navState.playAudio("Consultation added successfully. Patient's timeline updated across hospitals.")
            }
        )
    }
}

@Composable
private fun HospitalStatBadge(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = GovNavy)
        Text(label, fontSize = 10.sp, color = TextSecondary)
    }
}

@Composable
private fun DoctorOverviewSection(
    patient: com.example.data.model.Patient,
    repository: HealthcareRepository
) {
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
            Text("Clinical Summary & Vitals", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = GovNavy)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                VitalItem("Blood Pressure", "144/92 mmHg", "Elevated")
                VitalItem("Heart Rate", "82 bpm", "Regular")
                VitalItem("SpO2", "98%", "Normal")
                VitalItem("BMI", "24.6", "Normal")
            }

            HorizontalDivider(color = BorderSubtle)

            Text("Active Chronic Problems:", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            patient.chronicConditions.forEach { cond ->
                Text("• $cond (Treated at GGH Coimbatore)", fontSize = 12.sp, color = TextPrimary)
            }

            HorizontalDivider(color = BorderSubtle)

            Text("Current Medication Adherence:", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Text("• Tab. Amlodipine 5mg OD (Regular)", fontSize = 12.sp, color = TextSecondary)
            Text("• Tab. Atorvastatin 10mg HS (Regular)", fontSize = 12.sp, color = TextSecondary)
        }
    }
}

@Composable
private fun VitalItem(label: String, value: String, status: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, fontSize = 10.sp, color = TextMuted)
        Text(value, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = GovNavy)
        Text(status, fontSize = 10.sp, color = MedicalTeal, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun AiClinicalSummarySection(
    summary: com.example.data.model.AiClinicalSummary,
    doctorName: String,
    repository: HealthcareRepository,
    navState: NavigationState
) {
    var isEditing by remember { mutableStateOf(false) }
    var editedHpi by remember { mutableStateOf(summary.historyOfPresentIllness) }

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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "AI STRUCTURED CLINICAL SUMMARY",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = GovNavy
                )

                Surface(
                    color = Color(0xFFFEF3C7),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = summary.status,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF92400E),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Surface(
                color = WarningAmberLight,
                shape = RoundedCornerShape(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Physician verification required before medical decisions. The AI does not diagnose.",
                    fontSize = 11.sp,
                    color = WarningAmber,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(6.dp)
                )
            }

            SummaryRow("Chief Complaint:", summary.chiefComplaint)

            if (isEditing) {
                OutlinedTextField(
                    value = editedHpi,
                    onValueChange = { editedHpi = it },
                    label = { Text("Edit History of Present Illness") },
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                SummaryRow("History of Present Illness (HPI):", summary.historyOfPresentIllness)
            }

            SummaryRow("Past Medical History:", summary.pastMedicalHistory)
            SummaryRow("Past Surgical History:", summary.pastSurgicalHistory)
            SummaryRow("Drug History & Allergies:", "${summary.drugHistory} • ${summary.allergies}")
            SummaryRow("Family History:", summary.familyHistory)
            SummaryRow("Review of Systems:", summary.reviewOfSystems)
            SummaryRow("Previous Investigations:", summary.previousInvestigations)
            SummaryRow("Current Medications:", summary.currentMedications)

            HorizontalDivider(color = BorderSubtle)

            // Mandatory Buttons: EDIT, CONFIRM, REJECT
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        isEditing = !isEditing
                    },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(if (isEditing) "Done Edit" else "EDIT", fontSize = 11.sp)
                }

                Button(
                    onClick = {
                        repository.confirmAiSummary(doctorName, if (isEditing) editedHpi else null)
                        isEditing = false
                        navState.playAudio("AI clinical summary verified and confirmed by physician.")
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = VerifiedGreen),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("CONFIRM", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = {
                        repository.rejectAiSummary("Inaccurate symptom chronology")
                        navState.playAudio("AI summary rejected. Manual note entry required.")
                    },
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = EmergencyRed),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("REJECT", fontSize = 11.sp)
                }
            }
        }
    }
}

@Composable
private fun SummaryRow(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 2.dp)) {
        Text(text = label, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = GovNavy)
        Text(text = value, fontSize = 12.sp, color = TextPrimary, lineHeight = 16.sp)
    }
}

@Composable
private fun DoctorReportsSection(documents: List<com.example.data.model.MedicalDocument>) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        documents.forEach { doc ->
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(doc.title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = GovNavy)
                        Text(doc.date, fontSize = 11.sp, color = TextMuted)
                    }
                    Text("${doc.hospitalName} • Category: ${doc.category.label}", fontSize = 11.sp, color = TextSecondary)

                    Surface(
                        color = Color(0xFFF8FAFC),
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            doc.extractedValues.forEach { (k, v) ->
                                Text("• $k: $v", fontSize = 11.sp, color = TextPrimary)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DoctorPrescriptionsSection() {
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
            Text("Previous Government OPD Prescriptions", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = GovNavy)

            PrescriptionCardItem("Tab. Amlodipine 5mg", "1-0-0 (Morning)", "90 Days", "GGH Coimbatore - Dr. M. Senthil Nathan")
            PrescriptionCardItem("Tab. Atorvastatin 10mg", "0-0-1 (Bedtime)", "90 Days", "GGH Coimbatore - Dr. Rajesh Raman")
            PrescriptionCardItem("Tab. Aspirin 75mg", "0-1-0 (Afternoon)", "30 Days", "GMCH Madurai - Dr. Priya Sharma")
        }
    }
}

@Composable
private fun PrescriptionCardItem(med: String, dose: String, duration: String, hospital: String) {
    Surface(
        color = Color(0xFFEFF6FF),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text("💊 $med", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E40AF))
            Text("Dose: $dose • Duration: $duration", fontSize = 11.sp, color = TextPrimary)
            Text("Source: $hospital", fontSize = 10.sp, color = TextMuted)
        }
    }
}

@Composable
fun AddConsultationDialog(
    hospitalName: String,
    doctorName: String,
    onDismiss: () -> Unit,
    onSave: (diagnosis: String, notes: String, rxs: List<String>, advice: String) -> Unit
) {
    var chiefComplaint by remember { mutableStateOf("Chest tightness with exertion") }
    var observations by remember { mutableStateOf("BP 144/92 mmHg, S1 S2 heard. No murmurs. Bilateral breath sounds clear.") }
    var diagnosis by remember { mutableStateOf("Unstable Angina / Exertional Ischemia on Essential HTN") }
    var prescription by remember { mutableStateOf("Tab. Sorbitrate 5mg SOS, Tab. Metoprolol 25mg OD, Tab. Aspirin 75mg OD") }
    var advice by remember { mutableStateOf("Immediate Coronary Angiogram recommended. Avoid heavy exertion. Low sodium diet.") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Add Consultation ($hospitalName)",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = GovNavy
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 420.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = chiefComplaint,
                    onValueChange = { chiefComplaint = it },
                    label = { Text("Chief Complaint") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = observations,
                    onValueChange = { observations = it },
                    label = { Text("Clinical Observations") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = diagnosis,
                    onValueChange = { diagnosis = it },
                    label = { Text("Diagnosis") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = prescription,
                    onValueChange = { prescription = it },
                    label = { Text("Prescriptions (Comma separated)") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = advice,
                    onValueChange = { advice = it },
                    label = { Text("Advice & Follow-up Plan") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val rxs = prescription.split(",").map { it.trim() }.filter { it.isNotEmpty() }
                    onSave(diagnosis, observations, rxs, advice)
                },
                colors = ButtonDefaults.buttonColors(containerColor = MedicalTeal)
            ) {
                Text("Save Consultation", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
