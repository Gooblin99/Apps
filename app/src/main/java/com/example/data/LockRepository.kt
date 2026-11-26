package com.example.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import java.security.MessageDigest

private val Context.vaultDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "angka_bandar_vault"
)

sealed interface ChangePasswordResult {
    data object Success : ChangePasswordResult
    data object WrongCurrentPassword : ChangePasswordResult
    data class ValidationError(val message: String) : ChangePasswordResult
}

class LockRepository(private val context: Context) {

    private val passwordHashKey = stringPreferencesKey("creator_password_hash")

    suspend fun verifyPassword(candidate: String): Boolean {
        if (candidate.isEmpty()) return false
        val prefs = context.vaultDataStore.data.first()
        val storedHash = prefs[passwordHashKey] ?: DEFAULT_PASSWORD_HASH
        return hashPassword(candidate) == storedHash
    }

    suspend fun changePassword(
        currentPassword: String,
        newPassword: String,
        confirmPassword: String,
    ): ChangePasswordResult {
        if (currentPassword.isBlank()) {
            return ChangePasswordResult.ValidationError("Masukkan sandi saat ini.")
        }
        if (!verifyPassword(currentPassword)) {
            return ChangePasswordResult.WrongCurrentPassword
        }
        if (newPassword.length < 4) {
            return ChangePasswordResult.ValidationError("Sandi baru minimal 4 karakter.")
        }
        if (newPassword != confirmPassword) {
            return ChangePasswordResult.ValidationError("Konfirmasi sandi baru tidak cocok.")
        }
        if (newPassword == currentPassword) {
            return ChangePasswordResult.ValidationError("Sandi baru harus berbeda dari sandi saat ini.")
        }

        val newHash = hashPassword(newPassword)
        context.vaultDataStore.edit { prefs ->
            prefs[passwordHashKey] = newHash
        }
        return ChangePasswordResult.Success
    }

    companion object {
        const val DEFAULT_CREATOR_PASSWORD = "DJOKER4444"
        private val DEFAULT_PASSWORD_HASH = hashPassword(DEFAULT_CREATOR_PASSWORD)

        fun hashPassword(raw: String): String {
            val digest = MessageDigest.getInstance("SHA-256").digest(raw.toByteArray(Charsets.UTF_8))
            return digest.joinToString("") { byte -> "%02x".format(byte) }
        }
    }
}
