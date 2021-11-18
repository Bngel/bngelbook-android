package cn.bngel.bngelbook.ui

import cn.bngel.bngelbook.R


/**
 * @author: bngel
 * @date: 21.11.15
 * @description:
 */

object BillType {

    private val billTypeMap = mapOf(
        "测试" to R.drawable.default_profile
    )

    fun getTypeImg(type: String) = billTypeMap[type]?:R.drawable.default_profile

    fun getTypes() = billTypeMap.keys

}