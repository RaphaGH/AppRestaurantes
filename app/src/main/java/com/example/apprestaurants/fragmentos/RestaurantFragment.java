package com.example.apprestaurants.fragmentos;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apprestaurants.R;
import com.example.apprestaurants.clases.AdapterComentario;
import com.example.apprestaurants.clases.Comentario;
import com.example.apprestaurants.clases.Restaurante;
import com.example.apprestaurants.clases.Usuario;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.Base64;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RestaurantFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RestaurantFragment extends Fragment implements View.OnClickListener {
    Restaurante restaurante;
    TextView txt_titulo, txt_descripcion;
    ImageView iv_rest;
    Button btn_comentar,btn_regresar;
    String viene_de;
    Usuario usuario;

    ArrayList<Comentario> listacomentarios;
    RecyclerView recycler;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RestaurantFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RestaurantFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RestaurantFragment newInstance(String param1, String param2) {
        RestaurantFragment fragment = new RestaurantFragment();
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
        View view = inflater.inflate(R.layout.fragment_restaurant, container, false);

        Bundle datos = getArguments();
        restaurante = (Restaurante) datos.getSerializable("restaurante");
        viene_de = datos.getString("fragmento");
        usuario = (Usuario)datos.getSerializable("usuario");
        //restaurante = (Restaurante) getActivity().getIntent().getSerializableExtra("restaurante");

        txt_titulo = view.findViewById(R.id.frg_res_titulo);
        txt_descripcion = view.findViewById(R.id.frg_res_descripcion);
        iv_rest = view.findViewById(R.id.frg_res_img);
        btn_comentar = view.findViewById(R.id.frg_res_btn_comentar);
        btn_regresar = view.findViewById(R.id.frg_res_btn_regresar);

        txt_titulo.setText(restaurante.getNombre());
        txt_descripcion.setText(restaurante.getDescripcion());

        String imagen = restaurante.getFoto();
        byte[] image_byte = Base64.decode(imagen, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(image_byte, 0, image_byte.length);
        iv_rest.setImageBitmap(bitmap);


        listacomentarios = new ArrayList<>();
        recycler = (RecyclerView) view.findViewById(R.id.frg_res_recycler_comments);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));

        llenar_comentarios();

        btn_comentar.setOnClickListener(this);
        btn_regresar.setOnClickListener(this);

        return view;
    }

    private void llenar_comentarios() {

        AsyncHttpClient ahc_com = new AsyncHttpClient();
        String s_url = "http://veterinaria-fankito.atwebpages.com/ws_res/mostrar_comentarios_de_restaurante.php";

        RequestParams params = new RequestParams();
        params.add("id_restaurante",Integer.toString(restaurante.getId_restaurante()));

        ahc_com.post(s_url, params, new BaseJsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, Object response) {
                try {
                    JSONArray jsonArray = new JSONArray(rawJsonResponse);
                    for(int i = 0; i < jsonArray.length(); i++){
                        listacomentarios.add(new Comentario(jsonArray.getJSONObject(i).getString("nombre"),jsonArray.getJSONObject(i).getString("comentario"),Float.parseFloat(jsonArray.getJSONObject(i).getString("puntaje"))));
                    }

                    AdapterComentario adapter = new AdapterComentario(listacomentarios);
                    recycler.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, Object errorResponse) {
                Toast.makeText(getContext(), "Error: " + statusCode, Toast.LENGTH_SHORT).show();
            }

            @Override
            protected Object parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                return null;
            }
        });

        /*
        if(restaurante.getNombre().equals("KiLO")){
            listacomentarios.add(new Comentario("Isaac","Muy bueno.", 4.8f));
            listacomentarios.add(new Comentario("Renato", "Carne muy buena.", 5.0f));
            listacomentarios.add(new Comentario("Franco", "A veces la vida te da limones.", 5.0f));
            listacomentarios.add(new Comentario("Eduardo", "Perfecto.", 5.0f));
        }
        if(restaurante.getNombre().equals("KU-MAR")){
            listacomentarios.add(new Comentario("Maritza","Excelente comida, la verdad muy weno.", 4.0f));
            listacomentarios.add(new Comentario("Giovanni", "Su causa es excelente.", 5.0f));
        }
        if(restaurante.getNombre().equals("Javier")){
            listacomentarios.add(new Comentario("Jean Pier","Vista hermosa al mar.", 5.0f));
            listacomentarios.add(new Comentario("Tontony", "Muy caro. No es accesible para todos.", 0.5f));
            listacomentarios.add(new Comentario("Leonardo", "La extraño.", 5.0f));

        }
        if(restaurante.getNombre().equals("Grimanesa")){
            listacomentarios.add(new Comentario("Jorge", "La respuesta esta en Wikipedia.", 5));
            listacomentarios.add(new Comentario("OxkarxitOxxs", "Llamé a mi ex.", 5.0f));
        }
        if(restaurante.getNombre().equals("Siete sopas")){
            listacomentarios.add(new Comentario("Raphael", "Sin comentarios.", 5.0f));
        }
        if(restaurante.getNombre().equals("Rústica")){
            listacomentarios.add(new Comentario("Bryan", "Buena atención al cliente.", 5.0f));
            listacomentarios.add(new Comentario("Lenin", "Corregir ortografia de su carta, -1 punto.", 3.0f));
            listacomentarios.add(new Comentario("Luis", "Buena atención al cliente.", 5.0f));
        }
        if(restaurante.getNombre().equals("Pardos Chicken")){
            listacomentarios.add(new Comentario("Juan", "Buena box con este restaurante.", 5.0f));
            listacomentarios.add(new Comentario("Jorge", "No me gusta el diseño de su carta, fondo muy oscuro.", 3.0f));
        }
        */
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.frg_res_btn_comentar:
                comentar();
                break;
            case R.id.frg_res_btn_regresar:
                regresar();
                break;
        }
    }

    private void regresar() {
        Bundle datos = new Bundle();
        datos.putSerializable("usuario",usuario);

        Fragment frg;
        if(viene_de.equals("nos")){
            frg = new RecNosFragment();
            frg.setArguments(datos);
            FragmentManager fm = getActivity().getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.men_rel_menu, frg);
            ft.commit();
        }else if(viene_de.equals("uds")){
            frg = new RecUdsFragment();
            frg.setArguments(datos);
            FragmentManager fm = getActivity().getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.men_rel_menu, frg);
            ft.commit();
        }
    }

    private void comentar() {
        //Intent i_comentar = new Intent(getContext(), ComentarActivity.class);
        //i_comentar.putExtra("restaurante", restaurante);
        //startActivity(i_comentar);


        Bundle datos = new Bundle();
        datos.putSerializable("restaurante",restaurante);
        datos.putSerializable("fragmento",viene_de);
        datos.putSerializable("usuario",usuario);

        Fragment comentar = new ComentarFragment();
        comentar.setArguments(datos);

        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.men_rel_menu, comentar);
        ft.commit();
    }
}