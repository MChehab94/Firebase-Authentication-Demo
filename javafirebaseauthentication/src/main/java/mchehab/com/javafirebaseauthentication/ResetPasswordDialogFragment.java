package mchehab.com.javafirebaseauthentication;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordDialogFragment extends DialogFragment {

    private EditText editTextEmail;
    private ProgressBar progressBar;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_reset_password_dialog, container, false);
        editTextEmail = view.findViewById(R.id.editTextEmail);
        progressBar = view.findViewById(R.id.progressBar);
        view.findViewById(R.id.buttonSubmit).setOnClickListener(e -> {
            String email = editTextEmail.getText().toString();
            if (email.isEmpty()) {
                displaySubmitError();
                return;
            }
            progressBar.setVisibility(View.VISIBLE);
            FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener(task -> {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    displaySuccessDialog();
                } else {
                    displayErrorSendingEmailDialog();
                }
            });
        });
        view.findViewById(R.id.buttonCancel).setOnClickListener(e -> dismiss());
        return view;
    }

    private void displaySubmitError(){
        new AlertDialog.Builder(getActivity())
                .setTitle("Error")
                .setMessage("Please fill email and password fields")
                .setPositiveButton("Ok", null)
                .create()
                .show();
    }

    private void displaySuccessDialog(){
        new AlertDialog.Builder(getActivity())
                .setTitle("Success")
                .setMessage("Email has been sent")
                .setPositiveButton("Ok", (dialog, which) -> {
                    dialog.dismiss();
                    dismiss();
                })
                .create()
                .show();
    }

    private void displayErrorSendingEmailDialog(){
        new AlertDialog.Builder(getActivity())
                .setTitle("Error")
                .setMessage("Failed to send email, please try again later")
                .setPositiveButton("Ok", null)
                .create()
                .show();
    }
}
