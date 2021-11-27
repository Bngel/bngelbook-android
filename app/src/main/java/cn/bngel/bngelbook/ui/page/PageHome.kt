package cn.bngel.bngelbook.ui.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import cn.bngel.bngelbook.R
import cn.bngel.bngelbook.data.GlobalVariables
import cn.bngel.bngelbook.data.billDao.Bill
import cn.bngel.bngelbook.data.bookDao.Book
import cn.bngel.bngelbook.network.BillApi
import cn.bngel.bngelbook.network.BookApi
import cn.bngel.bngelbook.ui.widget.UiWidget.Dialog_Loading

object PageHome {

    val billUpdateState = mutableStateOf(true)
    private val loadingBills = mutableStateOf(false)
    private val billList = mutableListOf<Bill>()
    private val bookList = mutableListOf<Book>()
    private val curBalance = mutableStateOf(0.0)
    private val curCost = mutableStateOf(0.0)
    private val curIncome = mutableStateOf(0.0)
    private val curBookId = mutableStateOf(4L)
    private val curBook = mutableStateOf(GlobalVariables.BOOK)
    private val curMonth = mutableStateOf(13)
    private val curBookName = mutableStateOf("")
    private val bookSelected = mutableStateOf(false)

    @Composable
    fun HomePage() {
        Column {
            Home_Overview()
            if (billUpdateState.value)
                updateBills()
            if (loadingBills.value) {
                Dialog_Loading()
            }
            Home_BillList(billList)
        }
    }

    @Composable
    fun Home_Overview() {
        getCurBook()
        Column(horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color(0xFF66CCFF))) {
            if (curBook.value != null) {
                Row(modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 15.dp)
                    .clickable {
                        bookSelected.value = true
                    }
                    .background(color = Color.White, shape = RoundedCornerShape(10.dp))) {
                    Text(
                        text = curBook.value?.name ?: "", modifier = Modifier
                            .border(
                                width = 1.dp,
                                color = Color.Black,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .padding(start = 10.dp, end = 10.dp, top = 5.dp, bottom = 5.dp)
                    )
                }
            }
            Row{
                Column(horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(top = 20.dp, bottom = 20.dp)) {
                    Text(text = curBalance.value.toString(), fontSize = 25.sp)
                    Text(text = "${curMonth.value}月结余", fontSize = 16.sp)
                }
            }
            Row{
                Column(horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(1F)
                        .padding(10.dp)) {
                    Text(text = curIncome.value.toString(), fontSize = 18.sp)
                    Text(text = "${curMonth.value}月收入", fontSize = 13.sp)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(1F)
                        .padding(10.dp)) {
                    Text(text = curCost.value.toString(), fontSize = 18.sp)
                    Text(text = "${curMonth.value}月支出", fontSize = 13.sp)
                }
            }
        }
        if (bookSelected.value)
            BooksDialog()
    }

    @Composable
    fun Home_BillList(bills: List<Bill>) {
        LazyColumn {
            items(bills) { bill ->
                Home_Bill(bill)
            }
        }
    }

    @Composable
    fun Home_Bill(bill: Bill) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 5.dp)) {
            Image(painter = painterResource(id = R.drawable.default_profile),
                contentDescription = "bill type",
                modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 10.dp))
            Column(modifier = Modifier
                .weight(1F)
                .align(Alignment.CenterVertically)) {
                Text(text = bill.type?:"", fontSize = 18.sp, textAlign = TextAlign.Start)
                Text(text = if (bill.tags!=null) bill.tags.trim() else "", fontSize = 14.sp, color = Color.Gray, textAlign = TextAlign.Start)
            }
            Text(text = bill.balance.toString(), fontSize = 20.sp, modifier = Modifier
                .padding(start = 10.dp, end = 15.dp)
                .align(
                    Alignment.CenterVertically
                ))
        }
    }

    private fun updateBills() {
        loadingBills.value = true
        BillApi.getBillsByBookId(curBookId.value) { bills ->
            if (bills != null) {
                if (bills.code == 200)
                    billUpdateState.value = false
                if (bills.data != null) {
                    billList.clear()
                    val data = bills.data
                    var totalIncome = 0.0
                    var totalCost = 0.0
                    for (bill in data) {
                        if (bill.balance != null) {
                            if (bill.io == 1) {
                                totalIncome += bill.balance
                            }
                            else {
                                totalCost += bill.balance
                            }
                        }
                    }
                    curIncome.value = totalIncome
                    curCost.value = totalCost
                    curBalance.value = totalIncome - totalCost
                    billList.addAll(data)
                }
            }
            loadingBills.value = false
        }
    }

    @Composable
    fun BooksDialog() {
        getUserBooks()
        val selectedBook = remember {
            mutableStateOf("")
        }
        val selectedBookId = remember {
            mutableStateOf(curBookId.value)
        }
        Dialog(onDismissRequest = {}) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .background(color = Color(0xFFFFFFFF), shape = RoundedCornerShape(10.dp))
                    .padding(top = 10.dp, bottom = 10.dp, start = 10.dp, end = 10.dp)
                    .fillMaxWidth(0.8F)
                    .fillMaxHeight(0.6F)
            ) {
                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1F)
                        .padding(10.dp)
                ) {
                    if (bookList.size != 0) {
                        items(bookList) { book ->
                            if (book.name != null && book.id != null) {
                                Row(modifier = Modifier.padding(top = 5.dp, bottom = 5.dp)) {
                                    RadioButton(selected = book.name == selectedBook.value,
                                        onClick = {
                                            selectedBook.value = book.name
                                            selectedBookId.value = book.id
                                        })
                                    Text(
                                        text = book.name,
                                        fontSize = 18.sp,
                                        modifier = Modifier.padding(start = 10.dp, end = 10.dp),
                                        maxLines = 1
                                    )
                                }
                            }
                        }
                    }
                }
                Text(text = "确定", fontSize = 16.sp, color = Color(0xFF66CCFF), modifier = Modifier
                    .background(shape = RoundedCornerShape(10.dp), color = Color.White)
                    .border(
                        width = 1.dp,
                        color = Color(0xFF66CCFF),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .clickable {
                        bookSelected.value = false
                        if (selectedBook.value != curBookName.value && selectedBook.value != "")
                            billUpdateState.value = true
                        curBookName.value = selectedBook.value
                        curBookId.value = selectedBookId.value
                    }
                    .padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 10.dp))
            }
        }
    }

    private fun getCurBook() {
        BookApi.getBookById(curBookId.value) { book ->
            if (book?.data != null) {
                curBook.value = book.data
                GlobalVariables.BOOK = book.data
            }
        }
    }

    private fun getUserBooks() {
        val user = GlobalVariables.USER
        if (user?.id != null) {
            BookApi.getBooksByUserId(user.id) { books ->
                if (books?.data != null) {
                    bookList.clear()
                    bookList.addAll(books.data)
                }
            }
        }
    }
}