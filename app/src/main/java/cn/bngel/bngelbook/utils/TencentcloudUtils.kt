package cn.bngel.bngelbook.utils

import cn.bngel.bngelbook.activity.ActivityManager
import cn.bngel.bngelbook.activity.BaseActivity
import com.tencent.cos.xml.CosXmlServiceConfig
import com.tencent.cos.xml.CosXmlSimpleService
import com.tencent.cos.xml.listener.CosXmlProgressListener
import com.tencent.cos.xml.listener.CosXmlResultListener
import com.tencent.cos.xml.transfer.TransferConfig
import com.tencent.cos.xml.transfer.TransferManager
import com.tencent.cos.xml.transfer.TransferStateListener
import com.tencent.qcloud.core.auth.BasicLifecycleCredentialProvider
import com.tencent.qcloud.core.auth.QCloudLifecycleCredentials
import com.tencent.qcloud.core.auth.SessionQCloudCredentials
import java.util.*
import kotlin.collections.HashMap

object TencentcloudUtils{

    private val context by lazy {
        ActivityManager.getCurActivity() as BaseActivity
    }
    private val tencentcloudProp = PropertiesUtils.getProperties(context, "tencentcloudConfig.properties")
    private val cosXmlService by lazy {
        getCosService()
    }

    fun downloadFile(bucketName: String, cosPath: String?, savedFileName: String,
            cosXmlProgressListener: CosXmlProgressListener? = null,
            cosXmlResultListener: CosXmlResultListener? = null,
            transferStateListener: TransferStateListener? = null) {
        val transferConfig = TransferConfig.Builder().build()
        val transferManager = TransferManager(cosXmlService, transferConfig)
        val savePathDir = context.externalCacheDir.toString()
        val applicationContext = context.applicationContext
        val bucket = bucketName + "-" + tencentcloudProp["tencent-cloud.APPID"]
        val cosXmlDownloadTask = transferManager.download(
            applicationContext,
            bucket,
            cosPath?:"",
            savePathDir,
            savedFileName
        )
        cosXmlDownloadTask.setCosXmlProgressListener(cosXmlProgressListener)
        cosXmlDownloadTask.setCosXmlResultListener(cosXmlResultListener)
        cosXmlDownloadTask.setTransferStateListener(transferStateListener)
    }

    private fun getDataMap(): Map<String, String> {
        val resMap = HashMap<String, String>()
        tencentcloudProp.apply {
            resMap["APPID"] = getProperty("tencent-cloud.APPID")
            resMap["SecretId"] = getProperty("tencent-cloud.SecretId")
            resMap["SecretKey"] = getProperty("tencent-cloud.SecretKey")
        }
        return resMap
    }

    private class MySessionCredentialProvider: BasicLifecycleCredentialProvider() {
        override fun fetchNewCredentials(): QCloudLifecycleCredentials {
            val dataMap = getDataMap()
            val token = "TempSessionToken"
            val expireTime = Date().time + 24 * 60 * 60
            val startTime = Date().time
            return SessionQCloudCredentials(
                dataMap["SecretId"],
                dataMap["SecretKey"],
                token,
                startTime,
                expireTime
            )
        }
    }

    private fun getCosService(): CosXmlSimpleService {
        val region = tencentcloudProp.getProperty("tencent-cloud.cos.region")
        val mySessionCredentialProvider = MySessionCredentialProvider()
        val serviceConfig = CosXmlServiceConfig.Builder().setRegion(region).builder()
        return CosXmlSimpleService(
            context,
            serviceConfig,
            mySessionCredentialProvider
        )
    }
}