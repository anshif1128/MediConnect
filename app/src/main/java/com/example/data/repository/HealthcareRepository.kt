package com.example.data.repository

import com.example.data.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class HealthcareRepository {

    private val _patient = MutableStateFlow(
        Patient(
            id = "pat-001",
            healthId = "MHID-2026-00125",
            abhaAddress = "arun.kumar@abdm",
            name = "Arun Kumar",
            age = 45,
            gender = "Male",
            phone = "+91 98765 43210",
            preferredLanguage = "English",
            bloodGroup = "O+",
            emergencyContact = "Anand Kumar (Brother) - +91 98765 43211",
            allergies = listOf("Penicillin (Mild urticaria)", "Dust / Pollen"),
            chronicConditions = listOf("Essential Hypertension", "Mild Hyperlipidemia")
        )
    )
    val patient: StateFlow<Patient> = _patient.asStateFlow()

    private val _hospitals = MutableStateFlow(
        listOf(
            Hospital(
                id = "hosp-001",
                name = "Government General Hospital - Coimbatore",
                code = "GGH-CBE-01",
                city = "Coimbatore",
                state = "Tamil Nadu",
                type = "District Headquarters Govt Hospital"
            ),
            Hospital(
                id = "hosp-002",
                name = "Government Medical College Hospital - Madurai",
                code = "GMCH-MDU-04",
                city = "Madurai",
                state = "Tamil Nadu",
                type = "Government Tertiary Teaching Hospital"
            ),
            Hospital(
                id = "hosp-003",
                name = "All India Institute of Ayurveda (AIIA) - Ayush Center",
                code = "AIIA-ND-01",
                city = "New Delhi / Chennai Wing",
                state = "Ministry of Ayush",
                type = "National Apex Institute"
            )
        )
    )
    val hospitals: StateFlow<List<Hospital>> = _hospitals.asStateFlow()

    private val _doctors = MutableStateFlow(
        listOf(
            Doctor(
                id = "doc-001",
                name = "Dr. Rajesh Raman, MD (Cardiology)",
                hospitalId = "hosp-001",
                hospitalName = "Government General Hospital - Coimbatore",
                department = "Cardiology Department",
                designation = "Senior Civil Surgeon",
                registrationNo = "MCI-TN-48291",
                isAyush = false
            ),
            Doctor(
                id = "doc-002",
                name = "Dr. Priya Sharma, MD, DM",
                hospitalId = "hosp-002",
                hospitalName = "Government Medical College Hospital - Madurai",
                department = "Cardiology Department",
                designation = "Associate Professor",
                registrationNo = "MCI-TN-59124",
                isAyush = false
            ),
            Doctor(
                id = "doc-003",
                name = "Dr. A. Vaidyanathan, MD (Ayurveda)",
                hospitalId = "hosp-003",
                hospitalName = "All India Institute of Ayurveda (AIIA)",
                department = "Kayachikitsa (Internal Medicine)",
                designation = "Senior Ayush Physician",
                registrationNo = "AYUSH-IN-9921",
                isAyush = true
            )
        )
    )
    val doctors: StateFlow<List<Doctor>> = _doctors.asStateFlow()

    private val _timeline = MutableStateFlow(
        listOf(
            TimelineEvent(
                id = "time-001",
                year = "2024",
                date = "14 Mar 2024",
                hospitalName = "Government General Hospital - Coimbatore",
                department = "General Medicine",
                doctorName = "Dr. M. Senthil Nathan",
                title = "Initial Consultation & Diagnosis",
                category = "Consultation",
                description = "Patient presented with persistent headache. BP recorded 154/96 mmHg. Diagnosed with Grade 1 Essential Hypertension. Lifestyle modifications and Tab. Amlodipine 5mg OD advised.",
                prescriptionSummary = listOf("Tab. Amlodipine 5mg OD (Morning)")
            ),
            TimelineEvent(
                id = "time-002",
                year = "2025",
                date = "12 Aug 2025",
                hospitalName = "Government General Hospital - Coimbatore",
                department = "Central Biochemistry Lab",
                doctorName = "Dr. S. K. Lakshmi",
                title = "Annual Renal & Glycemic Panel",
                category = "Laboratory Report",
                description = "Routine surveillance tests for hypertensive patient. Serum Creatinine normal. Fasting Blood Glucose indicates prediabetes.",
                labValues = mapOf(
                    "Hemoglobin (Hb)" to "10.8 g/dL",
                    "Fasting Blood Sugar" to "112 mg/dL",
                    "Serum Creatinine" to "1.0 mg/dL",
                    "Serum Potassium" to "4.2 mEq/L"
                )
            ),
            TimelineEvent(
                id = "time-003",
                year = "2026",
                date = "15 Jan 2026",
                hospitalName = "Government General Hospital - Coimbatore",
                department = "Cardiology Department",
                doctorName = "Dr. Rajesh Raman",
                title = "Cardiology Consultation & ECG Upload",
                category = "Cardiology",
                description = "Follow-up visit. BP 138/88 mmHg. Lipid profile reviewed: Total Cholesterol 228 mg/dL. Atorvastatin 10mg added to regimen.",
                prescriptionSummary = listOf("Tab. Amlodipine 5mg OD", "Tab. Atorvastatin 10mg HS")
            ),
            TimelineEvent(
                id = "time-004",
                year = "2026",
                date = "20 Jul 2026",
                hospitalName = "Government General Hospital - Coimbatore",
                department = "Diagnostic Imaging",
                doctorName = "Dr. K. Balaji",
                title = "2D Echocardiography Study",
                category = "Imaging Report",
                description = "Concentric Left Ventricular Hypertrophy noted consistent with chronic hypertension. LVEF preserved at 60%. No regional wall motion abnormality.",
                labValues = mapOf(
                    "LVEF" to "60%",
                    "LV Diastolic Function" to "Grade 1 Impaired Relaxation",
                    "Aortic Valve" to "Trileaflet, No stenosis"
                )
            )
        )
    )
    val timeline: StateFlow<List<TimelineEvent>> = _timeline.asStateFlow()

    private val _documents = MutableStateFlow(
        listOf(
            MedicalDocument(
                id = "doc-item-001",
                patientHealthId = "MHID-2026-00125",
                title = "Renal & Metabolic Panel",
                category = DocumentCategory.LAB_REPORT,
                date = "12 Aug 2025",
                hospitalName = "Government General Hospital - Coimbatore",
                fileType = "Laboratory PDF",
                ocrRawText = "Govt General Hospital Coimbatore - Dept of Biochemistry\nPatient: Arun Kumar (45M) | Date: 12-08-2025\nHb: 10.8 g/dL | Fasting Glucose: 112 mg/dL | Serum Creatinine: 1.0 mg/dL | Urea: 28 mg/dL",
                extractedValues = mapOf(
                    "Hemoglobin" to "10.8 g/dL",
                    "Fasting Glucose" to "112 mg/dL",
                    "Serum Creatinine" to "1.0 mg/dL",
                    "Blood Urea" to "28 mg/dL"
                ),
                doctorVerified = true
            ),
            MedicalDocument(
                id = "doc-item-002",
                patientHealthId = "MHID-2026-00125",
                title = "Cardiology Prescription",
                category = DocumentCategory.PRESCRIPTION,
                date = "15 Jan 2026",
                hospitalName = "Government General Hospital - Coimbatore",
                fileType = "Prescription Slip",
                ocrRawText = "Rx: 1. Tab Amlodipine 5mg 1-0-0 x 90 days\n2. Tab Atorvastatin 10mg 0-0-1 x 90 days\nAdv: Low salt diet, 30 min daily brisk walk",
                extractedValues = mapOf(
                    "Medication 1" to "Amlodipine 5mg (Morning)",
                    "Medication 2" to "Atorvastatin 10mg (Bedtime)",
                    "Duration" to "90 Days"
                ),
                doctorVerified = true
            ),
            MedicalDocument(
                id = "doc-item-003",
                patientHealthId = "MHID-2026-00125",
                title = "2D Echocardiogram Study",
                category = DocumentCategory.IMAGING_REPORT,
                date = "20 Jul 2026",
                hospitalName = "Government General Hospital - Coimbatore",
                fileType = "Diagnostic Imaging PDF",
                ocrRawText = "Coimbatore Medical College Diagnostic Center\n2D Echo & Doppler Report\nEjection Fraction: 60%\nConcentric LVH present. Valves structurally normal.",
                extractedValues = mapOf(
                    "LVEF" to "60%",
                    "Finding" to "Concentric LVH",
                    "Valvular Status" to "Normal"
                ),
                doctorVerified = true
            )
        )
    )
    val documents: StateFlow<List<MedicalDocument>> = _documents.asStateFlow()

    private val _consents = MutableStateFlow(
        listOf(
            ConsentRecord(
                id = "consent-001",
                patientHealthId = "MHID-2026-00125",
                hospitalId = "hosp-001",
                hospitalName = "Government General Hospital - Coimbatore",
                doctorName = "Dr. Rajesh Raman",
                status = ConsentStatus.GRANTED,
                dateGranted = "14 Mar 2024, 09:30 AM",
                validUntil = "31 Dec 2026"
            ),
            ConsentRecord(
                id = "consent-002",
                patientHealthId = "MHID-2026-00125",
                hospitalId = "hosp-002",
                hospitalName = "Government Medical College Hospital - Madurai",
                doctorName = "Dr. Priya Sharma",
                status = ConsentStatus.GRANTED, // Ready for instant cross-hospital demo or toggle
                dateGranted = "15 Sep 2026, 08:45 AM",
                validUntil = "31 Dec 2026"
            ),
            ConsentRecord(
                id = "consent-003",
                patientHealthId = "MHID-2026-00125",
                hospitalId = "hosp-003",
                hospitalName = "All India Institute of Ayurveda (AIIA)",
                doctorName = "Dr. A. Vaidyanathan",
                status = ConsentStatus.GRANTED,
                dateGranted = "10 May 2025, 11:00 AM",
                validUntil = "31 Dec 2026"
            )
        )
    )
    val consents: StateFlow<List<ConsentRecord>> = _consents.asStateFlow()

    private val _accessLogs = MutableStateFlow(
        listOf(
            AccessLog(
                id = "log-001",
                timestamp = "15 Sep 2026, 08:52 AM",
                hospitalName = "Government Medical College Hospital - Madurai",
                doctorName = "Dr. Priya Sharma",
                department = "Cardiology",
                purpose = "Cross-Hospital Outpatient Case Review",
                status = "GRANTED",
                details = "Requested full timeline and previous Coimbatore lab/imaging records under patient consent."
            ),
            AccessLog(
                id = "log-002",
                timestamp = "20 Jul 2026, 11:15 AM",
                hospitalName = "Government General Hospital - Coimbatore",
                doctorName = "Dr. K. Balaji",
                department = "Diagnostic Imaging",
                purpose = "Echo Scan Upload & Confirmation",
                status = "ACCESSED",
                details = "Uploaded 2D Echo results to centralized patient timeline."
            ),
            AccessLog(
                id = "log-003",
                timestamp = "15 Jan 2026, 10:20 AM",
                hospitalName = "Government General Hospital - Coimbatore",
                doctorName = "Dr. Rajesh Raman",
                department = "Cardiology",
                purpose = "Routine Hypertensive Review",
                status = "ACCESSED",
                details = "Updated medication list and reviewed lipid profile."
            )
        )
    )
    val accessLogs: StateFlow<List<AccessLog>> = _accessLogs.asStateFlow()

    private val _socratesData = MutableStateFlow(SocratesChestPainData())
    val socratesData: StateFlow<SocratesChestPainData> = _socratesData.asStateFlow()

    private val _aiClinicalSummary = MutableStateFlow(
        AiClinicalSummary(
            chiefComplaint = "Chest pain for 2 days, radiating to left shoulder and arm",
            historyOfPresentIllness = "45-year-old male with a known history of essential hypertension (since 2024) presents with center chest pressure and tightness lasting 15-20 minutes, exacerbated by physical climbing. Associated with mild exertional dyspnea and diaphoresis.",
            pastMedicalHistory = "Essential Hypertension diagnosed March 2024 (GGH Coimbatore). Pre-diabetes noted August 2025. Concentric LVH with LVEF 60% noted July 2026.",
            pastSurgicalHistory = "No previous surgical interventions reported.",
            drugHistory = "Tab. Amlodipine 5mg OD (regular); Tab. Atorvastatin 10mg HS (regular). Non-adherent to antiplatelet therapy.",
            allergies = "Known mild skin hypersensitivity / urticaria to Penicillin. Dust pollen sensitivity.",
            familyHistory = "Father had ischemic heart disease at age 58.",
            personalHistory = "Non-smoker, moderate dietary salt intake, desk worker.",
            reviewOfSystems = "Cardiovascular: Chest tightness +, Palpitations -. Respiratory: Mild dyspnea on exertion +. GI: No acidity/GERD. Neuro: No syncope or focal deficit.",
            previousInvestigations = "Hb 10.8 g/dL, Creatinine 1.0 mg/dL (Aug 2025). ECG normal sinus (Jan 2026). Echo LVEF 60% with concentric LVH (Jul 2026).",
            currentMedications = "Amlodipine 5mg, Atorvastatin 10mg",
            status = "AI DRAFT — Physician Verification Required"
        )
    )
    val aiClinicalSummary: StateFlow<AiClinicalSummary> = _aiClinicalSummary.asStateFlow()

    private val _ayushAssessment = MutableStateFlow(
        AyushAssessment(
            patientHealthId = "MHID-2026-00125",
            date = "15 Sep 2026",
            doctorName = "Dr. A. Vaidyanathan (AIIA)",
            prakriti = "Vata-Pitta Pradhana",
            vikriti = "Vata-Kapha Avarana in Hridaya Srotas",
            agni = "Vishama Agni (Irregular digestive fire)",
            koshtha = "Madhyama Koshtha (Moderate bowel habits)",
            ahara = "Ushna, Tikshna & Lavana dominant diet; irregular meal timings",
            vihara = "Chinta (Mental stress), Ratrijagarana (Late sleeping), Divasvapna",
            nidana = "Vegadharana (Suppression of natural urges), excess psychological exertion",
            samprapti = "Dushta Doshas (Vata-Kapha) circulating via Rasavaha & Pranavaha Srotas causing Hridgraha (chest tightness).",
            dashavidhaPariksha = mapOf(
                "1. Prakriti (Constitution)" to "Vata-Pitta",
                "2. Vikriti (Pathology)" to "Vata-Kapha Avarana",
                "3. Sara (Tissue essence)" to "Madhyama (Moderate)",
                "4. Samhanana (Compactness)" to "Madhyama",
                "5. Pramana (Anthropometry)" to "Prakrita (Normal BMI)",
                "6. Satmya (Habituation)" to "Vyamishra (Mixed)",
                "7. Sattva (Mental strength)" to "Madhyama Sattva",
                "8. Ahara Shakti (Digestive capacity)" to "Abhyavaharana & Jarana Moderate",
                "9. Vyayama Shakti (Physical endurance)" to "Avara (Low exercise tolerance)",
                "10. Vaya (Age)" to "Madhyama Vaya (45 Yrs)"
            ),
            ayurvedicFormulations = listOf(
                "Arjuna Ksheerapaka - 50ml morning & evening empty stomach (Cardioprotective)",
                "Prabhakara Vati - 1 tab twice daily with lukewarm water",
                "Sarpagandha Ghanavati - 1 tab at bedtime for BP regulation",
                "Dashamula Kwatha - 20ml with equal warm water post meals"
            )
        )
    )
    val ayushAssessment: StateFlow<AyushAssessment> = _ayushAssessment.asStateFlow()

    fun updatePatient(name: String, age: Int, gender: String, phone: String, lang: String) {
        _patient.value = _patient.value.copy(
            name = name,
            age = age,
            gender = gender,
            phone = phone,
            preferredLanguage = lang
        )
    }

    fun updateConsent(hospitalId: String, granted: Boolean) {
        _consents.value = _consents.value.map { c ->
            if (c.hospitalId == hospitalId) {
                c.copy(
                    status = if (granted) ConsentStatus.GRANTED else ConsentStatus.REVOKED
                )
            } else c
        }
        val targetHosp = _hospitals.value.find { it.id == hospitalId }?.name ?: "Hospital"
        val newLog = AccessLog(
            timestamp = "Today, Just Now",
            hospitalName = targetHosp,
            doctorName = "Attending Physician",
            department = "Medical Records Dept",
            purpose = "Consent Policy Modification",
            status = if (granted) "GRANTED" else "REVOKED",
            details = "Patient updated consent permissions. Access is now ${if (granted) "authorized" else "withdrawn"}."
        )
        _accessLogs.value = listOf(newLog) + _accessLogs.value
    }

    fun updateConsentStatus(consentId: String, newStatus: ConsentStatus) {
        _consents.value = _consents.value.map { c ->
            if (c.id == consentId) {
                c.copy(status = newStatus)
            } else c
        }
        val consent = _consents.value.find { it.id == consentId }
        val newLog = AccessLog(
            timestamp = "Today, Just Now",
            hospitalName = consent?.hospitalName ?: "Hospital",
            doctorName = "Attending Physician",
            department = "Medical Records Dept",
            purpose = "Consent Policy Modification",
            status = newStatus.name,
            details = "Patient updated consent permissions to ${newStatus.name}."
        )
        _accessLogs.value = listOf(newLog) + _accessLogs.value
    }

    fun updateSocratesData(data: SocratesChestPainData) {
        _socratesData.value = data
        // Dynamically update the AI clinical summary based on captured complaint
        _aiClinicalSummary.value = _aiClinicalSummary.value.copy(
            chiefComplaint = "Chest pain (Severity ${data.severity}/10) for ${data.onset}",
            historyOfPresentIllness = "Patient presents with ${data.character.lowercase()} located at ${data.site.lowercase()}. Radiation: ${data.radiation}. Associated symptoms include: ${data.associatedSymptoms.joinToString(", ")}. ${data.exacerbatingRelieving}.",
            status = "AI DRAFT — Updated via Clinical Case-Taking"
        )
    }

    fun addDocument(
        title: String,
        category: DocumentCategory,
        hospitalName: String,
        extractedValues: Map<String, String>,
        rawOcr: String
    ) {
        val newDoc = MedicalDocument(
            patientHealthId = _patient.value.healthId,
            title = title,
            category = category,
            date = "15 Sep 2026",
            hospitalName = hospitalName,
            ocrRawText = rawOcr,
            extractedValues = extractedValues,
            doctorVerified = false
        )
        _documents.value = listOf(newDoc) + _documents.value

        // Automatically sync to medical timeline
        val newTimelineEvent = TimelineEvent(
            year = "2026",
            date = "15 Sep 2026",
            hospitalName = hospitalName,
            department = "Diagnostic & Case Intake",
            doctorName = "Automated OCR & Clinical Case Engine",
            title = "$title (Digitized via OCR)",
            category = category.label,
            description = "Medical record uploaded and structured by MediConnect AI. Pending doctor sign-off.",
            labValues = extractedValues
        )
        _timeline.value = listOf(newTimelineEvent) + _timeline.value
    }

    fun addDocument(doc: MedicalDocument) {
        _documents.value = listOf(doc) + _documents.value
        val newTimelineEvent = TimelineEvent(
            year = "2026",
            date = doc.date,
            hospitalName = doc.hospitalName,
            department = "Diagnostic & Case Intake",
            doctorName = "Automated OCR & Clinical Case Engine",
            title = "${doc.title} (Digitized via OCR)",
            category = doc.category.label,
            description = "Medical record uploaded and structured by MediConnect AI.",
            labValues = doc.extractedValues
        )
        _timeline.value = listOf(newTimelineEvent) + _timeline.value
    }

    fun addHospitalBConsultation(
        hospitalName: String,
        doctorName: String,
        department: String,
        diagnosis: String,
        notes: String,
        prescriptions: List<String>,
        advice: String
    ) {
        val newEvent = TimelineEvent(
            year = "2026",
            date = "15 Sep 2026",
            hospitalName = hospitalName,
            department = department,
            doctorName = doctorName,
            title = "Consultation: $diagnosis",
            category = "Consultation",
            description = "$notes\nAdvice: $advice",
            prescriptionSummary = prescriptions,
            isCrossHospitalAccess = true
        )
        _timeline.value = listOf(newEvent) + _timeline.value

        val newLog = AccessLog(
            timestamp = "15 Sep 2026, 09:10 AM",
            hospitalName = hospitalName,
            doctorName = doctorName,
            department = department,
            purpose = "Consultation & Record Append",
            status = "ACCESSED",
            details = "Appended new diagnosis ($diagnosis) and updated treatment plan to unified health timeline."
        )
        _accessLogs.value = listOf(newLog) + _accessLogs.value
    }

    fun confirmAiSummary(verifiedBy: String, updatedNotes: String? = null) {
        _aiClinicalSummary.value = _aiClinicalSummary.value.copy(
            status = "CONFIRMED & SIGNED BY PHYSICIAN",
            verifiedByDoctor = verifiedBy,
            historyOfPresentIllness = updatedNotes ?: _aiClinicalSummary.value.historyOfPresentIllness
        )
    }

    fun rejectAiSummary(reason: String) {
        _aiClinicalSummary.value = _aiClinicalSummary.value.copy(
            status = "REJECTED BY PHYSICIAN ($reason)",
            verifiedByDoctor = null
        )
    }

    fun saveAyushAssessment(assessment: AyushAssessment) {
        _ayushAssessment.value = assessment
        val timelineEvent = TimelineEvent(
            year = "2026",
            date = assessment.date,
            hospitalName = "All India Institute of Ayurveda (AIIA)",
            department = "Ayush Kayachikitsa",
            doctorName = assessment.doctorName,
            title = "AYUSH Prakriti & Dashavidha Assessment",
            category = "AYUSH Clinical Record",
            description = "Prakriti: ${assessment.prakriti} | Vikriti: ${assessment.vikriti} | Agni: ${assessment.agni}. Samprapti evaluated. Ayurvedic formulations prescribed.",
            prescriptionSummary = assessment.ayurvedicFormulations
        )
        _timeline.value = listOf(timelineEvent) + _timeline.value
    }

    companion object {
        val instance by lazy { HealthcareRepository() }
    }
}
