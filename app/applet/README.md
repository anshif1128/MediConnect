# MediConnect: AI-Assisted Clinical Case-Taking & Unified Patient Health Record

[![Platform](https://img.shields.io/badge/Platform-Android-green.svg)](https://developer.android.com)
[![Language](https://img.shields.io/badge/Language-Kotlin%20100%25-purple.svg)](https://kotlinlang.org)
[![UI Framework](https://img.shields.io/badge/UI-Jetpack%20Compose%20M3-blue.svg)](https://developer.android.com/jetpack/compose)
[![Compliance](https://img.shields.io/badge/Standards-ABDM%20%2F%20FHIR%20R4%20%2F%20AIIA-teal.svg)](https://abdm.gov.in)
[![Hackathon](https://img.shields.io/badge/SIH-Problem%20Statement%2026047-orange.svg)](https://sih.gov.in)

> **Smart India Hackathon (SIH) Prototype**  
> **Problem Statement ID:** 26047 — *"Patient Case-Taking Software"*  
> **Ministry / Department:** Ministry of Ayush / All India Institute of Ayurveda (AIIA)  
> **Category:** Software / MedTech / BioTech / HealthTech  

---

## 🌟 The Core Vision

> **ONE PATIENT → ONE DIGITAL HEALTH HISTORY → SECURE ACCESS ACROSS GOVERNMENT HOSPITALS**

In public healthcare, patients often visit **Hospital A** (e.g., Coimbatore) today and **Hospital B** (e.g., Madurai) weeks later. Currently, they carry paper prescriptions, lose previous laboratory reports, and have to explain their entire medical history from scratch.

**MediConnect** solves this "first-mile" healthcare problem:
1. Digitize previous prescriptions, blood tests, and scans into an encrypted longitudinal timeline.
2. AI-assisted conversational case-taking (SOCRATES protocol) with voice input and red-flag emergency detection.
3. Patient-controlled digital consent (ABDM-compliant).
4. Instant, authorized access for the attending doctor at Hospital B, ensuring continuous, high-quality care.
5. Specialized Ministry of Ayush / AIIA clinical assessment module (Prakriti, Agni, Koshtha, Dashavidha Pariksha).
6. **Senior-Citizen First Accessibility**: Large typography, high-contrast visual tiles, step-by-step questions, and multilingual audio guidance (English & தமிழ்).

---

## 🏗️ System Architecture & Workflow

```
PATIENT VISITS GOVERNMENT HOSPITAL A (Coimbatore)
        ↓
Patient registers / logs in (Demo Health ID: MHID-2026-00125)
        ↓
Digital consent granted via ABDM Consent Manager
        ↓
Patient speaks symptoms (SOCRATES protocol) & uploads lab papers
        ↓
AI extracts parameters (Hb, Glucose, ECG) via OCR
        ↓
Longitudinal digital timeline created and encrypted
        ↓
PATIENT LATER VISITS GOVERNMENT HOSPITAL B (Madurai)
        ↓
Patient presents Health ID
        ↓
Doctor requests record access under active patient consent
        ↓
Doctor views complete Coimbatore history + AI pre-structured draft
        ↓
Doctor verifies, edits, and adds new consultation & prescription
        ↓
Patient record updates instantaneously across the government health grid!
```

---

## ✨ Key Features

### 1. 👤 Patient Mobile Application (Senior-Citizen Accessible)
- **Demo Health ID (`MHID-2026-00125`)**: Formatted to ABDM ABHA standards with QR code, clearly labeled as a non-Aadhaar prototype.
- **Radical Simplicity**: 4 large, high-contrast action tiles for easy navigation by elderly citizens.
- **Voice Guidance (`🔊 Listen`)**: Complete audio readout available in English and Tamil (தமிழ்).
- **AI Case-Taking**: Step-by-step conversational intake with voice recording (`🎙️ Tap to Speak`).
- **Emergency Red-Flag Triage**: Automatically flags high-risk symptoms (e.g., chest pain radiating to left arm with sweating) and enables immediate hospital triage alerting.
- **Document Scanner & OCR**: Extracts diagnostic parameters (Hemoglobin, Glucose, Creatinine) from paper reports.
- **Granular Consent Manager**: Explicit Yes/No permission toggles with ability to grant or revoke hospital access individually.

### 2. 🩺 Doctor Clinical Workstation (Cross-Hospital Continuity)
- **Cross-Hospital Search**: Search by Health ID to retrieve records across participating government hospitals.
- **AI Clinical Draft Review**: AI-generated structured clinical summaries with physician **CONFIRM**, **EDIT**, and **REJECT** controls (strictly assists, never autonomously diagnoses).
- **Prescription & Note Entry**: Attending doctors at Hospital B can add clinical diagnoses and prescriptions that sync directly into the unified timeline.

### 3. 🌿 Ministry of Ayush & AIIA Clinical Module
- **Prakriti Assessment**: Vata, Pitta, Kapha constitutional evaluation.
- **Agni & Koshtha Diagnostics**: Manda, Tikshna, Vishama, Samagni digestion and bowel profiling.
- **Dashavidha Pariksha (दशविध परीक्षा)**: Standardized 10-fold clinical diagnostic framework.
- **Ayurvedic Pharmacopeia**: Standardized formulations (e.g., *Arjuna Ksheerapaka*, *Prabhakara Vati*).

### 4. 📊 Hospital Admin & ABDM Sandbox
- System counters (14,820+ registered patients, 42 participating hospitals, <2s cross-hospital retrieval).
- Interactive **FHIR R4 JSON** resource viewer (Patient, Condition, Observation, Consent Artefact).
- Real-time immutable audit trail recording every access attempt, hospital, and timestamp.

---

## 🛠️ Tech Stack & Android Implementation

- **Language:** 100% Kotlin
- **UI Framework:** Jetpack Compose (Modern Material Design 3)
- **Architecture:** Clean MVVM with Reactive State Management (`StateFlow`, `collectAsStateWithLifecycle`)
- **Concurrency:** Kotlin Coroutines & Flow
- **Data Persistence:** Android Room Database & Repository pattern
- **Design System:** Edge-to-Edge (`enableEdgeToEdge()`), Dynamic M3 Color Scheme, 48dp+ accessibility targets
- **Standards:** ABDM (Ayushman Bharat Digital Mission) EHR Guidelines & FHIR R4 Bundle Specifications

---

## 🚀 How to Build & Run Locally

### Prerequisites
- **Android Studio Ladybug (2024.2+)** or newer
- **Android SDK:** Compile SDK 36 / Min SDK 26
- **Java Development Kit (JDK):** JDK 17 or JDK 21

### Steps
1. Clone your repository:
   ```bash
   git clone https://github.com/<your-username>/<your-repo-name>.git
   cd <your-repo-name>
   ```
2. Open the project in **Android Studio**.
3. Allow Gradle to sync dependencies.
4. Select an Android Emulator (API 30+) or a physical device with USB Debugging enabled.
5. Click **Run (`Shift + F10`)** to launch MediConnect!

### Running Tests
To run unit and Robolectric tests from the command line:
```bash
./gradlew :app:testDebugUnitTest
```

---

## 👥 Hackathon Demonstration Guide (5-Minute Pitch)

1. **Role Switcher**: Click the top pill `[ 👤 Patient ▾ ]` to seamlessly switch between Patient, Madurai Doctor, Coimbatore Doctor, and Admin.
2. **Patient Experience**: Show the high-contrast Health Card, listen to the Tamil voice guidance, and walk through the step-by-step voice symptom intake.
3. **Emergency Alert**: Select *Chest Pain* + *Left Arm Radiation* to demonstrate the immediate Triage Alert.
4. **Cross-Hospital Demo**: Switch to *Doctor (Madurai)* and demonstrate that all past records from *Coimbatore GGH* load instantly under active patient consent.
5. **Add New Prescription**: Enter a new prescription in Madurai and show how it instantaneously updates the patient's unified timeline!

---

## 📄 License
Developed for the **Smart India Hackathon (SIH)** under Problem Statement ID 26047.
All Rights Reserved © 2026.
