package cn.bngel.bngelbook.ui.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.bngel.bngelbook.data.MainPages

object PageDefault: BasePage() {
    
    init {
        setPage(MainPages.DEFAULT_PAGE)
    }
    
    @Composable
    fun DefaultPage() {
        Box(contentAlignment = Alignment.Center, modifier = Modifier
            .fillMaxSize()) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(imageVector = Icons.Filled.Settings, contentDescription = null,
                    modifier = Modifier.padding(80.dp).height(120.dp).width(120.dp), tint = Color(0xFF66CCFF)
                )
                Text(text = "请先登录", fontSize = 30.sp, color = Color(0xFF66CCFF))
            }
        }
    }
}