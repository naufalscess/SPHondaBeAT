package com.example.sphondabeat.admin;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.request.JsonObjectRequest;
import com.example.sphondabeat.MySingleton;
import com.example.sphondabeat.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AturanEditActivity extends AppCompatActivity {

    private ProgressDialog pDialog;
    private static final String url = "http://192.168.233.132/sphonda/sp_honda/get_daftar_kendala.php";
    private static final String url_update = "http://192.168.233.132/sphonda/sp_honda/update_aturan.php";
    private MyCustomAdapter dataAdapter = null;
    private ArrayList<Kendala> kendalaList = new ArrayList<Kendala>();
    private Kendala kendala;
    private String id_kerusakan;
    private String nama_kerusakan = "";
    private StringBuffer responseText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aturan_edit);
        setTitle("Atur Ulang Kendala");

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id_kerusakan = extras.getString("id_kerusakan");
            nama_kerusakan = extras.getString("nama_kerusakan");
        }

        TextView tv_nama_kerusakan = findViewById(R.id.tv_nama_kerusakan);
        tv_nama_kerusakan.setText(nama_kerusakan);

        getDaftarKendala();

        Button btn_simpan = findViewById(R.id.btn_simpan);
        btn_simpan.setOnClickListener(v -> {
            responseText = new StringBuffer();
            if (!kendalaList.isEmpty()) {
                ArrayList<Kendala> kendalaList2 = dataAdapter.kendalaList;
                for (int i = 0; i < kendalaList2.size(); i++) {
                    Kendala kendala = kendalaList2.get(i);
                    if (kendala.isSelected()) {
                        responseText.append(kendala.getName()).append("#");
                    }
                }
            }

            if (responseText.toString().equals("")) {
                Toast.makeText(AturanEditActivity.this, "Pilih dulu kendala !", Toast.LENGTH_SHORT).show();
            } else {
                updateAturan();
            }
        });
    }

    private void getDaftarKendala() {
        displayLoader();
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, url, null, response -> {
                    pDialog.dismiss();
                    try {
                        if (response.getInt("status") == 0) {
                            kendalaList = new ArrayList<Kendala>();
                            JSONArray jsonArray = response.getJSONArray("kendala");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String name = jsonObject.getString("nama_kendala");
                                kendala = new Kendala(name, false);
                                kendalaList.add(kendala);
                            }
                            dataAdapter = new MyCustomAdapter(AturanEditActivity.this, R.layout.kendala_list_ceklis, kendalaList);
                            ListView listView = findViewById(R.id.lv_gejala);
                            listView.setAdapter(dataAdapter);
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    response.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
                    pDialog.dismiss();
                    Toast.makeText(getApplicationContext(),
                            error.getMessage(), Toast.LENGTH_SHORT).show();
                });

        MySingleton.getInstance(this).addToRequestQueue(jsArrayRequest);
    }

    private void displayLoader() {
        pDialog = new ProgressDialog(AturanEditActivity.this);
        pDialog.setMessage("Sedang diproses...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    private void updateAturan() {
        displayLoader();
        JSONObject request = new JSONObject();
        try {
            request.put("id_kerusakan", id_kerusakan);
            request.put("daftar_kendala", responseText.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, url_update, request, response -> {
                    pDialog.dismiss();
                    try {
                        if (response.getInt("status") == 0) {
                            Toast.makeText(getApplicationContext(),
                                    response.getString("message"), Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    response.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
                    pDialog.dismiss();
                    Toast.makeText(getApplicationContext(),
                            error.getMessage(), Toast.LENGTH_SHORT).show();
                });

        MySingleton.getInstance(this).addToRequestQueue(jsArrayRequest);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return false;
    }

    private class MyCustomAdapter extends ArrayAdapter<Kendala> {

        private ArrayList<Kendala> kendalaList;

        public MyCustomAdapter(Context context, int textViewResourceId,
                               ArrayList<Kendala> kendalaList) {
            super(context, textViewResourceId, kendalaList);
            this.kendalaList = new ArrayList<Kendala>();
            this.kendalaList.addAll(kendalaList);
        }

        private class ViewHolder {
            CheckBox name;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater) getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.kendala_list_ceklis, null);

                holder = new ViewHolder();
                holder.name = convertView.findViewById(R.id.checkBox1);
                convertView.setTag(holder);

                holder.name.setOnClickListener(v -> {
                    CheckBox cb = (CheckBox) v;
                    Kendala kendala = (Kendala) cb.getTag();
                    kendala.setSelected(cb.isChecked());
                });
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Kendala kendala = kendalaList.get(position);
            holder.name.setText(kendala.getName());
            holder.name.setChecked(kendala.isSelected());
            holder.name.setTag(kendala);

            return convertView;
        }
    }
}