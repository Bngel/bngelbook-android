package cn.bngel.bngelbook.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
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
import cn.bngel.bngelbook.ui.theme.BngelbookTheme
import cn.bngel.bngelbook.data.userDao.User

class UserDetailActivity : BaseActivity() {

    private var user: User? = null
    private var menuClickable = false
    private val menuExpanded = mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        user = intent.getSerializableExtra("user") as User
        menuClickable = intent.getBooleanExtra("menu", false)
        val menuType = intent.getIntExtra("menuType", -1)

        setContent {
            BngelbookTheme {
                Surface(color = MaterialTheme.colors.background) {
                    UserDetailPage()
                    TODO("create a bottom sheet")
                }
            }
        }
    }


    @Composable
    fun UserDetailPage() {
        Column {
            UserDetailTitle()
            UserDetailCard()
            UserDetailListCard()
        }
    }

    @Composable
    fun UserDetailTitle() {
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
                    Image(imageVector = Icons.Filled.Menu, contentDescription = "close_btn",
                        modifier = Modifier
                            .padding(start = 15.dp, end = 15.dp, top = 20.dp, bottom = 15.dp)
                            .width(30.dp)
                            .height(30.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {

                            })

                }
            }
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp), horizontalArrangement = Arrangement.Center) {
                Text(text = "账号信息", fontSize = 18.sp)
            }
        }
    }

    @Composable
    fun UserDetailCard() {
        Card(shape = RoundedCornerShape(10.dp), modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(painter = painterResource(id = R.drawable.default_profile),
                    contentDescription = null,
                    modifier = Modifier.padding(20.dp))
                Text(text = user?.username?:"", fontSize = 24.sp, overflow = TextOverflow.Ellipsis, maxLines = 1,
                    textAlign = TextAlign.End, modifier = Modifier
                        .weight(1F)
                        .padding(top = 10.dp, bottom = 10.dp, start = 10.dp, end = 30.dp))
            }
        }
    }

    @Composable
    fun UserDetailListCard() {
        Card(shape = RoundedCornerShape(10.dp), modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)) {
            Column {
                UserDetailListCardItem(icon = Icons.Filled.Person, text = user?.username?:"")
                UserDetailListCardItem(icon = Icons.Filled.Phone, text = user?.phone?:"")
                UserDetailListCardItem(icon = Icons.Filled.Email, text = user?.email?:"")
                UserDetailListCardItem(icon = Icons.Filled.DateRange, text = user?.birthday?:"")
            }
        }
    }

    @Composable
    fun UserDetailListCardItem(icon: ImageVector, text: String) {
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
}
