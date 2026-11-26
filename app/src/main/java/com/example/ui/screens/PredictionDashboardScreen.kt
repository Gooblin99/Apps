package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material.icons.rounded.DeleteOutline
import androidx.compose.material.icons.rounded.Diamond
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.VpnKey
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.CopyFormat
import com.example.domain.PredictionResult
import com.example.domain.PredictionSheet
import com.example.ui.AngkaBandarUiState
import com.example.ui.components.FourDigitSlotsRow
import com.example.ui.components.GoldOutlinedButton
import com.example.ui.components.GoldPrimaryButton
import com.example.ui.components.LineBoxTone
import com.example.ui.components.LuxuryCard
import com.example.ui.components.LuxuryEmblemBadge
import com.example.ui.components.LuxuryVaultBackdrop
import com.example.ui.components.PredictionLineTile
import com.example.ui.theme.BlueTileBorder
import com.example.ui.theme.BlueTileSurface
import com.example.ui.theme.BlueTileText
import com.example.ui.theme.ChampagneGold
import com.example.ui.theme.EmeraldAccent
import com.example.ui.theme.EmeraldSurface
import com.example.ui.theme.GoldBright
import com.example.ui.theme.GreenTileBorder
import com.example.ui.theme.GreenTileSurface
import com.example.ui.theme.GreenTileText
import com.example.ui.theme.IvoryText
import com.example.ui.theme.JetBrainsMonoFontFamily
import com.example.ui.theme.MutedPlatinum
import com.example.ui.theme.ObsidianCard
import com.example.ui.theme.ObsidianElevated
import com.example.ui.theme.ObsidianSurface
import com.example.ui.theme.RedTileBorder
import com.example.ui.theme.RedTileSurface
import com.example.ui.theme.RedTileText
import com.example.ui.theme.RubyError
import com.example.ui.theme.SubtleSlate
import com.example.ui.theme.YellowTileBorder
import com.example.ui.theme.YellowTileSurface
import com.example.ui.theme.YellowTileText
import kotlinx.coroutines.delay

