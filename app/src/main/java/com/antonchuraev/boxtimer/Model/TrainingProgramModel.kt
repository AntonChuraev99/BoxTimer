package com.antonchuraev.boxtimer.Model


import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
class TrainingProgramModel (
    name: String,
    lapDuration: Int,
    restDuration: Int,
    delayBeforeFirstLap: Int,
    lapCount: Int
): Serializable{
    @PrimaryKey(autoGenerate = true)
    var id:Int=0




    var name = name

    var lapDuration = lapDuration
    fun increaseLapDuration(){
        lapDuration+=5
    }
    fun reduceLapDuration(){
        if (lapDuration==5) {
            return
        }
        lapDuration-=5
    }


    var restDuration = restDuration
    fun increaseRestDuration(){
        restDuration+=5
    }
    fun reduceRestDuration(){
        if (restDuration==5) {
            return
        }
        restDuration-=5
    }


    var delayBeforeFirstLap = delayBeforeFirstLap
    fun increaseDelayBeforeFirstLap(){
        delayBeforeFirstLap+=5
    }
    fun reduceDelayBeforeFirstLap(){
        if (delayBeforeFirstLap==0) {
            return
        }
        delayBeforeFirstLap-=5
    }


    var lapCount = lapCount
    fun increaseLapCount(){
        lapCount++
    }
    fun reduceLapCount(){
        if (lapCount==1) {
            return
        }
        lapCount--
    }

    var fullTime = (lapDuration+restDuration)*lapCount+delayBeforeFirstLap
    fun changeFullTime(){
        fullTime = (lapDuration+restDuration)*lapCount+delayBeforeFirstLap
    }

    override fun toString(): String {
        return name
    }


}

