package com.pkm.clowdi;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.android.volley.Request;
import com.ekn.gruzer.gaugelibrary.ArcGauge;
import android.widget.Switch;
import android.widget.Toast;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ekn.gruzer.gaugelibrary.Range;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Home extends AppCompatActivity {
    TextView idnama, time,kondisimesin, modemesin, kondisi, idsuhuluar, idsuhudalam, idldr,jemuran,heater,kesimpulanraindrop, idsuhuudara, kesimpulanldr;
    Button Btnpengaturan,Btnhistory;
    private ArcGauge arcGauge;
    private float minGaugeValue = 0.0f; // Batas minimal
    private float maxGaugeValue = 100.0f; // Batas maksimal

    private Switch switchMesin, switchmesin2, switchlengan, switchheater;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();
        Btnpengaturan = findViewById(R.id.Btnpengaturan);
        Btnhistory = findViewById(R.id.Btnhistory);
        idnama = findViewById((R.id.idnama));
        time = findViewById((R.id.time));
        kondisimesin = findViewById((R.id.kondisimesin));
        modemesin = findViewById((R.id.modemesin));
        kondisi = findViewById((R.id.kondisi));
        idsuhuluar = findViewById((R.id.idsuhuluar));
        idsuhudalam = findViewById((R.id.idsuhudalam));
        idldr = findViewById((R.id.idldr));
        jemuran = findViewById((R.id.jemuran));
        heater = findViewById((R.id.heater));
        kesimpulanraindrop = findViewById((R.id.kesimpulanraindrop));
        idsuhuudara = findViewById((R.id.idsuhuudara));
        kesimpulanldr = findViewById((R.id.kesimpulanldr));

        arcGauge = findViewById(R.id.arcGaugenilai);
        arcGauge.setMaxValue(maxGaugeValue);
        arcGauge.setMinValue(minGaugeValue);
        Range range = new Range();
        range.setColor(Color.parseColor("#19288A"));
        range.setFrom(0.0);
        range.setTo(100.0);
        arcGauge.addRange(range);


        switchMesin = findViewById(R.id.switchmesin);
        switchmesin2 = findViewById(R.id.switchmesin2);
        switchlengan = findViewById(R.id.switchlengan);
        switchheater = findViewById(R.id.switchheater);





        Btnpengaturan.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Bundle bundle = getIntent().getExtras();
                String HWID = null;
                if (bundle != null) {
                    HWID = bundle.getString("HWID");
                }
                Intent intent = new Intent(Home.this, Pengaturan.class);
                intent.putExtra("HWID", HWID);
                startActivity(intent);
            }
        });

        Btnhistory.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Bundle bundle = getIntent().getExtras();
                String HWID = null;
                if (bundle != null) {
                    HWID = bundle.getString("HWID");
                }
                Intent intent = new Intent(Home.this, historysensor.class);
                intent.putExtra("HWID", HWID);
                startActivity(intent);
            }
        });

        switchMesin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    UpdateswitchMesinOn();
                    switchmesin2.setEnabled(true);
                    switchlengan.setEnabled(true);
                    switchheater.setEnabled(true);
                } else {
                    UpdateswitchMesinOff();
                    switchmesin2.setEnabled(false);
                    switchlengan.setEnabled(false);
                    switchheater.setEnabled(false);
                }
            }
        });

        switchmesin2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    UpdateswitchMesin2On();
                    switchlengan.setEnabled(false);
                    switchheater.setEnabled(false);
                } else {
                    UpdateswitchMesin2Off();
                    switchlengan.setEnabled(true);
                    switchheater.setEnabled(true);
                }
            }
        });

        switchlengan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    UpdateswitchlenganOn();
                } else {
                    UpdateswitchlenganOff();
                }
            }
        });

        switchheater.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    UpdateswitchheaterOn();
                } else {
                    UpdateswitchheaterOff();
                }
            }
        });


        ambilData();
    }

    Handler handler = new Handler();
    Runnable runnable;
    int delay = 15*1000; //Delay for 15 seconds.  One second = 1000 milliseconds.


    @Override
    protected void onResume() {
        //start handler as activity become visible

        handler.postDelayed( runnable = new Runnable() {
            public void run() {
                //do something
                ambilData();
                handler.postDelayed(runnable, delay);
            }
        }, delay);

        super.onResume();
    }

