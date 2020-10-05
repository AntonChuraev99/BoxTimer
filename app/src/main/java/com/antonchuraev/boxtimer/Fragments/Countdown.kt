package com.antonchuraev.boxtimer.Fragments

import android.media.AudioManager
import android.media.SoundPool
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.antonchuraev.boxtimer.Model.TrainingProgramModel
import com.antonchuraev.boxtimer.R
import com.filippudak.ProgressPieView.ProgressPieView


class Countdown : AppCompatActivity() {
    val TAG="Countdown"

    lateinit var trainingData:TrainingProgramModel

    lateinit var roundN:TextView
    lateinit var title:TextView
    lateinit var fullTime:TextView
    lateinit var progressBar: ProgressPieView

    lateinit var timer:TextView

    lateinit var pauseTimer: ImageButton
    lateinit var goButton:ImageButton
    lateinit var breakButton:ImageButton

    private val countDownInterval:Long=1*1000

    lateinit var delayBeforeFirstTimer: CountDownTimer
    lateinit var restTimer: CountDownTimer
    lateinit var fightTimer: CountDownTimer
    lateinit var fullTimeTimer: CountDownTimer
    lateinit var currentTimer: CountDownTimer

    lateinit var newTimer: CountDownTimer
    lateinit var newFullTimer: CountDownTimer


    var millisUntilFinish:Long=0
    var millisUntilFinishInFullTime:Long=0

    var iForTimers:Int=1
    var lapCount=0
    var stopped=false
    var newTimersInitialize=false

