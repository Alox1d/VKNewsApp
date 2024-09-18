package ru.alox1d.vknewsapp.presentation.main

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vk.id.AccessToken
import com.vk.id.OAuth
import com.vk.id.VKIDAuthFail
import com.vk.id.onetap.common.OneTapOAuth
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.alox1d.vknewsapp.domain.entity.AuthState
import ru.alox1d.vknewsapp.domain.formatToken
import ru.alox1d.vknewsapp.domain.usecases.GetAuthStateFlowUseCase
import ru.alox1d.vknewsapp.domain.usecases.UpdateAuthStateFlowUseCase
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val getAuthStateFlowUseCase: GetAuthStateFlowUseCase,
    private val updateAuthStateFlowUseCase: UpdateAuthStateFlowUseCase
) : ViewModel() {


    val authState: StateFlow<AuthState> = getAuthStateFlowUseCase.invoke()

    private var currentToast: Toast? = null

    fun onLoginSuccess(): (OneTapOAuth?, AccessToken) -> Unit = { oAuth, at ->
        // Storing an access token
        viewModelScope.launch {
            updateAuthStateFlowUseCase.invoke(at.token)
        }

//        onVKIDAuthSuccess(
//            application,
//            oAuth?.toOAuth(),
//            at
//        )
    }

    fun onLoginError(): (OneTapOAuth?, VKIDAuthFail) -> Unit = { oAuth, fail ->
        viewModelScope.launch {
            updateAuthStateFlowUseCase.invoke(null)
        }

//        onVKIDAuthFail(
//            application,
//            oAuth?.toOAuth(),
//            fail
//        )
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

    private fun showToast(
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


    //private lateinit var iv: ByteArray
//    private fun getSecretKey(): SecretKey {
//        val keyStore = KeyStore.getInstance("AndroidKeyStore")
//            .apply { load(null) }
//        val secretKeyEntry = keyStore.getEntry(
//            "VKID token",
//            null
//        ) as KeyStore.SecretKeyEntry
//        return secretKeyEntry.secretKey ?: generateSecretKey()
//    }
//
//    fun encrypt(data: String): ByteArray? {
//        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
//        cipher.init(
//            Cipher.ENCRYPT_MODE,
//            getSecretKey()
//        )
//        iv = cipher.iv
//        return cipher.doFinal(data.toByteArray())
//    }
//
//    private fun generateSecretKey(): SecretKey {
//        val keyGenerator = KeyGenerator.getInstance(
//            KeyProperties.KEY_ALGORITHM_AES,
//            "AndroidKeyStore"
//        )
//        val spec = KeyGenParameterSpec
//            .Builder(
//                "VKID token",
//                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
//            )
//            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
//            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
//            .build()
//
//        keyGenerator.init(spec)
//        return keyGenerator.generateKey()
//    }
//
//    fun decrypt(encrypted: ByteArray): String {
//        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
//        val spec = GCMParameterSpec(
//            128,
//            iv
//        )
//        cipher.init(
//            Cipher.DECRYPT_MODE,
//            getSecretKey(),
//            spec
//        )
//        val decoded = cipher.doFinal(encrypted)
//        return String(
//            decoded,
//            Charsets.UTF_8
//        )
//    }

}