// If onPause() is not included the threads will double up when you
// reload the activity

    @Override
    protected void onPause() {
        handler.removeCallbacks(runnable); //stop handler when activity not visible
        super.onPause();
    }

    private void ambilData() {
        Bundle bundle = getIntent().getExtras();
        String HWID = null;
        if (bundle != null) {
            HWID = bundle.getString("HWID");
        } else {
            Intent intent = getIntent();
            HWID = intent.getStringExtra("HWID");}

        String url = "https://clowdi.my.id/rest/rest/user/" + HWID;
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                showsensor(response);

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(Home.this, error.getMessage(), Toast.LENGTH_LONG).show();

                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void showsensor(String response) {
        String idnama2 = "";
        String time2 = "";
        String kondisimesin2 = "";
        String modemesin2 = "";
        String idsuhuluar2 = "";
        String idsuhudalam2 = "";
        String idldr2 = "";
        String jemuran2 = "";
        String heater2 = "";
        String rh = "";
        String nilai_kering = "";
        String kesimpulanraindrop2 = "";
        String idsuhuudara2= "";
        String kesimpulanldr2= "";



        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray("user");
            JSONObject collegeData = result.getJSONObject(0);
            idnama2 = collegeData.getString("nama");
            time2 = collegeData.getString("time");
            kondisimesin2 = collegeData.getString("kondisi_mesin");
            modemesin2 = collegeData.getString("mode_mesin");
            idsuhuluar2 = collegeData.getString("suhu_luar");
            idsuhudalam2 = collegeData.getString("suhu_dalam");
            idldr2 = collegeData.getString("ldr");
            jemuran2 = collegeData.getString("kondisi_jemuran");
            heater2 = collegeData.getString("heater");
            rh = collegeData.getString("rh");
            nilai_kering = collegeData.getString("nilai_kering");
            kesimpulanraindrop2 = collegeData.getString("batas_nilai_hujan");
            idsuhuudara2 = collegeData.getString("rain_drop");
            kesimpulanldr2 = collegeData.getString("batas_nilai_siang");


        } catch (JSONException e) {
            e.printStackTrace();
        }

        idnama.setText(idnama2);
        time.setText(time2);
        kondisimesin.setText(kondisimesin2);
        if ("ON".equals(kondisimesin2)) {switchMesin.setChecked(true);} else {switchMesin.setChecked(false);}
        modemesin.setText(modemesin2);
        if ("Otomatis".equals(modemesin2)) {switchmesin2.setChecked(true);} else {switchmesin2.setChecked(false);}
        idsuhuluar.setText(idsuhuluar2);
        idsuhudalam.setText(idsuhudalam2);
        idldr.setText(idldr2);
        jemuran.setText(jemuran2);
        if ("Luar".equals(jemuran2)) {switchlengan.setChecked(true);} else {switchlengan.setChecked(false);}
        heater.setText(heater2);
        if ("ON".equals(heater2)) {switchheater.setChecked(true);} else {switchheater.setChecked(false);}
        idsuhuudara.setText(idsuhuudara2);
        arcGauge.setValue(Float.parseFloat(rh));


        float rhValue = Float.parseFloat(rh);
        float kering = Float.parseFloat(nilai_kering);
        if (rhValue < kering) {kondisi.setText("kering");}
        else {kondisi.setText("Basah");}

        float batas_nilai_hujan = Float.parseFloat(kesimpulanraindrop2);
        float idsuhuudara3 = Float.parseFloat(idsuhuudara2);
        if (idsuhuudara3 < batas_nilai_hujan) {kesimpulanraindrop.setText("hujan");}
        else{kesimpulanraindrop.setText("Terang");}

        float batas_nilai_siang = Float.parseFloat(kesimpulanldr2);
        float idldr3 = Float.parseFloat(idldr2);
        if (idldr3 < batas_nilai_siang) {kesimpulanldr.setText("Malam");}
        else{kesimpulanldr.setText("Siang");}

    }

    private void UpdateswitchMesinOn(){
        final String kondisi_mesin = "ON";

        Intent intent = getIntent();
        String HWID = intent.getStringExtra("HWID");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://clowdi.my.id/rest/rest/updateswitchmesin/" + HWID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("sukses")) {
                            Toast.makeText(getApplicationContext(), "Update Data Berhasil", Toast.LENGTH_LONG).show();
                        }
                        if (response.contains("gagal")) {

                            Toast.makeText(getApplicationContext(), "Update Data Gagal", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("kondisi_mesin", kondisi_mesin);
                params.put("HWID", HWID);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void UpdateswitchMesinOff(){
        final String kondisi_mesin = "OFF";

        Intent intent = getIntent();
        String HWID = intent.getStringExtra("HWID");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://clowdi.my.id/rest/rest/updateswitchmesin/" + HWID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("sukses")) {
                            Toast.makeText(getApplicationContext(), "Update Data Berhasil", Toast.LENGTH_LONG).show();
                        }
                        if (response.contains("gagal")) {

                            Toast.makeText(getApplicationContext(), "Update Data Gagal", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("kondisi_mesin", kondisi_mesin);
                params.put("HWID", HWID);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void UpdateswitchMesin2On(){
        final String mode_mesin = "Otomatis";

        Intent intent = getIntent();
        String HWID = intent.getStringExtra("HWID");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://clowdi.my.id/rest/rest/updateswitchmesin2/" + HWID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("sukses")) {
                            Toast.makeText(getApplicationContext(), "Update Data Berhasil", Toast.LENGTH_LONG).show();
                        }
                        if (response.contains("gagal")) {

                            Toast.makeText(getApplicationContext(), "Update Data Gagal", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("mode_mesin", mode_mesin);
                params.put("HWID", HWID);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void UpdateswitchMesin2Off(){
        final String mode_mesin = "Manual";

        Intent intent = getIntent();
        String HWID = intent.getStringExtra("HWID");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://clowdi.my.id/rest/rest/updateswitchmesin2/" + HWID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("sukses")) {
                            Toast.makeText(getApplicationContext(), "Update Data Berhasil", Toast.LENGTH_LONG).show();
                        }
                        if (response.contains("gagal")) {

                            Toast.makeText(getApplicationContext(), "Update Data Gagal", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("mode_mesin", mode_mesin);
                params.put("HWID", HWID);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void UpdateswitchlenganOn(){
        final String kondisi_jemuran = "Luar";

        Intent intent = getIntent();
        String HWID = intent.getStringExtra("HWID");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://clowdi.my.id/rest/rest/updateswitchlengan/" + HWID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("sukses")) {
                            Toast.makeText(getApplicationContext(), "Update Data Berhasil", Toast.LENGTH_LONG).show();
                        }
                        if (response.contains("gagal")) {

                            Toast.makeText(getApplicationContext(), "Update Data Gagal", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("kondisi_jemuran", kondisi_jemuran);
                params.put("HWID", HWID);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void UpdateswitchlenganOff(){
        final String kondisi_jemuran = "Dalam";

        Intent intent = getIntent();
        String HWID = intent.getStringExtra("HWID");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://clowdi.my.id/rest/rest/updateswitchlengan/" + HWID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("sukses")) {
                            Toast.makeText(getApplicationContext(), "Update Data Berhasil", Toast.LENGTH_LONG).show();
                        }
                        if (response.contains("gagal")) {

                            Toast.makeText(getApplicationContext(), "Update Data Gagal", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("kondisi_jemuran", kondisi_jemuran);
                params.put("HWID", HWID);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void UpdateswitchheaterOn(){
        final String heater = "ON";

        Intent intent = getIntent();
        String HWID = intent.getStringExtra("HWID");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://clowdi.my.id/rest/rest/updateswitchheater/" + HWID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("sukses")) {
                            Toast.makeText(getApplicationContext(), "Update Data Berhasil", Toast.LENGTH_LONG).show();
                        }
                        if (response.contains("gagal")) {

                            Toast.makeText(getApplicationContext(), "Update Data Gagal", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("heater", heater);
                params.put("HWID", HWID);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void UpdateswitchheaterOff(){
        final String heater = "OFF";

        Intent intent = getIntent();
        String HWID = intent.getStringExtra("HWID");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://clowdi.my.id/rest/rest/updateswitchheater/" + HWID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("sukses")) {
                            Toast.makeText(getApplicationContext(), "Update Data Berhasil", Toast.LENGTH_LONG).show();
                        }
                        if (response.contains("gagal")) {

                            Toast.makeText(getApplicationContext(), "Update Data Gagal", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("heater", heater);
                params.put("HWID", HWID);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}