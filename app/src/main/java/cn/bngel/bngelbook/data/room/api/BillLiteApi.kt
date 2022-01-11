package cn.bngel.bngelbook.data.room.api

import cn.bngel.bngelbook.data.bean.Bill
import cn.bngel.bngelbook.data.room.DataManager
import cn.bngel.bngelbook.data.room.database.BillDatabase

object BillLiteApi {

    private val billDB by lazy {
        DataManager.getBillDB().billDao()
    }

    fun insertBill(bill: Bill) = billDB.insertBill(bill)

    fun deleteBill(bill: Bill) = billDB.deleteBill(bill)

    fun getBillById(id: Long) = billDB.getBillById(id)

    fun getBillsByBookId(bookId: Long) = billDB.getBillsByBookId(bookId)

    fun geiBillsByAccountId(accountId: Long) = billDB.getBillsByAccountId(accountId)
}