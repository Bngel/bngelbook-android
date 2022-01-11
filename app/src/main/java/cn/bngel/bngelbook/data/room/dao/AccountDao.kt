package cn.bngel.bngelbook.data.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import cn.bngel.bngelbook.data.bean.Account


/**
 * @author: bngel
 * @date: 22.1.11
 * @description:
 */

@Dao
interface AccountDao {

    @Insert
    fun insertAccount(account: Account): Int

    @Delete
    fun deleteAccount(account: Account): Int

    @Query("SELECT * FROM account WHERE id = :id")
    fun getAccountById(id: Long): Account

    @Query("SELECT * FROM account WHERE user_id = :userId")
    fun getUserAccounts(userId: Long): List<Account>
}