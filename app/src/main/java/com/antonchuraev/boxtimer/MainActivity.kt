package com.antonchuraev.boxtimer



import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.room.Room
import com.antonchuraev.boxtimer.DataBase.AppDatabase
import com.antonchuraev.boxtimer.Fragments.TrainingFragment
import com.antonchuraev.boxtimer.Model.TrainingProgramModel


class MainActivity : AppCompatActivity() , DialogInterface.OnClickListener {
    val TAG = "BoxTimer"

    lateinit var spinner: Spinner
    lateinit var adapter: ArrayAdapter<TrainingProgramModel>

    lateinit var changeName: ImageButton
    lateinit var addNewProgram: ImageButton

    lateinit var db: AppDatabase

    lateinit var editName: EditText
    lateinit var selectedModel:TrainingProgramModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db =
                Room.databaseBuilder(
                        applicationContext,
                        AppDatabase::class.java, "trainingProgramModel"
                ).allowMainThreadQueries().build()


        if (db.getDAO().getAll().isEmpty()) {
            Log.i("BoxTime", "Data Base is empty, add 2 standard programs")
            val firstProgram = TrainingProgramModel("Тренировка", 150, 90, 15, 5)
            val secProgram = TrainingProgramModel("Бой", 180, 60, 20, 12)

            db.getDAO().insertAll(firstProgram,secProgram)

        }

        selectedModel = db.getDAO().getAll()[0]

        var fr: Fragment? = supportFragmentManager.findFragmentById(R.id.fragment_container)

        if (fr == null) {
            createAndShowFragmentFromModel(db.getDAO().getAll()[0]) //TODO getLast
        }

        val toolBar: Toolbar = findViewById(R.id.toolbar)
        toolBar.title = ""
        setSupportActionBar(toolBar)

        spinner = findViewById(R.id.spinner)

        adapter = ArrayAdapter(applicationContext, R.layout.simple_spinner_item, db.getDAO().getAll())
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Log.i(TAG, "Pressed spinner item on position $position")
                selectedModel=db.getDAO().getAll()[position]
                createAndShowFragmentFromModel(selectedModel)

            }

        }

        changeName = findViewById(R.id.change_name_button)
        changeName.setOnClickListener {
            Log.i(TAG, "Pressed changeName")
            createAndShowDialog()
        }


        addNewProgram = findViewById(R.id.add_new_program_button)
        addNewProgram.setOnClickListener {
            if (db.getDAO().getAll().size>9){
                Toast.makeText(this, "Уже создано достаточно программ, измените или удалите старые" , Toast.LENGTH_LONG).show()
            }
            else{
                val newProgram = TrainingProgramModel("Новая программа" , 120, 90 , 15 ,5)
                db.getDAO().insertAll(newProgram)
                updateSpinner()
                createAndShowFragmentFromModel(newProgram)
                spinner.setSelection(db.getDAO().getAll().size-1)
            }


        }

    }

    private fun createAndShowDialog() {
        val adb: AlertDialog.Builder? = AlertDialog.Builder(this)
                .setView(R.layout.change_name_dialog)
                .setTitle("Изменить программу")
                .setPositiveButton("Ок", this )
                .setNegativeButton("Назад", this )
                .setNeutralButton("Удалить", this ) //TODO

        adb!!.create()

        val dialog:AlertDialog  = adb.show();
        editName = dialog.findViewById(R.id.change_name_edit_text)!!
        editName?.setText(selectedModel.name)
    }

    override fun onClick(dialog: DialogInterface?, which: Int) {

        when (which){
            Dialog.BUTTON_POSITIVE-> {
                Log.i(TAG , "BUTTON_POSITIVE Pressed")
                selectedModel.name = editName.text.toString()
                db.getDAO().updateDataBaseObject(selectedModel)

                //TODO REFACTOR
                updateSpinner()
            }


            Dialog.BUTTON_NEGATIVE->Log.i(TAG , "BUTTON_NEGATIVE Pressed")
            Dialog.BUTTON_NEUTRAL->{
                Log.i(TAG , "BUTTON Delete Pressed")

                if (db.getDAO().getAll().size==1){
                    Log.i(TAG , "Удаление единственного элемента невозможно")
                    Toast.makeText(this , "Удаление единственного элемента невозможно" , Toast.LENGTH_LONG).show()
                }
                else{
                    Log.i(TAG , "Удаление элемента")
                    db.getDAO().delete(selectedModel)

                    createAndShowFragmentFromModel(db.getDAO().getAll()[0])

                    updateSpinner()
                }

            }
        }
    }

    private fun createAndShowFragmentFromModel(modelToShow:TrainingProgramModel) {

        val newFragment = TrainingFragment.newInstance(modelToShow)
        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()

        fragmentTransaction.replace(R.id.fragment_container, newFragment, TAG)
        fragmentTransaction.commit()

    }

    private fun updateSpinner() {
        adapter.clear()
        adapter.addAll(db.getDAO().getAll())
        adapter.notifyDataSetChanged()
    }


}