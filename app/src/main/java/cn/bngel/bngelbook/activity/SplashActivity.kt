package cn.bngel.bngelbook.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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

            }
            Column {
                Spacer(modifier = Modifier
                    .height(100.dp)
                    .fillMaxWidth()
                    .background(Color.Black))
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