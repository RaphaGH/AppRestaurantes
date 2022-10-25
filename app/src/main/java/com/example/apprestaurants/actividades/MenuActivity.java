package com.example.apprestaurants.actividades;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.apprestaurants.R;
import com.example.apprestaurants.clases.InterfaceMenu;
import com.example.apprestaurants.clases.Usuario;
import com.example.apprestaurants.fragmentos.ConfigFragment;
import com.example.apprestaurants.fragmentos.RecNosFragment;
import com.example.apprestaurants.fragmentos.RecUdsFragment;
import com.example.apprestaurants.fragmentos.RegTuyoFragment;

public class MenuActivity extends AppCompatActivity implements InterfaceMenu {
    Fragment[] fragments;
    Usuario usuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        fragments = new Fragment[4];
        fragments[0] = new RecNosFragment();
        fragments[1] = new RecUdsFragment();
        fragments[2] = new RegTuyoFragment();
        fragments[3] = new ConfigFragment();

        usuario = (Usuario) getIntent().getSerializableExtra("usuario");

        int id_boton = getIntent().getIntExtra("ID_boton", -1);
        onClickMenu(id_boton);
    }

    @Override
    public void onClickMenu(int i_id_boton) {
        Bundle datos= new Bundle();
        datos.putSerializable("usuario", usuario);
        fragments[i_id_boton].setArguments(datos);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.men_rel_menu, fragments[i_id_boton]);
        ft.commit();
    }
}