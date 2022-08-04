package com.example.sphondabeat;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sphondabeat.admin.KendalaActivity;
import com.example.sphondabeat.admin.KerusakanActivity;
import com.example.sphondabeat.admin.AturanActivity;

public class AdminActivity extends AppCompatActivity {

    private SessionHandler session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        setTitle("Menu Utama Admin");

        session = new SessionHandler(getApplicationContext());

        Button btn_kendala = findViewById(R.id.btn_data_kendala);
        Button btn_kerusakan = findViewById(R.id.btn_data_kerusakan);
        Button btn_aturan = findViewById(R.id.btn_data_aturan);
        Button btn_logout = findViewById(R.id.btn_logout);

        btn_kendala.setOnClickListener(view -> {
            btn_kendala.setBackgroundColor(getResources().getColor(R.color.light_grey));
            Intent intent = new Intent(AdminActivity.this, KendalaActivity.class);
            startActivity(intent);
        });

        btn_kerusakan.setOnClickListener(view -> {
            btn_kerusakan.setBackgroundColor(getResources().getColor(R.color.light_grey));
            Intent intent = new Intent(AdminActivity.this, KerusakanActivity.class);
            startActivity(intent);
        });

        btn_aturan.setOnClickListener(view -> {
            btn_aturan.setBackgroundColor(getResources().getColor(R.color.light_grey));
            Intent intent = new Intent(AdminActivity.this, AturanActivity.class);
            startActivity(intent);
        });

        btn_logout.setOnClickListener(view -> new AlertDialog.Builder(AdminActivity.this)
                .setTitle("Konfirmasi")
                .setMessage("Anda yakin mau logout ?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Ya, Logout", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        session.logoutUser();
                        Intent intent = new Intent(AdminActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("Tidak", null).show());
        btn_logout.setBackgroundColor(getResources().getColor(R.color.light_grey));

    }
}