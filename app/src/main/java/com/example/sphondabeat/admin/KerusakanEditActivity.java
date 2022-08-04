package com.example.sphondabeat.admin;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.request.JsonObjectRequest;
import com.example.sphondabeat.MySingleton;
import com.example.sphondabeat.R;

import org.json.JSONException;
import org.json.JSONObject;

public class KerusakanEditActivity extends AppCompatActivity {

    private ProgressDialog pDialog;
    private static final String url = "https://sphondabeat.retechnology.id/sphonda/sp_honda/get_kerusakan.php";
    private static final String url_update = "https://sphondabeat.retechnology.id/sphonda/sp_honda/update_kerusakan.php";
    private static final String url_delete = "https://sphondabeat.retechnology.id/sphonda/sp_honda/delete_kerusakan.php";
    private EditText et_kode_kerusakan;
    private EditText et_nama_kerusakan;
    private EditText et_deskripsi;
    private EditText et_solusi;
    private String kode_kerusakan;
    private String nama_kerusakan;
    private String deskripsi;
    private String solusi;
    private String id_kerusakan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kerusakan_edit);
        setTitle("Ubah Kerusakan");

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id_kerusakan = extras.getString("id_kerusakan");
        }

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
                ubahKerusakan();
            }
        });

        getKerusakan();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_delete) {
            new AlertDialog.Builder(KerusakanEditActivity.this)
                    .setTitle("Konfirmasi")
                    .setMessage("Anda yakin kerusakan akan dihapus ?")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton("Ya, Hapus", (dialog, whichButton) -> {
                        hapusKerusakan();
                    })
                    .setNegativeButton("Tidak", null).show();
            return true;
        }

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return false;
    }

    private void hapusKerusakan() {
        displayLoader();
        JSONObject request = new JSONObject();
        try {
            request.put("id_kerusakan", id_kerusakan);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, url_delete, request, response -> {
                    pDialog.dismiss();
                    try {
                        Toast.makeText(getApplicationContext(),
                                response.getString("message"), Toast.LENGTH_SHORT).show();
                        finish();
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

    private void getKerusakan() {
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
                            et_kode_kerusakan.setText(response.getString("kode_kerusakan"));
                            et_nama_kerusakan.setText(response.getString("nama_kerusakan"));
                            et_deskripsi.setText(response.getString("deskripsi"));
                            et_solusi.setText(response.getString("solusi"));
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
        pDialog = new ProgressDialog(KerusakanEditActivity.this);
        pDialog.setMessage("Sedang diproses...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    private void ubahKerusakan() {
        displayLoader();
        JSONObject request = new JSONObject();
        try {
            request.put("id_kerusakan", id_kerusakan);
            request.put("kode_kerusakan", kode_kerusakan);
            request.put("nama_kerusakan", nama_kerusakan);
            request.put("deskripsi", deskripsi);
            request.put("solusi", solusi);
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
}