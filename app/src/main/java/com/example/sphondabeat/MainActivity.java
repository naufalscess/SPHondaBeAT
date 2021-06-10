package com.example.sphondabeat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private SessionHandler session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Menu Utama");

        session = new SessionHandler(getApplicationContext());

        Button btn_diagnosa_forward = findViewById(R.id.btn_diagnosa_forward);
        Button btn_riwayat = findViewById(R.id.btn_riwayat);
        Button btn_komponen = findViewById(R.id.btn_komponen);
        Button btn_tentang_aplikasi = findViewById(R.id.btn_tentang_aplikasi);
        Button btn_logout = findViewById(R.id.btn_logout);

        btn_diagnosa_forward.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, DiagnosaKerusakanActivity.class);
            startActivity(intent);
        });

        btn_riwayat.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, RiwayatActivity.class);
            startActivity(intent);
        });

        btn_komponen.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, DaftarKomponenMotorActivity.class);
            startActivity(intent);
        });

        btn_tentang_aplikasi.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, TentangAplikasiActivity.class);
            startActivity(intent);
        });

        btn_logout.setOnClickListener(view -> new AlertDialog.Builder(MainActivity.this)
                .setTitle("Konfirmasi")
                .setMessage("Anda yakin mau logout ?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Ya, Logout", (dialog, whichButton) -> {
                    session.logoutUser();
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("Tidak", null).show());

    }

}