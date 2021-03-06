package com.kovalsikoski.johan.mynotes

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_signup.*
import java.util.*
import java.util.regex.Pattern

class SignupActivity : AppCompatActivity() {

    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        realm = Realm.getDefaultInstance()

        btn_register.setOnClickListener {
            register()
        }

    }

    private fun register() {
        if (checkAllFields()) {
            if (isValidEmailPattern(et_user.text.toString())) {

                val uuid = UUID.randomUUID().toString()

                realm.beginTransaction()

                val result = realm.where(UserModel::class.java)
                        .equalTo("email", et_user.text.toString())
                        .findFirst()

                if(result!=null){
                    Toast.makeText(this, "Usuário já existente", Toast.LENGTH_LONG).show()
                } else {
                    val userObject = realm.createObject(UserModel::class.java, uuid)
                    userObject.email = et_user.text.toString()
                    userObject.passwordTip = et_tip.text.toString()
                    userObject.password = et_password.text.toString()

                    Toast.makeText(this, "Registrado com sucesso", Toast.LENGTH_LONG).show()
                    finish()
                }

                realm.commitTransaction()

            } else {
                Toast.makeText(this, "Formato de e-mail inválido", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun checkAllFields(): Boolean {
        if(isValidEmail()){
            return if(isValidTip()){
                if(isValidPassword()){
                    true
                } else {
                    Toast.makeText(this, "Campo de senha vazio", Toast.LENGTH_LONG).show()
                    false
                }
            } else {
                Toast.makeText(this, "Campo de dica vazio", Toast.LENGTH_LONG).show()
                false
            }
        } else {
            Toast.makeText(this, "Campo de e-mail vazio", Toast.LENGTH_LONG).show()
            return false
        }
    }

    private fun isValidEmail(): Boolean = et_user.text.toString().isNotBlank()
    private fun isValidTip(): Boolean = et_tip.text.toString().isNotBlank()
    private fun isValidPassword(): Boolean = et_password.text.toString().isNotBlank()

    fun isValidEmailPattern(email: String): Boolean =
            Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)\$",
                    Pattern.CASE_INSENSITIVE).matcher(email).matches()

}
