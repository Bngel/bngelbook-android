package cn.bngel.bngelbook.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import cn.bngel.bngelbook.data.CommonResult
import cn.bngel.bngelbook.data.GlobalVariables
import cn.bngel.bngelbook.data.MainPages
import cn.bngel.bngelbook.data.bean.Bean
import cn.bngel.bngelbook.data.bean.User
import cn.bngel.bngelbook.data.sharedPreferences.spApi
import cn.bngel.bngelbook.data.snapshot.UserState
import cn.bngel.bngelbook.network.api.UserApi
import cn.bngel.bngelbook.network.api.VersionApi
import cn.bngel.bngelbook.ui.page.*
import cn.bngel.bngelbook.ui.theme.BngelbookTheme
import cn.bngel.bngelbook.ui.widget.UiWidget
import cn.bngel.bngelbook.utils.TencentcloudUtils
import com.tencent.cos.xml.exception.CosXmlClientException
import com.tencent.cos.xml.exception.CosXmlServiceException
import com.tencent.cos.xml.listener.CosXmlProgressListener
import com.tencent.cos.xml.listener.CosXmlResultListener
import com.tencent.cos.xml.model.CosXmlRequest
import com.tencent.cos.xml.model.CosXmlResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.ObjectInputStream
import java.lang.Exception
import java.time.LocalDate


class MainActivity : BaseActivity() {

    private val loginState = mutableStateOf(GlobalVariables.USER != null)
    private val pageState = mutableStateOf(MainPages.DEFAULT_PAGE)
    private val userDays = mutableStateOf(0)
    private val resUpdate = mutableStateOf(false)
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
                    setPage(MainPages.HOME_PAGE)
                    userDays.value =
                        (LocalDate.now().toEpochDay() - LocalDate.parse(user.registerDate).toEpochDay()).toInt()
                }
            }
            val updateRequired = data.getBooleanExtra("updateState", false)
            if (updateRequired) {
                PageManager.updateAllPage()
            }
            resUpdate.value = true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PageManager.registerPageManager(this)
        setContent {
            BngelbookTheme {
                Surface(color = MaterialTheme.colors.background) {
                    initData()
                    MainPage()
                }
            }
        }
    }

    @Composable
    private fun MainPage() {
        scaffoldState = rememberScaffoldState()
        scope = rememberCoroutineScope()
        if (resUpdate.value)
            resUpdate.value = false
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
                MainPages.DEFAULT_PAGE -> PageDefault.DefaultPage()
                MainPages.HOME_PAGE -> PageHome.HomePage()
                MainPages.ACCOUNT_PAGE -> PageAccount.AccountPage()
                MainPages.FRIEND_PAGE -> PageFriend.FriendPage()
            }
        }
    }

    @Composable
    private fun Drawer() {
        Column {
            Drawer_ProfileCard()
            if (loginState.value) {
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
                        spApi.clearLocalUser()
                        setPage(MainPages.DEFAULT_PAGE)
                    }
                    .padding(20.dp)) {
                    Icon(imageVector = Icons.Filled.ExitToApp, contentDescription = "exit")
                    Text(text = "退出账号", modifier = Modifier.padding(start = 15.dp))
                }
            }
        }
    }

    @Composable
    private fun Drawer_ProfileCard() {
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
                            ActivityManager.launch<UserDetailActivity> {
                                putExtra("user", GlobalVariables.USER)
                            }
                        } else {
                            scope.launch { scaffoldState.drawerState.close() }
                            ActivityManager.launch<LoginActivity>()
                        }
                    }
            ) {
                if (!loginState.value) {
                    UiWidget.CustomCircleImage(
                        res = R.drawable.default_profile,
                        placeHolder = R.drawable.default_profile,
                        alignment = Alignment.TopCenter,
                        modifier = Modifier
                            .width(60.dp)
                            .height(60.dp)
                    )
                    Text(text = "点击登录", modifier = Modifier.padding(top = 10.dp))
                }
                else {
                    UiWidget.CustomProfileImage(filePath = UserState.profileImage.value,
                        modifier = Modifier.width(60.dp).height(60.dp))
                }
            }
            if (loginState.value) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(5f)
                ) {
                    Text(text = GlobalVariables.USER?.username?:"")
                    Text(text = "已记账: ${userDays.value} 天")
                }
            }
        }
    }

    @Composable
    private fun Drawer_Function(imageVector: ImageVector, functionName: String, onClick: ()-> Unit) {
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
        autoUpdateCheck()
    }

    private fun autoUpdateCheck() {
        VersionApi.getNewestVersion { result ->
            val version = getVersionName()
            Log.d("updateVersion", version)
            if (result?.code == CommonResult.SUCCESS_CODE) {
                val newestVersion = result.data
                if (newestVersion?.version != null && newestVersion.version != getVersionName()) {
                    Log.d("updateVersion", "版本需要更新 from ${getVersionName()} to ${newestVersion.version}")
                    val progressListener = CosXmlProgressListener { complete, target ->
                        Log.d("updateVersion", "$complete/$target")
                    }
                    val resultListener = object: CosXmlResultListener {
                        override fun onSuccess(p0: CosXmlRequest?, p1: CosXmlResult?) {
                            Log.d("updateVersion", "download successfully")
                        }

                        override fun onFail(
                            p0: CosXmlRequest?,
                            p1: CosXmlClientException?,
                            p2: CosXmlServiceException?
                        ) {
                            Log.d("updateVersion", "download failed")
                        }
                    }
                    VersionApi.downloadNewestVersion(newestVersion, progressListener, resultListener)
                }
            }
        }
    }

    private fun getVersionName() = try {
        val packageInfo = packageManager.getPackageInfo(packageName, 0)
        packageInfo.versionName
    } catch (e: Exception) {
        ""
    }

    private fun autoLogin() {
        val user = spApi.getLocalUser()
        if (user != null) {
            GlobalVariables.USER = user
            userDays.value =
                (LocalDate.now().toEpochDay() - LocalDate.parse(user.registerDate).toEpochDay()).toInt()
            loginState.value = true
            pageState.value = MainPages.HOME_PAGE
        }
        GlobalVariables.token = spApi.getToken()
    }

    private fun setPage(page: MainPages) {
        PageManager.setCurPage(page)
        pageState.value = page
    }

}