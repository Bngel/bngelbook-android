package cn.bngel.bngelbook.ui.widget

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import cn.bngel.bngelbook.R
import cn.bngel.bngelbook.activity.ActivityManager
import cn.bngel.bngelbook.activity.BaseActivity
import cn.bngel.bngelbook.data.GlobalVariables
import cn.bngel.bngelbook.data.snapshot.UserState
import cn.bngel.bngelbook.utils.TencentcloudUtils
import cn.bngel.bngelbook.utils.UiUtils
import com.google.accompanist.glide.rememberGlidePainter
import com.tencent.cos.xml.exception.CosXmlClientException
import com.tencent.cos.xml.exception.CosXmlServiceException
import com.tencent.cos.xml.listener.CosXmlResultListener
import com.tencent.cos.xml.model.CosXmlRequest
import com.tencent.cos.xml.model.CosXmlResult
import java.io.File

object UiWidget {

    val context by lazy{
        ActivityManager.getCurActivity() as BaseActivity
    }

    @Composable
    fun LoadingDialog(properties: DialogProperties? = null,
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
    fun UpdateCheckDialog(onConfirmListener: (() -> Unit)? = null,
                          onCancelListener: (() -> Unit)? = null) {
        Dialog(onDismissRequest = {}) {
            Column(modifier = Modifier
                .background(color = Color.White, shape = RoundedCornerShape(10.dp))
                .padding(20.dp)) {
                Text(text = "下载更新", fontSize = 18.sp, textAlign = TextAlign.Start, modifier = Modifier.fillMaxWidth())
                Row(modifier = Modifier
                    .padding(top = 10.dp, bottom = 10.dp)
                    .fillMaxWidth()) {
                    Text(text = "检测到有新版本, 是否立即更新?")
                }
                Row(modifier = Modifier.align(Alignment.End)) {
                    Text(text = "取消", fontSize = 14.sp, modifier = Modifier.padding(10.dp)
                        .clickable {
                            if (onCancelListener != null) {
                                onCancelListener()
                            }
                        })
                    Text(text = "确定", fontSize = 14.sp, modifier = Modifier.padding(10.dp)
                        .clickable {
                            if (onConfirmListener != null) {
                                onConfirmListener()
                            }
                        })
                }
            }
        }
    }

    @Composable
    fun UpdateProgressDialog(completed: Double, target: Double) {
        Dialog(onDismissRequest = {}) {
            Column(modifier = Modifier
                .background(color = Color.White, shape = RoundedCornerShape(10.dp))
                .padding(20.dp)) {
                Text(text = "下载更新", fontSize = 18.sp, textAlign = TextAlign.Start, modifier = Modifier.fillMaxWidth())
                Row(modifier = Modifier
                    .padding(top = 10.dp, bottom = 10.dp)
                    .border(width = 2.dp, color = Color.Gray, shape = RoundedCornerShape(5.dp))
                    .background(color = Color.White, shape = RoundedCornerShape(5.dp))
                    .fillMaxWidth()) {
                    Spacer(modifier = Modifier
                        .height(25.dp)
                        .fillMaxWidth((completed / target).toFloat())
                        .background(color = Color(0xAA66CCFF), shape = RoundedCornerShape(5.dp))
                    )
                }
                Text(text = String.format("%.2f%%", (completed/target) * 100), fontSize = 14.sp, textAlign = TextAlign.End, modifier = Modifier.fillMaxWidth())
            }
        }
    }

    @Composable
    fun UpdateProgressDialog(rate: Float) {
        Dialog(onDismissRequest = {}) {
            Column(modifier = Modifier
                .background(color = Color.White, shape = RoundedCornerShape(10.dp))
                .padding(20.dp)) {
                Text(text = "下载更新", fontSize = 18.sp, textAlign = TextAlign.Start, modifier = Modifier.fillMaxWidth())
                Row(modifier = Modifier
                    .padding(top = 10.dp, bottom = 10.dp)
                    .border(width = 2.dp, color = Color.Gray, shape = RoundedCornerShape(5.dp))
                    .background(color = Color.White, shape = RoundedCornerShape(5.dp))
                    .fillMaxWidth()) {
                    Spacer(modifier = Modifier
                        .height(25.dp)
                        .fillMaxWidth(rate)
                        .background(color = Color(0xAA66CCFF), shape = RoundedCornerShape(5.dp))
                    )
                }
                Text(text = String.format("%.2f%%", rate * 100), fontSize = 14.sp, textAlign = TextAlign.End, modifier = Modifier.fillMaxWidth())
            }
        }
    }

    @Composable
    fun CustomCircleImage(res: String?, placeHolder: Int, contentDescription: String? = null, modifier: Modifier = Modifier, alignment: Alignment = Alignment.Center) {
        Image(painter = rememberGlidePainter(
            request = res,
            previewPlaceholder = placeHolder
        ), contentDescription = contentDescription,
            contentScale = ContentScale.Crop,
            modifier = modifier
                .clip(RoundedCornerShape(50))
                .border(width = 2.dp, color = Color.White, shape = RoundedCornerShape(50)))
    }

    @Composable
    fun CustomCircleImage(res: Int, placeHolder: Int, contentDescription: String? = null, modifier: Modifier = Modifier, alignment: Alignment = Alignment.Center) {
        Image(painter = rememberGlidePainter(
            request = res,
            previewPlaceholder = placeHolder
        ), contentDescription = contentDescription,
            contentScale = ContentScale.Crop,
            modifier = modifier
                .clip(RoundedCornerShape(50))
                .border(width = 2.dp, color = Color.White, shape = RoundedCornerShape(50)))
    }

    @Composable
    fun CustomImage(res: String?, placeHolder: Int, contentDescription: String? = null, modifier: Modifier = Modifier, alignment: Alignment = Alignment.Center) {
        Image(painter = rememberGlidePainter(
            request = res,
            previewPlaceholder = placeHolder
        ), contentDescription = contentDescription, modifier = modifier)
    }

    @Composable
    fun CustomImage(res: Int, placeHolder: Int, contentDescription: String? = null, modifier: Modifier = Modifier, alignment: Alignment = Alignment.Center) {
        Image(painter = rememberGlidePainter(
            request = res,
            previewPlaceholder = placeHolder
        ), contentDescription = contentDescription, modifier = modifier, alignment = alignment)
    }

    @Composable
    fun CustomProfileImage(filePath: String = GlobalVariables.getDefaultProfile(), modifier: Modifier = Modifier) {
        val fileExist = remember {
            mutableStateOf(File(filePath).exists())
        }
        if (!fileExist.value) {
            CustomCircleImage(
                res = R.drawable.default_profile,
                placeHolder = R.drawable.default_profile,
                alignment = Alignment.TopCenter,
                modifier = modifier
            )
            val resultListener = object: CosXmlResultListener {
                override fun onSuccess(p0: CosXmlRequest?, p1: CosXmlResult?) {
                    fileExist.value = true
                }
                override fun onFail(
                    p0: CosXmlRequest?,
                    p1: CosXmlClientException?,
                    p2: CosXmlServiceException?
                ) {
                    p1?.printStackTrace()
                    p2?.printStackTrace()
                }
            }
            TencentcloudUtils.downloadFile("bngelbook-profile", GlobalVariables.USER?.profile?:"", "bngelbook-profile.png",
                cosXmlResultListener = resultListener)
        }
        else {
            CustomCircleImage(
                res = filePath,
                placeHolder = R.drawable.default_profile,
                alignment = Alignment.TopCenter,
                modifier = modifier
            )
        }
    }


}