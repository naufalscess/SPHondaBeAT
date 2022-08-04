package com.example.sphondabeat.admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.request.JsonObjectRequest;
import com.example.sphondabeat.MySingleton;
import com.example.sphondabeat.R;

import org.json.JSONException;
import org.json.JSONObject;

public class AturanViewActivity extends AppCompatActivity {

    private ProgressDialog pDialog;
    private static final String url = "https://sphondabeat.retechnology.id/sphonda/sp_honda/get_aturan.php";
    private TextView tv_nama_kerusakan;
    private TextView tv_daftar_kendala;
    private String id_kerusakan;
    private String nama_kerusakan = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aturan_view);
        setTitle("Detail Aturan");

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id_kerusakan = extras.getString("id_kerusakan");
        }

        tv_nama_kerusakan = findViewById(R.id.tv_nama_kerusakan);
        tv_daftar_kendala = findViewById(R.id.tv_daftar_gejala);

        Button btn_atur_ulang = findViewById(R.id.btn_atur_ulang);

        btn_atur_ulang.setOnClickListener(view -> {
            btn_atur_ulang.setBackgroundColor(getResources().getColor(R.color.light_grey));
            Intent intent = new Intent(AturanViewActivity.this, AturanEditActivity.class);
            intent.putExtra("id_kerusakan", id_kerusakan);
            intent.putExtra("nama_kerusakan", nama_kerusakan);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAturan();
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

    private void getAturan() {
        displayLoader();
        JSONObject request = new JSONObject();
        try {
            request.put("id_kerusakan", id_kerusakan);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, url, request, response -> {
                    pDialog.dismiss();
                    try {
                        if (response.getInt("status") == 0) {
                            tv_nama_kerusakan.setText(response.getString("nama_kerusakan"));
                            tv_daftar_kendala.setText(response.getString("daftar_kendala"));
                            nama_kerusakan = response.getString("nama_kerusakan");
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
        pDialog = new ProgressDialog(AturanViewActivity.this);
        pDialog.setMessage("Sedang diproses...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }
}