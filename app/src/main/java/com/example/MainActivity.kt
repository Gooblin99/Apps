package com.example

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.AngkaBandarViewModel
import com.example.ui.screens.ChangePasswordDialog
import com.example.ui.screens.LockVaultScreen
import com.example.ui.screens.PredictionDashboardScreen
import com.example.ui.theme.AngkaBandarTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
        )
        setContent {
            AngkaBandarTheme {
                val viewModel: AngkaBandarViewModel = viewModel()
                val state by viewModel.uiState.collectAsStateWithLifecycle()
                val snackbarHostState = remember { SnackbarHostState() }
                val scope = rememberCoroutineScope()

                Crossfade(
                    targetState = state.isUnlocked,
                    animationSpec = tween(durationMillis = 280),
                    label = "vault_transition",
                ) { unlocked ->
                    if (!unlocked) {
                        LockVaultScreen(
                            state = state,
                            onPasswordChanged = viewModel::onLockPasswordChanged,
                            onUnlockClick = viewModel::unlockApp,
                            onOpenChangePassword = viewModel::openChangePasswordDialog,
                        )
                    } else {
                        PredictionDashboardScreen(
                            state = state,
                            snackbarHostState = snackbarHostState,
                            on4DInputChanged = viewModel::on4DInputChanged,
                            onGenerateClick = viewModel::generatePrediction,
                            onResetClick = viewModel::resetPrediction,
                            onCopyFormatSelected = viewModel::setCopyFormat,
                            onHistoryItemSelected = viewModel::selectHistoryItem,
                            onClearHistory = viewModel::clearHistory,
                            onOpenChangePassword = viewModel::openChangePasswordDialog,
                            onLockApp = viewModel::lockApp,
                            onShowSnackbar = { message ->
                                scope.launch {
                                    snackbarHostState.currentSnackbarData?.dismiss()
                                    snackbarHostState.showSnackbar(message)
                                }
                            },
                        )
                    }
                }

                if (state.showChangePasswordDialog) {
                    ChangePasswordDialog(
                        state = state,
                        onCurrentPasswordChanged = viewModel::onCurrentPasswordChanged,
                        onNewPasswordChanged = viewModel::onNewPasswordChanged,
                        onConfirmPasswordChanged = viewModel::onConfirmPasswordChanged,
                        onDismiss = viewModel::dismissChangePasswordDialog,
                        onSubmit = {
                            viewModel.submitChangePassword {
                                scope.launch {
                                    snackbarHostState.currentSnackbarData?.dismiss()
                                    snackbarHostState.showSnackbar("Kata sandi pembuat berhasil diperbarui.")
                                }
                            }
                        },
                    )
                }
            }
        }
    }
}
