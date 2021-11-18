package cn.bngel.bngelbook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.MeasurePolicy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import cn.bngel.bngelbook.ui.BillType
import cn.bngel.bngelbook.ui.theme.BngelbookTheme

class BillSaveActivity : ComponentActivity() {

    private val curType = mutableStateOf("吃喝")
    private val curTags = mutableStateOf("")
    private val curBalance = mutableStateOf("")
    private var curPoint = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BngelbookTheme {
                Surface(color = MaterialTheme.colors.background) {
                    BillSavePage()
                }
            }
        }
    }

    @Composable
    fun BillSavePage(){
        Column {
            BillSaveTitle()
            BillBalanceRow()
            BillTypeChoices(types = listOf("吃喝","交通","服饰","日用品","娱乐","医疗","其他"),size = 5,
                modifier = Modifier.weight(1F))
            TagsRow(tags = listOf("test","nice","牛","厉害"))
            BottomRow(account = "不选账户")
            Calculator()
        }
    }

    @Composable
    fun BillSaveTitle() {
        Row {
            Row(modifier = Modifier.weight(1F), horizontalArrangement = Arrangement.Start) {
                Image(painter = painterResource(id = R.drawable.close), contentDescription = "close_btn",
                    modifier = Modifier
                        .padding(start = 15.dp, end = 15.dp, top = 10.dp, bottom = 10.dp)
                        .width(30.dp)
                        .height(30.dp))
            }
            Row(modifier = Modifier.weight(1F), horizontalArrangement = Arrangement.End) {
                Image(painter = painterResource(id = R.drawable.ok), contentDescription = "ok_btn",
                    modifier = Modifier
                        .padding(start = 15.dp, end = 15.dp, top = 10.dp, bottom = 10.dp)
                        .width(30.dp)
                        .height(30.dp))
            }
        }
    }

    @Composable
    fun BillBalanceRow() {
        Row(modifier = Modifier
            .border(width = 1.dp, color = Color.Gray.copy(alpha = 0.5F))
            .padding(start = 15.dp, end = 15.dp, top = 5.dp, bottom = 5.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Row(modifier = Modifier.weight(1F), verticalAlignment = Alignment.CenterVertically) {
                Image(painter = painterResource(id = BillType.getTypeImg(curType.value)), contentDescription = "balance",
                    modifier = Modifier.padding(5.dp))
                Text(text = curType.value, fontSize = 20.sp, modifier = Modifier.padding(start = 10.dp, end = 10.dp))
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = if (curBalance.value == "") "0" else curBalance.value, fontSize = 20.sp,
                    modifier = Modifier.padding(10.dp))
            }
        }
    }

    @Composable
    fun BillTypeChoices(types: List<String>, size: Int, modifier: Modifier = Modifier) {
        Column(modifier = modifier.padding(top = 10.dp, bottom = 10.dp)) {
            for (i in 0 until (if (types.size % size == 0) types.size/size else types.size/size+1)) {
                Row {
                    for (j in 0 until size) {
                        if (i * size + j >= types.size)
                            break
                        BillTypeChoice(type = types[i * size + j]) {
                            curType.value = types[i * size + j]
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun BillTypeChoice(type: String, onClick: () -> Unit) {
        Column(horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 10.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClickLabel = null,
                    role = null,
                    enabled = true,
                    onClick = onClick
                )) {
            Image(painter = painterResource(id = BillType.getTypeImg(type)), contentDescription = "billType",
                modifier = Modifier
                    .width(40.dp)
                    .height(40.dp))
            Text(text = type, fontSize = 15.sp, modifier = Modifier.padding(top = 5.dp))
        }
    }

    @Composable
    fun TagsRow(tags: List<String>) {
        Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(text = "标签", fontSize = 15.sp, modifier = Modifier.padding(5.dp))
            LazyRow(modifier = Modifier.padding(5.dp)) {
                items(tags) { tag ->
                    Text(text = tag, fontSize = 13.sp,
                        modifier = Modifier
                            .padding(start = 10.dp, end = 10.dp)
                            .border(
                                width = 1.dp,
                                color = Color.Gray,
                                shape = RoundedCornerShape(3.dp)
                            )
                            .padding(start = 5.dp, end = 5.dp, top = 3.dp, bottom = 3.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null, enabled = true
                            ) {
                                curTags.value += " $tag"
                            })
                }
            }
        }
    }

    @Composable
    fun BottomRow(account: String) {
        Row(modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Row(modifier = Modifier
                .weight(1F)
                .padding(start = 10.dp, end = 10.dp)) {
                Text(text = account, fontSize = 15.sp)
            }
            Text(text = if (curTags.value == "") "备注" else curTags.value.trim(), fontSize = 15.sp, modifier = Modifier
                .padding(start = 10.dp, end = 10.dp, top = 5.dp, bottom = 5.dp)
                .border(width = 1.dp, color = Color.Gray, shape = RoundedCornerShape(10.dp))
                .padding(start = 10.dp, end = 10.dp, top = 3.dp, bottom = 3.dp))

        }
    }

    @Composable
    fun Calculator() {
        Column(modifier = Modifier.background(Color.Black)) {
            Row {
                CalCell(cell = "7", modifier = Modifier.weight(1F))
                CalCell(cell = "8", modifier = Modifier.weight(1F))
                CalCell(cell = "9", modifier = Modifier.weight(1F))
                CalCell(cell = "X", modifier = Modifier.weight(1F))
            }
            Row {
                CalCell(cell = "4", modifier = Modifier.weight(1F))
                CalCell(cell = "5", modifier = Modifier.weight(1F))
                CalCell(cell = "6", modifier = Modifier.weight(1F))
                CalCell(cell = "+", modifier = Modifier.weight(1F))
            }
            Row {
                CalCell(cell = "1", modifier = Modifier.weight(1F))
                CalCell(cell = "2", modifier = Modifier.weight(1F))
                CalCell(cell = "3", modifier = Modifier.weight(1F))
                CalCell(cell = "-", modifier = Modifier.weight(1F))
            }
            Row {
                CalCell(cell = "￥", modifier = Modifier.weight(1F))
                CalCell(cell = "0", modifier = Modifier.weight(1F))
                CalCell(cell = ".", modifier = Modifier.weight(1F))
                CalCell(cell = "完成", modifier = Modifier.weight(1F))
            }
        }
    }

    @Composable
    fun CalCell(cell: String, modifier: Modifier = Modifier) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center,
            modifier = modifier.border(width = 1.dp, color = Color.Yellow)
                .clickable { calculate(cell) }) {
            Text(text = cell, modifier = Modifier.padding(10.dp), fontSize = 18.sp, color = Color.Yellow)
        }
    }

    private fun calculate(key: String) {
        if (key.isDigitsOnly() && curBalance.value.length < 7) {
            var balance =
                when {
                    curBalance.value == "" -> 0.0
                    curBalance.value.last() == '.' -> curBalance.value.substring(0, curBalance.value.length-1).toDouble()
                    else -> curBalance.value.toDouble()
                }
            when (curPoint) {
                -1 -> balance = balance * 10 + key.toDouble()
                0 -> {
                    balance += key.toDouble() / 10
                    curPoint += 1
                }
                1 -> {
                    balance += key.toDouble() / 100
                    curPoint += 1
                }
            }
            curBalance.value = if (curPoint == -1) balance.toInt().toString() else balance.toString()
        }
        else if (key == "." && curBalance.value.length < 7) {
            curPoint = 0
            curBalance.value += "."
        }
        else if (key == "X") {
            if (curPoint > -1) {
                curPoint -= 1
            }
            if (curBalance.value != ""){
                curBalance.value = curBalance.value.substring(0, curBalance.value.length-1)
            }
        }
    }
}