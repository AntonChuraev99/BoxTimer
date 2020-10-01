package com.antonchuraev.boxtimer.DataBase


import androidx.room.*
import com.antonchuraev.boxtimer.Model.TrainingProgramModel

@Dao
interface TrainingProgramDAO {
    @Query("SELECT * FROM trainingProgramModel")
    fun getAll(): List<TrainingProgramModel>

    @Query("SELECT * FROM trainingProgramModel WHERE id LIKE :pName ")
    fun findByName(pName: String): TrainingProgramModel

    @Update
    fun updateDataBaseObject(program: TrainingProgramModel)

    @Insert
    fun insertAll(vararg programs: TrainingProgramModel)

    @Delete
    fun delete(program: TrainingProgramModel)

}