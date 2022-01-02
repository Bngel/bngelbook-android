package cn.bngel.bngelbook.utils

import android.content.Context
import java.util.*

object PropertiesUtils {

    fun getProperties(context: Context, properties: String): Properties {
        val prop = Properties()
        try {
            val inputStream = context.assets.open(properties)
            prop.load(inputStream)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return prop
    }

}