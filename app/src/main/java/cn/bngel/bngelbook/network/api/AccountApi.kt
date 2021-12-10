package cn.bngel.bngelbook.network.api

import cn.bngel.bngelbook.dao.AccountDao
import cn.bngel.bngelbook.dao.BasicDao
import cn.bngel.bngelbook.data.CommonResult
import cn.bngel.bngelbook.data.bean.Account
import cn.bngel.bngelbook.utils.NetworkUtils

object AccountApi: BaseApi() {

    private val accountService by lazy {
        BasicDao.create<AccountDao>(BasicDao.ACCOUNT_URL)
    }

    fun postAccount(account: Account, event: ((CommonResult<Boolean>?) -> Unit)? = null) {
        accountService.postAccount(account).enqueue(basicCallback(event))
    }

    fun deleteAccountById(id: Long, event: ((CommonResult<Boolean>?) -> Unit)? = null) {
        accountService.deleteAccountById(id).enqueue(basicCallback(event))
    }

    fun updateAccountById(account: Account, event: ((CommonResult<Boolean>?) -> Unit)? = null) {
        accountService.updateAccountById(account).enqueue(basicCallback(event))
    }

    fun getAccountById(id: Long, event: ((CommonResult<Account>?) -> Unit)? = null) {
        accountService.getAccountById(id).enqueue(basicCallback(event))
    }

    fun getAccountsByUserId(userId: Long, event: ((CommonResult<List<Account>>?) -> Unit)? = null) {
        accountService.getAccountsByUserId(userId).enqueue(basicCallback(event))
    }
}