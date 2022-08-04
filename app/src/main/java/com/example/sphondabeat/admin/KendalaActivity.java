package com.example.sphondabeat.admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class KendalaActivity extends AppCompatActivity {

    private ProgressDialog pDialog;
    private static final String url = "https://sphondabeat.retechnology.id/sphonda/sp_honda/get_daftar_kendala.php";
    private ListView lv;
    private SimpleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kendala);
        setTitle("Data Kendala");

        lv = findViewById(R.id.list_kendala);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(KendalaActivity.this, KendalaTambahActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    private void displayLoader() {
        pDialog = new ProgressDialog(KendalaActivity.this);
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
                            JSONArray jsonArray = response.getJSONArray("kendala");
                            ArrayList<HashMap<String, String>> list = new ArrayList<>();
                            boolean kosong = true;
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                HashMap<String, String> map = new HashMap<String, String>();
                                map.put("id_kendala", jsonObject.getString("id_kendala"));
                                map.put("nama_kendala", jsonObject.getString("nama_kendala"));
                                list.add(map);
                                kosong = false;
                            }
                            adapter = new SimpleAdapter(
                                    KendalaActivity.this,
                                    list,
                                    R.layout.kendala_list,
                                    new String[]{"id_kendala", "nama_kendala"},
                                    new int[]{R.id.id_kendala, R.id.nama_kendala});
                            lv.setAdapter(adapter);

                            AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View container, int position, long id) {
                                    LinearLayout linearLayout = (LinearLayout) container;
                                    TextView tv_id = (TextView) linearLayout.getChildAt(0);
                                    Intent intent = new Intent(KendalaActivity.this, KendalaEditActivity.class);
                                    intent.putExtra("id_kendala", tv_id.getText().toString());
                                    startActivity(intent);
                                }
                            };

                            lv.setOnItemClickListener(itemClickListener);

                            if (kosong) {
                                Toast.makeText(KendalaActivity.this, "Tidak ada data kendala",
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