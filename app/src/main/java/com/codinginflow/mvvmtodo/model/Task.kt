package com.codinginflow.mvvmtodo.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.text.DateFormat

//TODO: estudar Parcelize e RoomDatabase

@Entity(tableName = "task_table")
@Parcelize
data class Task(
        val name: String,
        val priority: Boolean = false,
        val description: String = "",
        val completed: Boolean = false,
        val createdTime: Long = System.currentTimeMillis(),
        @PrimaryKey(autoGenerate = true) val id: Int = 0

) : Parcelable {

    val createdDateFormated: String
        get() = DateFormat.getDateTimeInstance().format(createdTime)

}