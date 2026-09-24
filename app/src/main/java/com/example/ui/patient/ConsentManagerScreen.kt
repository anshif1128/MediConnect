package com.example.ui.patient

import androidx.compose.foundation.background
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
import com.example.data.model.ConsentRecord
import com.example.data.model.ConsentStatus
import com.example.data.repository.HealthcareRepository
import com.example.ui.components.AudioListenButton
import com.example.ui.components.StatusBadge
import com.example.ui.navigation.NavigationState
import com.example.ui.theme.*

@Composable
fun ConsentManagerScreen(
    repository: HealthcareRepository,
    navState: NavigationState,
    modifier: Modifier = Modifier
) {
    val consents by repository.consents.collectAsState()
    val isTamil = navState.isTamil

    var allowViewHistory by remember { mutableStateOf(true) }
    var allowShareReports by remember { mutableStateOf(true) }
    var allowAiOrganize by remember { mutableStateOf(true) }

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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { navState.backToMain() }, modifier = Modifier.size(44.dp)) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = GovNavy)
                    }
                    Column {
                        Text(
                            text = if (isTamil) "என் அனுமதி & பாதுகாப்பு" else "My Health Privacy",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = GovNavy
                        )
                        Text(
                            text = if (isTamil) "யார் உங்கள் தகவலை பார்க்கலாம்?" else "Who can see your health records?",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }
                }

                AudioListenButton(
                    textToSpeak = if (isTamil) "உங்கள் மருத்துவ ஆவணங்களை அரசு மருத்துவர்கள் பார்க்க நீங்கள் மட்டுமே அனுமதி அளிக்க முடியும். எப்போது வேண்டுமானாலும் ரத்து செய்யலாம்."
                    else "You are in full control of your medical records. You decide which government hospital doctors can view your files.",
                    navState = navState
                )
            }
        }

        // Plain English explanation card
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(VerifiedGreen)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(VerifiedGreen),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Lock, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp))
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (isTamil) "உங்கள் தரவு 100% பாதுகாப்பானது" else "Your Data is 100% Private",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = GovNavy
                        )
                        Text(
                            text = if (isTamil) "உங்கள் அனுமதி இல்லாமல் எந்த மருத்துவரும் உங்கள் பழைய ஆவணங்களை பார்க்க முடியாது."
                            else "No doctor can view your past reports without your explicit digital consent.",
                            fontSize = 12.sp,
                            color = TextSecondary,
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }

        // Simple Yes/No toggles for Seniors
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
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = if (isTamil) "எளிய அனுமதிகள் (Yes / No):" else "Simple Permissions (Yes / No):",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = GovNavy
                    )

                    SeniorSwitchRow(
                        title = if (isTamil) "மருத்துவர்கள் முந்தைய பதிவுகளைப் பார்க்கலாமா?" else "Allow doctors to view past medical history?",
                        desc = if (isTamil) "கோயம்புத்தூரில் எடுத்த சிகிச்சையை மதுரையில் உள்ள மருத்துவர் அறிய உதவும்." else "Helps the new doctor understand your previous treatments.",
                        checked = allowViewHistory,
                        onCheckedChange = { allowViewHistory = it }
                    )

                    HorizontalDivider(color = Color(0xFFF1F5F9))

                    SeniorSwitchRow(
                        title = if (isTamil) "ரத்த பரிசோதனை மற்றும் ஸ்கேன் முடிவுகளைப் பகிரலாமா?" else "Allow sharing of blood tests and scan reports?",
                        desc = if (isTamil) "மீண்டும் அதே பரிசோதனைகளை தேவையின்றி செய்வதை தவிர்க்கும்." else "Avoids unnecessary repeat tests and needle pricks.",
                        checked = allowShareReports,
                        onCheckedChange = { allowShareReports = it }
                    )

                    HorizontalDivider(color = Color(0xFFF1F5F9))

                    SeniorSwitchRow(
                        title = if (isTamil) "செயற்கை நுண்ணறிவு (AI) ஆவணங்களை ஒழுங்குபடுத்தலாமா?" else "Allow AI to organize papers chronologically?",
                        desc = if (isTamil) "மருத்துவர் வேகமாக படிக்க வசதியாக சுருக்கம் தயாரிக்கும்." else "Sorts prescriptions so the doctor can read quickly.",
                        checked = allowAiOrganize,
                        onCheckedChange = { allowAiOrganize = it }
                    )
                }
            }
        }

        // Active Hospital Permissions List
        item {
            Text(
                text = if (isTamil) "அனுமதி பெற்றுள்ள மருத்துவமனைகள்:" else "Authorized Government Hospitals:",
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold,
                color = GovNavy
            )
        }

        items(consents) { consent ->
            SeniorHospitalConsentCard(
                consent = consent,
                onToggle = { newStatus ->
                    repository.updateConsentStatus(consent.id, newStatus)
                }
            )
        }
    }
}

@Composable
private fun SeniorSwitchRow(
    title: String,
    desc: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = GovNavy)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = desc, fontSize = 12.sp, color = TextSecondary, lineHeight = 16.sp)
        }
        Spacer(modifier = Modifier.width(12.dp))
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = MedicalTeal)
        )
    }
}

@Composable
private fun SeniorHospitalConsentCard(
    consent: ConsentRecord,
    onToggle: (ConsentStatus) -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
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
                    text = consent.hospitalName,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = GovNavy,
                    modifier = Modifier.weight(1f)
                )
                StatusBadge(status = consent.status)
            }

            Text(
                text = "Attending Physician: ${consent.doctorName}",
                fontSize = 12.sp,
                color = TextSecondary
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Valid Until: ${consent.validUntil}",
                    fontSize = 11.sp,
                    color = TextMuted
                )

                if (consent.status == ConsentStatus.GRANTED) {
                    OutlinedButton(
                        onClick = { onToggle(ConsentStatus.REVOKED) },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = EmergencyRed),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                        modifier = Modifier.height(36.dp)
                    ) {
                        Text("Revoke (ரத்து)", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                } else {
                    Button(
                        onClick = { onToggle(ConsentStatus.GRANTED) },
                        colors = ButtonDefaults.buttonColors(containerColor = VerifiedGreen),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                        modifier = Modifier.height(36.dp)
                    ) {
                        Text("Grant Permission", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
