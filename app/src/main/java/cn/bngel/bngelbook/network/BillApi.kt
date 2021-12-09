package cn.bngel.bngelbook.network

import cn.bngel.bngelbook.dao.BasicDao
import cn.bngel.bngelbook.dao.BillDao
import cn.bngel.bngelbook.data.CommonResult
import cn.bngel.bngelbook.data.bean.Bill

object BillApi: BaseApi() {

    private val billService by lazy {
        BasicDao.create<BillDao>(BasicDao.BILL_URL)
    }

    fun postBill(bill: Bill, event: ((CommonResult<Boolean>?) -> Unit)? = null) {
        billService.postBill(bill).enqueue(basicCallback(event))
    }

    fun deleteBillById(id: Long, event: ((CommonResult<Boolean>?) -> Unit)? = null) {
        billService.deleteBillById(id).enqueue(basicCallback(event))
    }

    fun updateBillById(bill: Bill, event: ((CommonResult<Boolean>?) -> Unit)? = null) {
        billService.updateBillById(bill).enqueue(basicCallback(event))
    }

    fun getBillById(id: Long, event: ((CommonResult<Bill>?) -> Unit)? = null) {
        billService.getBillById(id).enqueue(basicCallback(event))
    }

    fun getBillsByBookId(bookId: Long, event: ((CommonResult<List<Bill>>?) -> Unit)? = null) {
        billService.getBillsByBookId(bookId).enqueue(basicCallback(event))
    }

    fun getMonthBillsByBookId(bookId: Long, month: Int, event: ((CommonResult<List<Bill>>?) -> Unit)? = null) {
        billService.getMonthBillsByBookId(bookId, month).enqueue(basicCallback(event))
    }

    fun getBillsByAccountId(accountId: Long, event: ((CommonResult<List<Bill>>?) -> Unit)? = null) {
        billService.getBillsByAccountId(accountId).enqueue(basicCallback(event))
    }
}