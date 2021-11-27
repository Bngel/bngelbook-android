package cn.bngel.bngelbook.ui.page

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.bngel.bngelbook.MainActivity
import cn.bngel.bngelbook.data.GlobalVariables
import cn.bngel.bngelbook.data.accountDao.Account
import cn.bngel.bngelbook.network.AccountApi
import cn.bngel.bngelbook.ui.widget.UiWidget

object PageAccount {

    private val loading = mutableStateOf(false)
    private val accounts = mutableListOf<Account>()
    private val updateAccountsState = mutableStateOf(true)
    private val balance = mutableStateOf(0.00)

    @Composable
    fun AccountPage() {
        Column {
            Account_Title()
            Account_Overview()
            Account_Card()
        }
        if (loading.value)
            UiWidget.Dialog_Loading()
    }

    @Composable
    fun Account_Title() {
        Box(modifier = Modifier
            .fillMaxWidth()
            .shadow(1.dp)) {
            Text(text = "我的账户", fontSize = 18.sp, textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp))
        }
    }

    @Composable
    fun Account_Overview() {
        Column {
            Text(text = "净资产", fontSize = 18.sp, textAlign = TextAlign.Start ,
                modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 10.dp))
            Text(text = balance.value.toString(), fontSize = 26.sp, textAlign = TextAlign.Start,
                modifier = Modifier.padding(start = 20.dp, end = 20.dp, bottom = 20.dp))
        }
    }

    @Composable
    fun Account_Card() {
        if (updateAccountsState.value) {
            loading.value = true
            AccountApi.getAccountsByUserId(GlobalVariables.USER?.id ?: 0L) { result ->
                if (result?.data != null) {
                    accounts.clear()
                    accounts.addAll(result.data)
                    balance.value = 0.00
                    accounts.forEach { account ->
                        balance.value += account.balance ?: 0.00
                    }
                }
                loading.value = false
                updateAccountsState.value = false
            }
        }
        if (accounts.size > 0) {
            Card(modifier = Modifier.padding(start = 20.dp, end = 20.dp)) {
                LazyColumn(modifier = Modifier.padding(10.dp)) {
                    items(accounts) { account ->
                        Row(verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 5.dp, bottom = 5.dp)) {
                            Icon(imageVector = Icons.Filled.AccountBox, contentDescription = null)
                            Text(
                                text = account.name ?: "",
                                fontSize = 18.sp,
                                textAlign = TextAlign.Start,
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = 15.dp, end = 5.dp)
                            )
                            Text(
                                text = account.balance?.toString() ?: "", fontSize = 16.sp,
                                modifier = Modifier.padding(start = 5.dp, end = 5.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}