package cn.bngel.bngelbook.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.bngel.bngelbook.R
import cn.bngel.bngelbook.data.GlobalVariables
import cn.bngel.bngelbook.data.friendDao.Friend
import cn.bngel.bngelbook.data.userDao.User
import cn.bngel.bngelbook.network.FriendApi
import cn.bngel.bngelbook.network.UserApi
import cn.bngel.bngelbook.ui.page.PageManager
import cn.bngel.bngelbook.ui.theme.BngelbookTheme

class FriendAddActivity : BaseActivity() {

    private val usernameState = mutableStateOf("")
    private val userListState = mutableStateListOf<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BngelbookTheme {
                Surface(color = MaterialTheme.colors.background) {
                    FriendAddPage()
                }
            }
        }

    }

    @Composable
    fun FriendAddPage() {
        Column {
            FriendAddTitle()
            FriendAddSearch()
            FriendAddSearchList()
        }
    }

    @Composable
    fun FriendAddTitle() {
        Box(modifier = Modifier
            .fillMaxWidth()
            .shadow(1.dp)) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.weight(1F), horizontalArrangement = Arrangement.Start) {
                    Image(imageVector = Icons.Filled.Close, contentDescription = "close_btn",
                        modifier = Modifier
                            .padding(start = 15.dp, end = 15.dp, top = 20.dp, bottom = 15.dp)
                            .width(30.dp)
                            .height(30.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                val intent =
                                    Intent(this@FriendAddActivity, MainActivity::class.java)
                                setResult(RESULT_CANCELED, intent)
                                finish()
                            })
                }
            }
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp), horizontalArrangement = Arrangement.Center) {
                Text(text = "添加好友", fontSize = 18.sp)
            }
        }
    }

    @Composable
    fun FriendAddSearch() {
        TextField(value = usernameState.value, onValueChange = {
            usernameState.value = it
        }, modifier = Modifier.fillMaxWidth(), leadingIcon = { Icon(Icons.Filled.Search, null)},
            singleLine = true)
    }

    @Composable
    fun FriendAddSearchList() {
        searchFriendsByUsername()
        LazyColumn {
            items(userListState) { user ->
                FriendAddSearchItem(user = user)
            }
        }
    }
    
    @Composable
    fun FriendAddSearchItem(user: User) {
        Row(modifier = Modifier.fillMaxWidth().clickable {
            val intent = Intent(this@FriendAddActivity, UserDetailActivity::class.java)
            intent.putExtra("user", user)
            intent.putExtra("menuType", 3)
            launcher.launch(intent)
        }, verticalAlignment = Alignment.CenterVertically) {
            Image(painter = painterResource(id = R.drawable.default_profile), contentDescription = "profile",
                modifier = Modifier.padding(20.dp))
            Text(text = user.username ?: "", fontSize = 20.sp, color = Color.Black, modifier = Modifier.weight(1F))
            Icon(imageVector = Icons.Filled.AddCircle, null,
                modifier = Modifier.padding(20.dp).clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    val friend = Friend(null, GlobalVariables.USER?.id, user.id)
                    FriendApi.postFriend(friend) { result ->
                        Toast.makeText(this@FriendAddActivity, when(result?.code) {
                            200 -> "好友请求发送成功"
                            484 -> "你们已经是好友啦"
                            423 -> "不能添加自己为好友"
                            else -> "unknown error"
                        },Toast.LENGTH_SHORT).show()
                        if (result?.code == 200)
                            PageManager.updateAllPage()
                    }
                })
        }
    }

    private fun searchFriendsByUsername() {
        UserApi.getUsersByUsername(usernameState.value) { result ->
            val data = result?.data
            if (data != null) {
                userListState.clear()
                userListState.addAll(data)
            }
        }
    }
}