package cn.bngel.bngelbook.ui.page

import androidx.compose.runtime.mutableStateOf
import cn.bngel.bngelbook.data.MainPages


/**
 * @author: bngel
 * @date: 21.11.28
 * @description:
 */

abstract class BasePage {

    private var pageType: MainPages = MainPages.HOME_PAGE
    private val updateState = mutableStateOf(true)

    protected fun setPage(page: MainPages) {
        pageType = page
    }

    protected fun getPage() = pageType

    fun setUpdate(update: Boolean) {
        updateState.value = update
    }

    fun getUpdate() = updateState.value
}