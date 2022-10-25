package com.example.apprestaurants.actividades;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.apprestaurants.R;
import com.example.apprestaurants.clases.Hash;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.Calendar;

import cz.msebera.android.httpclient.Header;

public class RegistroActivity extends AppCompatActivity implements View.OnClickListener {
    EditText txt_dni, txt_nombre, txt_apellidos, txt_telefono, txt_correo, txt_fecha_nac, txt_direccion, txt_clave, txt_conf_clave;
    DatePickerDialog dpd_fecha_nac;
    RadioGroup rg_sexo;
    RadioButton rb_nd, rb_m, rb_f;
    CheckBox chk_terminos;
    Button btn_registrar, btn_regresar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        txt_dni = findViewById(R.id.reg_txt_dni);
        txt_nombre = findViewById(R.id.reg_txt_nombre);
        txt_apellidos = findViewById(R.id.reg_txt_apellidos);
        txt_telefono = findViewById(R.id.reg_txt_telefono);
        txt_correo = findViewById(R.id.reg_txt_correo);
        txt_fecha_nac = findViewById(R.id.reg_txt_fechanacimiento);
        rg_sexo = findViewById(R.id.reg_rg_sexo);
        rb_nd = findViewById(R.id.reg_rb_nd);
        rb_m = findViewById(R.id.reg_rb_m);
        rb_f = findViewById(R.id.reg_rb_f);
        txt_direccion = findViewById(R.id.reg_txt_direccion);
        txt_clave = findViewById(R.id.reg_txt_clave);
        txt_conf_clave = findViewById(R.id.reg_txt_conf_clave);
        btn_registrar = findViewById(R.id.reg_btn_registrar);
        btn_regresar = findViewById(R.id.reg_btn_regresar);
        chk_terminos = findViewById(R.id.reg_chk_terminos);
        
        btn_registrar.setOnClickListener(this);
        btn_regresar.setOnClickListener(this);
        txt_fecha_nac.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.reg_txt_fechanacimiento:
                cargar_selector_fechas();
                break;
            case R.id.reg_btn_registrar:
                registrar_usuario();
                break;
            case R.id.reg_btn_regresar:
                regresar();
                break;
        }
    }

    private void regresar() {
        Intent i_login = new Intent(this, LoginActivity.class);
        startActivity(i_login);
        finish();
    }

    private void registrar_usuario() {
        if(!validar()) {
            Toast.makeText(getApplicationContext(),"Falta completar", Toast.LENGTH_SHORT).show();
            return;
        }
        //precedemos a registar el usuario
        AsyncHttpClient ahc_usuario = new AsyncHttpClient();
        String s_url = "http://veterinaria-fankito.atwebpages.com/ws_res/usuario_res.php";
        RequestParams params = new RequestParams();
        Hash hash = new Hash();
        char c_sexo = 'X';
        params.add("nombre", txt_nombre.getText().toString().trim());
        params.add("apellidos", txt_apellidos.getText().toString().trim());
        int i_rb_seleccionado = rg_sexo.getCheckedRadioButtonId();
        switch (i_rb_seleccionado){
            case R.id.reg_rb_m: c_sexo = 'M'; break;
            case R.id.reg_rb_f: c_sexo = 'F'; break;
            case R.id.reg_rb_nd: c_sexo = 'X'; break;
        }
        params.add("sexo", String.valueOf(c_sexo));
        params.add("fecha_nacimiento", txt_fecha_nac.getText().toString().trim());
        params.add("telefono", txt_telefono.getText().toString().trim());
        params.add("direccion", txt_direccion.getText().toString().trim());
        params.add("correo", txt_correo.getText().toString().trim());
        params.add("clave", hash.StringToHash(txt_clave.getText().toString(), "SHA1"));
        params.add("documento", txt_dni.getText().toString().trim());

        ahc_usuario.post(s_url, params, new BaseJsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, Object response) {
                if(statusCode == 200){
                    int i_ret_val = rawJsonResponse.length() == 0 ? 0 : Integer.parseInt(rawJsonResponse);
                    if(i_ret_val == 1){
                        Toast.makeText(getApplicationContext(),"Successful Registration!!", Toast.LENGTH_SHORT).show();
                        finish();
                        Intent i_login = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(i_login);
                    }
                    else
                        Toast.makeText(getApplicationContext(),"Failed to register", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, Object errorResponse) {
                Toast.makeText(getApplicationContext(), "Error"+ statusCode, Toast.LENGTH_SHORT).show();
            }

            @Override
            protected Object parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                return null;
            }
        });
    }
    private boolean validar() {
        if(txt_dni.getText().toString().isEmpty() ||
                (txt_dni.getText().toString().trim().length()!=8 && txt_dni.getText().toString().trim().length()!=11)){
            Toast.makeText(this, "Invalid document",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(txt_nombre.getText().toString().isEmpty()){
            Toast.makeText(this, "Write a valid name",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(txt_apellidos.getText().toString().isEmpty()){
            Toast.makeText(this, "Write a valid last name",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(txt_telefono.getText().toString().isEmpty() || txt_telefono.getText().toString().length()>9){
            Toast.makeText(this, "Write a valid phone number",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(txt_correo.getText().toString().isEmpty()){
            Toast.makeText(this, "Write a valid e-mail",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(txt_fecha_nac.getText().toString().isEmpty()){
            Toast.makeText(this, "Select a birthday",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(txt_direccion.getText().toString().isEmpty()){
            Toast.makeText(this, "Write a location",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!txt_clave.getText().toString().equals(txt_conf_clave.getText().toString())){
            Toast.makeText(this, "Passwords do not match",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(txt_clave.getText().toString().isEmpty() || txt_conf_clave.getText().toString().isEmpty()){
            Toast.makeText(this, "Write your password in both ",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!chk_terminos.isChecked()){
            Toast.makeText(this, "You must agree the terms and conditions",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void cargar_selector_fechas() {
        final Calendar calendar = Calendar.getInstance();
        int dia = calendar.get(Calendar.DAY_OF_MONTH);
        int mes = calendar.get(Calendar.MONTH);
        int año = calendar.get(Calendar.YEAR);
        //cargar el calendario con la fecha actual
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            dpd_fecha_nac = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int i_año, int i_mes, int i_dia) {
                    //poner la fecha en edit text
                    txt_fecha_nac.setText(i_año+"-"+((i_mes+1)<10? "0"+(i_mes+1):(i_mes+1))+"-"+(i_dia < 10 ? "0"+i_dia:i_dia));
                }
            }, año, mes, dia);
        }
        dpd_fecha_nac.show();
    }
}