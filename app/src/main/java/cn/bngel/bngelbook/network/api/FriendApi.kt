package cn.bngel.bngelbook.network.api

import cn.bngel.bngelbook.dao.BasicDao
import cn.bngel.bngelbook.dao.FriendDao
import cn.bngel.bngelbook.data.CommonResult
import cn.bngel.bngelbook.data.GlobalVariables
import cn.bngel.bngelbook.data.bean.Friend
import cn.bngel.bngelbook.utils.NetworkUtils

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

    fun postFriendByUserId(userId: Long, event: ((CommonResult<Boolean>?) -> Unit)? = null) {
        GlobalVariables.USER.value?.apply {
            friendService.addFriend(Friend(null, id, userId)).enqueue(basicCallback(event))
        }
    }

    fun updateFriendById(friend: Friend, event: ((CommonResult<Boolean>?) -> Unit)? = null) {
        friendService.updateFriendById(friend).enqueue(basicCallback(event))
    }

    fun deleteFriendByFriend(friend: Friend, event: ((CommonResult<Boolean>?) -> Unit)? = null) {
        friendService.deleteFriendByFriend(friend).enqueue(basicCallback(event))
    }

    fun deleteFriendByUserId(userId: Long, event: ((CommonResult<Boolean>?) -> Unit)? = null) {
        GlobalVariables.USER.value?.apply {
            friendService.deleteFriendByFriend(Friend(null, id, userId)).enqueue(basicCallback(event))
        }
    }

    fun deleteFriendById(id: Long, event: ((CommonResult<Boolean>?) -> Unit)? = null) {
        friendService.deleteFriendById(id).enqueue(basicCallback(event))
    }

    fun getFriendsByUserId(userId: Long, event: ((CommonResult<List<Friend>>?) -> Unit)? = null) {
        friendService.getFriendsByUserId(userId).enqueue(basicCallback(event))
    }

    fun judgeFriendExists(user1Id: Long, user2Id: Long, event: ((CommonResult<Boolean>?) -> Unit)? = null) {
        friendService.judgeFriendExists(user1Id, user2Id).enqueue(basicCallback(event))
    }
}