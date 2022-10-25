package com.example.apprestaurants.fragmentos;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apprestaurants.R;
import com.example.apprestaurants.clases.Restaurante;
import com.example.apprestaurants.clases.Usuario;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;

import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ComentarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ComentarFragment extends Fragment implements View.OnClickListener {
    Restaurante restaurante;
    TextView lbl_titulo;
    EditText txt_puntaje, txt_comentario;
    Button btn_comentar, btn_regresar;
    String viene_de;
    Usuario usuario;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ComentarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ComentarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ComentarFragment newInstance(String param1, String param2) {
        ComentarFragment fragment = new ComentarFragment();
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
        View view = inflater.inflate(R.layout.fragment_comentar, container, false);

        Bundle datos = getArguments();

        restaurante = (Restaurante) datos.getSerializable("restaurante");
        viene_de = datos.getString("fragmento");
        usuario = (Usuario)datos.getSerializable("usuario");

        //restaurante = (Restaurante) getIntent().getSerializableExtra("restaurante");
        lbl_titulo = view.findViewById(R.id.frg_comentar_titulo);
        btn_comentar = view.findViewById(R.id.frg_comentar_enviar);
        btn_regresar = view.findViewById(R.id.frg_comentar_regresar);
        txt_puntaje = view.findViewById(R.id.frg_comentar_txt_puntaje);
        txt_comentario = view.findViewById(R.id.frg_comentar_txt_comentario);


        lbl_titulo.setText("Rate "+restaurante.getNombre()+ "!");
        btn_comentar.setOnClickListener(this);
        btn_regresar.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.frg_comentar_regresar:
                regresar();
                break;
            case R.id.frg_comentar_enviar:
                enviar();
                break;
        }
    }

    private void enviar() {
        if(validar()){
            AsyncHttpClient ahc_comment = new AsyncHttpClient();
            String s_url = "http://veterinaria-fankito.atwebpages.com/ws_res/com_res.php";

            RequestParams params = new RequestParams();
            params.add("id_usuario",Integer.toString(usuario.getId_usuario()));
            params.add("id_restaurante",Integer.toString(restaurante.getId_restaurante()));
            params.add("puntaje", txt_puntaje.getText().toString().trim());
            params.add("comentario",txt_comentario.getText().toString().trim());

            ahc_comment.post(s_url, params, new BaseJsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, Object response) {
                    try {
                        JSONArray jsonArray = new JSONArray(rawJsonResponse);
                        if(jsonArray.getJSONObject(0).getInt("id_comentario")==1){
                            Toast.makeText(getContext(),"Comment registered successfully",Toast.LENGTH_SHORT).show();
                        }
                        if(jsonArray.getJSONObject(0).getInt("id_comentario")==-1){
                            Toast.makeText(getContext(),"You already rated this restaurant!",Toast.LENGTH_SHORT).show();
                        }
                        regresar();
                    } catch (JSONException e) {
                        e.printStackTrace();
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

    private boolean validar() {
        if(txt_puntaje.getText().toString().isEmpty()){
            Toast.makeText(getContext(), "Write a score for this review",Toast.LENGTH_SHORT).show();
            return false;
        }

        if(!(Float.parseFloat(txt_puntaje.getText().toString().trim())>=0f && Float.parseFloat(txt_puntaje.getText().toString().trim())<=5f)){
            Toast.makeText(getContext(), "Write a value between 0 and 5",Toast.LENGTH_SHORT).show();
            return false;
        }

        if(txt_comentario.getText().toString().isEmpty()){
            Toast.makeText(getContext(), "Write a comment for this review",Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }


    private void regresar() {
        Bundle datos = new Bundle();
        datos.putSerializable("restaurante",restaurante);
        datos.putString("fragmento", viene_de);
        datos.putSerializable("usuario", usuario);

        Fragment frg_res = new RestaurantFragment();
        frg_res.setArguments(datos);

        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.men_rel_menu, frg_res);
        ft.commit();
    }
}