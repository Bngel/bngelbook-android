package cn.bngel.bngelbook

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.bngel.bngelbook.LoginActivity
import cn.bngel.bngelbook.R
import cn.bngel.bngelbook.data.billDao.Bill
import cn.bngel.bngelbook.data.userDao.User
import cn.bngel.bngelbook.ui.theme.BngelbookTheme
import kotlinx.coroutines.launch
import java.sql.Date

class MainActivity : ComponentActivity() {

    var loginState = mutableStateOf(false)

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { res ->
        val data = res.data
        if (data != null) {
            loginState.value = data.getBooleanExtra("loginState", false)
            if (loginState.value) {
                val user = data.getSerializableExtra("userInfo") as User
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BngelbookTheme {
                Surface(color = MaterialTheme.colors.background) {
                    MainPage()
                }
            }
        }
    }

    @Composable
    fun MainPage() {
        val scaffoldState = rememberScaffoldState()
        Scaffold(
            scaffoldState = scaffoldState,
            drawerContent = {
                Drawer()
            },
            floatingActionButton = {
                FloatingActionButton(
                    backgroundColor = Color(0xFF66CCFF),
                    contentColor = Color(0xFFFFFFFF),
                    onClick = {
                        //scope.launch { scaffoldState.drawerState.open() }
                    }
                ){
                    Icon(Icons.Filled.Add, "add a new bill")
                }
            },
            floatingActionButtonPosition = FabPosition.End
        ) {
            HomePage()
        }
    }

    @Composable
    fun HomePage() {
        val testList = listOf(
            Bill(1L, "吃喝", 13.0, 1L, 1L, "测试Tag", Date.valueOf("2001-01-01"), 0),
            Bill(1L, "吃喝", 13.0, 1L, 1L, "测试Tag", Date.valueOf("2001-01-01"), 0),
            Bill(1L, "吃喝", 13.0, 1L, 1L, "测试Tag", Date.valueOf("2001-01-01"), 0),
            Bill(1L, "吃喝", 13.0, 1L, 1L, "测试Tag", Date.valueOf("2001-01-01"), 0),
            Bill(1L, "吃喝", 13.0, 1L, 1L, "测试Tag", Date.valueOf("2001-01-01"), 0),
            Bill(1L, "吃喝", 13.0, 1L, 1L, "测试Tag", Date.valueOf("2001-01-01"), 0),
            Bill(1L, "吃喝", 13.0, 1L, 1L, "测试Tag", Date.valueOf("2001-01-01"), 0),
            Bill(1L, "吃喝", 13.0, 1L, 1L, "测试Tag", Date.valueOf("2001-01-01"), 0),
            Bill(1L, "吃喝", 13.0, 1L, 1L, "测试Tag", Date.valueOf("2001-01-01"), 0),
            Bill(1L, "吃喝", 13.0, 1L, 1L, "测试Tag", Date.valueOf("2001-01-01"), 0),
            Bill(1L, "吃喝", 13.0, 1L, 1L, "测试Tag", Date.valueOf("2001-01-01"), 0),
            Bill(1L, "吃喝", 13.0, 1L, 1L, "测试Tag", Date.valueOf("2001-01-01"), 0),
            Bill(1L, "吃喝", 13.0, 1L, 1L, "测试Tag", Date.valueOf("2001-01-01"), 0)
        )
        Column {
            Home_Overview()
            Home_BillList(bills = testList)
        }
    }

    @Composable
    fun Home_Overview() {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
            .background(Color(0xFF66CCFF))
            .fillMaxWidth()) {
            Row(modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 15.dp)){
                Text(text = "随便起的标题")
            }
            Row{
                Column(horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(top = 20.dp, bottom = 20.dp)) {
                    Text(text = "1313.13", fontSize = 25.sp)
                    Text(text = "13月结余", fontSize = 16.sp)
                }
            }
            Row{
                Column(horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(1F)
                        .padding(10.dp)) {
                    Text(text = "1313.13", fontSize = 18.sp)
                    Text(text = "13月收入", fontSize = 13.sp)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(1F)
                        .padding(10.dp)) {
                    Text(text = "1313.13", fontSize = 18.sp)
                    Text(text = "13月支出", fontSize = 13.sp)
                }
            }
        }
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
        Row(modifier = Modifier.fillMaxWidth().padding(top = 5.dp)) {
            Image(painter = painterResource(id = R.drawable.default_profile),
                contentDescription = "bill type",
                modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 10.dp))
            Column(modifier = Modifier.weight(1F).align(Alignment.CenterVertically)) {
                Text(text = bill.type, fontSize = 18.sp)
                Text(text = bill.tags?:"", fontSize = 12.sp)
            }
            Text(text = bill.balance.toString(), fontSize = 20.sp, modifier = Modifier.padding(start = 10.dp, end = 10.dp).align(Alignment.CenterVertically))
        }
    }

    @Composable
    fun Drawer() {
        if (loginState.value) {
            Column {
                Drawer_ProfileCard(profile = "", username = "bngel", daysCount = 3000)
                Column(modifier = Modifier.weight(1f)) {
                    Drawer_Function(imageVector = Icons.Filled.Home, functionName = "功能Home") {}
                    Drawer_Function(imageVector = Icons.Filled.Face, functionName = "功能Face") {}
                    Drawer_Function(imageVector = Icons.Filled.Favorite, functionName = "功能Favorite") {}
                    Drawer_Function(
                        imageVector = Icons.Filled.AccountBox,
                        functionName = "功能AccountBox"
                    ) {}
                    Drawer_Function(
                        imageVector = Icons.Filled.AccountCircle,
                        functionName = "功能AccountCircle"
                    ) {}
                    Drawer_Function(imageVector = Icons.Filled.Info, functionName = "功能Info") {}
                }
                Row(modifier = Modifier
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        loginState.value = false
                    }
                    .padding(20.dp)) {
                    Icon(imageVector = Icons.Filled.ExitToApp, contentDescription = "exit")
                    Text(text = "退出账号", modifier = Modifier.padding(start = 15.dp))
                }
            }
        }
        else {
            Column {
                Drawer_ProfileCard(profile = "", username = "点击登录", daysCount = 3000)
            }
        }
    }

    @Composable
    fun Drawer_ProfileCard(profile: String = "", username: String = "", daysCount: Int = 0) {
        val scaffoldState = rememberScaffoldState()
        val scope = rememberCoroutineScope()
        Row(modifier = Modifier
            .background(
                Brush.verticalGradient(listOf(Color(0xFF66CCFF), Color(0x00000000))),
                MaterialTheme.shapes.large
            )
            .padding(start = 10.dp, end = 10.dp, top = 40.dp, bottom = 50.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(2f)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        if (loginState.value) {
                            TODO("open account page")
                        } else {
                            scope.launch { scaffoldState.drawerState.close() }
                            launcher.launch(Intent(this@MainActivity, LoginActivity::class.java))
                        }
                    }
            ) {
                Image(painter = painterResource(id = R.drawable.default_profile), contentDescription = "profile",
                    alignment = Alignment.TopCenter, modifier = Modifier
                        .width(60.dp)
                        .height(60.dp))
                if (!loginState.value) {
                    Text(text = "点击登录", modifier = Modifier.padding(top = 10.dp))
                }
            }
            if (loginState.value) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(5f)
                ) {
                    Text(text = username)
                    Text(text = "已记账: $daysCount 天")
                }
            }
        }
    }

    @Composable
    fun Drawer_Function(imageVector: ImageVector, functionName: String, onClick: ()-> Unit) {
        Row(verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    onClick()
                }
                .padding(15.dp)) {
            Icon(imageVector = imageVector,
                contentDescription = functionName)
            Text(text = functionName, fontSize = 18.sp,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 15.dp, end = 5.dp))
            Image(painter = painterResource(id = R.drawable.right_next), functionName,
                modifier = Modifier
                    .width(20.dp)
                    .height(20.dp))
        }
    }

}
