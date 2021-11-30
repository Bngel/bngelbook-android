package cn.bngel.bngelbook.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.edit
import cn.bngel.bngelbook.R
import cn.bngel.bngelbook.data.GlobalVariables
import cn.bngel.bngelbook.data.MainPages
import cn.bngel.bngelbook.data.userDao.User
import cn.bngel.bngelbook.network.UserApi
import cn.bngel.bngelbook.ui.page.PageHome
import cn.bngel.bngelbook.ui.page.PageAccount
import cn.bngel.bngelbook.ui.page.PageFriend
import cn.bngel.bngelbook.ui.page.PageManager
import cn.bngel.bngelbook.ui.theme.BngelbookTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


class MainActivity : BaseActivity() {

    private val loginState = mutableStateOf(GlobalVariables.USER != null)
    private val pageState = mutableStateOf(PageManager.getCurPage())
    private val userDays = mutableStateOf(0)
    private lateinit var scaffoldState: ScaffoldState
    private lateinit var scope: CoroutineScope


    override fun event(result: ActivityResult) {
        val data = result.data
        if (data != null) {
            if (result.resultCode == RESULT_FIRST_USER) {
                loginState.value = data.getBooleanExtra("loginState", false)
                if (loginState.value) {
                    val user = data.getSerializableExtra("userInfo") as User
                    GlobalVariables.USER = user
                }
            }
            val updateRequired = data.getBooleanExtra("updateState", false)
            if (updateRequired) {
                PageManager.updateAllPage()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
        PageManager.registerPageManager(this)
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
        scaffoldState = rememberScaffoldState()
        scope = rememberCoroutineScope()
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
                        val intent = Intent(
                            this@MainActivity, when (PageManager.getCurPage()) {
                                MainPages.HOME_PAGE -> BillSaveActivity::class.java
                                MainPages.ACCOUNT_PAGE -> AccountSaveActivity::class.java
                                MainPages.FRIEND_PAGE -> FriendAddActivity::class.java
                                else -> BillSaveActivity::class.java
                            }
                        )
                        launcher.launch(intent)
                    }
                ) {
                    Icon(Icons.Filled.Add, null)
                }
            },
            floatingActionButtonPosition = FabPosition.End
        ) {
            when (pageState.value) {
                MainPages.HOME_PAGE -> PageHome.HomePage()
                MainPages.ACCOUNT_PAGE -> PageAccount.AccountPage()
                MainPages.FRIEND_PAGE -> PageFriend.FriendPage()
            }
        }
    }

    @Composable
    fun Drawer() {
        if (loginState.value) {
            Column {
                Drawer_ProfileCard(profile = "", username = GlobalVariables.USER?.username?:"", daysCount = userDays.value)
                Column(modifier = Modifier.weight(1f)) {
                    Drawer_Function(imageVector = Icons.Filled.Home, functionName = "Home") {
                        setPage(MainPages.HOME_PAGE)
                    }
                    Drawer_Function(imageVector = Icons.Filled.AccountBox, functionName = "Account") {
                        setPage(MainPages.ACCOUNT_PAGE)
                    }
                    Drawer_Function(imageVector = Icons.Filled.Face, functionName = "Friend") {
                        setPage(MainPages.FRIEND_PAGE)
                    }
                    // Drawer_Function(imageVector = Icons.Filled.Favorite, functionName = "Favorite") {}
                    // Drawer_Function(imageVector = Icons.Filled.AccountCircle, functionName = "AccountCircle") {}
                    // Drawer_Function(imageVector = Icons.Filled.Info, functionName = "Info") {}
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
                Drawer_ProfileCard(profile = "", username = "点击登录", daysCount = 0)
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
                    scope.launch {
                        scaffoldState.drawerState.close()
                    }
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

    private fun initData() {
        autoLogin()
    }

    private fun initUserDays() {
        if (loginState.value) {
            UserApi.getUserRegisterDays(GlobalVariables.USER?.id?:0L) { day ->
                userDays.value = day?.data?:0
            }
        }
    }

    private fun autoLogin() {
        getSharedPreferences("loginState", MODE_PRIVATE).apply {
            if (getBoolean("state", false)) {
                val account = getString("account", "")?:""
                val password = getString("password", "")?:""
                UserApi.postUserLogin(account, password) { result ->
                    if (result != null) {
                        when (result.code) {
                            200 -> {
                                GlobalVariables.USER = result.data
                                loginState.value = true
                                initUserDays()
                            }
                            else -> {
                                edit{
                                    putBoolean("state", false)
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    private fun setPage(page: MainPages) {
        PageManager.setCurPage(page)
        pageState.value = PageManager.getCurPage()
    }

}
