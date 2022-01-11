package cn.bngel.bngelbook.data.room.api

import cn.bngel.bngelbook.data.bean.Friend
import cn.bngel.bngelbook.data.room.DataManager


/**
 * @author: bngel
 * @date: 22.1.11
 * @description:
 */

object FriendLiteApi {

    private val friendDB by lazy {
        DataManager.getFriendDB().friendDao()
    }

    fun insertFriend(friend: Friend) = friendDB.insertFriend(friend)

    fun deleteFriend(friend: Friend) = friendDB.deleteFriend(friend)

    fun getUserFriends(userId: Long) = friendDB.getUserFriends(userId)
}