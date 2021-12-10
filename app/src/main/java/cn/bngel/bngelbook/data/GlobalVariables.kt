package cn.bngel.bngelbook.data

import cn.bngel.bngelbook.data.bean.Book
import cn.bngel.bngelbook.data.bean.User

object GlobalVariables {

    var USER: User? = null
    var BOOK: Book? = null

    private val tagsMap = mapOf(
        "吃喝" to listOf("早餐","午餐","晚餐","超市"),
        "交通" to listOf("车费","公交","单车月票"),
        "服饰" to listOf("上衣","裤子","袜子","内衣"),
        "日用品" to listOf("牙膏","纸巾","抽纸","超市"),
        "娱乐" to listOf("月卡","氪金","明日方舟"),
        "医疗" to listOf("药费","挂号"),
        "其他" to listOf("杂货")
    )

    fun getTagsByType(type: String) = tagsMap[type]?: listOf("杂货")

    private val outTypesList = listOf("吃喝","交通","服饰","日用品","娱乐","医疗","其他")
    private val inTypesList = listOf("工资")

    fun getDefaultTypes(io: Int) = if (io == 1) inTypesList else outTypesList
}