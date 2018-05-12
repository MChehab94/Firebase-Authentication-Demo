package mchehab.com.javafirebaseauthentication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeScreen extends AppCompatActivity {

    private ProgressBar progressBar;

    private TextView textViewEmail;
    private TextView textViewName;
    private TextView textViewDateOfBirth;
    private TextView textViewUID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        setupUI();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) {
            finish();
        }
        String uid = firebaseUser.getUid();
        setupUser(uid);
    }

    private void setupUI(){
        progressBar = findViewById(R.id.progressBar);
        textViewEmail = findViewById(R.id.textViewEmail);
        textViewName = findViewById(R.id.textViewName);
        textViewDateOfBirth = findViewById(R.id.textViewDateOfBirth);
        textViewUID = findViewById(R.id.textViewUID);
    }

    private void setupUser(String uid) {
        DatabaseReference databseReference = FirebaseDatabase.getInstance().getReference("users");
        databseReference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.GONE);
                User user = dataSnapshot.getValue(User.class);
                textViewEmail.setText(user.getEmail());
                textViewName.setText(user.getName());
                textViewDateOfBirth.setText(user.getDateOfBirth());
                textViewUID.setText(user.getUid());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
    }
}