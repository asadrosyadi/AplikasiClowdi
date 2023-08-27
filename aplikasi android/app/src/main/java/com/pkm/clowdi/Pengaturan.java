package com.pkm.clowdi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Pengaturan extends AppCompatActivity {
    EditText nilai_kering, batas_suhu, batas_nilai_hujan, batas_nilai_siang, nama, password;
    TextView email, HWID, token;
    Button BtnSubmit, BtnBack;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengaturan);
        getSupportActionBar().hide();
        nilai_kering = findViewById((R.id.nilai_kering));
        batas_suhu = findViewById((R.id.batas_suhu));
        batas_nilai_hujan = findViewById((R.id.batas_nilai_hujan));
        batas_nilai_siang = findViewById((R.id.batas_nilai_siang));

        nama = findViewById((R.id.nama));
        email = findViewById((R.id.email));
        HWID = findViewById((R.id.HWID));
        token = findViewById((R.id.token));
        password = findViewById((R.id.password));


        BtnSubmit = findViewById(R.id.BtnSubmit);
        BtnBack = findViewById(R.id.BtnBack);
        ambilData();

        BtnBack.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent1 = getIntent();
                String HWID = intent1.getStringExtra("HWID");
                Intent intent = new Intent(Pengaturan.this, Home.class);
                intent.putExtra("HWID", HWID);
                startActivity(intent);            }
        });

        BtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateData();
            }
        });
    }
    private void updateData() {
        final String nilai_kering2 = nilai_kering.getText().toString().trim();
        final String batas_suhu2 = batas_suhu.getText().toString().trim();
        final String batas_nilai_hujan2 = batas_nilai_hujan.getText().toString().trim();
        final String batas_nilai_siang2 = batas_nilai_siang.getText().toString().trim();

        final String nama2 = nama.getText().toString().trim();
        final String email2 = email.getText().toString().trim();
        final String token2 = token.getText().toString().trim();
        final String password2 = password.getText().toString().trim();

        Intent intent = getIntent();
        String HWID = intent.getStringExtra("HWID");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://clowdi.my.id/rest/rest/updateuser/" + HWID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("sukses")) {
                            Intent intent1 = getIntent();
                            String updatedHWID = intent1.getStringExtra("HWID"); // Rename HWID to avoid confusion
                            Intent intent = new Intent(Pengaturan.this, Home.class);
                            Toast.makeText(getApplicationContext(), "Update Data Berhasil", Toast.LENGTH_LONG).show();
                            intent.putExtra("HWID", updatedHWID);
                            startActivity(intent);
                            finish();
                        } else if (response.contains("gagal")) {
                            Toast.makeText(getApplicationContext(), "Update Data Gagal", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error response
                        Toast.makeText(getApplicationContext(), "Update Data Gagal", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("nilai_kering", nilai_kering2);
                params.put("batas_suhu", batas_suhu2);
                params.put("batas_nilai_hujan", batas_nilai_hujan2);
                params.put("batas_nilai_siang", batas_nilai_siang2);

                params.put("nama", nama2);
                params.put("email", email2);
                params.put("token", token2);
                params.put("password", password2);

                params.put("HWID", HWID);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void ambilData() {

        Intent intent = getIntent();
        String HWID = intent.getStringExtra("HWID");
        String url = "https://clowdi.my.id/rest/rest/user/" + HWID;

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                showJSON(response);

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Pengaturan.this, error.getMessage(), Toast.LENGTH_LONG).show();

                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void showJSON(String response) {
        String nilai_kering2 = "";
        String batas_suhu2 = "";
        String batas_nilai_hujan2 = "";
        String batas_nilai_siang2 = "";

        String nama2 = "";
        String email2 = "";
        String HWID2 = "";
        String token2 = "";

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray("user");
            JSONObject collegeData = result.getJSONObject(0);
            nilai_kering2 = collegeData.getString("nilai_kering");
            batas_suhu2 = collegeData.getString("batas_suhu");
            batas_nilai_hujan2 = collegeData.getString("batas_nilai_hujan");
            batas_nilai_siang2 = collegeData.getString("batas_nilai_siang");

            nama2 = collegeData.getString("nama");
            email2 = collegeData.getString("email");
            HWID2 = collegeData.getString("HWID");
            token2 = collegeData.getString("token");


        } catch (JSONException e) {
            e.printStackTrace();
        }

        nilai_kering.setText(nilai_kering2);
        batas_suhu.setText(batas_suhu2);
        batas_nilai_hujan.setText(batas_nilai_hujan2);
        batas_nilai_siang.setText(batas_nilai_siang2);

        nama.setText(nama2);
        email.setText(email2);
        HWID.setText(HWID2);
        token.setText(token2);

    }
}