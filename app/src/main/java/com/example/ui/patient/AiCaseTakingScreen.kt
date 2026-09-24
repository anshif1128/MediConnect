package com.example.ui.patient

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import com.example.data.model.SocratesChestPainData
import com.example.data.repository.HealthcareRepository
import com.example.ui.components.AudioListenButton
import com.example.ui.navigation.NavigationState
import com.example.ui.theme.*

@Composable
fun AiCaseTakingScreen(
    repository: HealthcareRepository,
    navState: NavigationState,
    modifier: Modifier = Modifier
) {
    val aiService = remember { DefaultAiHealthcareService() }
    val isTamil = navState.isTamil

    // Step-by-step flow (0 to 5)
    var stepIndex by remember { mutableIntStateOf(0) }
    var isRecording by remember { mutableStateOf(false) }
    var triageAlertSent by remember { mutableStateOf(false) }
    var flowCompleted by remember { mutableStateOf(false) }

    // Patient response states
    var selectedComplaint by remember { mutableStateOf("Chest Pain") }
    var site by remember { mutableStateOf("Center of chest (Substernal)") }
    var onset by remember { mutableStateOf("Two days ago") }
    var character by remember { mutableStateOf("Pressure / Heavy Tightness") }
    var radiation by remember { mutableStateOf("Left arm & shoulder") }
    var hasSweatingOrDyspnea by remember { mutableStateOf(true) }
    var severity by remember { mutableFloatStateOf(7f) }

    // Red flag evaluation
    val redFlagAssessment = remember(selectedComplaint, severity, radiation, hasSweatingOrDyspnea) {
        val syms = mutableListOf<String>()
        if (radiation.contains("arm", ignoreCase = true)) syms.add(radiation)
        if (hasSweatingOrDyspnea) syms.add("Breathlessness & Sweating")
        aiService.evaluateRedFlags(selectedComplaint, severity.toInt(), syms)
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .padding(horizontal = 18.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
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
                    IconButton(
                        onClick = { navState.backToMain() },
                        modifier = Modifier.size(44.dp)
                    ) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = GovNavy)
                    }
                    Column {
                        Text(
                            text = if (isTamil) "மருத்துவ உதவியாளர்" else "Doctor's AI Assistant",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = GovNavy
                        )
                        Text(
                            text = if (isTamil) "படிநிலை ${stepIndex + 1} / 6" else "Step ${stepIndex + 1} of 6",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = MedicalTeal
                        )
                    }
                }

                AudioListenButton(
                    textToSpeak = if (isTamil) "உங்கள் உடல்நலப் பிரச்சனையை எளிதாக கூறலாம். எங்கு வலிக்கிறது என்று தட்டுங்கள் அல்லது பேசுங்கள்."
                    else "Answer simple questions to help your doctor understand what hurts. Tap to speak or choose an option.",
                    navState = navState
                )
            }
        }

        // Progress Bar
        item {
            LinearProgressIndicator(
                progress = { (stepIndex + 1) / 6f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = MedicalTeal,
                trackColor = Color(0xFFE2E8F0)
            )
        }

        // Voice Assistant Mic Card (Always prominent for seniors)
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isRecording) Color(0xFFFEE2E2) else Color(0xFFE0F2FE)
                ),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = androidx.compose.ui.graphics.SolidColor(if (isRecording) EmergencyRed else MedicalTeal)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        isRecording = !isRecording
                        if (isRecording) {
                            navState.playAudio(
                                if (isTamil) "கேட்கிறேன்... பேசுங்கள்..."
                                else "Listening... Please speak your symptoms in your own words..."
                            )
                        }
                    }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(if (isRecording) EmergencyRed else MedicalTeal),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isRecording) Icons.Default.Mic else Icons.Default.MicNone,
                            contentDescription = "Speak",
                            tint = Color.White,
                            modifier = Modifier.size(30.dp)
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (isRecording) "🎙️ Listening... (பேசுங்கள்)" else "🎙️ Tap to Speak (பேச தட்டவும்)",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (isRecording) EmergencyRed else GovNavy
                        )
                        Text(
                            text = if (isTamil) "தமிழ் அல்லது ஆங்கிலத்தில் உங்கள் அறிகுறிகளை கூறலாம்"
                            else "You can speak naturally in English, Tamil, or regional language",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }
                }
            }
        }

        // Red Flag Alert Banner (If emergency criteria met)
        if (redFlagAssessment.isEmergency) {
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = EmergencyRedLight),
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(EmergencyRed)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Warning, contentDescription = null, tint = EmergencyRed, modifier = Modifier.size(24.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isTamil) "அவசர மருத்துவ கவனம் தேவைப்படலாம்!" else "Urgent Medical Attention Recommended!",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = EmergencyRed
                            )
                        }

                        Text(
                            text = if (isTamil) "நெஞ்சு வலி தோள்பட்டைக்கு பரவுவது அல்லது மூச்சு திணறல் இருந்தால் உடனே மருத்துவமனை அவசர பிரிவை அணுகவும்."
                            else "Chest pain radiating to the shoulder or accompanied by shortness of breath requires immediate clinical evaluation.",
                            fontSize = 13.sp,
                            color = TextPrimary,
                            lineHeight = 18.sp
                        )

                        Button(
                            onClick = {
                                triageAlertSent = true
                                navState.playAudio("Emergency alert sent to hospital staff.")
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = EmergencyRed),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                        ) {
                            Icon(Icons.Default.NotificationsActive, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (triageAlertSent) "✓ Hospital Staff Alerted!" else "Alert Hospital Triage Desk",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // ONE SINGLE CLEAR QUESTION AT A TIME FOR THE SENIOR CITIZEN
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    when (stepIndex) {
                        0 -> {
                            QuestionHeader(
                                qNumber = 1,
                                question = if (isTamil) "உங்கள் முதன்மையான உடல்நலப் பிரச்சனை என்ன?" else "What is your main health problem today?",
                                onListen = {
                                    navState.playAudio(
                                        if (isTamil) "உங்கள் முதன்மையான உடல்நலப் பிரச்சனை என்ன?"
                                        else "What is your main health problem today?"
                                    )
                                }
                            )

                            BigOptionButton(
                                label = if (isTamil) "🫀 நெஞ்சு வலி (Chest Pain)" else "🫀 Chest Pain / Tightness",
                                isSelected = selectedComplaint == "Chest Pain",
                                onClick = { selectedComplaint = "Chest Pain" }
                            )
                            BigOptionButton(
                                label = if (isTamil) "🌡️ காய்ச்சல் (Fever)" else "🌡️ Fever & Body Ache",
                                isSelected = selectedComplaint == "Fever",
                                onClick = { selectedComplaint = "Fever" }
                            )
                            BigOptionButton(
                                label = if (isTamil) "😮‍💨 மூச்சுத் திணறல் (Breathing Trouble)" else "😮‍💨 Breathing Difficulty",
                                isSelected = selectedComplaint == "Breathing Difficulty",
                                onClick = { selectedComplaint = "Breathing Difficulty" }
                            )
                            BigOptionButton(
                                label = if (isTamil) "🤕 கடுமையான தலைவலி (Headache)" else "🤕 Severe Headache / Dizziness",
                                isSelected = selectedComplaint == "Headache",
                                onClick = { selectedComplaint = "Headache" }
                            )
                        }

                        1 -> {
                            QuestionHeader(
                                qNumber = 2,
                                question = if (isTamil) "நெஞ்சில் எந்த இடத்தில் வலி உள்ளது?" else "Where specifically do you feel the pain?",
                                onListen = {
                                    navState.playAudio(
                                        if (isTamil) "நெஞ்சில் எந்த இடத்தில் வலி உள்ளது?"
                                        else "Where specifically do you feel the pain?"
                                    )
                                }
                            )

                            BigOptionButton(
                                label = if (isTamil) "நெஞ்சின் மையப்பகுதி (Center of Chest)" else "Center of chest (Substernal)",
                                isSelected = site.contains("Center"),
                                onClick = { site = "Center of chest (Substernal)" }
                            )
                            BigOptionButton(
                                label = if (isTamil) "இடது பக்கம் (Left Side)" else "Left side of chest",
                                isSelected = site.contains("Left"),
                                onClick = { site = "Left side of chest" }
                            )
                            BigOptionButton(
                                label = if (isTamil) "வலது பக்கம் (Right Side)" else "Right side of chest",
                                isSelected = site.contains("Right"),
                                onClick = { site = "Right side of chest" }
                            )
                        }

                        2 -> {
                            QuestionHeader(
                                qNumber = 3,
                                question = if (isTamil) "இந்த வலி எப்போது தொடங்கியது?" else "When did this pain begin?",
                                onListen = {
                                    navState.playAudio(
                                        if (isTamil) "இந்த வலி எப்போது தொடங்கியது?"
                                        else "When did this pain begin?"
                                    )
                                }
                            )

                            BigOptionButton(
                                label = if (isTamil) "திடீரென 1 மணி நேரம் முன்பு (Sudden)" else "Suddenly 1 hour ago",
                                isSelected = onset.contains("1 hour"),
                                onClick = { onset = "1 hour ago (Sudden)" }
                            )
                            BigOptionButton(
                                label = if (isTamil) "இன்று காலை (Today Morning)" else "Today morning",
                                isSelected = onset.contains("Today"),
                                onClick = { onset = "Today morning" }
                            )
                            BigOptionButton(
                                label = if (isTamil) "இரண்டு நாட்களுக்கு முன்பு (2 Days Ago)" else "Two days ago (Gradual)",
                                isSelected = onset.contains("Two days"),
                                onClick = { onset = "Two days ago" }
                            )
                        }

                        3 -> {
                            QuestionHeader(
                                qNumber = 4,
                                question = if (isTamil) "வலி எப்படி உணரப்படுகிறது?" else "What does the pain feel like?",
                                onListen = {
                                    navState.playAudio(
                                        if (isTamil) "வலி எப்படி உணரப்படுகிறது?"
                                        else "What does the pain feel like?"
                                    )
                                }
                            )

                            BigOptionButton(
                                label = if (isTamil) "பாரமான அழுத்தம் அல்லது இறுக்கம் (Pressure)" else "Heavy Pressure / Tightness",
                                isSelected = character.contains("Pressure"),
                                onClick = { character = "Pressure / Heavy Tightness" }
                            )
                            BigOptionButton(
                                label = if (isTamil) "கூர்மையான வலி (Sharp / Stabbing)" else "Sharp / Stabbing pain",
                                isSelected = character.contains("Sharp"),
                                onClick = { character = "Sharp / Stabbing" }
                            )
                            BigOptionButton(
                                label = if (isTamil) "எரிச்சல் போன்ற உணர்வு (Burning)" else "Burning / Acidity sensation",
                                isSelected = character.contains("Burning"),
                                onClick = { character = "Burning sensation" }
                            )
                        }

                        4 -> {
                            QuestionHeader(
                                qNumber = 5,
                                question = if (isTamil) "வலி தோள்பட்டை அல்லது தாடைக்கு பரவுகிறதா?" else "Does the pain spread to your arm or jaw?",
                                onListen = {
                                    navState.playAudio(
                                        if (isTamil) "வலி தோள்பட்டை அல்லது தாடைக்கு பரவுகிறதா?"
                                        else "Does the pain spread to your arm or jaw?"
                                    )
                                }
                            )

                            BigOptionButton(
                                label = if (isTamil) "ஆம், இடது கை மற்றும் தோள்பட்டைக்கு பரவுகிறது" else "Yes, spreads to left arm & shoulder",
                                isSelected = radiation.contains("Left arm"),
                                onClick = { radiation = "Left arm & shoulder" }
                            )
                            BigOptionButton(
                                label = if (isTamil) "ஆம், தாடை மற்றும் கழுத்துக்கு பரவுகிறது" else "Yes, spreads to jaw & neck",
                                isSelected = radiation.contains("Jaw"),
                                onClick = { radiation = "Jaw / Neck" }
                            )
                            BigOptionButton(
                                label = if (isTamil) "இல்லை, எங்கும் பரவவில்லை" else "No, stays in one place",
                                isSelected = radiation.contains("not"),
                                onClick = { radiation = "Does not spread" }
                            )
                        }

                        5 -> {
                            QuestionHeader(
                                qNumber = 6,
                                question = if (isTamil) "வலி எவ்வளவு தீவிரமாக உள்ளது? (1 முதல் 10 வரை)" else "How severe is the pain? (1 to 10)",
                                onListen = {
                                    navState.playAudio(
                                        if (isTamil) "வலி எவ்வளவு தீவிரமாக உள்ளது?"
                                        else "How severe is the pain from 1 to 10?"
                                    )
                                }
                            )

                            Text(
                                text = "${severity.toInt()} / 10 ${if (severity >= 7) "⚠️ (Severe Pain)" else "(Moderate)"}",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = if (severity >= 7) EmergencyRed else MedicalTeal
                            )

                            Slider(
                                value = severity,
                                onValueChange = { severity = it },
                                valueRange = 1f..10f,
                                steps = 8,
                                colors = SliderDefaults.colors(
                                    thumbColor = if (severity >= 7) EmergencyRed else MedicalTeal,
                                    activeTrackColor = if (severity >= 7) EmergencyRed else MedicalTeal
                                )
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Mild (1)", fontSize = 12.sp, color = TextSecondary)
                                Text("Moderate (5)", fontSize = 12.sp, color = TextSecondary)
                                Text("Severe (10)", fontSize = 12.sp, color = EmergencyRed, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Big Navigation Buttons (Senior Accessible)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        if (stepIndex > 0) {
                            OutlinedButton(
                                onClick = { stepIndex-- },
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(54.dp)
                            ) {
                                Text("⬅ Back", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        Button(
                            onClick = {
                                if (stepIndex < 5) {
                                    stepIndex++
                                } else {
                                    val socrates = SocratesChestPainData(
                                        site = site,
                                        onset = onset,
                                        character = character,
                                        radiation = radiation,
                                        associatedSymptoms = if (hasSweatingOrDyspnea) listOf("Breathlessness & Sweating") else emptyList(),
                                        severity = severity.toInt()
                                    )
                                    repository.updateSocratesData(socrates)
                                    flowCompleted = true
                                    navState.playAudio("Your health information is saved and ready for your doctor.")
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MedicalTeal),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1.5f)
                                .height(54.dp)
                        ) {
                            Text(
                                text = if (stepIndex == 5) "✓ Save for Doctor" else "Next Question ➔",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }
                }
            }
        }

        // Completion Confirmation Card
        if (flowCompleted) {
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = VerifiedGreenLight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = VerifiedGreen, modifier = Modifier.size(24.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isTamil) "தகவல்கள் பாதுகாப்பாக பதிவு செய்யப்பட்டன!" else "Information Saved for Your Doctor!",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = VerifiedGreen
                            )
                        }

                        Text(
                            text = if (isTamil) "உங்கள் மருத்துவ வரலாறு மருத்துவரின் பார்வைக்கு தயாராக உள்ளது. எங்கும் மீண்டும் சொல்லத் தேவையில்லை."
                            else "Your answers have been pre-structured for the attending doctor at the hospital. You don't have to repeat your story.",
                            fontSize = 13.sp,
                            color = TextPrimary
                        )

                        Button(
                            onClick = { navState.backToMain() },
                            colors = ButtonDefaults.buttonColors(containerColor = VerifiedGreen),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                        ) {
                            Text("Back to Home Screen", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun QuestionHeader(
    qNumber: Int,
    question: String,
    onListen: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Question $qNumber:",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = MedicalTeal
            )
            Text(
                text = question,
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                color = GovNavy,
                lineHeight = 22.sp
            )
        }

        IconButton(
            onClick = onListen,
            modifier = Modifier
                .size(42.dp)
                .background(Color(0xFFE0F2FE), CircleShape)
        ) {
            Icon(
                imageVector = Icons.Default.VolumeUp,
                contentDescription = "Read question",
                tint = MedicalTeal,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}

@Composable
private fun BigOptionButton(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = if (isSelected) MedicalTealLight else Color(0xFFF8FAFC),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = androidx.compose.ui.graphics.SolidColor(if (isSelected) MedicalTeal else BorderSubtle)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                fontSize = 15.sp,
                fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium,
                color = if (isSelected) GovNavy else TextPrimary,
                modifier = Modifier.weight(1f)
            )

            RadioButton(
                selected = isSelected,
                onClick = onClick,
                colors = RadioButtonDefaults.colors(selectedColor = MedicalTeal)
            )
        }
    }
}
