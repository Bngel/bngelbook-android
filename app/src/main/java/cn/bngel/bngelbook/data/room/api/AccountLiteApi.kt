package cn.bngel.bngelbook.data.room.api

import cn.bngel.bngelbook.data.bean.Account
import cn.bngel.bngelbook.data.room.DataManager

object AccountLiteApi {

    private val accountDB by lazy {
        DataManager.getAccountDB().accountDao()
    }

    fun insertAccount(account: Account) = accountDB.insertAccount(account)

    fun deleteAccount(account: Account) = accountDB.deleteAccount(account)

    fun getAccountById(id: Long) = accountDB.getAccountById(id)

    fun getUserAccounts(userId: Long) = accountDB.getUserAccounts(userId)
}