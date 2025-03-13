package castro.cristina.practicaautenticacioncastroc

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class SignInActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signin)

        auth = FirebaseAuth.getInstance()

        val email: EditText = findViewById(R.id.etrEmail)
        val password: EditText = findViewById(R.id.etrPassword)
        val confirmPassword: EditText = findViewById(R.id.etrConfirmPassword)
        val errorTv: TextView = findViewById(R.id.tvrError)

        val button: Button = findViewById(R.id.btnRegister)

        errorTv.visibility = View.INVISIBLE

        fun signIn(email: String, password: String) {
            Log.d("INFO", "email: ${email}, password: ${password}")

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        user?.reload()?.addOnCompleteListener {
                            Log.d("INFO", "signInWithEmail:success")
                            val intent = Intent(this, MainActivity::class.java)
                            intent.putExtra("user", auth.currentUser?.email ?: "Usuario desconocido")
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                            finish()
                        }
                    } else {
                        Log.w("ERROR", "signInWithEmail:failure", task.exception)
                        Toast.makeText(
                            baseContext,
                            "El registro falló: ${task.exception?.message}",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }
        }

        button.setOnClickListener({
            if(email.text.isEmpty() || password.text.isEmpty() || confirmPassword.text.isEmpty()){
                errorTv.text = "Todos los campos deben de ser llenados"
                errorTv.visibility = View.VISIBLE
            } else if(!password.text.toString().equals(confirmPassword.text.toString())){
                errorTv.text = "Las contraseñas no coinciden"
                errorTv.visibility = View.VISIBLE
            } else {
                errorTv.visibility = View.INVISIBLE
                signIn(email.text.toString(), password.text.toString())
            }
        })




    }



}