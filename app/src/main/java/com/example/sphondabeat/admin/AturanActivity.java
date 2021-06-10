package com.example.sphondabeat.admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.request.JsonObjectRequest;
import com.example.sphondabeat.MySingleton;
import com.example.sphondabeat.R;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class AturanActivity extends AppCompatActivity {

    private ProgressDialog pDialog;
    private static final String url = "http://192.168.233.132/sphonda/sp_honda/get_daftar_aturan.php";
    private ListView lv;
    private SimpleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aturan);
        setTitle("Data Aturan");

        lv = findViewById(R.id.list_aturan);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    private void displayLoader() {
        pDialog = new ProgressDialog(AturanActivity.this);
        pDialog.setMessage("Sedang diproses...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    private void getData() {
        displayLoader();
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, url, null, response -> {
                    pDialog.dismiss();
                    try {
                        if (response.getInt("status") == 0) {
                            JSONArray jsonArray = response.getJSONArray("aturan");
                            ArrayList<HashMap<String, String>> list = new ArrayList<>();
                            boolean kosong = true;
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                HashMap<String, String> map = new HashMap<String, String>();
                                map.put("id_kerusakan", jsonObject.getString("id_kerusakan"));
                                map.put("nama_kerusakan", "Kerusakan: " + jsonObject.getString("nama_kerusakan"));
                                map.put("daftar_kendala", "Kendala: " + jsonObject.getString("daftar_kendala"));
                                list.add(map);
                                kosong = false;
                            }
                            adapter = new SimpleAdapter(
                                    AturanActivity.this,
                                    list,
                                    R.layout.aturan_list,
                                    new String[]{"id_kerusakan", "nama_kerusakan", "daftar_kendala"},
                                    new int[]{R.id.id_kerusakan, R.id.nama_kerusakan, R.id.daftar_kendala});
                            lv.setAdapter(adapter);

                            AdapterView.OnItemClickListener itemClickListener = (parent, container, position, id) -> {
                                LinearLayout linearLayout = (LinearLayout) container;
                                TextView tv_id = (TextView) linearLayout.getChildAt(0);
                                Intent intent = new Intent(AturanActivity.this, AturanViewActivity.class);
                                intent.putExtra("id_kerusakan", tv_id.getText().toString());
                                startActivity(intent);
                            };

                            lv.setOnItemClickListener(itemClickListener);

                            if (kosong) {
                                Toast.makeText(AturanActivity.this, "Tidak ada data aturan",
                                        Toast.LENGTH_SHORT).show();
                            }

                            adapter.notifyDataSetChanged();
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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }
}