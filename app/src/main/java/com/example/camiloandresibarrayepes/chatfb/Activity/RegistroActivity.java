package com.example.camiloandresibarrayepes.chatfb.Activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.camiloandresibarrayepes.chatfb.Entidades.Usuario;
import com.example.camiloandresibarrayepes.chatfb.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Camilo Ibarra KAPTA on 26/02/2018.
 */

public class RegistroActivity extends AppCompatActivity {

    private EditText txtNombre, txtCorreo, txtContraseña, txtContraseñaRepetida;
    private Button btnRegistrar;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        txtNombre = (EditText)findViewById(R.id.RegistroNombre);
        txtCorreo = (EditText)findViewById(R.id.RegistroCorreo);
        txtContraseña = (EditText)findViewById(R.id.RegistroContraseña);
        txtContraseñaRepetida = (EditText)findViewById(R.id.RegistroContraseñaRepetida);
        btnRegistrar = (Button)findViewById(R.id.RegistroRegistrar);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();//Usuarios siempre con mayusculas


        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String correo = txtCorreo.getText().toString();
                final String nombre = txtNombre.getText().toString();
                if(isValidEmail(correo) && validarContraseña() && validarNombre(nombre)){
                    String contraseña = txtContraseña.getText().toString();
                    mAuth.createUserWithEmailAndPassword(correo, contraseña)
                            .addOnCompleteListener(RegistroActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(RegistroActivity.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                                        //Guardar datos de ingreso en tabla de Firebase llamada Usuarios
                                        Usuario usuario = new Usuario();
                                        usuario.setCorreo(correo);
                                        usuario.setNombre(nombre);
                                        //Modificacion para guardar ID de usuario
                                        FirebaseUser currentUser = mAuth.getCurrentUser();
                                        DatabaseReference reference = database.getReference("Usuarios/"+currentUser.getUid());
                                        reference.setValue(usuario);
                                        finish();
                                    }else{
                                        Toast.makeText(RegistroActivity.this, "Error al registrar", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }else{
                    Toast.makeText(RegistroActivity.this, "Validaciones", Toast.LENGTH_SHORT).show();
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
        contraseñaRepedita = txtContraseñaRepetida.getText().toString();
        if(contraseña.equals(contraseñaRepedita)){
            if(contraseña.length()>=6 && contraseña.length()<=16){
                return true;
            }else return false;
        }else return false;
    }

    public boolean validarNombre(String nombre){
        return !nombre.isEmpty();
    }
}
