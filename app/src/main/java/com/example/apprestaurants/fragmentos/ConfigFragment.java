package com.example.apprestaurants.fragmentos;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.apprestaurants.R;
import com.example.apprestaurants.actividades.LoginActivity;
import com.example.apprestaurants.clases.Hash;
import com.example.apprestaurants.clases.Usuario;
import com.example.apprestaurants.sqlite.Sesion;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConfigFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConfigFragment extends Fragment implements View.OnClickListener {
    EditText txt_correo, txt_phone, txt_location,txt_clave_anterior, txt_clave;
    Button btn_actualizar, btn_cerrar_sesion;
    Usuario usuario;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";
    private static final String ARG_PARAM5 = "param5";
    private static final String ARG_PARAM6 = "param6";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String mParam3;
    private String mParam4;
    private String mParam5;
    private String mParam6;


    public ConfigFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ConfigFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ConfigFragment newInstance(String param1, String param2, String param3, String param4, String param5, String param6) {
        ConfigFragment fragment = new ConfigFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, param3);
        args.putString(ARG_PARAM4, param4);
        args.putString(ARG_PARAM3, param5);
        args.putString(ARG_PARAM4, param6);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            mParam3 = getArguments().getString(ARG_PARAM3);
            mParam4 = getArguments().getString(ARG_PARAM4);
            mParam3 = getArguments().getString(ARG_PARAM5);
            mParam4 = getArguments().getString(ARG_PARAM6);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v_config = inflater.inflate(R.layout.fragment_config, container, false);
        txt_correo = v_config.findViewById(R.id.frag_config_correo);
        txt_phone = v_config.findViewById(R.id.frag_config_telefono);
        txt_location = v_config.findViewById(R.id.frag_config_direccion);
        txt_clave_anterior = v_config.findViewById(R.id.frag_config_clave_anterior);
        txt_clave = v_config.findViewById(R.id.frag_config_clave);
        btn_actualizar = v_config.findViewById(R.id.frag_btn_config_actualizar);
        btn_cerrar_sesion = v_config.findViewById(R.id.frag_btn_config_cs);

        btn_actualizar.setOnClickListener(this);
        btn_cerrar_sesion.setOnClickListener(this);
        cargar_datos_usuario();
        return v_config;
    }

    private void cargar_datos_usuario() {
        Bundle datos = getArguments();
        usuario = (Usuario)datos.getSerializable("usuario");
        //reemplazado por la base de datos interna
        txt_correo.setText(usuario.getCorreo());
        txt_phone.setText(usuario.getTelefono());
        txt_location.setText(usuario.getDireccion());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.frag_btn_config_actualizar:
                actualizar_datos();
                break;
            case R.id.frag_btn_config_cs:
                cerrar_sesion();
                break;
        }
    }

    private void actualizar_datos() {
        if(!validar()) {
            return;
        }
        //precedemos a registar el usuario
        AsyncHttpClient ahc_usuario = new AsyncHttpClient();
        String s_url = "http://veterinaria-fankito.atwebpages.com/ws_res/actualizar_res.php";
        RequestParams params = new RequestParams();
        Hash hash = new Hash();
        Bundle datos = getArguments();
        datos = getArguments();
        usuario = (Usuario)datos.getSerializable("usuario");
        params.add("id_usuario", Integer.toString(usuario.getId_usuario()));
        params.add("telefono", txt_phone.getText().toString().trim());
        params.add("telefono", txt_phone.getText().toString().trim());
        params.add("direccion", txt_location.getText().toString().trim());
        params.add("correo", txt_correo.getText().toString().trim());
        params.add("clave", hash.StringToHash(txt_clave.getText().toString(), "SHA1"));
        ahc_usuario.post(s_url, params, new BaseJsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, Object response) {
                if(statusCode == 200){
                    int i_ret_val = rawJsonResponse.length() == 0 ? 0 : Integer.parseInt(rawJsonResponse);
                    if(i_ret_val == 1){
                        Toast.makeText(getContext(),"Successful Update!!", Toast.LENGTH_SHORT).show();
                        getActivity().finish();
                        Intent i_login = new Intent(getContext(), LoginActivity.class);
                        startActivity(i_login);
                    }
                    else
                        Toast.makeText(getContext(),"Failed to update", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, Object errorResponse) {
                Toast.makeText(getContext(), "Error"+ statusCode, Toast.LENGTH_SHORT).show();
            }

            @Override
            protected Object parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                return null;
            }
        });
    }


    private boolean validar() {

        if(txt_phone.getText().toString().isEmpty() || txt_phone.getText().toString().length()>9){
            Toast.makeText(getContext(), "Write a valid phone number",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(txt_correo.getText().toString().isEmpty()){
            Toast.makeText(getContext(), "Write a valid e-mail",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(txt_location.getText().toString().isEmpty()){
            Toast.makeText(getContext(), "Write a location",Toast.LENGTH_SHORT).show();
            return false;
        }
        Hash hash = new Hash();
        String clave_anterior = hash.StringToHash(txt_clave_anterior.getText().toString(),"SHA1");
        if(!clave_anterior.equals(usuario.getClave())){
            Toast.makeText(getContext(), "Wrong Password",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(txt_clave.getText().toString().isEmpty() || txt_clave_anterior.getText().toString().isEmpty()){
            Toast.makeText(getContext(), "Write your password in both ",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void cerrar_sesion() {

        Sesion sesion = new Sesion(getContext());
        sesion.eliminar_usuario(usuario.getId_usuario());
        getActivity().finish();
        Intent i_login = new Intent(getContext(), LoginActivity.class);
        startActivity(i_login);
    }

}