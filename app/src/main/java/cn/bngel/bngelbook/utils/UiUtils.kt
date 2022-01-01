package cn.bngel.bngelbook.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cn.bngel.bngelbook.R
import com.google.accompanist.glide.rememberGlidePainter


/**
 * @author: bngel
 * @date: 21.11.15
 * @description:
 */

object UiUtils {

    private val billTypeMap = mapOf(
        "测试" to R.drawable.default_profile
    )

    fun getTypeImg(type: String) = billTypeMap[type]?:R.drawable.default_profile

    fun getTypes() = billTypeMap.keys

}