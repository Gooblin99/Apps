package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.AngkaBandarUiState
import com.example.ui.components.GoldBorderGradient
import com.example.ui.components.GoldOutlinedButton
import com.example.ui.components.GoldPrimaryButton
import com.example.ui.theme.ChampagneGold
import com.example.ui.theme.GoldBright
import com.example.ui.theme.IvoryText
import com.example.ui.theme.JetBrainsMonoFontFamily
import com.example.ui.theme.MutedPlatinum
import com.example.ui.theme.ObsidianCard
import com.example.ui.theme.RubyError
import com.example.ui.theme.RubySurface
import com.example.ui.theme.SubtleSlate

@Composable
fun ChangePasswordDialog(
    state: AngkaBandarUiState,
    onCurrentPasswordChanged: (String) -> Unit,
    onNewPasswordChanged: (String) -> Unit,
    onConfirmPasswordChanged: (String) -> Unit,
    onDismiss: () -> Unit,
    onSubmit: () -> Unit,
) {
    var showCurrent by remember { mutableStateOf(false) }
    var showNew by remember { mutableStateOf(false) }
    var showConfirm by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Surface(
            shape = MaterialTheme.shapes.large,
            color = ObsidianCard,
            border = BorderStroke(1.dp, GoldBorderGradient),
            tonalElevation = 10.dp,
            shadowElevation = 18.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(22.dp),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Rounded.VpnKey,
                        contentDescription = null,
                        tint = ChampagneGold,
                        modifier = Modifier.size(22.dp),
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "UBAH SANDI PEMBUAT",
                        style = MaterialTheme.typography.titleLarge,
                        color = GoldBright,
                        fontSize = 16.sp,
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Khusus pembuat aplikasi. Verifikasi sandi saat ini sebelum menetapkan sandi baru.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MutedPlatinum,
                )

                Spacer(modifier = Modifier.height(18.dp))

                PasswordField(
                    value = state.currentPasswordInput,
                    onValueChange = onCurrentPasswordChanged,
                    label = "Sandi Saat Ini",
                    visible = showCurrent,
                    onToggleVisibility = { showCurrent = !showCurrent },
                    imeAction = ImeAction.Next,
                    fieldTestTag = "current_password_input",
                )

                Spacer(modifier = Modifier.height(12.dp))

                PasswordField(
                    value = state.newPasswordInput,
                    onValueChange = onNewPasswordChanged,
                    label = "Sandi Baru (min. 4 karakter)",
                    visible = showNew,
                    onToggleVisibility = { showNew = !showNew },
                    imeAction = ImeAction.Next,
                    fieldTestTag = "new_password_input",
                )

                Spacer(modifier = Modifier.height(12.dp))

                PasswordField(
                    value = state.confirmPasswordInput,
                    onValueChange = onConfirmPasswordChanged,
                    label = "Konfirmasi Sandi Baru",
                    visible = showConfirm,
                    onToggleVisibility = { showConfirm = !showConfirm },
                    imeAction = ImeAction.Done,
                    fieldTestTag = "confirm_password_input",
                )

                AnimatedVisibility(visible = state.changePasswordError != null) {
                    Surface(
                        color = RubySurface,
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, RubyError.copy(alpha = 0.45f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                    ) {
                        Text(
                            text = state.changePasswordError.orEmpty(),
                            style = MaterialTheme.typography.bodyMedium,
                            color = RubyError,
                            modifier = Modifier
                                .padding(horizontal = 12.dp, vertical = 10.dp)
                                .testTag("change_password_error"),
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    GoldOutlinedButton(
                        text = "Batal",
                        onClick = onDismiss,
                        enabled = !state.isChangingPassword,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("cancel_change_password"),
                    )
                    GoldPrimaryButton(
                        text = if (state.isChangingPassword) "Menyimpan..." else "Simpan",
                        icon = Icons.Rounded.Check,
                        enabled = !state.isChangingPassword,
                        onClick = onSubmit,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("save_new_password"),
                    )
                }
            }
        }
    }
}

@Composable
private fun PasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    visible: Boolean,
    onToggleVisibility: () -> Unit,
    imeAction: ImeAction,
    fieldTestTag: String,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        label = { Text(label) },
        trailingIcon = {
            IconButton(onClick = onToggleVisibility) {
                Icon(
                    imageVector = if (visible) Icons.Rounded.VisibilityOff else Icons.Rounded.Visibility,
                    contentDescription = if (visible) "Sembunyikan sandi" else "Tampilkan sandi",
                    tint = MutedPlatinum,
                )
            }
        },
        visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = imeAction,
        ),
        textStyle = MaterialTheme.typography.bodyLarge.copy(
            fontFamily = JetBrainsMonoFontFamily,
            fontWeight = FontWeight.Medium,
        ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = ChampagneGold,
            unfocusedBorderColor = SubtleSlate,
            focusedLabelColor = GoldBright,
            unfocusedLabelColor = MutedPlatinum,
            focusedTextColor = IvoryText,
            unfocusedTextColor = IvoryText,
            cursorColor = ChampagneGold,
        ),
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .fillMaxWidth()
            .testTag(fieldTestTag),
    )
}
