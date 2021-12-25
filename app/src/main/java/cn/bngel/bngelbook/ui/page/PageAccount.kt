package cn.bngel.bngelbook.ui.page

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.bngel.bngelbook.activity.AccountSaveActivity
import cn.bngel.bngelbook.activity.ActivityManager
import cn.bngel.bngelbook.data.GlobalVariables
import cn.bngel.bngelbook.data.MainPages
import cn.bngel.bngelbook.data.bean.Account
import cn.bngel.bngelbook.data.bean.Book
import cn.bngel.bngelbook.network.api.AccountApi
import cn.bngel.bngelbook.network.api.BookApi
import cn.bngel.bngelbook.ui.widget.UiWidget

object PageAccount: BasePage() {

    private val loading = mutableStateOf(false)
    private val accounts = mutableStateListOf<Account>()
    private val books = mutableStateListOf<Book>()
    private val bookUpdated = mutableStateOf(false)
    private val accountUpdated = mutableStateOf(false)
    private val balance = mutableStateOf(0.00)

    init {
        setPage(MainPages.ACCOUNT_PAGE)
    }

    @Composable
    fun AccountPage() {
        Column {
            AccountTitle()
            AccountOverview()
            Column(modifier = Modifier.weight(1F)) {
                AccountCard()
            }
            Spacer(modifier = Modifier.height(20.dp))
            Column(modifier = Modifier.weight(1F)) {
                BookCard()
            }
        }
        if (loading.value)
            UiWidget.Dialog_Loading{ loading.value = false }
        loading.value = !bookUpdated.value || !accountUpdated.value
    }

    @Composable
    private fun AccountTitle() {
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
    private fun AccountOverview() {
        Column {
            Text(text = "净资产", fontSize = 18.sp, textAlign = TextAlign.Start ,
                modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 10.dp))
            Text(text = balance.value.toString(), fontSize = 26.sp, textAlign = TextAlign.Start,
                modifier = Modifier.padding(start = 20.dp, end = 20.dp, bottom = 20.dp))
        }
    }

    @Composable
    private fun AccountCard() {
        if (getUpdate()) {
            AccountApi.getAccountsByUserId(GlobalVariables.USER.value?.id ?: 0L) { result ->
                if (result?.data != null) {
                    accounts.clear()
                    accounts.addAll(result.data)
                    balance.value = 0.00
                    accounts.forEach { account ->
                        balance.value += account.balance ?: 0.00
                    }
                    setUpdate(true)
                    accountUpdated.value = true
                }
            }
        }
        if (accounts.size > 0) {
            Card(modifier = Modifier.padding(start = 20.dp, end = 20.dp, bottom = 20.dp)){
                Column {
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)) {
                        Text(text = "账户列表", fontSize = 14.sp, modifier = Modifier
                            .padding(start = 25.dp, end = 5.dp, top = 5.dp, bottom = 5.dp)
                            .weight(1F))
                        Icon(imageVector = Icons.Filled.Add, contentDescription = null, modifier = Modifier.padding(5.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                ActivityManager.launch<AccountSaveActivity> {
                                    putExtra("type", 0)
                                }
                            })
                    }
                    LazyColumn(modifier = Modifier.padding(10.dp)) {
                        items(accounts) { account ->
                            Row(verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(top = 5.dp, bottom = 5.dp)) {
                                Icon(imageVector = Icons.Default.AccountBox, contentDescription = null)
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

    @Composable
    private fun BookCard() {
        if (getUpdate()) {
            BookApi.getBooksByUserId(GlobalVariables.USER.value?.id ?: 0L) { result ->
                if (result?.data != null) {
                    books.clear()
                    books.addAll(result.data)
                    bookUpdated.value = true
                    setUpdate(true)
                }
            }
        }
        if (books.size > 0) {
            Card(modifier = Modifier.padding(start = 20.dp, end = 20.dp, bottom = 20.dp)){
                Column {
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)) {
                        Text(text = "账本列表", fontSize = 14.sp, modifier = Modifier
                            .padding(start = 25.dp, end = 5.dp, top = 5.dp, bottom = 5.dp)
                            .weight(1F))
                        Icon(imageVector = Icons.Filled.Add, contentDescription = null, modifier = Modifier.padding(5.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                ActivityManager.launch<AccountSaveActivity> {
                                    putExtra("type", 1)
                                }
                            })
                    }
                    LazyColumn(modifier = Modifier.padding(10.dp)) {
                        items(books) { book ->
                            Row(verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(top = 5.dp, bottom = 5.dp)) {
                                Icon(imageVector = Icons.Default.Edit, contentDescription = null)
                                Text(
                                    text = book.name ?: "",
                                    fontSize = 18.sp,
                                    textAlign = TextAlign.Start,
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(start = 15.dp, end = 5.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

}