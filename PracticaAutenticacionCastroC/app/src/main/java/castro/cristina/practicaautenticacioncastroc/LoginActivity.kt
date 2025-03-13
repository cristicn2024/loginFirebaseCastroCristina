package castro.cristina.practicaautenticacioncastroc

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        val email: EditText = findViewById(R.id.etEmail)
        val password: EditText = findViewById(R.id.etPassword)
        val errorTv: TextView = findViewById(R.id.tvError)
        val btnGoRegister : Button = findViewById(R.id.btnGoRegister)

        val button: Button = findViewById(R.id.btnLogin)

        errorTv.visibility = View.INVISIBLE

        button.setOnClickListener {
            val emailText = email.text.toString().trim()
            val passwordText = password.text.toString().trim()
            if (emailText.isNotEmpty() && passwordText.isNotEmpty()) {
                login(emailText, passwordText)
            } else {
                showError(text = "Por favor, completa todos los campos", visible = true)
            }
        }

        btnGoRegister.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

    }

    fun showError(text: String = "", visible: Boolean)
    {
        val errorTv: TextView = findViewById(R.id.tvError)

        errorTv.text = text

        errorTv.visibility = if (visible) View.VISIBLE else View.INVISIBLE
    }

    fun goToMain(user: FirebaseUser) {
        user.reload().addOnCompleteListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("user", user.email ?: "Usuario desconocido")
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            goToMain(currentUser)
        }
    }

    fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if (user != null) {
                        showError(visible = false)
                        goToMain(user)
                    } else {
                        showError("Error al obtener datos del usuario", true)
                    }
                } else {
                    showError("Usuario y/o contraseña equivocados", true)
                }
            }
    }


}