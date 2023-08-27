package com.pkm.clowdi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class daftar extends AppCompatActivity {
    EditText nama, email, HWID, password;
    Button btndaftar, btnkembali;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar);
        getSupportActionBar().hide();
        nama = findViewById((R.id.nama));
        email = findViewById((R.id.email));
        HWID = findViewById((R.id.HWID));
        password = findViewById((R.id.password));

        btndaftar = findViewById(R.id.btndaftar);
        btnkembali = findViewById(R.id.btnkembali);

        btnkembali.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(daftar.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btndaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KirimData();
            }
        });

    }


    private void KirimData(){
        final String nama2 = nama.getText().toString().trim();
        final String email2 = email.getText().toString().trim();
        final String HWID2 = HWID.getText().toString().trim();
        final String password2 = password.getText().toString().trim();

        String queryParams ="?&&nama=" + nama2 + "&&email=" + email2 + " &&HWID=" + HWID2 + "&&password=" + password2;

        String url = "https://clowdi.my.id/rest/rest/buatuser" + queryParams;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Tangani respons dari server di sini
                        if (response.contains("sukses")) {
                            Intent intent = new Intent(daftar.this, MainActivity.class);
                            Toast.makeText(getApplicationContext(), "Akun Sukses Dibuat", Toast.LENGTH_LONG).show();
                            startActivity(intent);
                            finish();
                        }
                        if (response.contains("HWID atau Email Sudah Terdaftar")) {
                            Toast.makeText(getApplicationContext(), "Email atau HWID Sudah Terdaftar", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Tangani error di sini
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}