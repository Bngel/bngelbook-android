package cn.bngel.bngelbook.data.bean

import android.util.Base64
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream


/**
 * @author: bngel
 * @date: 22.1.1
 * @description:
 */

open class Bean {

    open val base64: String by lazy {
        getCustomBase64()
    }

    private fun getCustomBase64(): String {
        val byteStream = ByteArrayOutputStream()
        val objStream = ObjectOutputStream(byteStream)
        objStream.writeObject(this)
        return String(Base64.encode(byteStream.toByteArray(), Base64.DEFAULT))
    }

    companion object {

        fun <T: Bean> fromCustomBase64(base64: String): T {
            val byteArrayInputStream =
                ByteArrayInputStream(Base64.decode(base64.toByteArray(), Base64.DEFAULT))
            val objectInputStream = ObjectInputStream(byteArrayInputStream)
            return objectInputStream.readObject() as T
        }

    }
}