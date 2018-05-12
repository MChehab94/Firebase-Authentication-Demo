package mchehab.com.firebaseauthentication

import android.app.DatePickerDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.text.InputType
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_sign_up.*
import java.util.*

class SignUpActivity : AppCompatActivity() {

    val firebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        setupButtonSignUpListener()
        setupButtonCancelListener()
        setupTimePickerDialog()
    }

    private fun setupButtonCancelListener() {
        buttonCancel.setOnClickListener {
            finish()
        }
    }

    private fun setupButtonSignUpListener(){
        buttonSignUp.setOnClickListener {
            if(!canSignUp()){
                displaySignUpError()
                return@setOnClickListener
            }
            progressBar.visibility = View.VISIBLE
            signUpUser()
        }
    }

    private fun displaySignUpError(){
        AlertDialog.Builder(this@SignUpActivity)
                .setTitle("Error")
                .setMessage("Please fill all the fields")
                .setPositiveButton("Ok", null)
                .create()
                .show()
    }

    private fun displayCreationError(message: String){
        AlertDialog.Builder(this@SignUpActivity)
                .setTitle("Error")
                .setMessage(message)
                .setPositiveButton("Ok", null)
                .create()
                .show()
    }

    private fun signUpUser(){
        val name = editTextName.text.toString()
        val email = editTextEmail.text.toString()
        val password = editTextPassword.text.toString()
        val dateOfBirth = editTextDateOfBirth.text.toString()
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            progressBar.visibility = View.GONE
            if(it.isSuccessful){
                val uid = it.result.user.uid
                val user = User(uid, name, dateOfBirth, email)
                FirebaseDatabase.getInstance().getReference("users").child(uid).setValue(user)
                it.result.user.sendEmailVerification().addOnCompleteListener {
                    if(it.isSuccessful){
                        Toast.makeText(this@SignUpActivity, "A verification email has been sent " +
                                "to $email", Toast.LENGTH_LONG).show()
                        firebaseAuth.signOut()
                        finish()
                    }
                }
            }else{
                displayCreationError(it.exception!!.message!!)
            }
        }
    }

    private fun setupTimePickerDialog(){
        editTextDateOfBirth.inputType = InputType.TYPE_NULL
        val datePickerDialogListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            editTextDateOfBirth.setText("$dayOfMonth/$month/$year")
        }
        val calendar = Calendar.getInstance()
        editTextDateOfBirth.setOnClickListener({
            val datePickerDialog = DatePickerDialog(this@SignUpActivity, datePickerDialogListener, calendar.get(Calendar.YEAR),
                    Calendar.MONTH, Calendar.DAY_OF_MONTH)
            datePickerDialog.show()
        })
    }

    private fun canSignUp():Boolean{
        if(editTextDateOfBirth.text.isEmpty())
            return false
        if(editTextEmail.text.isEmpty())
            return false
        if(editTextPassword.text.isEmpty())
            return false
        return true
    }
}