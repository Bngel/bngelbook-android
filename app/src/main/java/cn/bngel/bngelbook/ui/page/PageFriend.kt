package cn.bngel.bngelbook.ui.page

import android.content.Intent
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
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
import cn.bngel.bngelbook.R
import cn.bngel.bngelbook.activity.ActivityManager
import cn.bngel.bngelbook.activity.MainActivity
import cn.bngel.bngelbook.activity.UserDetailActivity
import cn.bngel.bngelbook.data.GlobalVariables
import cn.bngel.bngelbook.data.MainPages
import cn.bngel.bngelbook.data.userDao.User
import cn.bngel.bngelbook.network.UserApi
import cn.bngel.bngelbook.ui.widget.UiWidget

object PageFriend: BasePage() {

    private val loading = mutableStateOf(false)
    private val friends = mutableStateListOf<User>()

    init {
        setPage(MainPages.FRIEND_PAGE)
    }

    @Composable
    fun FriendPage() {
        Column {
            FriendTitle()
            FriendList()
        }
        if (loading.value)
            UiWidget.Dialog_Loading()
    }

    @Composable
    fun FriendTitle() {
        Box(modifier = Modifier
            .fillMaxWidth()
            .shadow(1.dp)) {
            Text(text = "好友列表", fontSize = 18.sp, textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp))
        }
    }

    @Composable
    fun FriendList() {
        if (getUpdate())
            initFriends()
        if (friends.size > 0) {
            LazyColumn {
                items(friends) { friend ->
                    FriendCard(friend)
                }
            }
        }
    }

    @Composable
    fun FriendCard(user: User) {
        Row(modifier = Modifier.fillMaxWidth().clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null
        ) {
            val intent = Intent(ActivityManager.getCurActivity(), UserDetailActivity::class.java)
            intent.putExtra("user", user)
            ActivityManager.getCurActivity()?.launcher?.launch(intent)
        }, verticalAlignment = Alignment.CenterVertically) {
            Image(painter = painterResource(id = R.drawable.default_profile), contentDescription = "profile",
                modifier = Modifier.padding(20.dp))
            Text(text = user.username ?: "", fontSize = 20.sp, color = Color.Black)
        }
    }

    private fun initFriends() {
        loading.value = true
        val userId = GlobalVariables.USER?.id
        if (userId != null) {
            UserApi.getFriendsById(userId) { result ->
                if (result?.data != null) {
                    friends.clear()
                    friends.addAll(result.data)
                    setUpdate(false)
                }
                loading.value = false
            }
        }
    }
}