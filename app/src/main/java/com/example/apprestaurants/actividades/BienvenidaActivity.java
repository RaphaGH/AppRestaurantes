package com.example.apprestaurants.actividades;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.apprestaurants.R;
import com.example.apprestaurants.clases.InterfaceMenu;
import com.example.apprestaurants.clases.Usuario;

public class BienvenidaActivity extends AppCompatActivity implements InterfaceMenu {
    TextView lbl_saludo;
    Usuario usuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bienvenida);
        lbl_saludo=findViewById(R.id.bie_lbl_bienvenida);
        usuario = (Usuario) getIntent().getSerializableExtra("usuario");
        lbl_saludo.setText("¡Welcome "+usuario.getNombre()+"!");
    }

    @Override
    public void onClickMenu(int i_id_boton) {
        Intent i_menu = new Intent(this, MenuActivity.class);
        i_menu.putExtra("ID_boton", i_id_boton);
        i_menu.putExtra("usuario", usuario);
        startActivity(i_menu);
    }
}