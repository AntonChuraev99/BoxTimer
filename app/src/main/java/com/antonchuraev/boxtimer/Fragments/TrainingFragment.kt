package com.antonchuraev.boxtimer.Fragments


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.room.Database
import androidx.room.Room
import com.antonchuraev.boxtimer.DataBase.AppDatabase
import com.antonchuraev.boxtimer.Model.TrainingProgramModel
import com.antonchuraev.boxtimer.Presenter.Presenter
import com.antonchuraev.boxtimer.Presenter.PresenterInterface
import com.antonchuraev.boxtimer.Presenter.ViewInterface
import com.antonchuraev.boxtimer.R


class TrainingFragment : Fragment() , ViewInterface{
    val TAG="BoxTimer"
    lateinit var db:AppDatabase

    companion object {
        fun newInstance(trainingProgramModel: TrainingProgramModel) : TrainingFragment {
            val trainingFragment: TrainingFragment = TrainingFragment()
            trainingFragment.viewModel=trainingProgramModel
            return  trainingFragment
        }
    }

    lateinit var presenter: PresenterInterface
    private lateinit var viewModel: TrainingProgramModel

    lateinit var lapDurationTime:TextView
    lateinit var increaseLapDurationButton: ImageButton
    lateinit var reduceLapDurationButton: ImageButton

    lateinit var restDurationTime: TextView
    lateinit var increaseRestDurationButton: ImageButton
    lateinit var reduceRestDurationButton: ImageButton

    lateinit var delayBeforeFirstLapTime: TextView
    lateinit var increaseDelayBeforeFirstLapButton: ImageButton
    lateinit var reduceDelayBeforeFirstLapButton: ImageButton

    lateinit var lapCount: TextView
    lateinit var increaseLapCountButton: ImageButton
    lateinit var reduceLapCountButton: ImageButton

    lateinit var fullTime:TextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v:View = inflater.inflate(R.layout.training_program_fragment, container, false)
        presenter = Presenter(this, viewModel)

        lapDurationTime=v.findViewById(R.id.round_duration)
        lapDurationTime.text= convertTimeToString(viewModel.lapDuration)
        increaseLapDurationButton=v.findViewById(R.id.button_add_lap_duration)
        increaseLapDurationButton.setOnClickListener { presenter.increaseLapDuration() }
        reduceLapDurationButton=v.findViewById(R.id.button_minus_lap_duration)
        reduceLapDurationButton.setOnClickListener { presenter.reduceLapDuration() }

        restDurationTime=v.findViewById(R.id.rest_duration)
        restDurationTime.text = convertTimeToString(viewModel.restDuration)
        increaseRestDurationButton = v.findViewById(R.id.button_add_rest_duration)
        increaseRestDurationButton.setOnClickListener { presenter.increaseRestDuration() }
        reduceRestDurationButton = v.findViewById(R.id.button_minus_rest_duration)
        reduceRestDurationButton.setOnClickListener{ presenter.reduceRestDuration()}

        delayBeforeFirstLapTime = v.findViewById(R.id.delay_before_first_lap)
        delayBeforeFirstLapTime.text = convertTimeToString(viewModel.delayBeforeFirstLap)
        increaseDelayBeforeFirstLapButton = v.findViewById(R.id.button_add_first_lap_delay)
        increaseDelayBeforeFirstLapButton.setOnClickListener { presenter.increaseDelayBeforeFirstLap() }
        reduceDelayBeforeFirstLapButton = v.findViewById(R.id.button_minus_first_lap_delay)
        reduceDelayBeforeFirstLapButton.setOnClickListener { presenter.reduceDelayBeforeFirstLap() }

        lapCount = v.findViewById(R.id.lap_count)
        lapCount.text = viewModel.lapCount.toString()
        increaseLapCountButton = v.findViewById(R.id.button_add_lap_count)
        increaseLapCountButton.setOnClickListener { presenter.increaseLapCount() }
        reduceLapCountButton = v.findViewById(R.id.button_minus_lap_count)
        reduceLapCountButton.setOnClickListener { presenter.reduceLapCount() }

        fullTime = v.findViewById(R.id.full_time_text)
        fullTime.text = convertTimeToString(viewModel.fullTime)

        db =
                Room.databaseBuilder(
                        (activity as AppCompatActivity).applicationContext,
                        AppDatabase::class.java, "trainingProgramModel"
                ).allowMainThreadQueries().build()

        return v
    }



    override fun updateLapDuration(int: Int) {
        lapDurationTime.text=convertTimeToString(int)
        updateDataBase()
    }

    override fun updateRestDuration(int: Int) {
        restDurationTime.text = convertTimeToString(int)
        updateDataBase()
    }

    override fun updateDelayBeforeFirstLap(int: Int) {
        delayBeforeFirstLapTime.text = convertTimeToString(int)
        updateDataBase()
    }

    override fun updateLapCount(int: Int) {
        lapCount.text = int.toString()
        updateDataBase()
    }

    override fun updateFullTime(int: Int) {
        fullTime.text = convertTimeToString(int)
        updateDataBase()
    }

    override fun onDestroyView() {
        Log.i(TAG , "onDestroyView")
        super.onDestroyView()
        presenter.onDetach()
    }

    private fun convertTimeToString(int: Int): String? {
        val str:String = "${if (int<600) "0${int/60}" else "${int/60}"}:${if (int%60<10) "0${int%60}" else "${int%60}"}"
        return str
    }

    private fun updateDataBase() {
        Log.i(TAG , " DataBase updated")
        db.getDAO().updateDataBaseObject(viewModel)
    }

}


