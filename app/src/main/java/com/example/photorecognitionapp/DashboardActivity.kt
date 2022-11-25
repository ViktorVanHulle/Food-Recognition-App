package com.example.photorecognitionapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.photorecognitionapp.firestore.CloudData
import com.example.photorecognitionapp.firestore.MealItem
import com.example.photorecognitionapp.firestore.getDate
import com.google.android.material.button.MaterialButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.DecimalFormat

class DashboardActivity : AppCompatActivity() {

    lateinit var fade_in : Animation
    lateinit var btn_camera:MaterialButton
    lateinit var dashboard_layout:ConstraintLayout
    lateinit var userId: String

    //firebase
    lateinit var auth: FirebaseAuth
    var cloudData = CloudData()
    private val db = Firebase.firestore
    private var list = arrayListOf<MealItem>()


    //hamburger menu
    lateinit var toggle:ActionBarDrawerToggle
    lateinit var drawerLayout: DrawerLayout
    lateinit var navView:NavigationView

    // Daily count fields
    lateinit var caloriesDaily: TextView
    lateinit var proteinDaily: TextView
    lateinit var fatDaily: TextView
    lateinit var carbsDaily: TextView

    //daily count progress fields
    lateinit var circularProgressIndicator: CircularProgressIndicator
    lateinit var linearProgressIndicator_protein: LinearProgressIndicator
    lateinit var linearProgressIndicator_fats: LinearProgressIndicator
    lateinit var linearProgressIndicator_carbs: LinearProgressIndicator

    //list of foods
    lateinit var rv_listOfFoods: RecyclerView

    lateinit var cal_dailyTotal: TextView
    lateinit var protein_dailyTotal: TextView
    lateinit var fats_dailyTotal: TextView
    lateinit var carbs_dailyTotal: TextView

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        auth = Firebase.auth
        // Get userId from intent
        userId = intent.getStringExtra("userId").toString()
        // Get daily intake from firebase and set textfields
        caloriesDaily = findViewById(R.id.calTot)
        proteinDaily = findViewById(R.id.proteinTot)
        fatDaily = findViewById(R.id.fatTot)
        carbsDaily = findViewById(R.id.carbsTot)
        //Get daily intake from firebase and set progress on dashboard
        circularProgressIndicator = findViewById(R.id.circularProgressIndicator)
        linearProgressIndicator_protein = findViewById(R.id.linearProgressIndicator)
        linearProgressIndicator_fats = findViewById(R.id.linearProgressIndicator2)
        linearProgressIndicator_carbs = findViewById(R.id.linearProgressIndicator3)
        //Set total daily intake textfields
        cal_dailyTotal = findViewById(R.id.cal_dailyTotal)
        protein_dailyTotal = findViewById(R.id.protein_dailyTotal)
        fats_dailyTotal = findViewById(R.id.fats_dailyTotal)
        carbs_dailyTotal = findViewById(R.id.carbs_dailyTotal)
        //set list of food by id
        rv_listOfFoods = findViewById(R.id.rv_listOfFoods)