    lateinit var  mSoundPool: SoundPool

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.countdown_fragment)

        trainingData = intent.getSerializableExtra("ViewModel") as TrainingProgramModel
        lapCount=trainingData.lapCount

        roundN = findViewById(R.id.round_n)
        roundN.text ="${resources.getString(R.string.Round)} $iForTimers/$lapCount"

        title = findViewById(R.id.title)

        fullTime = findViewById(R.id.full_time_text)
        fullTime.text = convertTimeToString(trainingData.fullTime)

        progressBar = findViewById(R.id.progressBar)
        progressBar.progress = 0

        timer = findViewById(R.id.timer)
        timer.text = convertTimeToString(trainingData.delayBeforeFirstLap)


        initializeTimers()
        if (trainingData.delayBeforeFirstLap==0){
            title.text = resources.getString(R.string.fight)
            fightTimer.start()
            progressBar.max = trainingData.lapDuration

            currentTimer=fightTimer

        }
        else{
            title.text = resources.getString(R.string.Preparation)
            delayBeforeFirstTimer.start()
            progressBar.max = trainingData.delayBeforeFirstLap

            currentTimer = delayBeforeFirstTimer
        }
        fullTimeTimer.start()

        pauseTimer = findViewById(R.id.pause_timer)
        pauseTimer.setOnClickListener {
            finishAllTimers()
            onPauseClick()
            stopped=true
        }

        goButton= findViewById(R.id.go_button)
        goButton.setOnClickListener {
            resumeTimer()
            onGoClick()
        }

        breakButton= findViewById(R.id.stop_button)
        breakButton.setOnClickListener {
            finishAllTimers()
            finish()
        }

        mSoundPool= SoundPool.Builder().build()
        mSoundPool.load(this,   R.raw.sound , 1);

    }

    private fun onGoClick() {
        pauseTimer.visibility = View.VISIBLE
        goButton.visibility = View.GONE
        breakButton.visibility = View.GONE
    }

    private fun resumeTimer() {

        newTimer=object:CountDownTimer(millisUntilFinish, countDownInterval){
            override fun onTick(millisUntilFinished: Long) {
                timer.text = convertTimeToString((millisUntilFinished / 1000).toInt())

                millisUntilFinish=millisUntilFinished

                if (stopped){
                    stopped=false
                }
                else{
                    progressBar.progress++
                }
                if (millisUntilFinished/1000==1L){
                    playSound()
                }
            }

            override fun onFinish() {
                Log.i(TAG, "onFinish newTimer")
                stopAllSound()
                //TODO bad look
                when(title.text) {
                    (resources.getString(R.string.Preparation)) -> {
                        title.text = (resources.getString(R.string.fight))
                        fightTimer.start()

                        progressBar.progress = 0
                        progressBar.max = trainingData.lapDuration

                        currentTimer = fightTimer
                    }
                    (resources.getString(R.string.fight)) -> {
                        title.text = (resources.getString(R.string.Rest))
                        restTimer.start()

                        progressBar.progress = 0
                        progressBar.max = trainingData.restDuration

                        currentTimer = restTimer
                    }
                    (resources.getString(R.string.Rest)) -> {

                        iForTimers++

                        if (iForTimers <= lapCount) {
                            title.text  = (resources.getString(R.string.fight))
                            roundN.text = "${resources.getString(R.string.Round)} $iForTimers/$lapCount"
                            fightTimer.start()

                            progressBar.progress = 0
                            progressBar.max = trainingData.lapDuration

                            currentTimer = fightTimer
                        } else {
                            //FINISH
                            Log.i(TAG, "training end")
                            finishAllTimers()
                            finish()
                        }

                    }
                }

            }
        }
        newTimer.start()
        currentTimer=newTimer;

        newFullTimer=object:CountDownTimer(millisUntilFinishInFullTime, countDownInterval){
            override fun onTick(millisUntilFinished: Long) {
                fullTime.text = convertTimeToString((millisUntilFinished / 1000).toInt())
                millisUntilFinishInFullTime=millisUntilFinished
            }

            override fun onFinish() {
                Log.i(TAG, "onFinish newFullTimer")


            }
        }
        newFullTimer.start()

        newTimersInitialize=true
    }

    private fun onPauseClick() {
        pauseTimer.visibility = View.GONE
        goButton.visibility = View.VISIBLE
        breakButton.visibility = View.VISIBLE

    }

    private fun initializeTimers() {
        restTimer=object:CountDownTimer(trainingData.restDuration * 1000.toLong(), countDownInterval){
            override fun onTick(millisUntilFinished: Long) {
                timer.text = convertTimeToString((millisUntilFinished / 1000).toInt())
                millisUntilFinish=millisUntilFinished
                progressBar.progress++

                if (millisUntilFinished/1000==1L){
                    playSound()
                }

            }

            override fun onFinish() {
                Log.i(TAG, "onFinish restTimer")

                iForTimers++

                stopAllSound()

                if (iForTimers<=lapCount){
                    title.text = resources.getString(R.string.fight)
                    roundN.text = "${resources.getString(R.string.Round)} $iForTimers/$lapCount"
                    fightTimer.start()

                    progressBar.progress = 0
                    progressBar.max = trainingData.lapDuration

                    currentTimer=fightTimer
                }else{
                    //FINISH
                    Log.i(TAG, "training end")
                    finishAllTimers()
                    finish()
                }

            }
        }

        fightTimer=object:CountDownTimer(trainingData.lapDuration * 1000.toLong(), countDownInterval){
            override fun onTick(millisUntilFinished: Long) {
                timer.text = convertTimeToString((millisUntilFinished / 1000).toInt())
                millisUntilFinish=millisUntilFinished
                progressBar.progress++

                if (millisUntilFinished/1000==1L){
                    playSound()
                }
            }

            override fun onFinish() {
                Log.i(TAG, "onFinish mainTimer")
                title.text = resources.getString(R.string.Rest)
                restTimer.start()

                progressBar.progress = 0
                progressBar.max = trainingData.restDuration

                currentTimer=restTimer

                stopAllSound()
            }
        }

        delayBeforeFirstTimer = object:CountDownTimer(trainingData.delayBeforeFirstLap * 1000.toLong(), countDownInterval){
            override fun onTick(millisUntilFinished: Long) {
                timer.text = convertTimeToString((millisUntilFinished / 1000).toInt())

                millisUntilFinish=millisUntilFinished

                progressBar.progress++

                if (millisUntilFinished/1000==1L){
                    playSound()
                }
            }

            override fun onFinish() {
                Log.i(TAG, "onFinish delayBeforeFirstTimer")
                title.text = resources.getString(R.string.fight)
                fightTimer.start()

                progressBar.progress = 0
                progressBar.max = trainingData.lapDuration

                currentTimer=fightTimer

                stopAllSound()

            }
        }

        fullTimeTimer = object:CountDownTimer(trainingData.fullTime * 1000.toLong(), countDownInterval){
            override fun onTick(millisUntilFinished: Long) {
                fullTime.text = convertTimeToString((millisUntilFinished / 1000).toInt())
                millisUntilFinishInFullTime=millisUntilFinished


            }

            override fun onFinish() {
                Log.i(TAG, "onFinish fullTimer")

            }
        }

    }

    private fun finishAllTimers(){
        currentTimer.cancel()
        delayBeforeFirstTimer.cancel()
        restTimer.cancel()
        fightTimer.cancel()

        fullTimeTimer.cancel()

        if (newTimersInitialize){
            newTimer.cancel()
            newFullTimer.cancel()
        }

    }

    private fun convertTimeToString(int: Int): String? {
        val str:String = "${if (int<600) "0${int/60}" else "${int/60}"}:${if (int%60<10) "0${int%60}" else "${int%60}"}"
        return str
    }


    private fun playSound(){
        mSoundPool.play(1 , 1f, 1f , 0 , 0 ,1f)
    }
    private fun stopAllSound(){
        mSoundPool.autoPause()
    }

}

