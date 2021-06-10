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

public class KendalaTambahActivity extends AppCompatActivity {

    private ProgressDialog pDialog;
    private static final String url = "http://192.168.233.132/sphonda/sp_honda/tambah_kendala.php";
    private EditText et_kode_kendala;
    private EditText et_nama_kendala;
    private String kode_kendala;
    private String nama_kendala;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kendala_tambah);
        setTitle("Tambah Kendala");

        et_kode_kendala = findViewById(R.id.et_kode_kendala);
        et_nama_kendala = findViewById(R.id.et_nama_kendala);

        Button btn_simpan = findViewById(R.id.btn_simpan);

        btn_simpan.setOnClickListener(view -> {
            kode_kendala = et_kode_kendala.getText().toString().trim();
            nama_kendala = et_nama_kendala.getText().toString().trim();
            if (validateInputs()) {
                tambahKendala();
            }
        });
    }

    private void displayLoader() {
        pDialog = new ProgressDialog(KendalaTambahActivity.this);
        pDialog.setMessage("Sedang diproses...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    private void tambahKendala() {
        displayLoader();
        JSONObject request = new JSONObject();
        try {
            request.put("kode_kendala", kode_kendala);
            request.put("nama_kendala", nama_kendala);
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
        if (kode_kendala.equals("")) {
            et_kode_kendala.setError("Kode Kendala tidak boleh kosong");
            et_kode_kendala.requestFocus();
            return false;
        }
        if (nama_kendala.equals("")) {
            et_nama_kendala.setError("Nama Kendala tidak boleh kosong");
            et_nama_kendala.requestFocus();
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