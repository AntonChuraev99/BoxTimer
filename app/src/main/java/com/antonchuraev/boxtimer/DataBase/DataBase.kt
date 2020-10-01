package com.antonchuraev.boxtimer.DataBase


import androidx.room.Database
import androidx.room.RoomDatabase
import com.antonchuraev.boxtimer.Model.TrainingProgramModel

@Database(entities = arrayOf(TrainingProgramModel::class), version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getDAO(): TrainingProgramDAO
}