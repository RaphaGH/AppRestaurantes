package com.example.apprestaurants.clases;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apprestaurants.R;
import com.loopj.android.http.Base64;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class AdapterRestaurantes extends RecyclerView.Adapter<AdapterRestaurantes.ViewHolderRestaurantes> implements View.OnClickListener {
    ArrayList<Restaurante> listaRestaurantes;
    private View.OnClickListener listener;
    public AdapterRestaurantes(ArrayList<Restaurante> listaRestaurantes) {
        this.listaRestaurantes = listaRestaurantes;
    }
    @NonNull
    @Override
    public ViewHolderRestaurantes onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ist,null,false);

        view.setOnClickListener(this);

        return new ViewHolderRestaurantes(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderRestaurantes holder, int position) {
        holder.txt_nombre.setText(listaRestaurantes.get(position).getNombre());
        holder.txt_descripcion.setText(listaRestaurantes.get(position).getDescripcion());
        String imagen = listaRestaurantes.get(position).getFoto();
        byte[] image_byte = Base64.decode(imagen, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(image_byte, 0, image_byte.length);
        holder.foto.setImageBitmap(bitmap);
        holder.txt_promedio.setText(Float.toString(listaRestaurantes.get(position).getPromedio()));
    }

    @Override
    public int getItemCount() {
        return listaRestaurantes.size();
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if(listener != null){
            listener.onClick(view);
        }
    }

    public class ViewHolderRestaurantes extends RecyclerView.ViewHolder {
        TextView txt_nombre, txt_descripcion, txt_promedio;
        ImageView foto;

        public ViewHolderRestaurantes(@NonNull View itemView) {
            super(itemView);
            txt_nombre = (TextView) itemView.findViewById(R.id.item_nombre_restaurante);
            txt_descripcion = (TextView) itemView.findViewById(R.id.item_descripcion_restaurante);
            txt_promedio = (TextView) itemView.findViewById(R.id.item_promedio);
            foto = (ImageView) itemView.findViewById(R.id.item_imagen);
        }
    }
}
