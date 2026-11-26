package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Key
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.LockOpen
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material.icons.rounded.VpnKey
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AngkaBandarUiState
import com.example.ui.components.GoldPrimaryButton
import com.example.ui.components.LuxuryCard
import com.example.ui.components.LuxuryEmblemBadge
import com.example.ui.components.LuxuryVaultBackdrop
import com.example.ui.theme.ChampagneGold
import com.example.ui.theme.GoldBright
import com.example.ui.theme.IvoryText
import com.example.ui.theme.JetBrainsMonoFontFamily
import com.example.ui.theme.MutedPlatinum
import com.example.ui.theme.ObsidianSurface
import com.example.ui.theme.RubyError
import com.example.ui.theme.RubySurface
import com.example.ui.theme.SubtleSlate

@Composable
fun LockVaultScreen(
    state: AngkaBandarUiState,
    onPasswordChanged: (String) -> Unit,
    onUnlockClick: () -> Unit,
    onOpenChangePassword: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var passwordVisible by remember { mutableStateOf(false) }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        LuxuryVaultBackdrop()

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 24.dp)
                .widthIn(max = 460.dp),
        ) {
            LuxuryEmblemBadge(size = 92.dp)

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "ANGKA BANDAR",
                style = MaterialTheme.typography.displaySmall,
                color = GoldBright,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                shape = RoundedCornerShape(50),
                color = ObsidianSurface.copy(alpha = 0.9f),
                border = BorderStroke(1.dp, ChampagneGold.copy(alpha = 0.45f)),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Lock,
                        contentDescription = null,
                        tint = ChampagneGold,
                        modifier = Modifier.size(14.dp),
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "AKSES TERKUNCI • KHUSUS PEMILIK",
                        style = MaterialTheme.typography.labelMedium,
                        color = ChampagneGold,
                        fontSize = 10.sp,
                    )
                }
            }

            Spacer(modifier = Modifier.height(26.dp))

            LuxuryCard {
                Text(
                    text = "Verifikasi Sandi Pembuat",
                    style = MaterialTheme.typography.titleMedium,
                    color = IvoryText,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Aplikasi ini dilindungi kata sandi khusus pembuat. Masukkan sandi untuk membuka panel prediksi 2D.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MutedPlatinum,
                )

                Spacer(modifier = Modifier.height(18.dp))

                OutlinedTextField(
                    value = state.lockPasswordInput,
                    onValueChange = onPasswordChanged,
                    singleLine = true,
                    label = { Text("Kata Sandi") },
                    placeholder = { Text("Masukkan kata sandi") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Rounded.Key,
                            contentDescription = null,
                            tint = ChampagneGold,
                        )
                    },
                    trailingIcon = {
                        IconButton(
                            onClick = { passwordVisible = !passwordVisible },
                            modifier = Modifier.testTag("toggle_lock_password_visibility"),
                        ) {
                            Icon(
                                imageVector = if (passwordVisible) {
                                    Icons.Rounded.VisibilityOff
                                } else {
                                    Icons.Rounded.Visibility
                                },
                                contentDescription = if (passwordVisible) {
                                    "Sembunyikan kata sandi"
                                } else {
                                    "Tampilkan kata sandi"
                                },
                                tint = MutedPlatinum,
                            )
                        }
                    },
                    visualTransformation = if (passwordVisible) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done,
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { onUnlockClick() }
                    ),
                    isError = state.lockError != null,
                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                        fontFamily = JetBrainsMonoFontFamily,
                        letterSpacing = 1.2.sp,
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ChampagneGold,
                        unfocusedBorderColor = SubtleSlate,
                        focusedLabelColor = GoldBright,
                        unfocusedLabelColor = MutedPlatinum,
                        focusedTextColor = IvoryText,
                        unfocusedTextColor = IvoryText,
                        cursorColor = ChampagneGold,
                        errorBorderColor = RubyError,
                        errorLabelColor = RubyError,
                    ),
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("lock_password_input"),
                )

                AnimatedVisibility(
                    visible = state.lockError != null,
                    enter = fadeIn(),
                    exit = fadeOut(),
                ) {
                    Surface(
                        color = RubySurface,
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, RubyError.copy(alpha = 0.45f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                    ) {
                        Text(
                            text = state.lockError.orEmpty(),
                            style = MaterialTheme.typography.bodyMedium,
                            color = RubyError,
                            modifier = Modifier
                                .padding(horizontal = 12.dp, vertical = 10.dp)
                                .testTag("lock_error_text"),
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                GoldPrimaryButton(
                    text = if (state.isVerifyingLock) "MEMVERIFIKASI..." else "BUKA AKSES",
                    icon = Icons.Rounded.LockOpen,
                    enabled = !state.isVerifyingLock,
                    onClick = onUnlockClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("unlock_button"),
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            TextButton(
                onClick = onOpenChangePassword,
                modifier = Modifier
                    .minimumInteractiveComponentSize()
                    .testTag("open_change_password_from_lock"),
            ) {
                Icon(
                    imageVector = Icons.Rounded.VpnKey,
                    contentDescription = null,
                    tint = ChampagneGold,
                    modifier = Modifier.size(16.dp),
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Ubah Sandi Pembuat",
                    style = MaterialTheme.typography.labelLarge,
                    color = ChampagneGold,
                    fontSize = 13.sp,
                )
            }
        }
    }
}
