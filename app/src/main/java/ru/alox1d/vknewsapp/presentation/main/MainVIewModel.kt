package ru.alox1d.vknewsapp.presentation.main

import android.app.Application
import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.vk.id.AccessToken
import com.vk.id.OAuth
import com.vk.id.VKIDAuthFail
import com.vk.id.onetap.common.OneTapOAuth
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import ru.alox1d.vknewsapp.domain.formatToken
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

class MainVIewModel(private val application: Application) : AndroidViewModel(application) {

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    private var currentToast: Toast? = null
    private lateinit var iv: ByteArray

    private val Context.appDataStore: DataStore<Preferences> by preferencesDataStore("app")
    private val dataStore = application.applicationContext.appDataStore
    private val preferencesKey = stringPreferencesKey(KEY)

    init {
        viewModelScope.launch {
            _authState.value = if (dataStore.data.first()[preferencesKey]?.isNotEmpty() == true) {
                AuthState.Authorized
            } else {
                AuthState.NotAuthorized
            }

        }
    }

    fun authSuccessCallback(): (OneTapOAuth?, AccessToken) -> Unit = { oAuth, at ->
        _authState.value = AuthState.Authorized

        // Storing an access token
        viewModelScope.launch {
            dataStore.edit { prefs ->
                prefs[preferencesKey] = at.token
            }
        }

        onVKIDAuthSuccess(
            application,
            oAuth?.toOAuth(),
            at
        )
    }

    fun authFailCallback(): (OneTapOAuth?, VKIDAuthFail) -> Unit = { oAuth, fail ->
        _authState.value = AuthState.NotAuthorized

        onVKIDAuthFail(
            application,
            oAuth?.toOAuth(),
            fail
        )
    }

    private fun getSecretKey(): SecretKey {
        val keyStore = KeyStore.getInstance("AndroidKeyStore")
            .apply { load(null) }
        val secretKeyEntry = keyStore.getEntry(
            "VKID token",
            null
        ) as KeyStore.SecretKeyEntry
        return secretKeyEntry.secretKey ?: generateSecretKey()
    }

    fun encrypt(data: String): ByteArray? {
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(
            Cipher.ENCRYPT_MODE,
            getSecretKey()
        )
        iv = cipher.iv
        return cipher.doFinal(data.toByteArray())
    }

    private fun generateSecretKey(): SecretKey {
        val keyGenerator = KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES,
            "AndroidKeyStore"
        )
        val spec = KeyGenParameterSpec
            .Builder(
                "VKID token",
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .build()

        keyGenerator.init(spec)
        return keyGenerator.generateKey()
    }

    fun decrypt(encrypted: ByteArray): String {
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        val spec = GCMParameterSpec(
            128,
            iv
        )
        cipher.init(
            Cipher.DECRYPT_MODE,
            getSecretKey(),
            spec
        )
        val decoded = cipher.doFinal(encrypted)
        return String(
            decoded,
            Charsets.UTF_8
        )
    }

    private fun onVKIDAuthSuccess(
        context: Context,
        oAuth: OAuth?,
        accessToken: AccessToken,
    ) {
        val oAuthLabel = oAuth?.name ?: "VK ID"
        showToast(
            context,
            "Auth from $oAuthLabel with token ${formatToken(accessToken.token)}"
        )
    }

    private fun onVKIDAuthFail(
        context: Context,
        oAuth: OAuth?,
        fail: VKIDAuthFail,
    ) {
        val oAuthLabel = oAuth?.name ?: "VK ID"
        when (fail) {
            is VKIDAuthFail.Canceled -> {
                showToast(
                    context,
                    "Auth with $oAuthLabel was canceled"
                )
            }

            else -> {
                showToast(
                    context,
                    "Auth with $oAuthLabel failed with: ${fail.description}"
                )
            }
        }
    }

    fun showToast(
        context: Context,
        text: String
    ) {
        currentToast?.cancel()
        currentToast = Toast.makeText(
            context,
            text,
            Toast.LENGTH_SHORT
        )
        currentToast?.show()
    }

    private companion object {

        const val KEY = "key"
    }
}