package cn.bngel.bngelbook.data.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import cn.bngel.bngelbook.data.bean.Friend


/**
 * @author: bngel
 * @date: 22.1.11
 * @description:
 */

@Dao
interface FriendDao {

    @Insert
    fun insertFriend(friend: Friend): Int

    @Delete
    fun deleteFriend(friend: Friend): Int

    @Query("SELECT * FROM friend WHERE user1_id = :userId OR user2_id = :userId")
    fun getUserFriends(userId: Long): List<Friend>
}