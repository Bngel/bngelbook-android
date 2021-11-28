package cn.bngel.bngelbook.network

import cn.bngel.bngelbook.dao.BasicDao
import cn.bngel.bngelbook.dao.FriendDao
import cn.bngel.bngelbook.data.CommonResult
import cn.bngel.bngelbook.data.friendDao.Friend

object FriendApi: BaseApi() {

    private val friendService by lazy {
        BasicDao.create<FriendDao>(BasicDao.FRIEND_URL)
    }

    fun getFriendById(id: Long, event: ((CommonResult<Friend>?) -> Unit)? = null) {
        friendService.getFriendById(id).enqueue(basicCallback(event))
    }

    fun postFriend(friend: Friend, event: ((CommonResult<Boolean>?) -> Unit)? = null) {
        friendService.addFriend(friend).enqueue(basicCallback(event))
    }

    fun updateFriendById(friend: Friend, event: ((CommonResult<Boolean>?) -> Unit)? = null) {
        friendService.updateFriendById(friend).enqueue(basicCallback(event))
    }

    fun deleteFriendByUserId(userId: Long, event: ((CommonResult<Boolean>?) -> Unit)? = null) {
        friendService.deleteFriendByUserId(userId).enqueue(basicCallback(event))
    }

    fun deleteFriendById(id: Long, event: ((CommonResult<Boolean>?) -> Unit)? = null) {
        friendService.deleteFriendById(id).enqueue(basicCallback(event))
    }

    fun getFriendsByUserId(userId: Long, event: ((CommonResult<List<Friend>>?) -> Unit)? = null) {
        friendService.getFriendsByUserId(userId).enqueue(basicCallback(event))
    }
}