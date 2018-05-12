package mchehab.com.javafirebaseauthentication;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class SignUpActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    private EditText editTextName;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextDateOfBirth;

    private Button buttonCancel;
    private Button buttonSignUp;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setupUI();
        setupButtonSignUpListener();
        setupButtonCancelListener();
        setupTimePickerDialog();
    }

    private void setupUI(){
        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextDateOfBirth = findViewById(R.id.editTextDateOfBirth);
        buttonCancel = findViewById(R.id.buttonCancel);
        buttonSignUp = findViewById(R.id.buttonSignUp);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setupButtonCancelListener() {
        buttonCancel.setOnClickListener(e -> finish());
    }

    private void setupButtonSignUpListener() {
        buttonSignUp.setOnClickListener(e -> {
            if (!canSignUp()) {
                displaySignUpError();
                return;
            }
            progressBar.setVisibility(View.VISIBLE);
            signUpUser();
        });
    }

    private void displaySignUpError() {
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage("Please fill all the fields")
                .setPositiveButton("Ok", null)
                .create()
                .show();
    }

    private void displayCreationError(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(message)
                .setPositiveButton("Ok", null)
                .create()
                .show();
    }

    private void signUpUser() {
        String name = editTextName.getText().toString();
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();
        String dateOfBirth = editTextDateOfBirth.getText().toString();
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            progressBar.setVisibility(View.GONE);
            if (task.isSuccessful()) {
                String uid = task.getResult().getUser().getUid();
                User user = new User(uid, name, dateOfBirth, email);
                FirebaseDatabase.getInstance().getReference("users").child(uid).setValue(user);
                task.getResult().getUser().sendEmailVerification().addOnCompleteListener
                        (taskEmailVerification -> {
                            if (taskEmailVerification.isSuccessful()) {
                                Toast.makeText(SignUpActivity.this, "A verification email has been sent "
                                        + "to " + email, Toast.LENGTH_LONG).show();
                                firebaseAuth.signOut();
                                finish();
                            }
                        });
            } else {
                displayCreationError(task.getException().getMessage());
            }
        });
    }

    private void setupTimePickerDialog() {
        editTextDateOfBirth.setInputType(InputType.TYPE_NULL);
        DatePickerDialog.OnDateSetListener datePickerDialogListener = (view, year, month, dayOfMonth) -> {
            editTextDateOfBirth.setText(dayOfMonth + "/" + month + "/" + year);
       };

        Calendar calendar = Calendar.getInstance();
        editTextDateOfBirth.setOnClickListener(e -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(SignUpActivity.this,
                    datePickerDialogListener, calendar.get(Calendar.YEAR), Calendar.MONTH, Calendar.DAY_OF_MONTH);
            datePickerDialog.show();
        });
    }

    private boolean canSignUp() {
        if (editTextDateOfBirth.getText().length() == 0) return false;
        if (editTextEmail.getText().length() == 0) return false;
        if (editTextPassword.getText().length() == 0) return false;
        return true;
    }
}