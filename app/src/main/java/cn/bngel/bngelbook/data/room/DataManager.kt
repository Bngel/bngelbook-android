package cn.bngel.bngelbook.data.room

import androidx.room.Room
import cn.bngel.bngelbook.activity.ActivityManager
import cn.bngel.bngelbook.activity.BaseActivity
import cn.bngel.bngelbook.data.room.database.*

object DataManager {

    private var userDB: UserDatabase? = null
    private var accountDB: AccountDatabase? = null
    private var billDB: BillDatabase? = null
    private var bookDB: BookDatabase? = null
    private var friendDB: FriendDatabase? = null

    fun getUserDB(): UserDatabase {
        if (userDB == null) {
            userDB = Room.databaseBuilder(ActivityManager.getCurActivity() as BaseActivity,
                        UserDatabase::class.java, "userDB").build()
        }
        return userDB!!
    }

    fun getAccountDB(): AccountDatabase {
        if (accountDB == null) {
            accountDB = Room.databaseBuilder(ActivityManager.getCurActivity() as BaseActivity,
                            AccountDatabase::class.java, "accountDB").build()
        }
        return accountDB!!
    }

    fun getBillDB(): BillDatabase {
        if (billDB == null) {
            billDB = Room.databaseBuilder(ActivityManager.getCurActivity() as BaseActivity,
                        BillDatabase::class.java, "billDB").build()
        }
        return billDB!!
    }

    fun getBookDB(): BookDatabase {
        if (bookDB == null) {
            bookDB = Room.databaseBuilder(ActivityManager.getCurActivity() as BaseActivity,
                        BookDatabase::class.java, "bookDB").build()
        }
        return bookDB!!
    }

    fun getFriendDB(): FriendDatabase {
        if (friendDB == null) {
            friendDB = Room.databaseBuilder(ActivityManager.getCurActivity() as BaseActivity,
                        FriendDatabase::class.java, "friendDB").build()
        }
        return friendDB!!
    }
}