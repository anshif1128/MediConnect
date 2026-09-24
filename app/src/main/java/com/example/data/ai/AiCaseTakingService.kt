package com.example.data.ai

import com.example.data.model.AiClinicalSummary
import com.example.data.model.DocumentCategory
import com.example.data.model.SocratesChestPainData
import kotlinx.coroutines.delay

interface AiHealthcareService {
    suspend fun conductConversationalStep(
        chiefComplaint: String,
        userResponse: String,
        stepIndex: Int
    ): AiConversationStep

    suspend fun performDocumentOcr(
        category: DocumentCategory,
        documentTitle: String
    ): OcrExtractionResult

    fun evaluateRedFlags(
        complaint: String,
        severity: Int,
        symptoms: List<String>
    ): RedFlagAssessment

    suspend fun generateStructuredSummary(
        patientName: String,
        age: Int,
        historyData: SocratesChestPainData,
        pastDiseases: List<String>
    ): AiClinicalSummary

    suspend fun processOcrDocument(
        documentTitle: String,
        category: DocumentCategory
    ): com.example.data.model.MedicalDocument
}

data class AiConversationStep(
    val question: String,
    val questionTamil: String,
    val options: List<String>,
    val optionsTamil: List<String>,
    val requiresFreeText: Boolean = false,
    val isRedFlagCheck: Boolean = false
)

data class OcrExtractionResult(
    val rawText: String,
    val extractedParameters: Map<String, String>,
    val confidenceScore: Float,
    val detectedDate: String,
    val doctorVerificationNeeded: Boolean = true
)

data class RedFlagAssessment(
    val isEmergency: Boolean,
    val urgencyLevel: String, // URGENT, MODERATE, ROUTINE
    val alertTitle: String,
    val alertMessage: String,
    val alertMessageTamil: String,
    val triageRecommended: Boolean
)

class DefaultAiHealthcareService : AiHealthcareService {

    override suspend fun conductConversationalStep(
        chiefComplaint: String,
        userResponse: String,
        stepIndex: Int
    ): AiConversationStep {
        delay(400) // Realistic asynchronous processing
        return when (chiefComplaint.lowercase()) {
            "chest pain" -> when (stepIndex) {
                0 -> AiConversationStep(
                    question = "When did this chest pain begin?",
                    questionTamil = "இந்த நெஞ்சு வலி எப்போது தொடங்கியது?",
                    options = listOf("Two days ago", "Today morning", "Suddenly 1 hour ago", "More than a week ago"),
                    optionsTamil = listOf("இரண்டு நாட்களுக்கு முன்பு", "இன்று காலை", "திடீரென 1 மணி நேரம் முன்பு", "ஒரு வாரத்திற்கும் மேல்")
                )
                1 -> AiConversationStep(
                    question = "Where specifically do you feel the pain?",
                    questionTamil = "குறிப்பாக வலி எங்கு உணரப்படுகிறது?",
                    options = listOf("Center of chest (Substernal)", "Left side of chest", "Right side", "Spreading across chest"),
                    optionsTamil = listOf("நெஞ்சின் மையப்பகுதி", "நெஞ்சின் இடது பகுதி", "வலது பகுதி", "முழு நெஞ்சிலும் பரவுகிறது")
                )
                2 -> AiConversationStep(
                    question = "How would you describe the character of the pain?",
                    questionTamil = "வலியை எவ்வாறு விவரிக்கிறீர்கள்?",
                    options = listOf("Pressure / Heavy Tightness", "Sharp / Stabbing", "Burning / Acidity sensation", "Dull ache"),
                    optionsTamil = listOf("அழுத்தம் / பாரமான இறுக்கம்", "கூர்மையான வலி", "எரிச்சல் போன்ற உணர்வு", "மந்தமான வலி")
                )
                3 -> AiConversationStep(
                    question = "Does the pain radiate or spread anywhere?",
                    questionTamil = "இந்த வலி மற்ற இடங்களுக்கு பரவுகிறதா?",
                    options = listOf("Left arm & shoulder", "Jaw / Neck", "Upper back", "Does not radiate"),
                    optionsTamil = listOf("இடது கை மற்றும் தோள்பட்டை", "தாடை / கழுத்து", "முதுகுப் பகுதி", "எங்கும் பரவவில்லை"),
                    isRedFlagCheck = true
                )
                4 -> AiConversationStep(
                    question = "Do you have any of these associated symptoms?",
                    questionTamil = "இவற்றுடன் வேறு ஏதேனும் அறிகுறிகள் உள்ளதா?",
                    options = listOf("Breathlessness & Sweating", "Nausea or vomiting", "Dizziness / lightheadedness", "None of these"),
                    optionsTamil = listOf("மூச்சுத் திணறல் & வியர்வை", "குமட்டல் அல்லது வாந்தி", "மயக்கம்", "எதுவுமில்லை"),
                    isRedFlagCheck = true
                )
                else -> AiConversationStep(
                    question = "Does anything make the pain worse or better?",
                    questionTamil = "எது வலியை அதிகப்படுத்துகிறது அல்லது குறைக்கிறது?",
                    options = listOf("Worse on walking/stairs; better on rest", "Worse on deep breathing or coughing", "Worse after meals", "No specific trigger"),
                    optionsTamil = listOf("நடக்கும்போது அதிகம்; ஓய்வில் குறைகிறது", "ஆழ்ந்து சுவாசிக்கும் போது அதிகம்", "உணவுக்குப் பின் அதிகம்", "குறிப்பிட்ட காரணி இல்லை")
                )
            }
            "fever" -> when (stepIndex) {
                0 -> AiConversationStep(
                    question = "How many days have you had the fever?",
                    questionTamil = "காய்ச்சல் எத்தனை நாட்களாக உள்ளது?",
                    options = listOf("1-2 days", "3-5 days", "More than a week", "Intermittent / comes and goes"),
                    optionsTamil = listOf("1-2 நாட்கள்", "3-5 நாட்கள்", "ஒரு வாரத்திற்கு மேல்", "விட்டு விட்டு வருகிறது")
                )
                1 -> AiConversationStep(
                    question = "Do you have chills, body aches, or shivering?",
                    questionTamil = "குளிர் நடுக்கம் அல்லது உடல் வலி உள்ளதா?",
                    options = listOf("High fever with shivering", "Moderate fever with headache", "Mild fever with body pain", "None"),
                    optionsTamil = listOf("நடுக்கத்துடன் தீவிர காய்ச்சல்", "தலைவலியுடன் மிதமான காய்ச்சல்", "உடல் வலியுடன் லேசான காய்ச்சல்", "இல்லை")
                )
                else -> AiConversationStep(
                    question = "Any associated cough, cold, or throat pain?",
                    questionTamil = "இருமல், சளி அல்லது தொண்டை வலி உள்ளதா?",
                    options = listOf("Severe dry cough", "Productive cough with sputum", "Sore throat", "No respiratory symptoms"),
                    optionsTamil = listOf("வறட்டு இருமல்", "சளியுடன் இருமல்", "தொண்டை வலி", "சுவாச அறிகுறிகள் இல்லை")
                )
            }
            else -> AiConversationStep(
                question = "Could you specify the onset and severity of your symptoms?",
                questionTamil = "உங்கள் அறிகுறிகளின் காலம் மற்றும் தீவிரத்தை குறிப்பிட முடியுமா?",
                options = listOf("Started suddenly today", "Gradual over few days", "Chronic / long-standing", "Triggered by exertion"),
                optionsTamil = listOf("திடீரென தொடங்கியது", "சில நாட்களாக அதிகரித்துள்ளது", "நீண்ட நாட்களாக உள்ளது", "வேலை செய்யும்போது ஏற்படுகிறது")
            )
        }
    }