@Composable
fun PredictionDashboardScreen(
    state: AngkaBandarUiState,
    snackbarHostState: SnackbarHostState,
    on4DInputChanged: (String) -> Unit,
    onGenerateClick: () -> Unit,
    onResetClick: () -> Unit,
    onCopyFormatSelected: (CopyFormat) -> Unit,
    onHistoryItemSelected: (PredictionResult) -> Unit,
    onClearHistory: () -> Unit,
    onOpenChangePassword: () -> Unit,
    onLockApp: () -> Unit,
    onShowSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val clipboardManager = LocalClipboardManager.current
    val focusManager = LocalFocusManager.current
    val inputFocusRequester = remember { FocusRequester() }
    var justCopiedAll by remember { mutableStateOf(false) }
    var copiedSheet by remember { mutableStateOf<PredictionSheet?>(null) }

    LaunchedEffect(justCopiedAll) {
        if (justCopiedAll) {
            delay(1800)
            justCopiedAll = false
        }
    }

    LaunchedEffect(copiedSheet) {
        if (copiedSheet != null) {
            delay(1800)
            copiedSheet = null
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        LuxuryVaultBackdrop()

        Scaffold(
            containerColor = Color.Transparent,
            contentWindowInsets = WindowInsets.safeDrawing,
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState) { data ->
                    Surface(
                        shape = MaterialTheme.shapes.medium,
                        color = ObsidianElevated,
                        border = BorderStroke(1.dp, EmeraldAccent.copy(alpha = 0.6f)),
                        shadowElevation = 10.dp,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Check,
                                contentDescription = null,
                                tint = EmeraldAccent,
                                modifier = Modifier.size(18.dp),
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = data.visuals.message,
                                style = MaterialTheme.typography.bodyMedium,
                                color = IvoryText,
                                fontWeight = FontWeight.SemiBold,
                            )
                        }
                    }
                }
            },
        ) { innerPadding ->
            Box(
                contentAlignment = Alignment.TopCenter,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            ) {
                Column(
                    modifier = Modifier
                        .widthIn(max = 560.dp)
                        .fillMaxSize()
                        .imePadding()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                ) {
                    DashboardHeader(
                        onOpenChangePassword = onOpenChangePassword,
                        onLockApp = onLockApp,
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // 4D Input Card
                    LuxuryCard {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Column {
                                Text(
                                    text = "INPUT RESULT 4D",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = GoldBright,
                                    fontSize = 15.sp,
                                )
                                Text(
                                    text = "Masukkan 4 angka result untuk dianalisa",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MutedPlatinum,
                                )
                            }
                            Surface(
                                shape = RoundedCornerShape(50),
                                color = ObsidianSurface,
                                border = BorderStroke(1.dp, ChampagneGold.copy(alpha = 0.4f)),
                            ) {
                                Text(
                                    text = "${state.input4D.length}/4",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = ChampagneGold,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        FourDigitSlotsRow(
                            digits = state.input4D,
                            onSlotClick = { inputFocusRequester.requestFocus() },
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        OutlinedTextField(
                            value = state.input4D,
                            onValueChange = on4DInputChanged,
                            singleLine = true,
                            label = { Text("Angka Result 4D (Contoh: 4829)") },
                            placeholder = { Text("Ketik 4 digit angka") },
                            trailingIcon = {
                                if (state.input4D.isNotEmpty()) {
                                    IconButton(
                                        onClick = { on4DInputChanged("") },
                                        modifier = Modifier.testTag("clear_4d_input_button"),
                                    ) {
                                        Icon(
                                            imageVector = Icons.Rounded.Clear,
                                            contentDescription = "Hapus input 4D",
                                            tint = MutedPlatinum,
                                        )
                                    }
                                }
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Done,
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    focusManager.clearFocus()
                                    onGenerateClick()
                                }
                            ),
                            isError = state.inputError != null,
                            supportingText = state.inputError?.let { err ->
                                {
                                    Text(
                                        text = err,
                                        color = RubyError,
                                        style = MaterialTheme.typography.bodyMedium,
                                    )
                                }
                            },
                            textStyle = MaterialTheme.typography.titleMedium.copy(
                                fontFamily = JetBrainsMonoFontFamily,
                                letterSpacing = 4.sp,
                                fontWeight = FontWeight.Bold,
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
                            ),
                            shape = MaterialTheme.shapes.medium,
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(inputFocusRequester)
                                .testTag("input_4d_field"),
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        GoldPrimaryButton(
                            text = "GENERATE PREDIKSI 2D",
                            icon = Icons.Rounded.AutoAwesome,
                            onClick = {
                                focusManager.clearFocus()
                                onGenerateClick()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("generate_button"),
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // 100 Line 2D Prediction Result Card (4 Lembar Transparan x 25 Line)
                    LuxuryCard {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Text(
                                text = "PREDIKSI 2D • 100 LINE",
                                style = MaterialTheme.typography.titleLarge,
                                color = GoldBright,
                                fontSize = 15.sp,
                            )
                            if (state.predictionResult != null) {
                                Surface(
                                    shape = RoundedCornerShape(50),
                                    color = EmeraldSurface,
                                    border = BorderStroke(1.dp, EmeraldAccent.copy(alpha = 0.4f)),
                                ) {
                                    Text(
                                        text = "4D: ${state.predictionResult.input4D} • E=${state.predictionResult.biji2D}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = EmeraldAccent,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        AnimatedVisibility(
                            visible = state.predictionResult == null,
                            enter = fadeIn(),
                            exit = fadeOut(),
                        ) {
                            EmptyPredictionState()
                        }

                        AnimatedVisibility(
                            visible = state.predictionResult != null,
                            enter = fadeIn() + expandVertically(
                                animationSpec = spring(stiffness = Spring.StiffnessMediumLow)
                            ),
                            exit = fadeOut() + shrinkVertically(),
                        ) {
                            state.predictionResult?.let { result ->
                                PredictionResultContent(
                                    result = result,
                                    copyFormat = state.copyFormat,
                                    justCopiedAll = justCopiedAll,
                                    copiedSheet = copiedSheet,
                                    onCopyFormatSelected = onCopyFormatSelected,
                                    onResetClick = {
                                        justCopiedAll = false
                                        copiedSheet = null
                                        onResetClick()
                                    },
                                    onCopySheetClick = { sheet ->
                                        val formatted = result.formatSheetStarSeparated(sheet)
                                        clipboardManager.setText(AnnotatedString(formatted))
                                        copiedSheet = sheet
                                        onShowSnackbar("25 Line ${sheet.title} berhasil disalin!")
                                    },
                                    onCopyAllClick = {
                                        val formatted = result.formatForClipboard(state.copyFormat)
                                        clipboardManager.setText(AnnotatedString(formatted))
                                        justCopiedAll = true
                                        onShowSnackbar("100 Line 2D berhasil disalin ke clipboard!")
                                    },
                                )
                            }
                        }
                    }

                    // Session History Strip
                    AnimatedVisibility(visible = state.history.isNotEmpty()) {
                        Column {
                            Spacer(modifier = Modifier.height(16.dp))
                            LuxuryCard {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth(),
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Rounded.History,
                                            contentDescription = null,
                                            tint = ChampagneGold,
                                            modifier = Modifier.size(18.dp),
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "RIWAYAT SESI",
                                            style = MaterialTheme.typography.titleSmall,
                                            color = GoldBright,
                                        )
                                    }
                                    TextButton(
                                        onClick = onClearHistory,
                                        modifier = Modifier.testTag("clear_history_button"),
                                    ) {
                                        Icon(
                                            imageVector = Icons.Rounded.DeleteOutline,
                                            contentDescription = null,
                                            tint = MutedPlatinum,
                                            modifier = Modifier.size(16.dp),
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "Hapus",
                                            style = MaterialTheme.typography.labelMedium,
                                            color = MutedPlatinum,
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .horizontalScroll(rememberScrollState()),
                                ) {
                                    state.history.forEach { item ->
                                        val selected = state.predictionResult?.input4D == item.input4D
                                        Surface(
                                            shape = RoundedCornerShape(12.dp),
                                            color = if (selected) ObsidianElevated else ObsidianSurface,
                                            border = BorderStroke(
                                                width = 1.dp,
                                                color = if (selected) ChampagneGold else SubtleSlate,
                                            ),
                                            modifier = Modifier
                                                .minimumInteractiveComponentSize()
                                                .clickable { onHistoryItemSelected(item) }
                                                .testTag("history_chip_${item.input4D}"),
                                        ) {
                                            Column(
                                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                            ) {
                                                Text(
                                                    text = "4D ${item.input4D}",
                                                    fontFamily = JetBrainsMonoFontFamily,
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 14.sp,
                                                    color = GoldBright,
                                                )
                                                Text(
                                                    text = item.lines2D.take(3).joinToString("*") + "...",
                                                    style = MaterialTheme.typography.labelSmall,
                                                    color = MutedPlatinum,
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
private fun DashboardHeader(
    onOpenChangePassword: () -> Unit,
    onLockApp: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f),
        ) {
            LuxuryEmblemBadge(size = 46.dp)
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "ANGKA BANDAR",
                    style = MaterialTheme.typography.titleLarge,
                    color = GoldBright,
                    fontSize = 18.sp,
                )
                Text(
                    text = "KALKULATOR 2D EKSKLUSIF",
                    style = MaterialTheme.typography.labelMedium,
                    color = MutedPlatinum,
                    fontSize = 10.sp,
                )
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Surface(
                shape = CircleShape,
                color = ObsidianCard,
                border = BorderStroke(1.dp, ChampagneGold.copy(alpha = 0.5f)),
            ) {
                IconButton(
                    onClick = onOpenChangePassword,
                    modifier = Modifier.testTag("change_password_button"),
                ) {
                    Icon(
                        imageVector = Icons.Rounded.VpnKey,
                        contentDescription = "Ubah Sandi Pembuat",
                        tint = GoldBright,
                    )
                }
            }
            Surface(
                shape = CircleShape,
                color = ObsidianCard,
                border = BorderStroke(1.dp, SubtleSlate),
            ) {
                IconButton(
                    onClick = onLockApp,
                    modifier = Modifier.testTag("lock_app_button"),
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Lock,
                        contentDescription = "Kunci Aplikasi",
                        tint = MutedPlatinum,
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyPredictionState() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 22.dp, horizontal = 12.dp),
    ) {
        Surface(
            shape = CircleShape,
            color = ObsidianSurface,
            border = BorderStroke(1.dp, ChampagneGold.copy(alpha = 0.35f)),
            modifier = Modifier.size(60.dp),
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Rounded.Diamond,
                    contentDescription = null,
                    tint = ChampagneGold.copy(alpha = 0.85f),
                    modifier = Modifier.size(28.dp),
                )
            }
        }
        Spacer(modifier = Modifier.height(14.dp))
        Text(
            text = "Belum Ada Hasil Generate",
            style = MaterialTheme.typography.titleMedium,
            color = IvoryText,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Masukkan 4 digit Result 4D di atas lalu tekan Generate untuk mendapatkan 100 line prediksi 2D (25 line di Lembar Merah, Hijau, Kuning, dan Biru transparan).",
            style = MaterialTheme.typography.bodyMedium,
            color = MutedPlatinum,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun PredictionResultContent(
    result: PredictionResult,
    copyFormat: CopyFormat,
    justCopiedAll: Boolean,
    copiedSheet: PredictionSheet?,
    onCopyFormatSelected: (CopyFormat) -> Unit,
    onResetClick: () -> Unit,
    onCopySheetClick: (PredictionSheet) -> Unit,
    onCopyAllClick: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        // Pola 5-Digit & Angka Main summary bar
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = ObsidianSurface,
            border = BorderStroke(1.dp, SubtleSlate),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = "POLA 5-DIGIT [ABCD|E]",
                        style = MaterialTheme.typography.labelMedium,
                        color = MutedPlatinum,
                    )
                    Text(
                        text = "${result.input4D}-${result.biji2D}",
                        fontFamily = JetBrainsMonoFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = GreenTileText,
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = "ANGKA MAIN",
                        style = MaterialTheme.typography.labelMedium,
                        color = MutedPlatinum,
                    )
                    Text(
                        text = result.angkaMain.joinToString("  •  "),
                        fontFamily = JetBrainsMonoFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = GoldBright,
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 4 Lembar Transparan (masing-masing 25 line, dipisah tanda *)
        Column(
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("prediction_sheets_container"),
        ) {
            PredictionSheet.entries.forEach { sheet ->
                TransparentPredictionSheetCard(
                    sheet = sheet,
                    lines = result.linesForSheet(sheet),
                    isCopied = copiedSheet == sheet,
                    onCopySheetClick = { onCopySheetClick(sheet) },
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider(color = SubtleSlate.copy(alpha = 0.5f))
        Spacer(modifier = Modifier.height(12.dp))

        // Format selector for Copy Semua 100 Line
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            CopyFormat.entries.forEach { format ->
                val isSelected = copyFormat == format
                FilterChip(
                    selected = isSelected,
                    onClick = { onCopyFormatSelected(format) },
                    label = {
                        Text(
                            text = format.label,
                            style = MaterialTheme.typography.labelMedium,
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = ChampagneGold.copy(alpha = 0.2f),
                        selectedLabelColor = GoldBright,
                        containerColor = ObsidianSurface,
                        labelColor = MutedPlatinum,
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = isSelected,
                        borderColor = SubtleSlate,
                        selectedBorderColor = ChampagneGold,
                    ),
                    modifier = Modifier.testTag("format_chip_${format.name.lowercase()}"),
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Reset & Copy Semua 100 Line Action Row
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            GoldOutlinedButton(
                text = "RESET",
                icon = Icons.Rounded.Refresh,
                onClick = onResetClick,
                modifier = Modifier
                    .weight(1f)
                    .testTag("reset_button"),
            )
            GoldPrimaryButton(
                text = if (justCopiedAll) "TERSALIN" else "COPY SEMUA",
                icon = if (justCopiedAll) Icons.Rounded.Check else Icons.Rounded.ContentCopy,
                onClick = onCopyAllClick,
                modifier = Modifier
                    .weight(1.25f)
                    .testTag("copy_button"),
            )
        }
    }
}

@Composable
private fun TransparentPredictionSheetCard(
    sheet: PredictionSheet,
    lines: List<String>,
    isCopied: Boolean,
    onCopySheetClick: () -> Unit,
) {
    val sheetBg = when (sheet) {
        PredictionSheet.RED -> RedTileSurface
        PredictionSheet.GREEN -> GreenTileSurface
        PredictionSheet.YELLOW -> YellowTileSurface
        PredictionSheet.BLUE -> BlueTileSurface
    }
    val sheetBorder = when (sheet) {
        PredictionSheet.RED -> RedTileBorder
        PredictionSheet.GREEN -> GreenTileBorder
        PredictionSheet.YELLOW -> YellowTileBorder
        PredictionSheet.BLUE -> BlueTileBorder
    }
    val sheetText = when (sheet) {
        PredictionSheet.RED -> RedTileText
        PredictionSheet.GREEN -> GreenTileText
        PredictionSheet.YELLOW -> YellowTileText
        PredictionSheet.BLUE -> BlueTileText
    }
    val tileTone = when (sheet) {
        PredictionSheet.RED -> LineBoxTone.RED_TRANSPARENT
        PredictionSheet.GREEN -> LineBoxTone.GREEN_TRANSPARENT
        PredictionSheet.YELLOW -> LineBoxTone.YELLOW_TRANSPARENT
        PredictionSheet.BLUE -> LineBoxTone.BLUE_TRANSPARENT
    }

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = sheetBg,
        border = BorderStroke(1.2.dp, sheetBorder),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("sheet_card_${sheet.name.lowercase()}"),
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "${sheet.title} • 25 LINE",
                        style = MaterialTheme.typography.titleSmall,
                        color = sheetText,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = "Baris ${sheet.rangeLabel}",
                        style = MaterialTheme.typography.labelSmall,
                        color = sheetText.copy(alpha = 0.8f),
                    )
                }

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = ObsidianSurface.copy(alpha = 0.85f),
                    border = BorderStroke(1.dp, sheetBorder),
                    modifier = Modifier
                        .minimumInteractiveComponentSize()
                        .clickable(onClick = onCopySheetClick)
                        .testTag("copy_sheet_${sheet.name.lowercase()}"),
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    ) {
                        Icon(
                            imageVector = if (isCopied) Icons.Rounded.Check else Icons.Rounded.ContentCopy,
                            contentDescription = "Copy ${sheet.title}",
                            tint = sheetText,
                            modifier = Modifier.size(16.dp),
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isCopied) "TERSALIN" else "COPY",
                            style = MaterialTheme.typography.labelMedium,
                            color = sheetText,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 5x5 Grid of 25 lines inside the sheet (clean 2D numbers without *)
            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                lines.chunked(5).forEachIndexed { rowIdx, rowItems ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        rowItems.forEachIndexed { colIdx, number2D ->
                            val localIndex = rowIdx * 5 + colIdx
                            val globalIndex = sheet.startIndex + localIndex
                            PredictionLineTile(
                                lineIndex = globalIndex,
                                number2D = number2D,
                                tone = tileTone,
                                showTrailingStar = false,
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("line_tile_$globalIndex"),
                            )
                        }
                    }
                }
            }
        }
    }
}
