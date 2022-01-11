package cn.bngel.bngelbook.data.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import cn.bngel.bngelbook.data.bean.User


/**
 * @author: bngel
 * @date: 22.1.10
 * @description:
 */

@Dao
interface UserDao {

    @Insert
    fun insertUser(user: User): Int

    @Delete
    fun deleteUser(user: User): Int

    @Query("SELECT * FROM user WHERE id = :uid")
    fun getUserById(uid: Long): User

    @Query("SELECT * FROM user")
    fun getAllUsers(): List<User>

    @Query("SELECT * FROM user")
    fun getDefaultUser(): User

    @Query("DELETE FROM user")
    fun clearUsers(): Int
}