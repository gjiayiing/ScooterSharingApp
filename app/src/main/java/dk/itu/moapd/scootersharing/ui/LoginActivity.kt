package dk.itu.moapd.scootersharing.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import dk.itu.moapd.scootersharing.R
import java.util.regex.Pattern

class LoginActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()

        val signInBtn = findViewById<Button>(R.id.button_sign_in)
        val emailText = findViewById<TextInputLayout>(R.id.emailText)
        val passwordText = findViewById<TextInputLayout>(R.id.passwordText)

        signInBtn.setOnClickListener {
            val email = emailText.editText?.text.toString().trim()
            val password = passwordText.editText?.text.toString().trim()

            //validation
            if(isValidEmail(email)){
                loginUser(email,password)
            }else{
                Toast.makeText(this,"Wrong format for email, please try again",Toast.LENGTH_SHORT).show()
            }

        }
    }
    val EMAIL_ADDRESS_PATTERN: Pattern = Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )
    fun isValidEmail(email: String): Boolean{
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }
    private fun loginUser(email: String, password: String){
        val progressbar = findViewById<ProgressBar>(R.id.progressbar)
        progressbar.visibility = View.VISIBLE


        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this){   task ->
                progressbar.visibility = View.GONE
                if(task.isSuccessful){
                    val intent = Intent(this@LoginActivity, ScooterSharingActivity::class.java ).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    startActivity(intent)
                }else{
                    task.exception?.message?.let{
                        Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                    }
                }
            }

    }
    override fun onStart(){
        super.onStart()
        mAuth.currentUser?.let {
            val intent = Intent(this@LoginActivity, ScooterSharingActivity::class.java ).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }
    }
}