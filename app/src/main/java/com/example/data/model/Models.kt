package com.example.data.model

import java.util.UUID

enum class UserRole {
    PATIENT,
    DOCTOR,
    ADMIN
}

enum class ConsentStatus {
    GRANTED,
    PENDING,
    REVOKED
}

enum class DocumentCategory(val label: String) {
    PRESCRIPTION("Prescription"),
    LAB_REPORT("Laboratory Report"),
    DISCHARGE_SUMMARY("Discharge Summary"),
    IMAGING_REPORT("Imaging Report"),
    DIAGNOSIS("Diagnosis"),
    SURGERY_RECORD("Surgery Record"),
    OTHER("Other Document")
}

data class Patient(
    val id: String = "pat-001",
    val healthId: String = "MHID-2026-00125",
    val abhaAddress: String = "arun.kumar@abdm",
    val name: String = "Arun Kumar",
    val age: Int = 45,
    val gender: String = "Male",
    val phone: String = "+91 98765 43210",
    val preferredLanguage: String = "English",
    val bloodGroup: String = "O+",
    val emergencyContact: String = "Anand Kumar (Brother) - +91 98765 43211",
    val emergencyContactName: String = "Anand Kumar (Brother)",
    val emergencyContactPhone: String = "+91 98765 43211",
    val allergies: List<String> = listOf("Penicillin (Mild rash)", "Dust / Pollen"),
    val chronicConditions: List<String> = listOf("Essential Hypertension", "Mild Hyperlipidemia")
)

data class Hospital(
    val id: String,
    val name: String,
    val code: String,
    val city: String,
    val state: String = "Tamil Nadu",
    val type: String = "District Headquarters Govt Hospital",
    val nodalOfficer: String = "Dr. S. K. Ramanathan",
    val departments: List<String> = listOf("Cardiology", "General Medicine", "Pathology", "Emergency"),
    val activeDoctorsCount: Int = 48
)

data class Doctor(
    val id: String,
    val name: String,
    val hospitalId: String,
    val hospitalName: String,
    val department: String,
    val designation: String,
    val registrationNo: String,
    val isAyush: Boolean = false
)

data class PrescriptionItem(
    val id: String = UUID.randomUUID().toString(),
    val medicineName: String,
    val dosage: String,
    val frequency: String,
    val duration: String,
    val instructions: String,
    val isAyush: Boolean = false
)

data class MedicalDocument(
    val id: String = UUID.randomUUID().toString(),
    val patientHealthId: String,
    val title: String,
    val category: DocumentCategory,
    val date: String,
    val hospitalName: String,
    val fileType: String = "PDF / Image",
    val ocrRawText: String,
    val extractedValues: Map<String, String>,
    val ocrConfidence: Float = 0.96f,
    val doctorVerified: Boolean = false
)

data class TimelineEvent(
    val id: String = UUID.randomUUID().toString(),
    val year: String,
    val date: String,
    val hospitalName: String,
    val department: String,
    val doctorName: String,
    val title: String,
    val category: String,
    val description: String,
    val labValues: Map<String, String> = emptyMap(),
    val prescriptionSummary: List<String> = emptyList(),
    val isCrossHospitalAccess: Boolean = false
)

data class ConsentRecord(
    val id: String = UUID.randomUUID().toString(),
    val patientHealthId: String,
    val hospitalId: String,
    val hospitalName: String,
    val doctorName: String,
    val allowViewHistory: Boolean = true,
    val allowShareReports: Boolean = true,
    val allowAiOrganize: Boolean = true,
    val allowAddRecords: Boolean = true,
    val status: ConsentStatus = ConsentStatus.GRANTED,
    val dateGranted: String,
    val validUntil: String
)

data class AccessLog(
    val id: String = UUID.randomUUID().toString(),
    val timestamp: String,
    val patientId: String = "MHID-2026-00125",
    val hospitalName: String,
    val doctorName: String,
    val department: String,
    val purpose: String,
    val status: String,
    val details: String
)

data class AyushAssessment(
    val patientHealthId: String,
    val date: String,
    val doctorName: String,
    val prakriti: String, // e.g. Vata-Pitta
    val vikriti: String, // e.g. Kapha-Vata Vitiation
    val agni: String, // Manda, Tikshna, Vishama, Samagni
    val koshtha: String, // Krura, Mridu, Madhyama
    val ahara: String, // Diet habits
    val vihara: String, // Lifestyle
    val nidana: String, // Etiological factors
    val samprapti: String, // Pathogenesis
    val dashavidhaPariksha: Map<String, String>,
    val ayurvedicFormulations: List<String>
)

data class AiClinicalSummary(
    val chiefComplaint: String,
    val historyOfPresentIllness: String,
    val pastMedicalHistory: String,
    val pastSurgicalHistory: String,
    val drugHistory: String,
    val allergies: String,
    val familyHistory: String,
    val personalHistory: String,
    val reviewOfSystems: String,
    val previousInvestigations: String,
    val currentMedications: String,
    val status: String = "AI DRAFT - Physician Review Required",
    val verifiedByDoctor: String? = null
)

data class FhirResource(
    val resourceType: String,
    val id: String,
    val status: String,
    val title: String,
    val jsonSnippet: String
)

data class SocratesChestPainData(
    val site: String = "Center of chest (Substernal)",
    val onset: String = "2 days ago, worsening on climbing stairs",
    val character: String = "Pressure sensation / heavy tightness",
    val radiation: String = "Radiating to left shoulder and arm",
    val associatedSymptoms: List<String> = listOf("Mild breathlessness", "Diaphoresis / Sweating"),
    val timing: String = "Intermittent episodes (15-20 mins each)",
    val exacerbatingRelieving: String = "Exacerbated by physical exertion; relieved partially by rest",
    val severity: Int = 7
)
