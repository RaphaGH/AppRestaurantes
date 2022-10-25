package com.example.apprestaurants.clases;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apprestaurants.R;

import java.util.ArrayList;

public class AdapterComentario extends RecyclerView.Adapter<AdapterComentario.ViewHolderComentario> {
    ArrayList<Comentario> listaComentarios;

    public AdapterComentario(ArrayList<Comentario> listacomentarios) {
        this.listaComentarios = listacomentarios;
    }

    @NonNull
    @Override
    public ViewHolderComentario onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comentario,null,false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        return new ViewHolderComentario(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderComentario holder, int position) {
        holder.txt_nombre_usuario.setText(listaComentarios.get(position).getNombre());
        holder.txt_comentario.setText(listaComentarios.get(position).getComentario());
        holder.txt_puntaje.setText(Float.toString(listaComentarios.get(position).getPuntaje()));
    }

    @Override
    public int getItemCount() {
        return listaComentarios.size();
    }

    public class ViewHolderComentario extends RecyclerView.ViewHolder {
        TextView txt_nombre_usuario, txt_comentario, txt_puntaje;

        public ViewHolderComentario(@NonNull View itemView) {
            super(itemView);
            txt_nombre_usuario = (TextView) itemView.findViewById(R.id.it_cm_nombre_usuario);
            txt_comentario = (TextView) itemView.findViewById(R.id.it_cm_descripcion);
            txt_puntaje = (TextView) itemView.findViewById(R.id.it_cm_puntaje);
        }
    }
}
