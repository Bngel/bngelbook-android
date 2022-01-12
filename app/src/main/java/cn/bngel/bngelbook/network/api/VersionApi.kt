package cn.bngel.bngelbook.network.api

import cn.bngel.bngelbook.dao.BasicDao
import cn.bngel.bngelbook.dao.VersionDao
import cn.bngel.bngelbook.data.CommonResult
import cn.bngel.bngelbook.data.bean.Version
import cn.bngel.bngelbook.utils.TencentcloudUtils
import com.tencent.cos.xml.listener.CosXmlProgressListener
import com.tencent.cos.xml.listener.CosXmlResultListener

object VersionApi: BaseApi() {

    private val versionService by lazy {
        BasicDao.create<VersionDao>(BasicDao.VERSION_URL)
    }

    fun getNewestVersion(event: ((CommonResult<Version>?) -> Unit)) {
        versionService.getNewestVersion().enqueue(basicCallback(event))
    }

    fun downloadNewestVersion(version: Version,
                              cosXmlProgressListener: CosXmlProgressListener? = null,
                              cosXmlResultListener: CosXmlResultListener? = null) {
        val bucketName = "bngelbook-version"
        val fileName = "bngelbook-${version.version}.apk"
        TencentcloudUtils.downloadFile(bucketName, fileName, fileName,
            cosXmlProgressListener = cosXmlProgressListener,
            cosXmlResultListener = cosXmlResultListener)
    }
}