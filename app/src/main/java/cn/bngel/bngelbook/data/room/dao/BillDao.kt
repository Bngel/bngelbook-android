package cn.bngel.bngelbook.data.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import cn.bngel.bngelbook.data.bean.Bill


/**
 * @author: bngel
 * @date: 22.1.11
 * @description:
 */

@Dao
interface BillDao {

    @Insert
    fun insertBill(bill: Bill): Int

    @Delete
    fun deleteBill(bill: Bill): Int

    @Query("SELECT * FROM bill WHERE id = :id")
    fun getBillById(id: Long): Bill?

    @Query("SELECT * FROM bill WHERE book_id = :bookId")
    fun getBillsByBookId(bookId: Long): List<Bill>

    @Query("SELECT * FROM bill WHERE account_id = :accountId")
    fun getBillsByAccountId(accountId: Long): List<Bill>
}