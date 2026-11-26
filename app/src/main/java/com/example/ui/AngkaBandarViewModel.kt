package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.ChangePasswordResult
import com.example.data.LockRepository
import com.example.domain.CopyFormat
import com.example.domain.PredictionEngine
import com.example.domain.PredictionResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AngkaBandarUiState(
    val isUnlocked: Boolean = false,
    val lockPasswordInput: String = "",
    val lockError: String? = null,
    val isVerifyingLock: Boolean = false,
    val failedAttempts: Int = 0,
    val input4D: String = "",
    val inputError: String? = null,
    val predictionResult: PredictionResult? = null,
    val copyFormat: CopyFormat = CopyFormat.STAR_SEPARATED,
    val history: List<PredictionResult> = emptyList(),
    val showChangePasswordDialog: Boolean = false,
    val currentPasswordInput: String = "",
    val newPasswordInput: String = "",
    val confirmPasswordInput: String = "",
    val changePasswordError: String? = null,
    val isChangingPassword: Boolean = false,
)

class AngkaBandarViewModel(application: Application) : AndroidViewModel(application) {

    private val lockRepository = LockRepository(application.applicationContext)

    private val _uiState = MutableStateFlow(AngkaBandarUiState())
    val uiState: StateFlow<AngkaBandarUiState> = _uiState.asStateFlow()

    fun onLockPasswordChanged(value: String) {
        _uiState.update {
            it.copy(
                lockPasswordInput = value,
                lockError = null,
            )
        }
    }

    fun unlockApp() {
        val candidate = _uiState.value.lockPasswordInput
        if (candidate.isBlank()) {
            _uiState.update { it.copy(lockError = "Masukkan kata sandi pembuat.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isVerifyingLock = true, lockError = null) }
            val matched = lockRepository.verifyPassword(candidate)
            if (matched) {
                _uiState.update {
                    it.copy(
                        isUnlocked = true,
                        lockPasswordInput = "",
                        lockError = null,
                        isVerifyingLock = false,
                        failedAttempts = 0,
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        isVerifyingLock = false,
                        failedAttempts = it.failedAttempts + 1,
                        lockError = "Kata sandi salah. Akses ditolak.",
                    )
                }
            }
        }
    }

    fun lockApp() {
        _uiState.update {
            it.copy(
                isUnlocked = false,
                lockPasswordInput = "",
                lockError = null,
            )
        }
    }

    fun on4DInputChanged(raw: String) {
        val digitsOnly = raw.filter { it.isDigit() }.take(4)
        _uiState.update {
            it.copy(
                input4D = digitsOnly,
                inputError = null,
            )
        }
    }

    fun generatePrediction() {
        val clean = _uiState.value.input4D
        if (clean.length != 4) {
            _uiState.update {
                it.copy(inputError = "Masukkan tepat 4 digit angka Result 4D.")
            }
            return
        }

        val result = PredictionEngine.generate2D(clean)
        _uiState.update { state ->
            val updatedHistory = listOf(result) + state.history.filterNot { it.input4D == result.input4D }
            state.copy(
                inputError = null,
                predictionResult = result,
                history = updatedHistory.take(6),
            )
        }
    }

    fun resetPrediction() {
        _uiState.update {
            it.copy(
                input4D = "",
                inputError = null,
                predictionResult = null,
            )
        }
    }

    fun setCopyFormat(format: CopyFormat) {
        _uiState.update { it.copy(copyFormat = format) }
    }

    fun selectHistoryItem(item: PredictionResult) {
        _uiState.update {
            it.copy(
                input4D = item.input4D,
                inputError = null,
                predictionResult = item,
            )
        }
    }

    fun clearHistory() {
        _uiState.update { it.copy(history = emptyList()) }
    }

    fun openChangePasswordDialog() {
        _uiState.update {
            it.copy(
                showChangePasswordDialog = true,
                currentPasswordInput = "",
                newPasswordInput = "",
                confirmPasswordInput = "",
                changePasswordError = null,
            )
        }
    }

    fun dismissChangePasswordDialog() {
        _uiState.update {
            it.copy(
                showChangePasswordDialog = false,
                currentPasswordInput = "",
                newPasswordInput = "",
                confirmPasswordInput = "",
                changePasswordError = null,
                isChangingPassword = false,
            )
        }
    }

    fun onCurrentPasswordChanged(value: String) {
        _uiState.update { it.copy(currentPasswordInput = value, changePasswordError = null) }
    }

    fun onNewPasswordChanged(value: String) {
        _uiState.update { it.copy(newPasswordInput = value, changePasswordError = null) }
    }

    fun onConfirmPasswordChanged(value: String) {
        _uiState.update { it.copy(confirmPasswordInput = value, changePasswordError = null) }
    }

    fun submitChangePassword(onSuccess: () -> Unit) {
        val state = _uiState.value
        viewModelScope.launch {
            _uiState.update { it.copy(isChangingPassword = true, changePasswordError = null) }
            when (
                val result = lockRepository.changePassword(
                    currentPassword = state.currentPasswordInput,
                    newPassword = state.newPasswordInput,
                    confirmPassword = state.confirmPasswordInput,
                )
            ) {
                ChangePasswordResult.Success -> {
                    dismissChangePasswordDialog()
                    onSuccess()
                }

                ChangePasswordResult.WrongCurrentPassword -> {
                    _uiState.update {
                        it.copy(
                            isChangingPassword = false,
                            changePasswordError = "Sandi saat ini tidak sesuai.",
                        )
                    }
                }

                is ChangePasswordResult.ValidationError -> {
                    _uiState.update {
                        it.copy(
                            isChangingPassword = false,
                            changePasswordError = result.message,
                        )
                    }
                }
            }
        }
    }
}
