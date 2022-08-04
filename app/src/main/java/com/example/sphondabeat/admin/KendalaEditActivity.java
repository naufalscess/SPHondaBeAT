package com.example.sphondabeat.admin;

import android.app.ProgressDialog;
import android.content.DialogInterface;
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

public class KendalaEditActivity extends AppCompatActivity {

    private ProgressDialog pDialog;
    private static final String url = "https://sphondabeat.retechnology.id/sphonda/sp_honda/get_kendala.php";
    private static final String url_update = "https://sphondabeat.retechnology.id/sphonda/sp_honda/update_kendala.php";
    private static final String url_delete = "https://sphondabeat.retechnology.id/sphonda/sp_honda/delete_kendala.php";
    private EditText et_kode_kendala;
    private EditText et_nama_kendala;
    private String kode_kendala;
    private String nama_kendala;
    private String id_kendala;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kendala_edit);
        setTitle("Ubah Kendala");

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id_kendala = extras.getString("id_kendala");
        }

        et_kode_kendala = findViewById(R.id.et_kode_kendala);
        et_nama_kendala = findViewById(R.id.et_nama_kendala);

        Button btn_simpan = findViewById(R.id.btn_simpan);

        btn_simpan.setOnClickListener(view -> {
            btn_simpan.setBackgroundColor(getResources().getColor(R.color.light_grey));
            kode_kendala = et_kode_kendala.getText().toString().trim();
            nama_kendala = et_nama_kendala.getText().toString().trim();
            if (validateInputs()) {
                ubahKendala();
            }
        });

        getKendala();
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
            new AlertDialog.Builder(KendalaEditActivity.this)
                    .setTitle("Konfirmasi")
                    .setMessage("Anda yakin kendala akan dihapus ?")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton("Ya, Hapus", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            hapusKendala();
                            finish();
                        }
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

    private void hapusKendala() {
        displayLoader();
        JSONObject request = new JSONObject();
        try {
            request.put("id_kendala", id_kendala);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, url_delete, request, response -> {
                    pDialog.dismiss();
                    try {
                        Toast.makeText(getApplicationContext(),
                                response.getString("message"), Toast.LENGTH_SHORT).show();
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

    private void getKendala() {
        displayLoader();
        JSONObject request = new JSONObject();
        try {
            request.put("id_kendala", id_kendala);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, url, request, response -> {
                    pDialog.dismiss();
                    try {
                        if (response.getInt("status") == 0) {
                            et_kode_kendala.setText(response.getString("kode_kendala"));
                            et_nama_kendala.setText(response.getString("nama_kendala"));
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
        pDialog = new ProgressDialog(KendalaEditActivity.this);
        pDialog.setMessage("Sedang diproses...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    private void ubahKendala() {
        displayLoader();
        JSONObject request = new JSONObject();
        try {
            request.put("id_kendala", id_kendala);
            request.put("kode_kendala", kode_kendala);
            request.put("nama_kendala", nama_kendala);
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
}