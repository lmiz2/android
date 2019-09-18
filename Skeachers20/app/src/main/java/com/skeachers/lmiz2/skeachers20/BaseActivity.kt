package com.skeachers.lmiz2.skeachers20

import Model.User
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

open class BaseActivity : AppCompatActivity() {

    var Global_FontSize = 15

    private lateinit var globalAuth: FirebaseAuth
    private lateinit var globalUser: FirebaseUser
    private lateinit var globalRef: DatabaseReference
    private lateinit var globalGApi: GoogleApiClient
    private lateinit var globalDB: FirebaseDatabase
    private lateinit var LoginUserSession: User
    private lateinit var firebaseObj : FirebaseDatabase

    override fun onResume() {
        super.onResume()
    }

    override fun onRestart() {
        super.onRestart()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
