package com.kalex.bookyouu_notesapp.moreMenu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kalex.bookyouu_notesapp.ads.AdmobBanner
import com.kalex.bookyouu_notesapp.ads.AdsUniqueIds
import com.kalex.bookyouu_notesapp.core.common.ViewModelState
import com.kalex.bookyouu_notesapp.core.common.composables.SwitchCard
import com.kalex.bookyouu_notesapp.core.common.getAuthenticationFlag
import com.kalex.bookyouu_notesapp.core.common.getNotificationFlag
import kotlinx.coroutines.flow.update

@Composable
fun MainMoreMenu(
    moreMenuViewModel: SwitchMenuViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AdmobBanner(
            modifier = Modifier.fillMaxWidth(),
            AdsUniqueIds.MoreMenuConfigurationTop
        )
        Text(
            text = stringResource(id = R.string.more_menu_screen_title),
            fontSize = 24.sp,
        )
        Spacer(modifier = Modifier.height(24.dp))

        SwitchCard(
            initialState = context.getAuthenticationFlag()?.toBooleanStrictOrNull() ?: false,
            switchText = stringResource(id = R.string.more_menu_biometric_item_text),
            onChecked = {
                moreMenuViewModel.authenticationSwitchState(it)
            }
        )
        Spacer(modifier = Modifier.height(16.dp))

        SwitchCard(
            initialState = context.getNotificationFlag()?.toBooleanStrictOrNull() ?: false,
            switchText = stringResource(id = R.string.more_menu_notifications_item_text),
            onChecked = {
                moreMenuViewModel.notificationSwitchState(it)
            }
        )
    }
}