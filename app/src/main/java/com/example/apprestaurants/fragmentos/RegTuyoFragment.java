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
import com.example.apprestaurants.actividades.BienvenidaActivity;
import com.example.apprestaurants.actividades.MenuActivity;
import com.example.apprestaurants.clases.Usuario;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;

import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegTuyoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegTuyoFragment extends Fragment implements View.OnClickListener {
    EditText txt_nombre, txt_tipo, txt_telefono, txt_direccion, txt_comentario;
    Button btn_enviar, btn_regresar;
    Usuario usuario;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RegTuyoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegTuyoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegTuyoFragment newInstance(String param1, String param2) {
        RegTuyoFragment fragment = new RegTuyoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View frg_reg_tuyo = inflater.inflate(R.layout.fragment_reg_tuyo, container, false);

        Bundle datos = new Bundle();
        datos = getArguments();

        usuario = (Usuario) datos.getSerializable("usuario");


        txt_nombre=frg_reg_tuyo.findViewById(R.id.frag_reg_tuyo_txt_name);
        txt_tipo=frg_reg_tuyo.findViewById(R.id.frag_reg_tuyo_txt_tipo);
        txt_telefono=frg_reg_tuyo.findViewById(R.id.frag_reg_tuyo_txt_telefono);
        txt_direccion=frg_reg_tuyo.findViewById(R.id.frag_reg_tuyo_txt_direccion);
        txt_comentario=frg_reg_tuyo.findViewById(R.id.frag_reg_tuyo_txt_comentario);
        btn_enviar=frg_reg_tuyo.findViewById(R.id.frag_reg_tuyo_btn_registrar);
        btn_regresar=frg_reg_tuyo.findViewById(R.id.frag_reg_tuyo_btn_regresar);


        btn_enviar.setOnClickListener(this);
        btn_regresar.setOnClickListener(this);


        return frg_reg_tuyo;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.frag_reg_tuyo_btn_registrar:
                enviar_formulario();
                break;
            case R.id.frag_reg_tuyo_btn_regresar:
                regresar_formulario();
                break;
        }
    }

    private void regresar_formulario() {
        Intent i_menu = new Intent(getContext(), BienvenidaActivity.class);
        i_menu.putExtra("usuario",usuario);
        startActivity(i_menu);
        getActivity().finish();
    }

    private void enviar_formulario() {
        if(!validar_formulario()) {
            return;
        }
        else{

            AsyncHttpClient ahc_tuyo = new AsyncHttpClient();
            String s_url = "http://veterinaria-fankito.atwebpages.com/ws_res/con_solicitar_restaurante.php";

            RequestParams params = new RequestParams();
            params.add("nombre",txt_nombre.getText().toString().trim());
            params.add("tipo_comida", txt_tipo.getText().toString().trim());
            params.add("telefono",txt_telefono.getText().toString().trim());
            params.add("direccion",txt_direccion.getText().toString().trim());
            params.add("nota",txt_comentario.getText().toString().trim());
            params.add("id_usuario",Integer.toString(usuario.getId_usuario()));

            ahc_tuyo.post(s_url, params, new BaseJsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, Object response) {
                    if(statusCode==200){
                        Toast.makeText(getContext(), "Sent successfully!", Toast.LENGTH_SHORT).show();
                        regresar_formulario();
                    }else{
                        Toast.makeText(getContext(),"Something went wrong",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, Object errorResponse) {
                    Toast.makeText(getContext(),"Error: "+statusCode,Toast.LENGTH_SHORT).show();
                }

                @Override
                protected Object parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                    return null;
                }
            });
        }

    }

    private boolean validar_formulario(){
        if(txt_nombre.getText().toString().isEmpty() || txt_tipo.getText().toString().isEmpty() ||
                txt_telefono.getText().toString().isEmpty() || txt_direccion.getText().toString().isEmpty() ||
                txt_comentario.getText().toString().isEmpty()){
            Toast.makeText(getContext(), "Complete the spaces empty", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(txt_telefono.getText().toString().length() > 9  || txt_telefono.getText().toString().length() < 7 ||
                txt_telefono.getText().toString().length() == 8 ){
            Toast.makeText(getContext(), "Enter the correct digits", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;

    }

}