    override suspend fun performDocumentOcr(
        category: DocumentCategory,
        documentTitle: String
    ): OcrExtractionResult {
        delay(800) // Simulated high-accuracy OCR engine
        return when (category) {
            DocumentCategory.LAB_REPORT -> OcrExtractionResult(
                rawText = "TAMIL NADU GOVT HEALTH SYSTEM\nDiagnostic Pathology Report\nPatient: Arun Kumar | Ref: MHID-2026-00125\nHemoglobin: 10.2 g/dL (Normal: 13-17)\nFasting Blood Glucose: 156 mg/dL (High)\nSerum Creatinine: 1.1 mg/dL (Normal: 0.7-1.3)\nTotal Leukocyte Count: 7,400 /uL",
                extractedParameters = mapOf(
                    "Hemoglobin (Hb)" to "10.2 g/dL (Mild Anemia)",
                    "Fasting Blood Glucose" to "156 mg/dL (Elevated)",
                    "Serum Creatinine" to "1.1 mg/dL (Normal)",
                    "Total Leukocyte Count" to "7,400 /uL (Normal)"
                ),
                confidenceScore = 0.97f,
                detectedDate = "15 Sep 2026"
            )
            DocumentCategory.PRESCRIPTION -> OcrExtractionResult(
                rawText = "GOVT GENERAL HOSPITAL OPD Rx\nPatient: Arun Kumar | Date: 15-09-2026\n1. Tab. Amlodipine 5mg - 1-0-0 - 30 days\n2. Tab. Atorvastatin 10mg - 0-0-1 - 30 days\n3. Tab. Aspirin 75mg - 0-1-0 - 30 days",
                extractedParameters = mapOf(
                    "Medication 1" to "Tab. Amlodipine 5mg (1-0-0)",
                    "Medication 2" to "Tab. Atorvastatin 10mg (0-0-1)",
                    "Medication 3" to "Tab. Aspirin 75mg (0-1-0)"
                ),
                confidenceScore = 0.95f,
                detectedDate = "15 Sep 2026"
            )
            DocumentCategory.IMAGING_REPORT -> OcrExtractionResult(
                rawText = "DEPARTMENT OF CARDIAC RADIOLOGY\nStandard 12-Lead Electrocardiogram\nRate: 82 bpm | Rhythm: Sinus Rhythm\nPR Interval: 160ms | QRS: 88ms\nFindings: T-wave flattening in lead V5-V6. Correlate clinically.",
                extractedParameters = mapOf(
                    "Heart Rate" to "82 bpm",
                    "Rhythm" to "Normal Sinus Rhythm",
                    "Electrocardiogram" to "Mild lateral T-wave flattening",
                    "Clinical Priority" to "Cardiology Review Recommended"
                ),
                confidenceScore = 0.96f,
                detectedDate = "15 Sep 2026"
            )
            else -> OcrExtractionResult(
                rawText = "CLINICAL SUMMARY DOCUMENT\nExtracted summary from uploaded government health file.",
                extractedParameters = mapOf(
                    "Document Status" to "Successfully Digitize-Indexed",
                    "Validation" to "Awaiting Attending Physician Verification"
                ),
                confidenceScore = 0.92f,
                detectedDate = "15 Sep 2026"
            )
        }
    }

