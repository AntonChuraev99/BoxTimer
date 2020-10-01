package com.antonchuraev.boxtimer.Presenter


import com.antonchuraev.boxtimer.Model.TrainingProgramModel


open class Presenter(v:ViewInterface?,model:TrainingProgramModel):PresenterInterface{

    var view = v
    var model = model

    override fun increaseLapDuration() {
        model.increaseLapDuration()
        view?.updateLapDuration(model.lapDuration)
        changeFullTime()
    }

    override fun reduceLapDuration() {
        model.reduceLapDuration()
        view?.updateLapDuration(model.lapDuration)
        changeFullTime()
    }

    override fun increaseRestDuration() {
        model.increaseRestDuration()
        view?.updateRestDuration(model.restDuration)
        changeFullTime()
    }

    override fun reduceRestDuration() {
        model.reduceRestDuration()
        view?.updateRestDuration(model.restDuration)
        changeFullTime()
    }

    override fun increaseDelayBeforeFirstLap() {
        model.increaseDelayBeforeFirstLap()
        view?.updateDelayBeforeFirstLap(model.delayBeforeFirstLap)
        changeFullTime()
    }

    override fun reduceDelayBeforeFirstLap() {
        model.reduceDelayBeforeFirstLap()
        view?.updateDelayBeforeFirstLap(model.delayBeforeFirstLap)
        changeFullTime()
    }

    override fun increaseLapCount() {
        model.increaseLapCount()
        view?.updateLapCount(model.lapCount)
        changeFullTime()
    }

    override fun reduceLapCount() {
        model.reduceLapCount()
        view?.updateLapCount(model.lapCount)
        changeFullTime()
    }

    override fun changeFullTime() {
        model.changeFullTime()
        view?.updateFullTime(model.fullTime)
    }


    override fun onDetach() {
        view=null
    }

}

interface ViewInterface{
    fun updateLapDuration(int: Int)
    fun updateRestDuration(int: Int)
    fun updateDelayBeforeFirstLap(int: Int)
    fun updateLapCount(int: Int)
    fun updateFullTime(int: Int)
}

interface PresenterInterface{

    fun increaseLapDuration()
    fun reduceLapDuration()

    fun increaseRestDuration()
    fun reduceRestDuration()

    fun increaseDelayBeforeFirstLap()
    fun reduceDelayBeforeFirstLap()

    fun increaseLapCount()
    fun reduceLapCount()

    fun changeFullTime()

    fun onDetach()
}