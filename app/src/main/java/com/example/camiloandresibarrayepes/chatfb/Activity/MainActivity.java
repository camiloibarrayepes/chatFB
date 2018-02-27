package com.example.camiloandresibarrayepes.chatfb.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.camiloandresibarrayepes.chatfb.AdapterMensajes;
import com.example.camiloandresibarrayepes.chatfb.Entidades.MensajeEnviar;
import com.example.camiloandresibarrayepes.chatfb.Entidades.MensajeRecibir;
import com.example.camiloandresibarrayepes.chatfb.Entidades.Usuario;
import com.example.camiloandresibarrayepes.chatfb.Manifest;
import com.example.camiloandresibarrayepes.chatfb.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private CircleImageView fotoPerfil;
    private TextView nombre;
    private RecyclerView rvMensajes;
    private EditText txtMensaje;
    private Button btnEnviar;
    private ImageButton btnEnviarFoto;
    private Button logout;

    private AdapterMensajes adapter;

    //Envio datos a FIREBASE
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private static final int  PHOTO_SEND = 1;
    private static final int PHOTO_PERFIL = 2;
    private String fotoPerfilCadena;

    private FirebaseAuth mAuth;
    private String NOMBRE_USUARIO;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fotoPerfil = (CircleImageView)findViewById(R.id.fotoPerfil);
        nombre = (TextView)findViewById(R.id.nombre);
        rvMensajes = (RecyclerView)findViewById(R.id.rvMensajes);
        txtMensaje = (EditText)findViewById(R.id.txtMensaje);
        btnEnviar = (Button)findViewById(R.id.btnEnviar);
        btnEnviarFoto = (ImageButton)findViewById(R.id.btnEnviarFoto);
        logout = (Button)findViewById(R.id.logout);
        //inicia sin foto de perfil
        fotoPerfilCadena = "";
        nombre.setText(NOMBRE_USUARIO);


        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("chatV2");//Sala de chat (nombre donde se guardan mensajes)
        storage = FirebaseStorage.getInstance();
        mAuth = FirebaseAuth.getInstance();


        adapter = new AdapterMensajes(this);
        LinearLayoutManager l = new LinearLayoutManager(this);
        rvMensajes.setLayoutManager(l);
        rvMensajes.setAdapter(adapter);

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Insertar mensaje enla base de datos
                databaseReference.push().setValue(new MensajeEnviar(txtMensaje.getText().toString(), NOMBRE_USUARIO, fotoPerfilCadena, "1", ServerValue.TIMESTAMP));
                //adapter.addMensaje(new Mensaje(txtMensaje.getText().toString(), nombre.getText().toString(), "", "1", "00:00" ));
                txtMensaje.setText("");
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                returnLogin();
            }
        });

        btnEnviarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("image/jpg");
                i.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(i, "Selecciona una foto"), PHOTO_SEND);
            }
        });

        fotoPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("image/jpg");
                i.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(i, "Selecciona una foto"), PHOTO_PERFIL);
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
                MensajeRecibir m = dataSnapshot.getValue(MensajeRecibir.class);
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

        //verifyStoragePermissions(this);
    }

    //permite que los mensajes de desplacen hacia arriba a medida que aparecen
    private void setScrollBar(){
        rvMensajes.scrollToPosition(adapter.getItemCount()-1);
    }

    //PERMISOS VERIFICACION
    /*
    public static boolean verifyStoragePermissions(Activity activity) {
        String[] PERMISSIONS_STORAGE = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        int REQUEST_EXTERNAL_STORAGE = 1;
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
            return false;
        }else{
            return true;
        }
    }
    */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PHOTO_SEND && resultCode == RESULT_OK)
        {
            Uri u = data.getData();
            storageReference = storage.getReference("imagenes_chat");//Imagenes de chat
            //se obtiene un KEY de las fotos para diferenciarlas
            final StorageReference fotoReferencia = storageReference.child(u.getLastPathSegment());
            fotoReferencia.putFile(u).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri u = taskSnapshot.getDownloadUrl();
                    MensajeEnviar m = new MensajeEnviar(NOMBRE_USUARIO + " te ha enviado una foto", u.toString(),NOMBRE_USUARIO, fotoPerfilCadena, "2", ServerValue.TIMESTAMP);
                    databaseReference.push().setValue(m);
                }
            });
        }else if(requestCode == PHOTO_PERFIL && resultCode == RESULT_OK)
        {
            Uri u = data.getData();
            storageReference = storage.getReference("foto_perfil");//Imagenes de chat
            //se obtiene un KEY de las fotos para diferenciarlas
            final StorageReference fotoReferencia = storageReference.child(u.getLastPathSegment());
            fotoReferencia.putFile(u).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri u = taskSnapshot.getDownloadUrl();
                    fotoPerfilCadena = u.toString();
                    MensajeEnviar m = new MensajeEnviar( NOMBRE_USUARIO + " ha actualizado su foto", u.toString(),NOMBRE_USUARIO, fotoPerfilCadena, "2", ServerValue.TIMESTAMP);
                    databaseReference.push().setValue(m);
                    //Actualizar foto perfil (arriba derecha)
                    Glide.with(MainActivity.this).load(u.toString()).into(fotoPerfil);

                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null){
            //Inhabilitar boton enviar antes de autenticar
            btnEnviar.setEnabled(false);
            //Se captura el ID del usuario
            DatabaseReference reference = database.getReference("Usuarios/"+currentUser.getUid());
            //Se puede entonces, llamar el nombre
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Usuario usuario = dataSnapshot.getValue(Usuario.class);
                    NOMBRE_USUARIO = usuario.getNombre();
                    nombre.setText(NOMBRE_USUARIO);
                    //Cuando ya tiene los datos se habilita
                    btnEnviar.setEnabled(true);
                    //Toast.makeText(MainActivity.this, "Bienvenido " +usuario.getNombre(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }else{
            returnLogin();
        }
    }

    private void returnLogin(){
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }


}
