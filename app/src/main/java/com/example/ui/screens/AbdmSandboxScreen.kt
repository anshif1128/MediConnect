package com.example.ui.screens

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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.AudioListenButton
import com.example.ui.navigation.NavigationState
import com.example.ui.theme.*

@Composable
fun AbdmSandboxScreen(
    navState: NavigationState,
    modifier: Modifier = Modifier
) {
    var selectedSandboxTab by remember { mutableIntStateOf(0) }

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
                            text = "ABDM & FHIR Sandbox Integration",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = GovNavy
                        )
                        Text(
                            text = "Ayushman Bharat Digital Mission Interoperability",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                }

                AudioListenButton(
                    textToSpeak = "Ayushman Bharat Digital Mission and FHIR interoperability sandbox. Displays standard electronic health record specifications.",
                    navState = navState
                )
            }
        }

        // ABDM Protocol Status Banner
        item {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MedicalTealLight),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CloudSync,
                        contentDescription = null,
                        tint = MedicalTeal,
                        modifier = Modifier.size(24.dp)
                    )
                    Column {
                        Text(
                            text = "ABDM Sandbox Mock / Milestone Ready",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = GovNavy
                        )
                        Text(
                            text = "M1 (ABHA Verification) • M2 (HIP Data Push) • M3 (HIU Consent Request)",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                }
            }
        }

        // Tabs: FHIR R4 Bundle | Consent Artefact | ABHA Address Verification
        item {
            TabRow(
                selectedTabIndex = selectedSandboxTab,
                containerColor = SurfaceWhite,
                contentColor = MedicalTeal
            ) {
                Tab(
                    selected = selectedSandboxTab == 0,
                    onClick = { selectedSandboxTab = 0 },
                    text = { Text("FHIR R4 Bundle", fontSize = 11.sp, fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = selectedSandboxTab == 1,
                    onClick = { selectedSandboxTab = 1 },
                    text = { Text("Consent Artefact", fontSize = 11.sp, fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = selectedSandboxTab == 2,
                    onClick = { selectedSandboxTab = 2 },
                    text = { Text("HIP / HIU Roles", fontSize = 11.sp, fontWeight = FontWeight.Bold) }
                )
            }
        }

        when (selectedSandboxTab) {
            0 -> {
                item {
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Standardized FHIR R4 Clinical Document Bundle:",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF38BDF8)
                            )
                            Text(
                                text = """{
  "resourceType": "Bundle",
  "id": "bundle-mhid-2026-00125",
  "type": "document",
  "timestamp": "2026-09-15T09:30:00Z",
  "entry": [
    {
      "resource": {
        "resourceType": "Patient",
        "id": "mhid-2026-00125",
        "identifier": [{
          "system": "https://healthid.abdm.gov.in",
          "value": "arun.kumar@abdm"
        }],
        "name": [{"text": "Arun Kumar"}],
        "gender": "male",
        "birthDate": "1981-04-12"
      }
    },
    {
      "resource": {
        "resourceType": "Condition",
        "clinicalStatus": "active",
        "verificationStatus": "confirmed",
        "code": {
          "coding": [{
            "system": "http://snomed.info/sct",
            "code": "38341003",
            "display": "Hypertensive disorder"
          }]
        }
      }
    },
    {
      "resource": {
        "resourceType": "Observation",
        "code": {"text": "Blood Pressure"},
        "component": [
          {"code": {"text": "Systolic"}, "valueQuantity": {"value": 144, "unit": "mmHg"}},
          {"code": {"text": "Diastolic"}, "valueQuantity": {"value": 92, "unit": "mmHg"}}
        ]
      }
    }
  ]
}""",
                                fontFamily = FontFamily.Monospace,
                                fontSize = 10.sp,
                                color = Color(0xFFCBD5E1),
                                lineHeight = 14.sp
                            )
                        }
                    }
                }
            }

            1 -> {
                item {
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "ABDM Electronic Consent Artefact Schema:",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF4ADE80)
                            )
                            Text(
                                text = """{
  "consentId": "CA-2026-993812",
  "status": "GRANTED",
  "createdAt": "2026-09-15T08:00:00Z",
  "patient": {
    "id": "arun.kumar@abdm"
  },
  "purpose": {
    "code": "CAREST",
    "text": "Care Management and Clinical Consultation"
  },
  "hiu": {
    "id": "IN33000002",
    "name": "Govt Medical College Hospital - Madurai"
  },
  "hip": {
    "id": "IN33000001",
    "name": "Govt General Hospital - Coimbatore"
  },
  "hiTypes": [
    "DiagnosticReport",
    "Prescription",
    "OPConsultation",
    "DischargeSummary"
  ],
  "permission": {
    "accessMode": "VIEW",
    "dateRange": {
      "from": "2024-01-01T00:00:00Z",
      "to": "2026-09-15T23:59:59Z"
    },
    "dataEraseAt": "2026-10-15T00:00:00Z"
  }
}""",
                                fontFamily = FontFamily.Monospace,
                                fontSize = 10.sp,
                                color = Color(0xFFCBD5E1),
                                lineHeight = 14.sp
                            )
                        }
                    }
                }
            }

            2 -> {
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
                            Text("Federated Role Architecture:", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = GovNavy)

                            RoleDescItem(
                                title = "HIP (Health Information Provider)",
                                role = "Hospital A (GGH Coimbatore)",
                                desc = "Holds the patient's historical lab records, 2024 hypertension diagnosis, and prescriptions. Responds to federated data requests upon consent validation."
                            )

                            RoleDescItem(
                                title = "HIU (Health Information User)",
                                role = "Hospital B (GMCH Madurai)",
                                desc = "Requests access to the patient's records using ABHA/MHID when the patient presents for cardiology consultation. Consumes verified FHIR records."
                            )

                            RoleDescItem(
                                title = "HIE-CM (Health Information Exchange - Consent Manager)",
                                role = "National Gateway / MediConnect Core",
                                desc = "Enforces patient consent tokens, logs audit records, and securely routes encrypted clinical payloads without storing medical data centrally."
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RoleDescItem(title: String, role: String, desc: String) {
    Surface(
        color = Color(0xFFF8FAFC),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MedicalTeal)
            Text(role, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = GovNavy)
            Spacer(modifier = Modifier.height(2.dp))
            Text(desc, fontSize = 11.sp, color = TextSecondary, lineHeight = 15.sp)
        }
    }
}
