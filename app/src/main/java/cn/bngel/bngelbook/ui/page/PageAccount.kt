package cn.bngel.bngelbook.ui.page

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.bngel.bngelbook.MainActivity

object PageAccount {

    @Composable
    fun AccountPage() {
        Column {
            Account_Title()
            Account_Overview()
        }
    }

    @Composable
    fun Account_Title() {
        Box(modifier = Modifier.fillMaxWidth().shadow(1.dp)) {
            Text(text = "我的账户", fontSize = 18.sp, textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp))
        }
    }

    @Composable
    fun Account_Overview() {
        Column {
            Text(text = "净资产", fontSize = 18.sp, textAlign = TextAlign.Start ,
                modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 10.dp))
            Text(text = "-130.97", fontSize = 26.sp, textAlign = TextAlign.Start,
                modifier = Modifier.padding(start = 20.dp, end = 20.dp, bottom = 20.dp))
        }
    }
}