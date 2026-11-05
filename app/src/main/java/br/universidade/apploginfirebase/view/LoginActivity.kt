package br.universidade.apploginfirebase.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.universidade.apploginfirebase.R
import br.universidade.apploginfirebase.databinding.ActivityLoginBinding
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { result ->
        if (result.resultCode == RESULT_OK && FirebaseAuth.getInstance().currentUser != null) {
            abrirTelaPrincipal()
        } else if (result.idpResponse != null) {
            Toast.makeText(this, R.string.sign_in_error, Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (FirebaseAuth.getInstance().currentUser != null) {
            abrirTelaPrincipal()
            return
        }

        binding.btnEntrar.setOnClickListener { iniciarLogin() }
        binding.btnCadastrar.setOnClickListener { iniciarLogin() }
    }

    private fun iniciarLogin() {
        val provider = AuthUI.IdpConfig.EmailBuilder()
            .setAllowNewAccounts(true)
            .setRequireName(true)
            .build()
        val intent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(listOf(provider))
            .setTheme(R.style.Theme_AccountAuth)
            .build()
        signInLauncher.launch(intent)
    }

    private fun abrirTelaPrincipal() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}