package com.gamesofni.memoriarty.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.gamesofni.memoriarty.user.User
import java.util.*


const val USER_TABLE = "user"

@Entity(tableName = USER_TABLE)
data class UserEntity (
    @PrimaryKey(autoGenerate = false)
    var username: String = "",

    var email: String = "",

    var tz: String = "",

    var unconfirmed: Boolean = false,

    @ColumnInfo(name = "user_created")
    var userCreated: Date = Date(),

)

fun UserEntity.asDomainModel(): User {
    return User(
        username = username,
        email = email,
        tz = tz,
        unconfirmed = unconfirmed,
        userCreated = userCreated,
    )
}
