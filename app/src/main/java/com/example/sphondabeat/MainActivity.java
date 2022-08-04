package com.example.sphondabeat;

import android.content.Intent;
import android.net.Uri;
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
        Button btn_cara_memperbaiki_kerusakan = findViewById(R.id.btn_cara_memperbaiki_kerusakan);
        Button btn_tentang_aplikasi = findViewById(R.id.btn_tentang_aplikasi);
        Button btn_logout = findViewById(R.id.btn_logout);

        btn_diagnosa_forward.setOnClickListener(view -> {
            btn_diagnosa_forward.setBackgroundColor(getResources().getColor(R.color.light_grey));
            Intent intent = new Intent(MainActivity.this, DiagnosaKerusakanActivity.class);
            startActivity(intent);
        });

        btn_riwayat.setOnClickListener(view -> {
            btn_riwayat.setBackgroundColor(getResources().getColor(R.color.light_grey));
            Intent intent = new Intent(MainActivity.this, RiwayatActivity.class);
            startActivity(intent);
        });

        btn_komponen.setOnClickListener(view -> {
            btn_komponen.setBackgroundColor(getResources().getColor(R.color.light_grey));
            Intent intent = new Intent(MainActivity.this, DaftarKomponenMotorActivity.class);
            startActivity(intent);
        });

        btn_cara_memperbaiki_kerusakan.setOnClickListener(view -> {
            btn_cara_memperbaiki_kerusakan.setBackgroundColor(getResources().getColor(R.color.light_grey));
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://kursus.kemdikbud.go.id/v3/front/tsm"));
            startActivity(intent);
        });

        btn_tentang_aplikasi.setOnClickListener(view -> {
            btn_tentang_aplikasi.setBackgroundColor(getResources().getColor(R.color.light_grey));
            Intent intent = new Intent(MainActivity.this, TentangAplikasiActivity.class);
            startActivity(intent);
        });

        btn_logout.setOnClickListener(view ->
                new AlertDialog.Builder(MainActivity.this)
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
             btn_logout.setBackgroundColor(getResources().getColor(R.color.light_grey));
    }

}