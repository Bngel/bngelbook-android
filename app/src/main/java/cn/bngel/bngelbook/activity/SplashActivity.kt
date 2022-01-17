package cn.bngel.bngelbook.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.bngel.bngelbook.R
import cn.bngel.bngelbook.ui.page.PageManager
import cn.bngel.bngelbook.ui.theme.BngelbookTheme
import java.util.*
import kotlin.concurrent.thread

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity() {
    private val SKIP_SECONDS = 3
    private var skip = SKIP_SECONDS
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BngelbookTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    SplashPage()
                }
            }
        }
        skipSplash()
    }


    @Composable
    private fun SplashPage() {
        Column {
            Column(modifier = Modifier.weight(1F)) {
                Image(painter = painterResource(id = R.drawable.splash_default), contentDescription = null, contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize())
            }
            Column(modifier = Modifier
                .height(100.dp)
                .fillMaxWidth()
                .background(Color.White),
                horizontalAlignment = Alignment.CenterHorizontally) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxHeight()) {
                    Image(painter = painterResource(id = R.drawable.bngelbook_ic), contentDescription = null, modifier = Modifier
                        .width(40.dp)
                        .height(40.dp)
                        .clip(RoundedCornerShape(5.dp)))
                    Text(text = "十三账本", modifier = Modifier.padding(start = 20.dp), style = TextStyle(color = Color.Black, fontSize = 18.sp, fontWeight = FontWeight.Bold))
                }
            }
        }
    }

    private fun skipSplash() {
        thread {
            while (skip >= 0) {
                if (skip == 0) {
                    launch<MainActivity>()
                    finish()
                }
                Thread.sleep(1000)
                skip -= 1
            }
        }
    }
}