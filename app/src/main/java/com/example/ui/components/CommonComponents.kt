package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ConsentStatus
import com.example.data.model.Patient
import com.example.data.model.UserRole
import com.example.ui.navigation.NavigationState
import com.example.ui.theme.*

@Composable
fun GovernmentHeader(
    navState: NavigationState,
    modifier: Modifier = Modifier
) {
    Surface(
        color = GovNavy,
        tonalElevation = 4.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Brand and Emblem
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(MedicalTeal),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalHospital,
                            contentDescription = "Medical Cross",
                            tint = Color.White,
                            modifier = Modifier.size(26.dp)
                        )
                    }

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "MediConnect",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                color = AyushGreen,
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = "GOVT HEALTH",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Text(
                            text = if (navState.isTamil) "ஆயுஷ் அமைச்சகம் • எளிய அரசு மருத்துவ சேவை"
                            else "Ministry of Ayush • Simple Public Healthcare",
                            fontSize = 11.sp,
                            color = Color(0xFFCAD5E2)
                        )
                    }
                }

                // Action buttons: Clean Language Switch & Role Switcher
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Language Switch Pill
                    FilledTonalButton(
                        onClick = { navState.isTamil = !navState.isTamil },
                        shape = RoundedCornerShape(20.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = Color(0xFF1E3A5F),
                            contentColor = Color.White
                        ),
                        modifier = Modifier.height(36.dp)
                    ) {
                        Text(
                            text = if (navState.isTamil) "English" else "தமிழ்",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Role Switcher Pill
                    FilledTonalButton(
                        onClick = { navState.showRoleDialog = true },
                        shape = RoundedCornerShape(20.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = MedicalTeal,
                            contentColor = Color.White
                        ),
                        modifier = Modifier.height(36.dp)
                    ) {
                        Text(
                            text = when (navState.currentRole) {
                                UserRole.PATIENT -> "👤 Patient ▾"
                                UserRole.DOCTOR -> "🩺 Doctor ▾"
                                UserRole.ADMIN -> "📊 Admin ▾"
                            },
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }

    // Role Selection Dialog
    if (navState.showRoleDialog) {
        RoleSelectionDialog(
            currentRole = navState.currentRole,
            isDoctorAtMadurai = navState.selectedDoctorHospital.contains("Madurai"),
            onDismiss = { navState.showRoleDialog = false },
            onSelectRole = { role, hospital, doctor ->
                hospital?.let { navState.selectedDoctorHospital = it }
                doctor?.let { navState.selectedDoctorName = it }
                navState.switchRole(role)
            }
        )
    }
}

@Composable
fun RoleSelectionDialog(
    currentRole: UserRole,
    isDoctorAtMadurai: Boolean,
    onDismiss: () -> Unit,
    onSelectRole: (UserRole, String?, String?) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Switch Application View",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = GovNavy
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Select who is currently using this application:",
                    fontSize = 13.sp,
                    color = TextSecondary
                )

                RoleOptionCard(
                    title = "👤 Patient View (Arun Kumar)",
                    desc = "Easy patient mode: Health card, speak symptoms, view test results & consent.",
                    isSelected = currentRole == UserRole.PATIENT,
                    onClick = { onSelectRole(UserRole.PATIENT, null, null) }
                )

                RoleOptionCard(
                    title = "🩺 Hospital B Doctor (Madurai)",
                    desc = "Dr. Priya Sharma accessing patient's prior records from Hospital A.",
                    isSelected = currentRole == UserRole.DOCTOR && isDoctorAtMadurai,
                    onClick = {
                        onSelectRole(
                            UserRole.DOCTOR,
                            "Government Medical College Hospital - Madurai",
                            "Dr. Priya Sharma, MD, DM"
                        )
                    }
                )

                RoleOptionCard(
                    title = "🏥 Hospital A Doctor (Coimbatore)",
                    desc = "Dr. Rajesh Raman (Cardiology) who initially diagnosed the patient.",
                    isSelected = currentRole == UserRole.DOCTOR && !isDoctorAtMadurai,
                    onClick = {
                        onSelectRole(
                            UserRole.DOCTOR,
                            "Government General Hospital - Coimbatore",
                            "Dr. Rajesh Raman, MD (Cardiology)"
                        )
                    }
                )

                RoleOptionCard(
                    title = "📊 Hospital Administrator",
                    desc = "Network metrics, hospital directory, and live privacy audit logs.",
                    isSelected = currentRole == UserRole.ADMIN,
                    onClick = { onSelectRole(UserRole.ADMIN, null, null) }
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close", fontWeight = FontWeight.Bold)
            }
        }
    )
}

