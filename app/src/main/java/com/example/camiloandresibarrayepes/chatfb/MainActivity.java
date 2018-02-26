package com.example.camiloandresibarrayepes.chatfb;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private CircleImageView fotoPerfil;
    private TextView nombre;
    private RecyclerView rvMensajes;
    private EditText txtMensaje;
    private Button btnEnviar;

    private AdapterMensajes adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fotoPerfil = (CircleImageView)findViewById(R.id.fotoPerfil);
        nombre = (TextView)findViewById(R.id.nombre);
        rvMensajes = (RecyclerView)findViewById(R.id.rvMensajes);
        txtMensaje = (EditText)findViewById(R.id.txtMensaje);
        btnEnviar = (Button)findViewById(R.id.btnEnviar);

        adapter = new AdapterMensajes(this);
        LinearLayoutManager l = new LinearLayoutManager(this);
        rvMensajes.setLayoutManager(l);
        rvMensajes.setAdapter(adapter);

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.addMensaje(new Mensaje(txtMensaje.getText().toString(), nombre.getText().toString(), "", "1", "00:00" ));
            }
        });
    }
}
