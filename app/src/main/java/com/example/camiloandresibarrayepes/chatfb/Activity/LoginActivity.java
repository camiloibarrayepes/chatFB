package com.example.camiloandresibarrayepes.chatfb.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.camiloandresibarrayepes.chatfb.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Camilo Ibarra KAPTA on 26/02/2018.
 */

public class LoginActivity extends AppCompatActivity {

    private EditText txtCorreo, txtContraseña;
    private Button btnLogin, btnRegistro;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtCorreo = (EditText)findViewById(R.id.correoLogin);
        txtContraseña = (EditText)findViewById(R.id.contraseñaLogin);
        btnLogin = (Button)findViewById(R.id.loginLogin);
        btnRegistro = (Button) findViewById(R.id.RegistroLogin);

        mAuth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String correo = txtCorreo.getText().toString();
                String contraseña =txtContraseña.getText().toString();
                if(isValidEmail(correo) && validarContraseña()){
                    mAuth.signInWithEmailAndPassword(correo, contraseña)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(LoginActivity.this, "Ingreso exitoso", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));

                                    } else {
                                        Toast.makeText(LoginActivity.this, "Datos de acceso incorrectos", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                }else{
                    Toast.makeText(LoginActivity.this, "validaciones", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private boolean isValidEmail(CharSequence target){
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public boolean validarContraseña()
    {
        String contraseña, contraseñaRepedita;
        contraseña = txtContraseña.getText().toString();
            if(contraseña.length()>=6 && contraseña.length()<=16){
                return true;
            }else return false;

    }
}
