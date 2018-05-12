package mchehab.com.firebaseauthentication

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_home_screen.*

class HomeScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen)

        val firebaseUser = FirebaseAuth.getInstance().currentUser
        if (firebaseUser == null) {
            finish()
        }
        val uid = firebaseUser!!.uid
        setupUser(uid)
    }

    private fun setupUser(uid: String) {
        val databseReference = FirebaseDatabase.getInstance().getReference("users")
        databseReference.child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot?) {
                progressBar.visibility = View.GONE
                val user = dataSnapshot?.getValue(User::class.java)!!
                textViewEmail.text = user.email
                textViewName.text = user.name
                textViewDateOfBirth.text = user.dateOfBirth
                textViewUID.text = user.uid
            }

            override fun onCancelled(databaseError: DatabaseError?) {}
        })
    }
}