package br.universidade.apploginfirebase.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.universidade.apploginfirebase.R
import br.universidade.apploginfirebase.databinding.ActivityMainBinding
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            voltarAoLogin()
            return
        }

        val name = user.displayName?.takeIf { it.isNotBlank() }
            ?: user.email?.substringBefore('@')
            ?: getString(R.string.default_name)
        binding.txtBemVindo.text = getString(R.string.welcome, name)
        binding.txtEmail.text = user.email
        binding.btnSair.setOnClickListener {
            binding.btnSair.isEnabled = false
            AuthUI.getInstance().signOut(this).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    voltarAoLogin()
                } else {
                    binding.btnSair.isEnabled = true
                    Toast.makeText(this, R.string.sign_out_error, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun voltarAoLogin() {
        val intent = Intent(this, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish()
    }
}