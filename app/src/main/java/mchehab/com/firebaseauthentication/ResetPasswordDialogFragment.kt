package mchehab.com.firebaseauthentication


import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_reset_password_dialog.*


class ResetPasswordDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window.requestFeature(Window.FEATURE_NO_TITLE)

        return dialog
    }

    override fun onStart() {
        super.onStart()
        dialog?.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_reset_password_dialog, container, false)
        view.findViewById<Button>(R.id.buttonSubmit).setOnClickListener {
            val email = editTextEmail.text.toString()
            if (email.isEmpty()) {
                AlertDialog.Builder(activity!!)
                        .setTitle("Error")
                        .setMessage("Please fill email and password fields")
                        .setPositiveButton("Ok", null)
                        .create()
                        .show()
                return@setOnClickListener
            }
            progressBar.visibility = View.VISIBLE
            FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener {
                progressBar.visibility = View.GONE
                if (it.isSuccessful) {
                    AlertDialog.Builder(activity!!)
                            .setTitle("Success")
                            .setMessage("Email has been sent")
                            .setPositiveButton("Ok", { dialog, which ->
                                    dialog.dismiss()
                                    dismiss()
                            })
                            .create()
                            .show()
                } else {
                    AlertDialog.Builder(activity!!)
                            .setTitle("Error")
                            .setMessage("Failed to send email, please try again later")
                            .setPositiveButton("Ok", null)
                            .create()
                            .show()
                }
            }
        }
        view.findViewById<Button>(R.id.buttonCancel).setOnClickListener {
            dismiss()
        }
        return view
    }
}