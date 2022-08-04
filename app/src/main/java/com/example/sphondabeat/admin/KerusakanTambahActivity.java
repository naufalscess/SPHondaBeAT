package com.example.sphondabeat.admin;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.request.JsonObjectRequest;
import com.example.sphondabeat.MySingleton;
import com.example.sphondabeat.R;

import org.json.JSONException;
import org.json.JSONObject;

public class KerusakanTambahActivity extends AppCompatActivity {

    private ProgressDialog pDialog;
    private static final String url = "https://sphondabeat.retechnology.id/sphonda/sp_honda/tambah_kerusakan.php";
    private EditText et_kode_kerusakan;
    private EditText et_nama_kerusakan;
    private EditText et_deskripsi;
    private EditText et_solusi;
    private String kode_kerusakan;
    private String nama_kerusakan;
    private String deskripsi;
    private String solusi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kerusakan_tambah);
        setTitle("Tambah Kerusakan");

        et_kode_kerusakan = findViewById(R.id.et_kode_kerusakan);
        et_nama_kerusakan = findViewById(R.id.et_nama_kerusakan);
        et_deskripsi = findViewById(R.id.et_deskripsi);
        et_solusi = findViewById(R.id.et_solusi);

        Button btn_simpan = findViewById(R.id.btn_simpan);

        btn_simpan.setOnClickListener(view -> {
            btn_simpan.setBackgroundColor(getResources().getColor(R.color.light_grey));
            kode_kerusakan = et_kode_kerusakan.getText().toString().trim();
            nama_kerusakan = et_nama_kerusakan.getText().toString().trim();
            deskripsi = et_deskripsi.getText().toString();
            solusi = et_solusi.getText().toString();
            if (validateInputs()) {
                tambahKerusakan();
            }
        });
    }

    private void displayLoader() {
        pDialog = new ProgressDialog(KerusakanTambahActivity.this);
        pDialog.setMessage("Sedang diproses...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    private void tambahKerusakan() {
        displayLoader();
        JSONObject request = new JSONObject();
        try {
            request.put("kode_kerusakan", kode_kerusakan);
            request.put("nama_kerusakan", nama_kerusakan);
            request.put("deskripsi", deskripsi);
            request.put("solusi", solusi);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, url, request, response -> {
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

    private boolean validateInputs() {
        if (kode_kerusakan.equals("")) {
            et_kode_kerusakan.setError("Kode Kerusakan tidak boleh kosong");
            et_kode_kerusakan.requestFocus();
            return false;
        }
        if (nama_kerusakan.equals("")) {
            et_nama_kerusakan.setError("Nama Kerusakan tidak boleh kosong");
            et_nama_kerusakan.requestFocus();
            return false;
        }
        if (deskripsi.equals("")) {
            et_deskripsi.setError("Deskripsi tidak boleh kosong");
            et_deskripsi.requestFocus();
            return false;
        }
        if (solusi.equals("")) {
            et_solusi.setError("Solusi tidak boleh kosong");
            et_solusi.requestFocus();
            return false;
        }
        return true;
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