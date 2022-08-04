package com.example.sphondabeat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.request.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class HasilDiagnosaActivity extends AppCompatActivity {

    private ProgressDialog pDialog;
    private static final String url = "https://sphondabeat.retechnology.id/sphonda/sp_honda/get_hasil_diagnosa.php";
    private String hasil;
    private Button btn_kerusakan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hasil_diagnosa);
        setTitle("Hasil Diagnosa");

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            hasil = extras.getString("hasil");
        }

        btn_kerusakan = findViewById(R.id.btn_kerusakan);

        getHasilDiagnosa();
    }

    private void displayLoader() {
        pDialog = new ProgressDialog(HasilDiagnosaActivity.this);
        pDialog.setMessage("Sedang diproses...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    private void getHasilDiagnosa() {
        displayLoader();
        JSONObject request = new JSONObject();
        SessionHandler session = new SessionHandler(this);
        User user = session.getUserDetails();
        try {
            request.put("hasil", hasil);
            request.put("metode", "Forward Chaining");
            request.put("id_pengguna", user.getIdPengguna());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, url, request, response -> {
                    pDialog.dismiss();
                    try {
                        if (response.getInt("status") == 0) {
                            if (response.getString("id_kerusakan").equals("")) {
                                TextView tv_title = findViewById(R.id.tv_title);
                                tv_title.setText(response.getString("nama_kerusakan"));
                                btn_kerusakan.setVisibility(View.GONE);
                            } else {
                                final String id_kerusakan = response.getString("id_kerusakan");
                                btn_kerusakan.setText(response.getString("nama_kerusakan"));
                                btn_kerusakan.setOnClickListener(v -> {
                                    Intent myIntent = new Intent(v.getContext(), KerusakanDetailActivity.class);
                                    myIntent.putExtra("id_kerusakan", id_kerusakan);
                                    startActivity(myIntent);
                                });
                            }
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
}