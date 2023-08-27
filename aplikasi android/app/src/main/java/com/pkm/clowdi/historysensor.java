package com.pkm.clowdi;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
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


import java.util.ArrayList;
import java.util.List;

public class historysensor extends AppCompatActivity {
    public class SensorData  {
        private String time_text_view;
        private String rh_text_view;
        private String suhu_luar;
        private String suhu_dalam;
        private String raindrop;
        private String ldr;
        private String kondisijemuran;
        private String menyala;
        private String modemesin;

        public SensorData(String time_text_view, String rh_text_view, String suhu_luar, String suhu_dalam, String raindrop, String ldr, String kondisijemuran, String menyala, String modemesin) {
            this.time_text_view = time_text_view;
            this.rh_text_view = rh_text_view;
            this.suhu_luar = suhu_luar;
            this.suhu_dalam = suhu_dalam;
            this.raindrop = raindrop;
            this.ldr = ldr;
            this.kondisijemuran = kondisijemuran;
            this.menyala = menyala;
            this.modemesin = modemesin;
        }

        public String gettime_text_view() {
            return time_text_view;
        }

        public String getrh_text_view() {
            return rh_text_view;
        }

        public String getsuhu_luar() {
            return suhu_luar;
        }

        public String getraindrop() {
            return raindrop;
        }

        public String getldr() {
            return ldr;
        }

        public String getkondisijemuran() {
            return kondisijemuran;
        }

        public String getmenyala() {
            return menyala;
        }

        public String getmodemesin() {
            return modemesin;
        }

    }
    public class SensorDataAdapter extends ArrayAdapter<SensorData> {

        private List<SensorData> dataList;

        public SensorDataAdapter(Context context, List<SensorData> dataList) {
            super(context, 0, dataList);
            this.dataList = dataList;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
            }

            TextView time_text_view = convertView.findViewById(R.id.time_text_view);
            TextView rh_text_view = convertView.findViewById(R.id.rh_text_view);
            TextView suhu_luar = convertView.findViewById(R.id.suhu_luar);
            TextView raindrop = convertView.findViewById(R.id.raindrop);
            TextView ldr = convertView.findViewById(R.id.ldr);
            TextView kondisijemuran = convertView.findViewById(R.id.kondisijemuran);
            TextView menyala = convertView.findViewById(R.id.menyala);
            TextView modemesin = convertView.findViewById(R.id.modemesin);

            SensorData data = dataList.get(position);

            time_text_view.setText(data.gettime_text_view());
            rh_text_view.setText(data.getrh_text_view());
            suhu_luar.setText(data.getsuhu_luar());
            raindrop.setText(data.getraindrop());
            ldr.setText(data.getldr());
            kondisijemuran.setText(data.getkondisijemuran());
            menyala.setText(data.getmenyala());
            modemesin.setText(data.getmodemesin());

            return convertView;
        }
    }
    Button pindahRumah;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historysensor);
        pindahRumah = findViewById(R.id.pindahRumah);

        pindahRumah.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Bundle bundle = getIntent().getExtras();
                String HWID = null;
                if (bundle != null) {
                    HWID = bundle.getString("HWID");
                }
                Intent intent = new Intent(historysensor.this, Home.class);
                intent.putExtra("HWID", HWID);
                startActivity(intent);
            }
        });

        RequestQueue queue = Volley.newRequestQueue(this);
        Intent intent = getIntent();
        String HWID = intent.getStringExtra("HWID");
        String url = "https://clowdi.my.id/rest/rest/log_sensor/" + HWID;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ArrayList<SensorData> data = new ArrayList<>();
                    String jsonData = response.toString();

                    try {
                    JSONObject jsonObject = new JSONObject(jsonData);
                    JSONArray jsonArray = jsonObject.getJSONArray("log_sensor");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject sensorJson = jsonArray.getJSONObject(i);
                        SensorData sensorData = new SensorData(
                                sensorJson.getString("time"),
                                sensorJson.getString("rh"),
                                sensorJson.getString("suhu_luar"),
                                sensorJson.getString("suhu_dalam"),
                                sensorJson.getString("rain_drop"),
                                sensorJson.getString("ldr"),
                                sensorJson.getString("kondisi_jemuran"),
                                sensorJson.getString("kondisi_heater"),
                                sensorJson.getString("kondisi_mesin")
                        );
                        data.add(sensorData);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                SensorDataAdapter adapter = new SensorDataAdapter(historysensor.this, data);
                ListView listView = (ListView) findViewById(R.id.listView);
                listView.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(historysensor.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(stringRequest);

    }

}