package mchehab.com.javafirebaseauthentication;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    private TextView textViewResetPassword;
    private Button buttonSignUp;
    private Button buttonLogin;
    private ProgressBar progressBar;
    private EditText editTextEmail;
    private EditText editTextPassword;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupUI();
        setupButtonLoginListener();
        setupButtonSignUp();
        setupTextViewReset();
    }

    private void setupUI(){
        textViewResetPassword = findViewById(R.id.textViewResetPassword);
        buttonSignUp = findViewById(R.id.buttonSignUp);
        buttonLogin = findViewById(R.id.buttonLogin);
        progressBar = findViewById(R.id.progressBar);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
    }

    private void setupTextViewReset() {
        textViewResetPassword.setOnClickListener(e -> new ResetPasswordDialogFragment().show(getSupportFragmentManager(), ""));
    }

    private void setupButtonSignUp() {
        buttonSignUp.setOnClickListener(e -> {
            Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
            startActivity(intent);
        });
    }

    private void setupButtonLoginListener() {
        buttonLogin.setOnClickListener(e -> {
            if (!canSubmit()) {
                displaySubmitErrorDialog();
                return;
            }
            buttonLogin.setClickable(false);
            progressBar.setVisibility(View.VISIBLE);
            signIn();
        });
    }

    private boolean canSubmit(){
        if(editTextEmail.getText().length() == 0 || editTextPassword.getText().length() == 0)
            return false;
        return true;
    }

    private void displaySubmitErrorDialog(){
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage("Please fill email and password fields")
                .setPositiveButton("Ok", null)
                .create()
                .show();
    }

    private void signIn(){
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            buttonLogin.setClickable(true);
            progressBar.setVisibility(View.GONE);
            if (task.isSuccessful()) {
                if(isUserVerified()){
                    goToHomeScreen();
                }else{
                    Toast.makeText(MainActivity.this, "You must verify your " +
                            "mail in order to login", Toast.LENGTH_LONG).show();
                }
            } else {
                displaySignInError();
            }
        });
    }

    private void displaySignInError(){
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage("Invalid credentials")
                .setPositiveButton("Ok", null)
                .create()
                .show();
    }

    private boolean isUserVerified(){
        if(firebaseAuth.getCurrentUser() == null)
            return false;
        return firebaseAuth.getCurrentUser().isEmailVerified();
    }

    private void goToHomeScreen(){
        Intent intent = new Intent(getApplicationContext(), HomeScreen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}