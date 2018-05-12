package mchehab.com.firebaseauthentication

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    val firebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupButtonLoginListener()
        setupButtonSignUp()
        setupTextViewReset()
    }

    private fun setupTextViewReset() {
        textViewResetPassword.setOnClickListener {
            ResetPasswordDialogFragment().show(supportFragmentManager, "")
        }
    }

    private fun setupButtonSignUp() {
        buttonSignUp.setOnClickListener {
            val intent = Intent(this@MainActivity, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupButtonLoginListener() {
        buttonLogin.setOnClickListener {
            if (!canSubmit()) {
                displaySubmitErrorDialog()
                return@setOnClickListener
            }
            buttonLogin.isClickable = false
            progressBar.visibility = View.VISIBLE
            signIn()
        }
    }

    private fun canSubmit(): Boolean{
        if(editTextEmail.text.isEmpty() || editTextPassword.text.isEmpty())
            return false
        return true
    }

    private fun displaySubmitErrorDialog(){
        AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage("Please fill email and password fields")
                .setPositiveButton("Ok", null)
                .create()
                .show()
    }

    private fun signIn(){
        val email = editTextEmail.text.toString()
        val password = editTextPassword.text.toString()
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            buttonLogin.isClickable = true
            progressBar.visibility = View.GONE
            if (it.isSuccessful) {
                if(isUserVerified()){
                    goToHomeScreen()
                }else{
                    Toast.makeText(this@MainActivity, "You must verify your " +
                    "mail in order to login", Toast.LENGTH_LONG).show()
                }
            } else {
                displaySignInError()
            }
        }
    }

    private fun displaySignInError(){
        AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage("Invalid credentials")
                .setPositiveButton("Ok", null)
                .create()
                .show()
    }

    private fun isUserVerified(): Boolean{
        return firebaseAuth.currentUser!!.isEmailVerified
    }

    private fun goToHomeScreen(){
        val intent = Intent(applicationContext, HomeScreen::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}