        val df = DecimalFormat("#.##")
        db.collection(userId).get().addOnSuccessListener { documents ->
            var proteinTot = 0.0
            var caloriesTot = 0.0
            var fatTot = 0.0
            var carbsTot = 0.0
            var intakeQuan = 0.0
            for (document in documents) {
                if(document.get("date") == getDate()) {
                    intakeQuan = document.get("intakeQuan").toString().toDouble()
                    proteinTot += document.get("protein").toString().toDouble() * intakeQuan
                    caloriesTot += document.get("calories").toString().toDouble() * intakeQuan
                    fatTot += document.get("fat").toString().toDouble() * intakeQuan
                    carbsTot += document.get("carbohydrates").toString().toDouble() * intakeQuan
                    list.add(document.toObject(MealItem::class.java))

                }
            }

            db.collection(userId).document("userSettings").get().addOnSuccessListener { document ->

                val CALORIES: Int
                val PROTEIN: Int
                val FATS: Int
                val CARBS: Int
                if(document.exists()) {
                    CALORIES = document.get("caloriesGoal").toString().toInt()
                    PROTEIN = document.get("proteinGoal").toString().toInt()
                    FATS = document.get("fatGoal").toString().toInt()
                    CARBS = document.get("carbsGoal").toString().toInt()
                } else {
                    CALORIES = 2000
                    PROTEIN = 50
                    FATS = 66
                    CARBS = 215
                }

                //set progress -> for now total = 2000 cal; 80g protein; 20g fats; 120g carbs;
                val progressCal_cal = df.format(caloriesTot).toDouble() / CALORIES
                val progressCal_prot = df.format(proteinTot).toDouble() / PROTEIN
                val progressCal_fats = df.format(fatTot).toDouble() / FATS
                val progressCal_carbs = df.format(carbsTot).toDouble() / CARBS
                circularProgressIndicator.progress = if(progressCal_cal > 1) 100 else (progressCal_cal * 100.0).toInt() // 100.0 -> 100%
                linearProgressIndicator_protein.progress = if(progressCal_prot > 1) 100 else (progressCal_prot * 100.0).toInt()   // 100.0 -> 100%
                linearProgressIndicator_fats.progress = if(progressCal_fats > 1) 100 else (progressCal_fats * 100.0).toInt()   // 100.0 -> 100%
                linearProgressIndicator_carbs.progress = if(progressCal_carbs > 1) 100 else (progressCal_carbs * 100.0).toInt()   // 100.0 -> 100%

                //set textfields
                //daily nutrients amount already eaten
                caloriesDaily.text = df.format(caloriesTot).toString()
                proteinDaily.text = df.format(proteinTot).toString()
                fatDaily.text = df.format(fatTot).toString()
                carbsDaily.text = df.format(carbsTot).toString()
                //daily nutrients amount total
                cal_dailyTotal.text = "of " + CALORIES.toString() + " kcal"
                protein_dailyTotal.text = "of " + PROTEIN.toString()
                fats_dailyTotal.text = "of " + FATS.toString()
                carbs_dailyTotal.text = "of " + CARBS.toString()
            }



            //RECYCLERVIEW
            //rv config
            rv_listOfFoods.setNestedScrollingEnabled(false);
            //layout manager & adapter
            val layoutManager = LinearLayoutManager(this)
            val adapter = DashboardAdapter(list)
            rv_listOfFoods.layoutManager = layoutManager
            rv_listOfFoods.adapter = adapter
        }

        //get by id
        fade_in = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        btn_camera = findViewById<MaterialButton>(R.id.btn_camera)
        dashboard_layout = findViewById<ConstraintLayout>(R.id.dashboard_layout)
        drawerLayout = findViewById(R.id.drawerLayout)
        navView = findViewById(R.id.nav_view)

        //Open and close navigation drawer
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        navView.bringToFront()

        //Functionality of navigation item clicked
        navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nav_settings ->    {
                    openSettings()
                }
                R.id.nav_report -> {
                    reportProblem()
                }
                R.id.nav_logout -> {
                    logout()
                }
            }
            true
        }

        //set the username as userId
        val username = navView.getHeaderView(0).findViewById<TextView>(R.id.user_name).text
        if(username != null){
            navView.getHeaderView(0).findViewById<TextView>(R.id.user_name).text = userId
        }



        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                arrayOf<String>(Manifest.permission.CAMERA), 101)
        }

        btn_camera.setOnClickListener(View.OnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, 101)
        })
        dashboard_layout.startAnimation(fade_in)
    }

    private fun openSettings() {
        val intent = Intent(this, SettingsActivity::class.java)
        intent.putExtra("userId", userId)
        intent.putExtra("cloudData", cloudData)
        startActivity(intent)
    }

    private fun logout() {
        auth.signOut()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    //Reporting a problem by sending an email
    private fun reportProblem() {

        val email: String = "viktov@stud.ntnu.no"
        val subject: String = "Reporting for app " + getString(R.string.app_name)

        val intent = Intent(Intent.ACTION_SENDTO)
        intent.setData(Uri.parse("mailto:"))
        intent.putExtra(Intent.EXTRA_EMAIL, email)
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        intent.putExtra(Intent.EXTRA_TEXT, "")

        startActivity(Intent.createChooser(intent, "Choose an email client:"))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode==101){
            //bitmap containing the picture
            val bitmap = data?.extras?.get("data") as Bitmap
            //create intent and pass bitmap
            val intent = Intent(this, FoodActivity::class.java)
            intent.putExtra("BitmapImage", bitmap)
            intent.putExtra("userId", userId)
            intent.putExtra("cloudData", cloudData)
            //start viewActivity
            startActivity(intent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}