package com.example.apprestaurants.fragmentos;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.apprestaurants.R;
import com.example.apprestaurants.clases.AdapterRestaurantes;
import com.example.apprestaurants.clases.Restaurante;
import com.example.apprestaurants.clases.Usuario;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BaseJsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.Console;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecNosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecNosFragment extends Fragment {
    ArrayList<Restaurante> listRestaurantes;
    RecyclerView recycler;
    Usuario usuario;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RecNosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecNosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecNosFragment newInstance(String param1, String param2) {
        RecNosFragment fragment = new RecNosFragment();
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
        View frg_rec_nos = inflater.inflate(R.layout.fragment_rec_nos, container, false);

        Bundle datos = new Bundle();
        datos = getArguments();

        usuario = (Usuario) datos.getSerializable("usuario");

        listRestaurantes = new ArrayList<>();
        recycler = (RecyclerView) frg_rec_nos.findViewById(R.id.frg_rec_nos_recycler);
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        llenar_restaurantes();

        return frg_rec_nos;
    }

    private void llenar_restaurantes() {
        AsyncHttpClient ahc_res = new AsyncHttpClient();
        String s_url = "http://veterinaria-fankito.atwebpages.com/ws_res/reco_nos.php";

        ahc_res.post(s_url, new BaseJsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, Object response) {
                if(statusCode == 200){
                    try {
                        JSONArray jsonArray = new JSONArray(rawJsonResponse);
                        for (int i=0;i<jsonArray.length();i++){
                            listRestaurantes.add(new Restaurante(jsonArray.getJSONObject(i).getInt("id_restaurante"),jsonArray.getJSONObject(i).getString("nombre"),jsonArray.getJSONObject(i).getString("descripcion"),jsonArray.getJSONObject(i).getString("imagen"),Float.parseFloat(jsonArray.getJSONObject(i).getString("promedio"))));
                        }

                        AdapterRestaurantes adapter = new AdapterRestaurantes(listRestaurantes);

                        adapter.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(getActivity(), "Selección: " +
                                                listRestaurantes.get(recycler.getChildAdapterPosition(view)).getNombre()
                                        ,Toast.LENGTH_SHORT).show();

                                Bundle datos = new Bundle();
                                datos.putSerializable("restaurante",listRestaurantes.get(recycler.getChildAdapterPosition(view)));
                                datos.putString("fragmento","nos");
                                datos.putSerializable("usuario",usuario);


                                Fragment frg_res = new RestaurantFragment();
                                frg_res.setArguments(datos);

                                FragmentManager fm = getActivity().getSupportFragmentManager();
                                FragmentTransaction ft = fm.beginTransaction();
                                ft.replace(R.id.men_rel_menu, frg_res);
                                ft.commit();

                            }
                        });

                        recycler.setAdapter(adapter);

                        return;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, Object errorResponse) {
                Toast.makeText(getContext(), "Error: "+statusCode,Toast.LENGTH_SHORT).show();
            }

            @Override
            protected Object parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                return null;
            }
        });

        //listRestaurantes.add(new Restaurante("Kilo"));
    }
}