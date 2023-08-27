package com.pkm.clowdi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    Button btnLogin, btndaftar;
    EditText edtHWID, edtPassword;
    String HWID = "";
    String password = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        btnLogin = findViewById(R.id.btnLogin);
        btndaftar = findViewById(R.id.btndaftar);
        edtHWID = findViewById(R.id.edtHWID);
        edtPassword = findViewById(R.id.edtpassword);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtHWID.getText().toString().matches("") && edtPassword.getText().toString().matches("" ))
                {
                    Toast.makeText(getApplicationContext(), "HWID dan Password tidak boleh kosong", Toast.LENGTH_LONG).show();

                }
                else
                {

                    login();
                }
            }
        });

        btndaftar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, daftar.class);
                startActivity(intent);
            }
        });

    }

    private void login() {
        final String HWID = edtHWID.getText().toString().trim();
        final String password = edtPassword.getText().toString().trim();
        String url = "https://clowdi.my.id/rest/auth/rest";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("Sukses")) {

                            SharedPreferences sharedPreferences = MainActivity.this.getSharedPreferences("My Farm", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("loggedin", true);
                            editor.putString("HWID", HWID);
                            editor.commit();
                            Intent intent = new Intent(MainActivity.this, Home.class);
                            intent.putExtra("HWID", HWID);
                            startActivity(intent);
                            finish();
                        }
                        if (response.contains("Gagal")) {
                            Toast.makeText(getApplicationContext(), "HWID atau Password Salah", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("HWID", HWID);
                params.put("password", password);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }


}