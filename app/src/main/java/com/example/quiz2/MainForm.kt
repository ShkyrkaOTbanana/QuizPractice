package com.example.quiz2

import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.HapticFeedbackConstants
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView

class MainForm : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private var isAdmin: Boolean = false

    private lateinit var hideNewQuiz: ImageView
    private lateinit var hideEditQuiz: ImageView
    private lateinit var hideDeleteQuiz:ImageView



    var previouslySelectedRadioButtonId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_form)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        isAdmin = intent.getBooleanExtra("admin", false)

        hideNewQuiz = findViewById(R.id.new_quiz)
        hideEditQuiz = findViewById(R.id.editQuiz)
        hideDeleteQuiz = findViewById(R.id.deleteQuiz)


        val names = loadData()

        for(qname in names)
            println(qname)


        val quizList = findViewById<RadioGroup>(R.id.quizNamesRadio)
        quizList.gravity = Gravity.CENTER
        for(quiz in names){
            val button = RadioButton(this)

            button.setPadding(10, 20, 10, 20)
            val params = RadioGroup.LayoutParams(this, null)
            params.setMargins(0, 20, 0, 20)
            params.width = RadioGroup.LayoutParams.MATCH_PARENT
            params.height = RadioGroup.LayoutParams.MATCH_PARENT
            button.layoutParams = params

            button.text = quiz
            button.textSize = 24F
            button.setTextColor(Color.WHITE)
            button.typeface = Typeface.DEFAULT_BOLD
            button.setBackgroundResource(R.drawable.spinner_bg)
            button.gravity = Gravity.CENTER

            button.buttonDrawable = null

            quizList.addView(button)
        }

        quizList.setOnCheckedChangeListener { radioGroup, i ->
            radioGroup.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
            var selectedQuiz = quizList.checkedRadioButtonId
            println(selectedQuiz)
            var radioButton = findViewById<RadioButton>(selectedQuiz)
            if(selectedQuiz != -1) {
                radioButton.background = ContextCompat.getDrawable(this, R.drawable.admin_log_design)
                if(previouslySelectedRadioButtonId != -1 && previouslySelectedRadioButtonId != selectedQuiz){
                    var previouslySelectedRadioButton = findViewById<RadioButton>(previouslySelectedRadioButtonId)
                    previouslySelectedRadioButton.background = ContextCompat.getDrawable(this, R.drawable.spinner_bg)
                }
                previouslySelectedRadioButtonId = selectedQuiz
            }
        }

        val btnBack: ImageView = findViewById(R.id.back_imageView)

        btnBack.setOnClickListener(){
            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
        }

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }

        val supportShow = findViewById<ImageView>(R.id.show_dialoge)
        supportShow.setOnClickListener{
            val supportDialoge = layoutInflater.inflate(R.layout.support_dialoge, null)
            val myDialog = Dialog(this)
            myDialog.setContentView(supportDialoge)
            myDialog.setCancelable(true)
            myDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            myDialog.show()
            val btnClose = supportDialoge.findViewById<Button>(R.id.ok_back)
            btnClose.setOnClickListener{
                myDialog.dismiss()
            }
        }

        val spinner = findViewById<Spinner>(R.id.theme_spinner)
        val arrayAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.Themes,
            android.R.layout.simple_spinner_item
        )
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
        spinner.adapter = arrayAdapter
        spinner.onItemSelectedListener = this


        val doQuiz = findViewById<Button>(R.id.doQuiz)
        val newQuiz = findViewById<ImageView>(R.id.new_quiz)
        val editQuiz = findViewById<ImageView>(R.id.editQuiz)
        val deleteQuiz = findViewById<ImageView>(R.id.deleteQuiz)

        doQuiz.setOnClickListener{ view ->
            var selectedQuiz = quizList.checkedRadioButtonId
            println(selectedQuiz)
            var radioButton = findViewById<RadioButton>(selectedQuiz)
            if(selectedQuiz != -1){
                view.performHapticFeedback(HapticFeedbackConstants.CONFIRM)
                var quizName = radioButton.text
                println(quizName)
                val intent = Intent(this, DoQuiz::class.java)
                intent.putExtra("Quiz Name", quizName)
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
            }
            else{
                view.performHapticFeedback(HapticFeedbackConstants.REJECT)
                val toast = Toast.makeText(this, "Выберите викторину", Toast.LENGTH_LONG)
                toast.show()
            }
        }

        newQuiz.setOnClickListener{ view ->
            view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
            val intent = Intent(this, NewQuiz::class.java)
            intent.putStringArrayListExtra("Current Quiz Names", names)
            startActivity(intent)
        }

        editQuiz.setOnClickListener{ view ->
            var selectedQuiz = quizList.checkedRadioButtonId
            println(selectedQuiz)
            var radioButton = findViewById<RadioButton>(selectedQuiz)
            if(selectedQuiz != -1){
                view.performHapticFeedback(HapticFeedbackConstants.CONFIRM)
                var quizName = radioButton.text
                println(quizName)
                val intent = Intent(this, AddOrEditQuiz::class.java)
                intent.putExtra("Quiz Name", quizName)
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
            }
            else{
                view.performHapticFeedback(HapticFeedbackConstants.REJECT)
                val toast = Toast.makeText(this, "Выберите викторину", Toast.LENGTH_LONG)
                toast.show()
            }
        }

        deleteQuiz.setOnClickListener{ view ->
            var selectedQuiz = quizList.checkedRadioButtonId
            println(selectedQuiz)
            var radioButton = findViewById<RadioButton>(selectedQuiz)
            if(selectedQuiz != -1){
                view.performHapticFeedback(HapticFeedbackConstants.CONFIRM)
                var quizName = radioButton.text
                println(quizName)
                val db = DBhelper(this, null)
                db.deleteQuiz(quizName as String)

                recreate()
            }
            else{
                view.performHapticFeedback(HapticFeedbackConstants.REJECT)
                val toast = Toast.makeText(this, "Выберите викторину", Toast.LENGTH_LONG)
                toast.show()
            }
        }

        val privacypolicy = findViewById<TextView>(R.id.tg_button)
        privacypolicy.setOnClickListener{ view ->
            view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
            val intent = Intent(android.content.Intent.ACTION_VIEW)
            intent.data = Uri.parse("t.me/baz00kamew1ng")
            startActivity(intent)
        }

        updateUI()

    }

    private fun updateUI(){
        if(isAdmin){
            hideNewQuiz.visibility = View.GONE
            hideEditQuiz.visibility = View.GONE
            hideDeleteQuiz.visibility = View.GONE
        } else{
            hideNewQuiz.visibility = View.VISIBLE
            hideEditQuiz.visibility = View.VISIBLE
            hideDeleteQuiz.visibility = View.VISIBLE
        }
    }

    override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val text = adapterView?.getItemAtPosition(position).toString()
    }

    override fun onNothingSelected(adapterView: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    @SuppressLint("Range")
    fun loadData(): ArrayList<String> {
        val result = ArrayList<String>()
        val dbHelper = DBhelper(this, null)
        val cursor = dbHelper.getQuizNames()
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val quizName = cursor.getString(cursor.getColumnIndex(DBhelper.NAME_COl))
                result.add(quizName)
            }
        }
        cursor?.close()
        return result
    }

}