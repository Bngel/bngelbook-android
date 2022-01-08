package cn.bngel.bngelbook.data

import java.io.Serializable

data class CommonResult<T>(
    val code: Int,
    val `data`: T?,
    val message: String
): Serializable {

    companion object {

        const val SUCCESS_CODE = 200
        const val FAILURE_CODE = 400
        const val TIME_OUT_CODE = 408
        const val NO_NETWORK_CODE = 498
        const val TOKEN_ERROR_CODE = 490

    }
}
