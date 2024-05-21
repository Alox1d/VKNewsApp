package ru.alox1d.vknewsapp.presentation.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vk.id.AccessToken
import com.vk.id.VKIDAuthFail
import com.vk.id.onetap.common.OneTapOAuth
import com.vk.id.onetap.common.OneTapStyle
import com.vk.id.onetap.common.button.style.OneTapButtonCornersStyle
import com.vk.id.onetap.compose.onetap.OneTap
import ru.alox1d.vknewsapp.R

private const val MIN_WIDTH_DP = 48f
private const val TOTAL_WIDTH_PADDING_DP = 16

@Composable
fun LoginScreen(
    onSuccessAuth: (OneTapOAuth?, AccessToken) -> Unit,
    onFailAuth: (OneTapOAuth?, VKIDAuthFail) -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        val screenWidth = LocalConfiguration.current.screenWidthDp - TOTAL_WIDTH_PADDING_DP

        val widthPercent = remember { mutableFloatStateOf(1f) }
        val width = maxOf(
            MIN_WIDTH_DP,
            (screenWidth * widthPercent.floatValue)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier.size(100.dp),
                painter = painterResource(id = R.drawable.vk_id_logo),
                contentDescription = null,
            )
            Spacer(modifier = Modifier.height(100.dp))
            OneTap(
                modifier = Modifier.width(width.dp),
                style = (
                        if (isSystemInDarkTheme()) {
                            OneTapStyle.Dark(cornersStyle = OneTapButtonCornersStyle.Rounded)
                        } else {
                            OneTapStyle.Light(cornersStyle = OneTapButtonCornersStyle.Rounded)
                        }
                        ),
                onAuth = onSuccessAuth,
                onFail = onFailAuth,
                signInAnotherAccountButtonEnabled = true,
            )
        }
    }
}

@Preview
@Composable
private fun LoginScreenPrev() {
    LoginScreen(
        onSuccessAuth = { oneTapOAuth: OneTapOAuth?, accessToken: AccessToken -> },
        onFailAuth = { oneTapOAuth: OneTapOAuth?, vkidAuthFail: VKIDAuthFail -> })
}