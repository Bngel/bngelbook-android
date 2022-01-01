package cn.bngel.bngelbook.ui.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import cn.bngel.bngelbook.R
import cn.bngel.bngelbook.utils.UiUtils
import com.google.accompanist.glide.rememberGlidePainter

object UiWidget {

    @Composable
    fun Dialog_Loading(properties: DialogProperties? = null,
                       onDismissRequest: (() -> Unit)? = null) {
        val dialogProperties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = false
        )
        Dialog(onDismissRequest = { onDismissRequest?.invoke() },
            properties = properties ?: dialogProperties,
        ) {
            Column(modifier = Modifier
                .background(color = Color(0xFFFFFFFF), shape = RoundedCornerShape(10.dp))
                .padding(top = 30.dp, bottom = 20.dp, start = 30.dp, end = 30.dp)) {
                CustomImage(res = R.drawable.loading, placeHolder = R.drawable.loading, modifier = Modifier
                    .width(40.dp)
                    .height(40.dp)
                    .align(Alignment.CenterHorizontally))
                val loadingText by remember {
                    mutableStateOf("Loading")
                }
                Text(text = loadingText, modifier = Modifier.padding(10.dp))
            }
        }
    }

    @Composable
    fun CustomImage(res: String, placeHolder: Int, contentDescription: String? = null, modifier: Modifier = Modifier) {
        Image(painter = rememberGlidePainter(
            request = res,
            previewPlaceholder = placeHolder
        ), contentDescription = contentDescription, modifier = modifier)
    }

    @Composable
    fun CustomImage(res: Int, placeHolder: Int, contentDescription: String? = null, modifier: Modifier = Modifier) {
        Image(painter = rememberGlidePainter(
            request = res,
            previewPlaceholder = placeHolder
        ), contentDescription = contentDescription, modifier = modifier)
    }


}