    override fun evaluateRedFlags(
        complaint: String,
        severity: Int,
        symptoms: List<String>
    ): RedFlagAssessment {
        val hasSevereChestPain = complaint.contains("chest", ignoreCase = true) && severity >= 7
        val hasRadiatingPain = symptoms.any { it.contains("arm", ignoreCase = true) || it.contains("jaw", ignoreCase = true) }
        val hasBreathlessnessOrSweating = symptoms.any { it.contains("breath", ignoreCase = true) || it.contains("sweat", ignoreCase = true) }

        if (hasSevereChestPain || (complaint.contains("chest", ignoreCase = true) && (hasRadiatingPain || hasBreathlessnessOrSweating))) {
            return RedFlagAssessment(
                isEmergency = true,
                urgencyLevel = "URGENT MEDICAL ATTENTION MAY BE REQUIRED",
                alertTitle = "Potential Emergency Symptom Detected",
                alertMessage = "Immediate clinical assessment recommended. Chest symptoms with radiation or dyspnea require prompt emergency triage by hospital staff.",
                alertMessageTamil = "உடனடி மருத்துவ பரிசோதனை தேவைப்படலாம். அவசர சிகிச்சை பிரிவை உடனடியாக தொடர்பு கொள்ளவும்.",
                triageRecommended = true
            )
        }

        return RedFlagAssessment(
            isEmergency = false,
            urgencyLevel = "ROUTINE CLINICAL CARE",
            alertTitle = "Standard Clinical Intake",
            alertMessage = "Your symptoms have been structured for the attending doctor's review.",
            alertMessageTamil = "உங்கள் அறிகுறிகள் மருத்துவரின் மதிப்பாய்விற்காக பதிவு செய்யப்பட்டுள்ளன.",
            triageRecommended = false
        )
    }

    override suspend fun generateStructuredSummary(
        patientName: String,
        age: Int,
        historyData: SocratesChestPainData,
        pastDiseases: List<String>
    ): AiClinicalSummary {
        delay(600)
        return AiClinicalSummary(
            chiefComplaint = "Chest tightness / pressure for ${historyData.onset}",
            historyOfPresentIllness = "$age-year-old male presenting with ${historyData.character.lowercase()} at ${historyData.site.lowercase()}. Radiation to ${historyData.radiation.lowercase()}. Associated with ${historyData.associatedSymptoms.joinToString(", ")}. Symptoms worsen with physical exertion. Pain severity reported as ${historyData.severity}/10.",
            pastMedicalHistory = pastDiseases.joinToString(", "),
            pastSurgicalHistory = "None reported.",
            drugHistory = "Amlodipine 5mg OD, Atorvastatin 10mg HS. Known Penicillin allergy.",
            allergies = "Penicillin (Mild skin hypersensitivity)",
            familyHistory = "Paternal history of ischemic heart disease at age 58.",
            personalHistory = "Non-smoker, sedentary desk lifestyle.",
            reviewOfSystems = "Positive for exertional dyspnea & chest pressure. Negative for hemoptysis, syncope, or orthopnea.",
            previousInvestigations = "Hb 10.8 g/dL (Aug 2025), Echo LVEF 60% with concentric LVH (Jul 2026).",
            currentMedications = "Amlodipine 5mg, Atorvastatin 10mg",
            status = "AI DRAFT — Physician Verification Required"
        )
    }

    override suspend fun processOcrDocument(
        documentTitle: String,
        category: DocumentCategory
    ): com.example.data.model.MedicalDocument {
        val ocr = performDocumentOcr(category, documentTitle)
        return com.example.data.model.MedicalDocument(
            patientHealthId = "MHID-2026-00125",
            title = documentTitle,
            category = category,
            date = ocr.detectedDate,
            hospitalName = "Government General Hospital - Coimbatore",
            fileType = "Paper Scan / OCR",
            ocrRawText = ocr.rawText,
            extractedValues = ocr.extractedParameters,
            ocrConfidence = ocr.confidenceScore,
            doctorVerified = false
        )
    }
}