@Composable
private fun RoleOptionCard(
    title: String,
    desc: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = if (isSelected) MedicalTealLight else Color(0xFFF8FAFC),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = androidx.compose.ui.graphics.SolidColor(if (isSelected) MedicalTeal else BorderSubtle)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) GovNavy else TextPrimary
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = desc,
                fontSize = 11.sp,
                color = TextSecondary,
                lineHeight = 15.sp
            )
        }
    }
}

@Composable
fun DemoHealthIdCard(
    patient: Patient,
    isTamil: Boolean,
    onAudioClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = modifier
            .fillMaxWidth()
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(GovNavy, Color(0xFF1B4965))
                ),
                shape = RoundedCornerShape(20.dp)
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(MedicalTeal),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Badge,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = if (isTamil) "அரசு மருத்துவ அடையாள அட்டை" else "GOVERNMENT HEALTH CARD",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF64B5F6),
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "Valid at all Government Hospitals",
                            fontSize = 10.sp,
                            color = Color(0xFFCBD5E1)
                        )
                    }
                }

                Surface(
                    color = Color.White.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.clickable { onAudioClick() }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.VolumeUp,
                            contentDescription = "Read Card",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isTamil) "கேள்" else "Listen",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Patient Name Large & Easy to Read
            Text(
                text = patient.name,
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )
            Text(
                text = "${patient.age} Years • ${patient.gender} • Blood: ${patient.bloodGroup}",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFFE2E8F0)
            )

            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(color = Color.White.copy(alpha = 0.2f))
            Spacer(modifier = Modifier.height(14.dp))

            // Large Health ID display & QR
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = if (isTamil) "சுகாதார அடையாள எண் (Health ID):" else "Your Health ID Number:",
                        fontSize = 11.sp,
                        color = Color(0xFF94A3B8)
                    )
                    Text(
                        text = patient.healthId,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF38BDF8),
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "ABHA: ${patient.abhaAddress}",
                        fontSize = 11.sp,
                        color = Color(0xFFE2E8F0)
                    )
                }

                // QR Box
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .background(Color.White, RoundedCornerShape(10.dp))
                        .padding(4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.QrCode2,
                        contentDescription = "Health ID QR",
                        tint = GovNavy,
                        modifier = Modifier.size(46.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = VerifiedGreen,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "✓ Digital Consent Active",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Text(
                    text = "No Paper Required",
                    fontSize = 11.sp,
                    color = Color(0xFFA0AEC0),
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun AudioGuidanceNotification(
    message: String?,
    isPlaying: Boolean,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(visible = isPlaying && message != null) {
        Surface(
            color = Color(0xFF0F766E),
            shape = RoundedCornerShape(14.dp),
            shadowElevation = 6.dp,
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Hearing,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "🔊 Voice Assistant Speaking (குரல் உதவி):",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                        Text(
                            text = message ?: "",
                            fontSize = 13.sp,
                            color = Color(0xFFCCFBF1),
                            maxLines = 3,
                            lineHeight = 17.sp
                        )
                    }
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close audio",
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun StatusBadge(
    status: ConsentStatus,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor, text) = when (status) {
        ConsentStatus.GRANTED -> Triple(VerifiedGreenLight, VerifiedGreen, "GRANTED")
        ConsentStatus.PENDING -> Triple(WarningAmberLight, WarningAmber, "PENDING")
        ConsentStatus.REVOKED -> Triple(EmergencyRedLight, EmergencyRed, "REVOKED")
    }

    Surface(
        color = bgColor,
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
    ) {
        Text(
            text = text,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = textColor,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun AudioListenButton(
    textToSpeak: String,
    navState: NavigationState,
    modifier: Modifier = Modifier
) {
    FilledTonalButton(
        onClick = { navState.playAudio(textToSpeak) },
        shape = RoundedCornerShape(20.dp),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
        colors = ButtonDefaults.filledTonalButtonColors(
            containerColor = Color(0xFFE0F2FE),
            contentColor = GovNavy
        ),
        modifier = modifier.height(36.dp)
    ) {
        Icon(
            imageVector = Icons.Default.VolumeUp,
            contentDescription = "Listen",
            tint = MedicalTeal,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = if (navState.isTamil) "🔊 கேள்" else "🔊 Listen",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
