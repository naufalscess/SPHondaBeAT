package com.example.sphondabeat;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.request.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class KerusakanDetailActivity extends AppCompatActivity {

    private ProgressDialog pDialog;
    private static final String url = "https://sphondabeat.retechnology.id/sphonda/sp_honda/get_kerusakan.php";
    private TextView tv_nama_kerusakan;
    private TextView tv_deskripsi;
    private TextView tv_solusi;
    private String id_kerusakan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kerusakan_detail);
        setTitle("Detail Kerusakan");

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id_kerusakan = extras.getString("id_kerusakan");
        }

        tv_nama_kerusakan = findViewById(R.id.tv_nama_kerusakan);
        tv_deskripsi = findViewById(R.id.tv_deskripsi);
        tv_solusi = findViewById(R.id.tv_solusi);

        getKerusakan();
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
                            tv_nama_kerusakan.setText(response.getString("nama_kerusakan"));
                            tv_deskripsi.setText(response.getString("deskripsi"));
                            tv_solusi.setText(response.getString("solusi"));
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
        pDialog = new ProgressDialog(KerusakanDetailActivity.this);
        pDialog.setMessage("Sedang diproses...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }
}