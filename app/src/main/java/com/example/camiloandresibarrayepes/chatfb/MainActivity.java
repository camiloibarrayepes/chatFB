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

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private CircleImageView fotoPerfil;
    private TextView nombre;
    private RecyclerView rvMensajes;
    private EditText txtMensaje;
    private Button btnEnviar;

    private AdapterMensajes adapter;

    //Envio datos a FIREBASE
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fotoPerfil = (CircleImageView)findViewById(R.id.fotoPerfil);
        nombre = (TextView)findViewById(R.id.nombre);
        rvMensajes = (RecyclerView)findViewById(R.id.rvMensajes);
        txtMensaje = (EditText)findViewById(R.id.txtMensaje);
        btnEnviar = (Button)findViewById(R.id.btnEnviar);

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("chat");//Sala de chat (nombre donde se guardan mensajes)

        adapter = new AdapterMensajes(this);
        LinearLayoutManager l = new LinearLayoutManager(this);
        rvMensajes.setLayoutManager(l);
        rvMensajes.setAdapter(adapter);

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Insertar mensaje enla base de datos
                databaseReference.push().setValue(new Mensaje(txtMensaje.getText().toString(), nombre.getText().toString(), "", "1", "00:00" ));
                //adapter.addMensaje(new Mensaje(txtMensaje.getText().toString(), nombre.getText().toString(), "", "1", "00:00" ));
                txtMensaje.setText("");
            }
        });

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                setScrollBar();

            }
        });

        databaseReference.addChildEventListener(new ChildEventListener() {
            //Este metodo permite
            //Cuando se agrege un dato a la base de datos, lo agrega a la vista
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Mensaje m = dataSnapshot.getValue(Mensaje.class);
                adapter.addMensaje(m);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //permite que los mensajes de desplacen hacia arriba a medida que aparecen
    private void setScrollBar(){
        rvMensajes.scrollToPosition(adapter.getItemCount()-1);
    }
}
