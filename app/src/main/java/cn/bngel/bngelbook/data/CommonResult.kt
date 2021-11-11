package cn.bngel.bngelbook.data

import java.io.Serializable

data class CommonResult<T>(
    val code: Int,
    val `data`: T?,
    val message: String
): Serializable
