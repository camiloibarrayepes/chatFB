package com.example.camiloandresibarrayepes.chatfb;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by camiloandresibarrayepes on 25/02/18.
 */

public class AdapterMensajes extends RecyclerView.Adapter<HolderMensaje> {

    private List<Mensaje> listMensaje = new ArrayList<>();
    private Context c;

    public AdapterMensajes(Context c) {
        this.c = c;
    }

    public void addMensaje(Mensaje m){
        listMensaje.add(m);
        notifyItemInserted(listMensaje.size());
    }

    @Override
    public HolderMensaje onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.card_view_mensajes, parent, false);
        //Enviamos la vista
        return new HolderMensaje(v);
    }

    @Override
    public void onBindViewHolder(HolderMensaje holder, int position) {
        holder.getNombre().setText(listMensaje.get(position).getNombre());
        holder.getMensaje().setText(listMensaje.get(position).getMensaje());
        holder.getHora().setText(listMensaje.get(position).getHora());

    }

    @Override
    public int getItemCount() {
        return listMensaje.size();
    }
}
