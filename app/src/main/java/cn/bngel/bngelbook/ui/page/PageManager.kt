package cn.bngel.bngelbook.ui.page

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import cn.bngel.bngelbook.data.MainPages

object PageManager {

    private val pages = mapOf(
        MainPages.ACCOUNT_PAGE to PageAccount,
        MainPages.FRIEND_PAGE to PageFriend,
        MainPages.HOME_PAGE to PageHome
    )
    private val curPage = MutableLiveData(MainPages.HOME_PAGE)

    fun registerPageManager(owner: LifecycleOwner) {
        curPage.observe(owner) { newPage ->
            for (page in pages.entries) {
                if (page.key != newPage) {
                    // page.value.setUpdate(true)
                }
            }
        }
    }

    fun setPageUpdate(page: MainPages) {
        pages[page]?.setUpdate(true)
    }

    fun getPageUpdate(page: MainPages) = pages[page]?.getUpdate()

    fun setCurPage(page: MainPages) {
        curPage.value = page
    }

    fun getCurPage() = curPage.value

    fun updateAllPage() {
        for (page in pages.values) {
            page.setUpdate(true)
        }
    }
}