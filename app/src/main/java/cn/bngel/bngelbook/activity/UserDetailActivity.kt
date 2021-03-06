package cn.bngel.bngelbook.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import cn.bngel.bngelbook.R
import cn.bngel.bngelbook.data.GlobalVariables
import cn.bngel.bngelbook.ui.theme.BngelbookTheme
import cn.bngel.bngelbook.data.bean.User
import cn.bngel.bngelbook.data.snapshot.UserState
import cn.bngel.bngelbook.network.api.FriendApi
import cn.bngel.bngelbook.ui.page.PageManager
import cn.bngel.bngelbook.ui.widget.UiWidget

class UserDetailActivity : BaseActivity() {

    private val menuTypeUnknown = -1
    private var menuType: Int = menuTypeUnknown
    private val menuExpanded = mutableStateOf(false)
    private val menuTypeSelf = 1
    private val menuTypeFriend = 2
    private val menuTypeStranger = 3
    private val menuTypeUpdated = mutableStateOf(false)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            BngelbookTheme {
                Surface(color = MaterialTheme.colors.background) {
                    UserDetailPage()
                }
            }
        }
    }


    @Composable
    private fun UserDetailPage() {
        if (!menuTypeUpdated.value) {
            initType()
        }
        Column {
            UserDetailTitle()
            UserDetailCard()
            UserDetailListCard()
        }
    }

    private fun initType() {
        UserState.id.value.let {
            val userId = GlobalVariables.USER?.id
            if (userId != null && it == GlobalVariables.USER?.id) {
                menuType = menuTypeSelf
            }
            else {
                if (userId != null) {
                    FriendApi.judgeFriendExists(it, userId) { result ->
                        menuType = when (result?.data) {
                            true -> menuTypeFriend
                            false -> menuTypeStranger
                            else -> menuTypeUnknown
                        }
                        if (menuType != menuTypeUnknown) {
                            menuTypeUpdated.value = true
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun UserDetailTitle() {
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
                                    Intent(this@UserDetailActivity, MainActivity::class.java)
                                setResult(RESULT_CANCELED, intent)
                                finish()
                            })

                }
                Row(modifier = Modifier.weight(1F), horizontalArrangement = Arrangement.End) {
                    Column {
                        Image(imageVector = Icons.Filled.Menu, contentDescription = "menu_btn",
                            modifier = Modifier
                                .padding(start = 15.dp, end = 15.dp, top = 20.dp, bottom = 15.dp)
                                .width(30.dp)
                                .height(30.dp)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) {
                                    menuExpanded.value = true
                                })
                        when (menuType) {
                            menuTypeSelf -> UserDetailPopupMenuForSelf()
                            menuTypeFriend -> UserDetailPopupMenuForFriend()
                            menuTypeStranger -> UserDetailPopupMenuForStranger()
                        }
                    }
                }
            }
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp), horizontalArrangement = Arrangement.Center) {
                Text(text = "????????????", fontSize = 18.sp)
            }
        }
    }

    @Composable
    private fun UserDetailCard() {
        Card(shape = RoundedCornerShape(10.dp), modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                UiWidget.CustomProfileImage(filePath = UserState.profileImage.value, modifier = Modifier.padding(20.dp).width(60.dp).height(60.dp))
                Text(text = UserState.username.value, fontSize = 24.sp, overflow = TextOverflow.Ellipsis, maxLines = 1,
                    textAlign = TextAlign.End, modifier = Modifier
                        .weight(1F)
                        .padding(top = 10.dp, bottom = 10.dp, start = 10.dp, end = 30.dp))
            }
        }
    }

    @Composable
    private fun UserDetailListCard() {
        Card(shape = RoundedCornerShape(10.dp), modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)) {
            Column {
                UserDetailListCardItem(icon = Icons.Filled.Person, text = UserState.username.value)
                UserDetailListCardItem(icon = Icons.Filled.Phone, text = UserState.phone.value)
                UserDetailListCardItem(icon = Icons.Filled.Email, text = UserState.email.value)
                UserDetailListCardItem(icon = Icons.Filled.DateRange, text = UserState.birthday.value)
            }
        }
    }

    @Composable
    private fun UserDetailListCardItem(icon: ImageVector, text: String) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = icon,
                contentDescription = null,
                modifier = Modifier.padding(10.dp))
            Text(text = text, fontSize = 15.sp, overflow = TextOverflow.Ellipsis, maxLines = 1,
                textAlign = TextAlign.End, modifier = Modifier
                    .weight(1F)
                    .padding(top = 10.dp, bottom = 10.dp, start = 10.dp, end = 20.dp))
        }
    }

    @Composable
    private fun UserDetailPopupMenuForSelf() {
        DropdownMenu(expanded = menuExpanded.value,
            onDismissRequest = { menuExpanded.value = false },
            offset = DpOffset(10.dp,10.dp),
        ) {
            UserDetailPopupMenuItem(icon = Icons.Filled.Edit, text = "??????"){
                ActivityManager.launch<UserInfoUpdateActivity>()
            }
        }
    }

    @Composable
    private fun UserDetailPopupMenuForFriend() {
        DropdownMenu(expanded = menuExpanded.value,
            onDismissRequest = { menuExpanded.value = false },
            offset = DpOffset(10.dp,10.dp),
        ) {
            UserDetailPopupMenuItem(icon = Icons.Filled.Delete, text = "????????????"){
                UserState.id.apply {
                    FriendApi.deleteFriendByUserId(this.value) { result ->
                        if (result?.code == 200) {
                            Toast.makeText(this@UserDetailActivity, "??????????????????", Toast.LENGTH_SHORT).show()
                            PageManager.updateAllPage()
                            finish()
                        }
                        else {
                            Toast.makeText(this@UserDetailActivity, "??????????????????", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun UserDetailPopupMenuForStranger() {
        DropdownMenu(expanded = menuExpanded.value,
            onDismissRequest = { menuExpanded.value = false },
            offset = DpOffset(10.dp,10.dp),
        ) {
            UserDetailPopupMenuItem(icon = Icons.Filled.Add, text = "????????????"){
                UserState.id.apply {
                    FriendApi.postFriendByUserId(this.value) { result ->
                        if (result?.code == 200) {
                            Toast.makeText(this@UserDetailActivity, "??????????????????", Toast.LENGTH_SHORT).show()
                            PageManager.updateAllPage()
                            finish()
                        }
                        else {
                            Toast.makeText(this@UserDetailActivity, "??????????????????", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun UserDetailPopupMenuItem(icon: ImageVector, text: String, onClick: () -> Unit) {
        DropdownMenuItem(onClick = {
            onClick()
            menuExpanded.value = false
        }) {
            Icon(imageVector = icon, contentDescription = null, modifier = Modifier.padding(10.dp))
            Text(text = text, textAlign = TextAlign.Center)
        }
    }

}
